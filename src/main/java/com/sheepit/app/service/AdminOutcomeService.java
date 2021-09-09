package com.sheepit.app.service;

import com.sheepit.app.domain.*;
import com.sheepit.app.domain.enumeration.GameOutcome;
import com.sheepit.app.repository.AdminOutcomeRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

/**
 * Service Implementation for managing {@link AdminOutcome}.
 */
@Service
@Transactional
public class AdminOutcomeService {

    private final Logger log = LoggerFactory.getLogger(AdminOutcomeService.class);

    private final AdminOutcomeRepository adminOutcomeRepository;

    private final GameService gameService;

    private final UserOutcomeService userOutcomeService;

    private final GameConfigService gameConfigService;

    private final ScoreService scoreService;

    public AdminOutcomeService(
        AdminOutcomeRepository adminOutcomeRepository,
        GameService gameService,
        UserOutcomeService userOutcomeService,
        GameConfigService gameConfigService,
        ScoreService scoreService
    ) {
        this.adminOutcomeRepository = adminOutcomeRepository;
        this.gameService = gameService;
        this.userOutcomeService = userOutcomeService;
        this.gameConfigService = gameConfigService;
        this.scoreService = scoreService;
    }

    /**
     * Save a adminOutcome.
     *
     * @param adminOutcome the entity to save.
     * @return the persisted entity.
     */
    public AdminOutcome save(AdminOutcome adminOutcome) {
        log.debug("Request to save Scores for users that inserted outcome for game : {}", adminOutcome.getGame().getName());
        createOrUpdateScore(adminOutcome);
        log.debug("Request to save AdminOutcome : {}", adminOutcome);
        return adminOutcomeRepository.save(adminOutcome);
    }

    private void createOrUpdateScore(AdminOutcome adminOutcome) {
        //Fetch game id from adminOutcome, retrieve game object - will use it for getting gameType and gameStage fields
        Optional<Game> game = gameService.findOne(adminOutcome.getGame().getId());
        if (game != null) {
            //Get the gameConfig list per gameType and gameStage from above, will use the result for checking game outcome and getting the points to use for scoring
            List<GameConfig> gameConfigs = gameConfigService.findPartialWithEagerRelationships(
                game.get().getGameType(),
                game.get().getStageType()
            );
            //Get all userOutcomes that insert an outcome to this specific game
            List<UserOutcome> userOutcomes = userOutcomeService.findPartialWithEagerRelationships(game.get().getId());

            List<Score> scores = new ArrayList<>();
            userOutcomes
                .stream()
                .forEach(
                    userOutcome -> {
                        //Compare between userOutcome and adminOutcome and set score object for save
                        Score score = compareOutcomes(adminOutcome, userOutcome, gameConfigs);
                        if (score != null) {
                            scores.add(score);
                        }
                    }
                );

            if (!CollectionUtils.isEmpty(scores)) {
                scoreService.saveAll(scores);
            }
        }
    }

    /**
     *  adminA == userB && adminB == userB : Special
     *  adminA > adminB && userA > userB : Hit
     *  adminA < adminB && userA < userB : Hit
     *  adminA == adminB && userA == userB : Hit
     *  else : Missed
     */
    private Score compareOutcomes(AdminOutcome adminOutcome, UserOutcome userOutcome, List<GameConfig> gameConfigs) {
        int endAdminOutcomeOpponentA = Integer.valueOf(adminOutcome.getEndOutcomeOpponentA()).intValue();
        int endAdminOutcomeOpponentB = Integer.valueOf(adminOutcome.getEndOutcomeOpponentB()).intValue();

        int endUserOutcomeOpponentA = Integer.valueOf(userOutcome.getEndOutcomeOpponentA()).intValue();
        int endUserOutcomeOpponentB = Integer.valueOf(userOutcome.getEndOutcomeOpponentB()).intValue();

        if (endAdminOutcomeOpponentA == endUserOutcomeOpponentA && endAdminOutcomeOpponentB == endUserOutcomeOpponentB) {
            return findGameConfig(userOutcome, gameConfigs, GameOutcome.SPECIAL);
        } else if (endAdminOutcomeOpponentA > endAdminOutcomeOpponentB && endUserOutcomeOpponentA > endUserOutcomeOpponentB) {
            return findGameConfig(userOutcome, gameConfigs, GameOutcome.HIT);
        } else if (endAdminOutcomeOpponentA < endAdminOutcomeOpponentB && endUserOutcomeOpponentA < endUserOutcomeOpponentB) {
            return findGameConfig(userOutcome, gameConfigs, GameOutcome.HIT);
        } else if (endAdminOutcomeOpponentA == endAdminOutcomeOpponentB && endUserOutcomeOpponentA == endUserOutcomeOpponentB) {
            return findGameConfig(userOutcome, gameConfigs, GameOutcome.HIT);
        } else return findGameConfig(userOutcome, gameConfigs, GameOutcome.MISSED);
    }

    private Score findGameConfig(UserOutcome userOutcome, List<GameConfig> gameConfigs, GameOutcome gameOutcome) {
        GameConfig gameConfig = gameConfigs.stream().filter(gc -> gameOutcome.equals(gc.getGameOutcome())).findAny().orElse(null);

        if (gameConfig == null) return null;
        Integer points = gameConfig.getPoints();
        log.debug("Points for game outcome {} : {} ", gameOutcome.getValue(), points);
        return setScoreObject(userOutcome, points);
    }

    private Score setScoreObject(UserOutcome userOutcome, Integer points) {
        log.debug(
            "Save score for user {} , game {} , tournament {} : ",
            userOutcome.getUserId(),
            userOutcome.getGameId(),
            userOutcome.getTournamentId()
        );
        Score score = new Score();
        score.setUserId(userOutcome.getUserId());
        score.setGameId(userOutcome.getGameId());
        score.setTournamentId(userOutcome.getTournamentId());
        score.setUser(userOutcome.getUser());
        score.setGame(userOutcome.getGame());
        score.setTournament(userOutcome.getTournament());
        score.setPoints(points);
        //        scoreService.save(score);
        return score;
    }

    /**
     * Partially update a adminOutcome.
     *
     * @param adminOutcome the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AdminOutcome> partialUpdate(AdminOutcome adminOutcome) {
        log.debug("Request to update Scores for users that inserted outcome for game : {}", adminOutcome.getGame().getName());
        createOrUpdateScore(adminOutcome);
        log.debug("Request to partially update AdminOutcome : {}", adminOutcome);
        return adminOutcomeRepository
            .findById(
                new AdminOutcomeId(adminOutcome.getGame().getId(), adminOutcome.getUser().getId(), adminOutcome.getTournament().getId())
            )
            //            .findById(adminOutcome.getId())
            .map(
                existingAdminOutcome -> {
                    if (adminOutcome.getEndOutcomeOpponentA() != null) {
                        existingAdminOutcome.setEndOutcomeOpponentA(adminOutcome.getEndOutcomeOpponentA());
                    }
                    if (adminOutcome.getEndOutcomeOpponentB() != null) {
                        existingAdminOutcome.setEndOutcomeOpponentB(adminOutcome.getEndOutcomeOpponentB());
                    }

                    return existingAdminOutcome;
                }
            )
            .map(adminOutcomeRepository::save);
    }

    /**
     * Get all the adminOutcomes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AdminOutcome> findAll(Pageable pageable) {
        log.debug("Request to get all AdminOutcomes");
        return adminOutcomeRepository.findAll(pageable);
    }

    /**
     * Get one adminOutcome by adminOutcomeId.
     *
     * @param adminOutcomeId the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AdminOutcome> findOne(AdminOutcomeId adminOutcomeId) {
        log.debug("Request to get AdminOutcome : {}", adminOutcomeId);
        log.info("####################Request to get adminOutcomeId : {}", adminOutcomeId);
        return adminOutcomeRepository.findById(adminOutcomeId);
    }

    /**
     * Delete the adminOutcome by adminOutcomeId.
     *
     * @param adminOutcomeId the id of the entity.
     */
    public void delete(AdminOutcomeId adminOutcomeId) {
        log.debug("Request to delete AdminOutcome : {}", adminOutcomeId);

        log.info("#######################Request to delete AdminOutcome : {}", adminOutcomeId);
        adminOutcomeRepository.deleteById(adminOutcomeId);
    }
}

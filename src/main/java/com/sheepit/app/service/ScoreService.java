package com.sheepit.app.service;

import com.sheepit.app.domain.Score;
import com.sheepit.app.domain.ScoreId;
import com.sheepit.app.repository.ScoreRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Score}.
 */
@Service
@Transactional
public class ScoreService {

    private final Logger log = LoggerFactory.getLogger(ScoreService.class);

    private final ScoreRepository scoreRepository;

    public ScoreService(ScoreRepository scoreRepository) {
        this.scoreRepository = scoreRepository;
    }

    /**
     * Save a score.
     *
     * @param score the entity to save.
     * @return the persisted entity.
     */
    public Score save(Score score) {
        log.debug("Request to save Score : {}", score);
        //        log.info("###############################Request to save Score : {} {} {} {} ",
        //            score.getId(), score.getGame().getId(), score.getUser().getId() , score.getTournament().getId());
        log.info(
            "###############################Request to save Score : {} {} {} {} ",
            score.getId(),
            score.getGameId(),
            score.getUserId(),
            score.getTournamentId()
        );
        return scoreRepository.save(score);
    }

    /**
     * Save a score.
     *
     * @param scores the entities to save.
     * @return the persisted entities.
     */
    public List<Score> saveAll(List<Score> scores) {
        log.debug("Request to save number of scores : {}", scores.size());
        return scoreRepository.saveAll(scores);
    }

    /**
     * Partially update a score.
     *
     * @param score the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Score> partialUpdate(Score score) {
        log.debug("Request to partially update Score : {}", score);

        return scoreRepository
            .findById(new ScoreId(score.getGame().getId(), score.getUser().getId(), score.getTournament().getId()))
            //            .findById(adminOutcome.getId())
            .map(
                existingScore -> {
                    if (score.getPoints() != null) {
                        existingScore.setPoints(score.getPoints());
                    }

                    return existingScore;
                }
            )
            .map(scoreRepository::save);
    }

    /**
     * Get all the scores.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Score> findAll(Pageable pageable) {
        log.debug("Request to get all Scores");
        return scoreRepository.findAll(pageable);
    }

    /**
     * Get one score by scoreId.
     *
     * @param scoreId the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Score> findOne(ScoreId scoreId) {
        log.debug("Request to get Score : {}", scoreId);
        log.info("####################Request to get scoreId : {}", scoreId);
        return scoreRepository.findById(scoreId);
    }

    /**
     * Delete the score by scoreId.
     *
     * @param scoreId the id of the entity.
     */
    public void delete(ScoreId scoreId) {
        log.debug("Request to delete Score : {}", scoreId);

        log.info("#######################Request to delete Score : {}", scoreId);
        scoreRepository.deleteById(scoreId);
    }
}

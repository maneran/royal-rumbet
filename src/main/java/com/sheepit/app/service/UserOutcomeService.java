package com.sheepit.app.service;

import com.sheepit.app.domain.GameConfig;
import com.sheepit.app.domain.UserOutcome;
import com.sheepit.app.domain.UserOutcomeId;
import com.sheepit.app.domain.enumeration.GameStageType;
import com.sheepit.app.domain.enumeration.GameType;
import com.sheepit.app.repository.UserOutcomeRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link UserOutcome}.
 */
@Service
@Transactional
public class UserOutcomeService {

    private final Logger log = LoggerFactory.getLogger(UserOutcomeService.class);

    private final UserOutcomeRepository userOutcomeRepository;

    public UserOutcomeService(UserOutcomeRepository userOutcomeRepository) {
        this.userOutcomeRepository = userOutcomeRepository;
    }

    /**
     * Save a userOutcome.
     *
     * @param userOutcome the entity to save.
     * @return the persisted entity.
     */
    public UserOutcome save(UserOutcome userOutcome) {
        log.debug("Request to save UserOutcome : {}", userOutcome);
        log.info(
            "###############################Request to save UserOutcome : {} {} {} {} ",
            userOutcome.getId(),
            userOutcome.getGame().getId(),
            userOutcome.getUser().getId(),
            userOutcome.getTournament().getId()
        );
        return userOutcomeRepository.save(userOutcome);
    }

    /**
     * Partially update a userOutcome.
     *
     * @param userOutcome the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UserOutcome> partialUpdate(UserOutcome userOutcome) {
        log.debug("Request to partially update UserOutcome : {}", userOutcome);
        log.info(
            "##########################Request to partially update UserOutcome : {} {} {} {}",
            userOutcome,
            userOutcome.getGame().getId(),
            userOutcome.getUser().getId(),
            userOutcome.getTournament().getId()
        );
        return userOutcomeRepository
            .findById(new UserOutcomeId(userOutcome.getGame().getId(), userOutcome.getUser().getId(), userOutcome.getTournament().getId()))
            //            .findById(userOutcome.getId())
            .map(
                existingUserOutcome -> {
                    if (userOutcome.getEndOutcomeOpponentA() != null) {
                        existingUserOutcome.setEndOutcomeOpponentA(userOutcome.getEndOutcomeOpponentA());
                    }
                    if (userOutcome.getEndOutcomeOpponentB() != null) {
                        existingUserOutcome.setEndOutcomeOpponentB(userOutcome.getEndOutcomeOpponentB());
                    }

                    return existingUserOutcome;
                }
            )
            .map(userOutcomeRepository::save);
    }

    /**
     * Get all the userOutcomes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<UserOutcome> findAll(Pageable pageable) {
        log.debug("Request to get all UserOutcomes");
        return userOutcomeRepository.findAll(pageable);
    }

    //    /**
    //     * Get one userOutcome by id.
    //     *
    //     * @param id the id of the entity.
    //     * @return the entity.
    //     */
    //    @Transactional(readOnly = true)
    //    public Optional<UserOutcome> findOne(Long id) {
    //        log.debug("Request to get UserOutcome : {}", id);
    //        return userOutcomeRepository.findById(id);
    //    }

    /**
     * Get all the userOutcomes with eager load.
     *
     * @return the list of entities.
     */
    public List<UserOutcome> findPartialWithEagerRelationships(Long gameId) {
        log.debug("Request to get UserOutcomes : {} ", gameId);
        return userOutcomeRepository.findPartialWithEagerRelationships(gameId);
    }

    /**
     * Get one userOutcome by id.
     *
     * @param userOutcomeId the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UserOutcome> findOne(UserOutcomeId userOutcomeId) {
        log.debug("Request to get UserOutcome : {}", userOutcomeId);
        log.info("####################Request to get userOutcomeId : {}", userOutcomeId);
        return userOutcomeRepository.findById(userOutcomeId);
    }

    //    /**
    //     * Delete the userOutcome by id.
    //     *
    //     * @param id the id of the entity.
    //     */
    //    public void delete(Long id) {
    //        log.debug("Request to delete UserOutcome : {}", id);
    //        userOutcomeRepository.deleteById(id);
    //    }

    /**
     * Delete the userOutcome by userOutcomeId.
     *
     * @param userOutcomeId the id of the entity.
     */
    public void delete(UserOutcomeId userOutcomeId) {
        log.debug("Request to delete UserOutcome : {}", userOutcomeId);

        log.info("#######################Request to delete UserOutcome : {}", userOutcomeId);
        userOutcomeRepository.deleteById(userOutcomeId);
    }
}

package com.sheepit.app.service;

import com.sheepit.app.domain.Opponent;
import com.sheepit.app.repository.OpponentRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Opponent}.
 */
@Service
@Transactional
public class OpponentService {

    private final Logger log = LoggerFactory.getLogger(OpponentService.class);

    private final OpponentRepository opponentRepository;

    public OpponentService(OpponentRepository opponentRepository) {
        this.opponentRepository = opponentRepository;
    }

    /**
     * Save a opponent.
     *
     * @param opponent the entity to save.
     * @return the persisted entity.
     */
    public Opponent save(Opponent opponent) {
        log.debug("Request to save Opponent : {}", opponent);
        return opponentRepository.save(opponent);
    }

    /**
     * Partially update a opponent.
     *
     * @param opponent the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Opponent> partialUpdate(Opponent opponent) {
        log.debug("Request to partially update Opponent : {}", opponent);

        return opponentRepository
            .findById(opponent.getId())
            .map(
                existingOpponent -> {
                    if (opponent.getName() != null) {
                        existingOpponent.setName(opponent.getName());
                    }
                    if (opponent.getDescription() != null) {
                        existingOpponent.setDescription(opponent.getDescription());
                    }
                    if (opponent.getOpponentType() != null) {
                        existingOpponent.setType(opponent.getOpponentType());
                    }
                    if (opponent.getNumberOfPlayers() != null) {
                        existingOpponent.setNumberOfPlayers(opponent.getNumberOfPlayers());
                    }

                    return existingOpponent;
                }
            )
            .map(opponentRepository::save);
    }

    /**
     * Get all the opponents.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Opponent> findAll(Pageable pageable) {
        log.debug("Request to get all Opponents");
        return opponentRepository.findAll(pageable);
    }

    /**
     * Get one opponent by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Opponent> findOne(Long id) {
        log.debug("Request to get Opponent : {}", id);
        return opponentRepository.findById(id);
    }

    /**
     * Delete the opponent by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Opponent : {}", id);
        opponentRepository.deleteById(id);
    }
}

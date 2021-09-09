package com.sheepit.app.service;

import com.sheepit.app.domain.Tournament;
import com.sheepit.app.repository.TournamentRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Tournament}.
 */
@Service
@Transactional
public class TournamentService {

    private final Logger log = LoggerFactory.getLogger(TournamentService.class);

    private final TournamentRepository tournamentRepository;

    public TournamentService(TournamentRepository tournamentRepository) {
        this.tournamentRepository = tournamentRepository;
    }

    /**
     * Save a tournament.
     *
     * @param tournament the entity to save.
     * @return the persisted entity.
     */
    public Tournament save(Tournament tournament) {
        log.debug("Request to save Tournament : {}", tournament);
        return tournamentRepository.save(tournament);
    }

    /**
     * Partially update a tournament.
     *
     * @param tournament the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Tournament> partialUpdate(Tournament tournament) {
        log.debug("Request to partially update Tournament : {}", tournament);

        return tournamentRepository
            .findById(tournament.getId())
            .map(
                existingTournament -> {
                    if (tournament.getName() != null) {
                        existingTournament.setName(tournament.getName());
                    }
                    if (tournament.getDateStart() != null) {
                        existingTournament.setDateStart(tournament.getDateStart());
                    }
                    if (tournament.getDateEnd() != null) {
                        existingTournament.setDateEnd(tournament.getDateEnd());
                    }
                    if (tournament.getRegistrationEndDate() != null) {
                        existingTournament.setRegistrationEndDate(tournament.getRegistrationEndDate());
                    }

                    return existingTournament;
                }
            )
            .map(tournamentRepository::save);
    }

    /**
     * Get all the tournaments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Tournament> findAll(Pageable pageable) {
        log.debug("Request to get all Tournaments");
        return tournamentRepository.findAll(pageable);
    }

    /**
     * Get one tournament by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Tournament> findOne(Long id) {
        log.debug("Request to get Tournament : {}", id);
        return tournamentRepository.findById(id);
    }

    /**
     * Delete the tournament by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Tournament : {}", id);
        tournamentRepository.deleteById(id);
    }
}

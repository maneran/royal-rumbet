package com.sheepit.app.service;

import com.sheepit.app.domain.Game;
import com.sheepit.app.repository.GameRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Game}.
 */
@Service
@Transactional
public class GameService {

    private final Logger log = LoggerFactory.getLogger(GameService.class);

    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    /**
     * Save a game.
     *
     * @param game the entity to save.
     * @return the persisted entity.
     */
    public Game save(Game game) {
        log.debug("Request to save Game : {}", game);
        return gameRepository.save(game);
    }

    /**
     * Partially update a game.
     *
     * @param game the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Game> partialUpdate(Game game) {
        log.debug("Request to partially update Game : {}", game);

        return gameRepository
            .findById(game.getId())
            .map(
                existingGame -> {
                    if (game.getName() != null) {
                        existingGame.setName(game.getName());
                    }
                    if (game.getDateStart() != null) {
                        existingGame.setDateStart(game.getDateStart());
                    }
                    if (game.getGameType() != null) {
                        existingGame.setGameType(game.getGameType());
                    }
                    if (game.getStageType() != null) {
                        existingGame.setStageType(game.getStageType());
                    }

                    return existingGame;
                }
            )
            .map(gameRepository::save);
    }

    /**
     * Get all the games.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Game> findAll(Pageable pageable) {
        log.debug("Request to get all Games");
        return gameRepository.findAll(pageable);
    }

    /**
     * Get all the games with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Game> findAllWithEagerRelationships(Pageable pageable) {
        return gameRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one game by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Game> findOne(Long id) {
        log.debug("Request to get Game : {}", id);
        return gameRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the game by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Game : {}", id);
        gameRepository.deleteById(id);
    }
}

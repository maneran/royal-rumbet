package com.sheepit.app.service;

import com.sheepit.app.domain.GameConfig;
import com.sheepit.app.domain.enumeration.GameStageType;
import com.sheepit.app.domain.enumeration.GameType;
import com.sheepit.app.repository.GameConfigRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GameConfigService {

    private final Logger log = LoggerFactory.getLogger(GameConfigService.class);

    private final GameConfigRepository gameConfigRepository;

    public GameConfigService(GameConfigRepository gameConfigRepository) {
        this.gameConfigRepository = gameConfigRepository;
    }

    /**
     * Save a gameConfig.
     *
     * @param gameConfig the entity to save.
     * @return the persisted entity.
     */
    public GameConfig save(GameConfig gameConfig) {
        log.debug("Request to save GameConfig : {}", gameConfig);
        return gameConfigRepository.save(gameConfig);
    }

    /**
     * Partially update a gameConfig.
     *
     * @param gameConfig the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<GameConfig> partialUpdate(GameConfig gameConfig) {
        log.debug("Request to partially update GameConfig : {}", gameConfig);

        return gameConfigRepository
            .findById(gameConfig.getId())
            .map(
                existingGameConfig -> {
                    if (gameConfig.getGameType() != null) {
                        existingGameConfig.setGameType(gameConfig.getGameType());
                    }
                    if (gameConfig.getGameStageType() != null) {
                        existingGameConfig.setGameStageType(gameConfig.getGameStageType());
                    }
                    if (gameConfig.getGameOutcome() != null) {
                        existingGameConfig.setGameOutcome(gameConfig.getGameOutcome());
                    }
                    if (gameConfig.getPoints() != null) {
                        existingGameConfig.setPoints(gameConfig.getPoints());
                    }

                    return existingGameConfig;
                }
            )
            .map(gameConfigRepository::save);
    }

    /**
     * Get all the gameConfigs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<GameConfig> findAll(Pageable pageable) {
        log.debug("Request to get all GameConfigs");
        return gameConfigRepository.findAll(pageable);
    }

    /**
     * Get all the gameConfigs with eager load.
     *
     * @return the list of entities.
     */
    public List<GameConfig> findPartialWithEagerRelationships(GameType gameType, GameStageType gameStageType) {
        log.debug("Request to get GameConfigs : {} , {} ", gameType, gameStageType);
        return gameConfigRepository.findPartialWithEagerRelationships(gameType, gameStageType);
    }

    /**
     * Get one gameConfig by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<GameConfig> findOne(Long id) {
        log.debug("Request to get GameConfig : {}", id);
        return gameConfigRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the gameConfig by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete GameConfig : {}", id);
        gameConfigRepository.deleteById(id);
    }
}

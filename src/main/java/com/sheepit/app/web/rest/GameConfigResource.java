package com.sheepit.app.web.rest;

import com.sheepit.app.domain.GameConfig;
import com.sheepit.app.domain.Opponent;
import com.sheepit.app.repository.GameConfigRepository;
import com.sheepit.app.service.GameConfigService;
import com.sheepit.app.service.OpponentService;
import com.sheepit.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.sheepit.app.domain.GameConfig}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class GameConfigResource {

    private final Logger log = LoggerFactory.getLogger(GameConfigResource.class);

    private static final String ENTITY_NAME = "gameConfig";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GameConfigService gameConfigService;

    private final GameConfigRepository gameConfigRepository;

    public GameConfigResource(GameConfigService gameConfigService, GameConfigRepository gameConfigRepository) {
        this.gameConfigService = gameConfigService;
        this.gameConfigRepository = gameConfigRepository;
    }

    /**
     * {@code POST  /game-configs} : Create a new gameConfig.
     *
     * @param gameConfig the gameConfig to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new gameConfig, or with status {@code 400 (Bad Request)} if the gameConfig has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/game-configs")
    public ResponseEntity<GameConfig> createGameConfig(@Valid @RequestBody GameConfig gameConfig) throws URISyntaxException {
        log.debug("REST request to save GameConfig : {}", gameConfig);
        if (gameConfig.getId() != null) {
            throw new BadRequestAlertException("A new gameConfig cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GameConfig result = gameConfigService.save(gameConfig);
        return ResponseEntity
            .created(new URI("/api/game-configs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /game-configs/:id} : Updates an existing gameConfig.
     *
     * @param id the id of the gameConfig to save.
     * @param gameConfig the gameConfig to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gameConfig,
     * or with status {@code 400 (Bad Request)} if the gameConfig is not valid,
     * or with status {@code 500 (Internal Server Error)} if the gameConfig couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/game-configs/{id}")
    public ResponseEntity<GameConfig> updateGameConfig(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody GameConfig gameConfig
    ) throws URISyntaxException {
        log.debug("REST request to update GameConfig : {}, {}", id, gameConfig);
        if (gameConfig.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gameConfig.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gameConfigRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        GameConfig result = gameConfigService.save(gameConfig);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, gameConfig.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /game-configs/:id} : Partial updates given fields of an existing gameConfig, field will ignore if it is null
     *
     * @param id the id of the gameConfig to save.
     * @param gameConfig the gameConfig to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gameConfig,
     * or with status {@code 400 (Bad Request)} if the gameConfig is not valid,
     * or with status {@code 404 (Not Found)} if the gameConfig is not found,
     * or with status {@code 500 (Internal Server Error)} if the gameConfig couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/game-configs/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<GameConfig> partialUpdateGameConfig(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody GameConfig gameConfig
    ) throws URISyntaxException {
        log.debug("REST request to partial update GameConfig partially : {}, {}", id, gameConfig);
        if (gameConfig.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gameConfig.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gameConfigRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GameConfig> result = gameConfigService.partialUpdate(gameConfig);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, gameConfig.getId().toString())
        );
    }

    /**
     * {@code GET  /game-configs} : get all the gameConfigs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of gameConfigs in body.
     */
    @GetMapping("/game-configs")
    public List<GameConfig> getAllGameConfigs() {
        log.debug("REST request to get all GameConfigs");
        return gameConfigRepository.findAll();
    }

    /**
     * {@code GET  /game-configs/:id} : get the "id" gameConfig.
     *
     * @param id the id of the gameConfig to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the gameConfig, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/game-configs/{id}")
    public ResponseEntity<GameConfig> getGameConfig(@PathVariable Long id) {
        log.debug("REST request to get GameConfig : {}", id);
        Optional<GameConfig> gameConfig = gameConfigRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(gameConfig);
    }

    /**
     * {@code DELETE  /game-configs/:id} : delete the "id" gameConfig.
     *
     * @param id the id of the gameConfig to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/game-configs/{id}")
    public ResponseEntity<Void> deleteGameConfig(@PathVariable Long id) {
        log.debug("REST request to delete GameConfig : {}", id);
        gameConfigRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}

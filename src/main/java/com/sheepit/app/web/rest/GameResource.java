package com.sheepit.app.web.rest;

import com.sheepit.app.config.Constants;
import com.sheepit.app.domain.Game;
import com.sheepit.app.repository.GameRepository;
import com.sheepit.app.service.GameQueryService;
import com.sheepit.app.service.GameService;
import com.sheepit.app.service.criteria.GameCriteria;
import com.sheepit.app.util.DateUtils;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.sheepit.app.domain.Game}.
 */
@RestController
@RequestMapping("/api")
public class GameResource {

    private final Logger log = LoggerFactory.getLogger(GameResource.class);

    private static final String ENTITY_NAME = "game";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GameService gameService;

    private final GameRepository gameRepository;

    private final GameQueryService gameQueryService;

    public GameResource(GameService gameService, GameRepository gameRepository, GameQueryService gameQueryService) {
        this.gameService = gameService;
        this.gameRepository = gameRepository;
        this.gameQueryService = gameQueryService;
    }

    /**
     * {@code POST  /games} : Create a new game.
     *
     * @param game the game to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new game, or with status {@code 400 (Bad Request)} if the game has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/games")
    public ResponseEntity<Game> createGame(@Valid @RequestBody Game game) throws URISyntaxException {
        log.debug("REST request to save Game : {}", game);
        if (game.getId() != null) {
            throw new BadRequestAlertException("A new game cannot already have an ID", ENTITY_NAME, "idexists");
        }
        generateName(game);
        Game result = gameService.save(game);
        return ResponseEntity
            .created(new URI("/api/games/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    private void generateName(Game game) {
        if (game != null) {
            game.setName(
                new StringBuffer()
                    .append(game.getTournament().getName())
                    .append(Constants.UNDERSCORE)
                    .append(game.getOpponentA().getName())
                    .append(Constants.UNDERSCORE)
                    .append(Constants.VERSUS)
                    .append(Constants.UNDERSCORE)
                    .append(game.getOpponentB().getName())
                    .append(Constants.UNDERSCORE)
                    .append(game.getStageType().getValue().toLowerCase())
                    .append(Constants.UNDERSCORE)
                    .append(DateUtils.getDateAsString(game.getDateStart()))
                    .toString()
            );
        }
    }

    /**
     * {@code PUT  /games/:id} : Updates an existing game.
     *
     * @param id the id of the game to save.
     * @param game the game to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated game,
     * or with status {@code 400 (Bad Request)} if the game is not valid,
     * or with status {@code 500 (Internal Server Error)} if the game couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/games/{id}")
    public ResponseEntity<Game> updateGame(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Game game)
        throws URISyntaxException {
        log.debug("REST request to update Game : {}, {}", id, game);
        if (game.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, game.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gameRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        generateName(game);
        Game result = gameService.save(game);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, game.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /games/:id} : Partial updates given fields of an existing game, field will ignore if it is null
     *
     * @param id the id of the game to save.
     * @param game the game to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated game,
     * or with status {@code 400 (Bad Request)} if the game is not valid,
     * or with status {@code 404 (Not Found)} if the game is not found,
     * or with status {@code 500 (Internal Server Error)} if the game couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/games/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Game> partialUpdateGame(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Game game
    ) throws URISyntaxException {
        log.debug("REST request to partial update Game partially : {}, {}", id, game);
        if (game.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, game.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gameRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Game> result = gameService.partialUpdate(game);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, game.getId().toString())
        );
    }

    /**
     * {@code GET  /games} : get all the games.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of games in body.
     */
    @GetMapping("/games")
    public ResponseEntity<List<Game>> getAllGames(GameCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Games by criteria: {}", criteria);
        Page<Game> page = gameQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /games/count} : count all the games.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/games/count")
    public ResponseEntity<Long> countGames(GameCriteria criteria) {
        log.debug("REST request to count Games by criteria: {}", criteria);
        return ResponseEntity.ok().body(gameQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /games/:id} : get the "id" game.
     *
     * @param id the id of the game to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the game, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/games/{id}")
    public ResponseEntity<Game> getGame(@PathVariable Long id) {
        log.debug("REST request to get Game : {}", id);
        Optional<Game> game = gameService.findOne(id);
        return ResponseUtil.wrapOrNotFound(game);
    }

    /**
     * {@code DELETE  /games/:id} : delete the "id" game.
     *
     * @param id the id of the game to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/games/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable Long id) {
        log.debug("REST request to delete Game : {}", id);
        gameService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}

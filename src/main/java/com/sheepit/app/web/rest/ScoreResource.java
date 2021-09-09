package com.sheepit.app.web.rest;

import com.sheepit.app.domain.Score;
import com.sheepit.app.domain.ScoreId;
import com.sheepit.app.repository.ScoreRepository;
import com.sheepit.app.service.ScoreQueryService;
import com.sheepit.app.service.ScoreService;
import com.sheepit.app.service.criteria.ScoreCriteria;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.sheepit.app.domain.Score}.
 */
@RestController
@RequestMapping("/api")
public class ScoreResource {

    private final Logger log = LoggerFactory.getLogger(ScoreResource.class);

    private static final String ENTITY_NAME = "score";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ScoreService scoreService;

    private final ScoreRepository scoreRepository;

    private final ScoreQueryService scoreQueryService;

    public ScoreResource(ScoreService scoreService, ScoreRepository scoreRepository, ScoreQueryService scoreQueryService) {
        this.scoreService = scoreService;
        this.scoreRepository = scoreRepository;
        this.scoreQueryService = scoreQueryService;
    }

    /**
     * {@code POST  /scores} : Create a new score.
     *
     * @param score the score to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new score, or with status {@code 400 (Bad Request)} if the score has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/scores")
    public ResponseEntity<Score> createScore(@Valid @RequestBody Score score) throws URISyntaxException {
        log.debug("REST request to save Score : {}", score);
        if (score.getId() != null) {
            throw new BadRequestAlertException("A new score cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Score result = scoreService.save(score);
        StringBuffer sb = new StringBuffer()
            .append(result.getGame().getId())
            .append("_")
            .append(result.getUser().getId())
            .append("_")
            .append(result.getTournament().getId());
        return ResponseEntity
            .created(new URI("/api/scores/" + sb.toString()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, sb.toString()))
            .body(result);
    }

    /**
     * {@code PUT  /scores/:id} : Updates an existing score.
     *
     * @param gameId the id of the score to save.
     * @param userId the id of the score to save.
     * @param tournamentId the id of the score to save.
     * @param score the score to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated score,
     * or with status {@code 400 (Bad Request)} if the score is not valid,
     * or with status {@code 500 (Internal Server Error)} if the score couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/scores/{gameId}_{userId}_{tournamentId}")
    public ResponseEntity<Score> updateScore(
        @PathVariable(value = "gameId", required = false) final Long gameId,
        @PathVariable(value = "userId", required = false) final String userId,
        @PathVariable(value = "tournamentId", required = false) final Long tournamentId,
        @Valid @RequestBody Score score
    ) throws URISyntaxException {
        log.debug("REST request to update AdminOutcome : {}, {} {} {}", gameId, userId, tournamentId, score);
        if (score.getGame() == null || score.getUser() == null || score.getTournament() == null) {
            throw new BadRequestAlertException("Invalid scoreId", ENTITY_NAME, "scoreidnull");
        }
        if (!Objects.equals(gameId, score.getGame().getId())) {
            throw new BadRequestAlertException("Invalid scoreId", ENTITY_NAME, "gameidinvalid");
        }
        if (!Objects.equals(userId, score.getUser().getId())) {
            throw new BadRequestAlertException("Invalid scoreId", ENTITY_NAME, "useridinvalid");
        }
        if (!Objects.equals(tournamentId, score.getTournament().getId())) {
            throw new BadRequestAlertException("Invalid scoreId", ENTITY_NAME, "tournamentidinvalid");
        }

        if (!scoreRepository.existsById(new ScoreId(score.getGame().getId(), score.getUser().getId(), score.getTournament().getId()))) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "scoreidnotfound");
        }

        Score result = scoreService.save(score);
        StringBuffer sb = new StringBuffer()
            .append(result.getGame().getId())
            .append("_")
            .append(result.getUser().getId())
            .append("_")
            .append(result.getTournament().getId());
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sb.toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /scores/:id} : Partial updates given fields of an existing score, field will ignore if it is null
     *
     * @param gameId the id of the score to save.
     * @param userId the id of the score to save.
     * @param tournamentId the id of the score to save.
     * @param score the score to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated score,
     * or with status {@code 400 (Bad Request)} if the score is not valid,
     * or with status {@code 404 (Not Found)} if the score is not found,
     * or with status {@code 500 (Internal Server Error)} if the score couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/scores/{gameId}_{userId}_{tournamentId}", consumes = "application/merge-patch+json")
    public ResponseEntity<Score> partialUpdateScore(
        @PathVariable(value = "gameId", required = false) final Long gameId,
        @PathVariable(value = "userId", required = false) final String userId,
        @PathVariable(value = "tournamentId", required = false) final Long tournamentId,
        @NotNull @RequestBody Score score
    ) throws URISyntaxException {
        log.debug("REST request to partial update Score partially : {}, {} {} {}", gameId, userId, tournamentId, score);
        if (score.getGame() == null || score.getUser() == null || score.getTournament() == null) {
            throw new BadRequestAlertException("Invalid scoreId", ENTITY_NAME, "scoreidnull");
        }
        if (!Objects.equals(gameId, score.getGame().getId())) {
            throw new BadRequestAlertException("Invalid scoreId", ENTITY_NAME, "gameidinvalid");
        }
        if (!Objects.equals(userId, score.getUser().getId())) {
            throw new BadRequestAlertException("Invalid scoreId", ENTITY_NAME, "useridinvalid");
        }
        if (!Objects.equals(tournamentId, score.getTournament().getId())) {
            throw new BadRequestAlertException("Invalid scoreId", ENTITY_NAME, "tournamentidinvalid");
        }

        if (!scoreRepository.existsById(new ScoreId(score.getGame().getId(), score.getUser().getId(), score.getTournament().getId()))) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "scoreidnotfound");
        }

        Optional<Score> result = scoreService.partialUpdate(score);

        StringBuffer sb = new StringBuffer()
            .append(result.get().getGame().getId())
            .append("_")
            .append(result.get().getUser().getId())
            .append("_")
            .append(result.get().getTournament().getId());
        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sb.toString()));
    }

    /**
     * {@code GET  /scores} : get all the scores.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of scores in body.
     */
    @GetMapping("/scores")
    public ResponseEntity<List<Score>> getAllScores(ScoreCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Scores by criteria: {}", criteria);
        Page<Score> page = scoreQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /scores/count} : count all the scores.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/scores/count")
    public ResponseEntity<Long> countScores(ScoreCriteria criteria) {
        log.debug("REST request to count Scores by criteria: {}", criteria);
        return ResponseEntity.ok().body(scoreQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /scores/:gameId_userId_tournamentId} : get the composite "id" score.
     *
     * @param gameId the id of the adminOutcome to retrieve.
     * @param userId the id of the adminOutcome to retrieve.
     * @param tournamentId the id of the adminOutcome to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the score, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/scores/{gameId}_{userId}_{tournamentId}")
    public ResponseEntity<Score> getScore(@PathVariable Long gameId, @PathVariable String userId, @PathVariable Long tournamentId) {
        log.debug("REST request to get Score : {} {} {} ", gameId, userId, tournamentId);
        Optional<Score> adminOutcome = scoreService.findOne(new ScoreId(gameId, userId, tournamentId));
        return ResponseUtil.wrapOrNotFound(adminOutcome);
    }

    /**
     * {@code DELETE  /scores/:gameId_userId_tournamentId} : delete the composite "id" score.
     *
     * @param gameId the id of the adminOutcome to delete.
     * @param userId the id of the adminOutcome to delete.
     * @param tournamentId the id of the adminOutcome to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/scores/{gameId}_{userId}_{tournamentId}")
    public ResponseEntity<Void> deleteScore(@PathVariable Long gameId, @PathVariable String userId, @PathVariable Long tournamentId) {
        log.debug("REST request to delete Score : {} {} {} ", gameId, userId, tournamentId);
        log.info("############################REST request to delete Score : {} {} {} ", gameId, userId, tournamentId);

        scoreService.delete(new ScoreId(gameId, userId, tournamentId));

        StringBuffer sb = new StringBuffer().append(gameId).append("_").append(userId).append("_").append(tournamentId);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, sb.toString()))
            .build();
    }
}

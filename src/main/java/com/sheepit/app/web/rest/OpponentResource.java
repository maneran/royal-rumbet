package com.sheepit.app.web.rest;

import com.sheepit.app.domain.Opponent;
import com.sheepit.app.repository.OpponentRepository;
import com.sheepit.app.service.OpponentQueryService;
import com.sheepit.app.service.OpponentService;
import com.sheepit.app.service.criteria.OpponentCriteria;
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
 * REST controller for managing {@link com.sheepit.app.domain.Opponent}.
 */
@RestController
@RequestMapping("/api")
public class OpponentResource {

    private final Logger log = LoggerFactory.getLogger(OpponentResource.class);

    private static final String ENTITY_NAME = "opponent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OpponentService opponentService;

    private final OpponentRepository opponentRepository;

    private final OpponentQueryService opponentQueryService;

    public OpponentResource(
        OpponentService opponentService,
        OpponentRepository opponentRepository,
        OpponentQueryService opponentQueryService
    ) {
        this.opponentService = opponentService;
        this.opponentRepository = opponentRepository;
        this.opponentQueryService = opponentQueryService;
    }

    /**
     * {@code POST  /opponents} : Create a new opponent.
     *
     * @param opponent the opponent to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new opponent, or with status {@code 400 (Bad Request)} if the opponent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/opponents")
    public ResponseEntity<Opponent> createOpponent(@Valid @RequestBody Opponent opponent) throws URISyntaxException {
        log.debug("REST request to save Opponent : {}", opponent);
        if (opponent.getId() != null) {
            throw new BadRequestAlertException("A new opponent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Opponent result = opponentService.save(opponent);
        return ResponseEntity
            .created(new URI("/api/opponents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /opponents/:id} : Updates an existing opponent.
     *
     * @param id the id of the opponent to save.
     * @param opponent the opponent to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated opponent,
     * or with status {@code 400 (Bad Request)} if the opponent is not valid,
     * or with status {@code 500 (Internal Server Error)} if the opponent couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/opponents/{id}")
    public ResponseEntity<Opponent> updateOpponent(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Opponent opponent
    ) throws URISyntaxException {
        log.debug("REST request to update Opponent : {}, {}", id, opponent);
        if (opponent.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, opponent.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!opponentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Opponent result = opponentService.save(opponent);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, opponent.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /opponents/:id} : Partial updates given fields of an existing opponent, field will ignore if it is null
     *
     * @param id the id of the opponent to save.
     * @param opponent the opponent to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated opponent,
     * or with status {@code 400 (Bad Request)} if the opponent is not valid,
     * or with status {@code 404 (Not Found)} if the opponent is not found,
     * or with status {@code 500 (Internal Server Error)} if the opponent couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/opponents/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Opponent> partialUpdateOpponent(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Opponent opponent
    ) throws URISyntaxException {
        log.debug("REST request to partial update Opponent partially : {}, {}", id, opponent);
        if (opponent.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, opponent.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!opponentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Opponent> result = opponentService.partialUpdate(opponent);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, opponent.getId().toString())
        );
    }

    /**
     * {@code GET  /opponents} : get all the opponents.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of opponents in body.
     */
    @GetMapping("/opponents")
    public ResponseEntity<List<Opponent>> getAllOpponents(OpponentCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Opponents by criteria: {}", criteria);
        Page<Opponent> page = opponentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /opponents/count} : count all the opponents.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/opponents/count")
    public ResponseEntity<Long> countOpponents(OpponentCriteria criteria) {
        log.debug("REST request to count Opponents by criteria: {}", criteria);
        return ResponseEntity.ok().body(opponentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /opponents/:id} : get the "id" opponent.
     *
     * @param id the id of the opponent to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the opponent, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/opponents/{id}")
    public ResponseEntity<Opponent> getOpponent(@PathVariable Long id) {
        log.debug("REST request to get Opponent : {}", id);
        Optional<Opponent> opponent = opponentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(opponent);
    }

    /**
     * {@code DELETE  /opponents/:id} : delete the "id" opponent.
     *
     * @param id the id of the opponent to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/opponents/{id}")
    public ResponseEntity<Void> deleteOpponent(@PathVariable Long id) {
        log.debug("REST request to delete Opponent : {}", id);
        opponentService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}

package com.sheepit.app.web.rest;

import com.sheepit.app.domain.Tournament;
import com.sheepit.app.repository.TournamentRepository;
import com.sheepit.app.service.TournamentQueryService;
import com.sheepit.app.service.TournamentService;
import com.sheepit.app.service.criteria.TournamentCriteria;
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
 * REST controller for managing {@link com.sheepit.app.domain.Tournament}.
 */
@RestController
@RequestMapping("/api")
public class TournamentResource {

    private final Logger log = LoggerFactory.getLogger(TournamentResource.class);

    private static final String ENTITY_NAME = "tournament";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TournamentService tournamentService;

    private final TournamentRepository tournamentRepository;

    private final TournamentQueryService tournamentQueryService;

    public TournamentResource(
        TournamentService tournamentService,
        TournamentRepository tournamentRepository,
        TournamentQueryService tournamentQueryService
    ) {
        this.tournamentService = tournamentService;
        this.tournamentRepository = tournamentRepository;
        this.tournamentQueryService = tournamentQueryService;
    }

    /**
     * {@code POST  /tournaments} : Create a new tournament.
     *
     * @param tournament the tournament to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tournament, or with status {@code 400 (Bad Request)} if the tournament has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tournaments")
    public ResponseEntity<Tournament> createTournament(@Valid @RequestBody Tournament tournament) throws URISyntaxException {
        log.debug("REST request to save Tournament : {}", tournament);
        if (tournament.getId() != null) {
            throw new BadRequestAlertException("A new tournament cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Tournament result = tournamentService.save(tournament);
        return ResponseEntity
            .created(new URI("/api/tournaments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tournaments/:id} : Updates an existing tournament.
     *
     * @param id the id of the tournament to save.
     * @param tournament the tournament to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tournament,
     * or with status {@code 400 (Bad Request)} if the tournament is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tournament couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tournaments/{id}")
    public ResponseEntity<Tournament> updateTournament(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Tournament tournament
    ) throws URISyntaxException {
        log.debug("REST request to update Tournament : {}, {}", id, tournament);
        if (tournament.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tournament.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tournamentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Tournament result = tournamentService.save(tournament);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tournament.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tournaments/:id} : Partial updates given fields of an existing tournament, field will ignore if it is null
     *
     * @param id the id of the tournament to save.
     * @param tournament the tournament to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tournament,
     * or with status {@code 400 (Bad Request)} if the tournament is not valid,
     * or with status {@code 404 (Not Found)} if the tournament is not found,
     * or with status {@code 500 (Internal Server Error)} if the tournament couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tournaments/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Tournament> partialUpdateTournament(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Tournament tournament
    ) throws URISyntaxException {
        log.debug("REST request to partial update Tournament partially : {}, {}", id, tournament);
        if (tournament.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tournament.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tournamentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Tournament> result = tournamentService.partialUpdate(tournament);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tournament.getId().toString())
        );
    }

    /**
     * {@code GET  /tournaments} : get all the tournaments.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tournaments in body.
     */
    @GetMapping("/tournaments")
    public ResponseEntity<List<Tournament>> getAllTournaments(TournamentCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Tournaments by criteria: {}", criteria);
        Page<Tournament> page = tournamentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tournaments/count} : count all the tournaments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/tournaments/count")
    public ResponseEntity<Long> countTournaments(TournamentCriteria criteria) {
        log.debug("REST request to count Tournaments by criteria: {}", criteria);
        return ResponseEntity.ok().body(tournamentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tournaments/:id} : get the "id" tournament.
     *
     * @param id the id of the tournament to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tournament, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tournaments/{id}")
    public ResponseEntity<Tournament> getTournament(@PathVariable Long id) {
        log.debug("REST request to get Tournament : {}", id);
        Optional<Tournament> tournament = tournamentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tournament);
    }

    /**
     * {@code DELETE  /tournaments/:id} : delete the "id" tournament.
     *
     * @param id the id of the tournament to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tournaments/{id}")
    public ResponseEntity<Void> deleteTournament(@PathVariable Long id) {
        log.debug("REST request to delete Tournament : {}", id);
        tournamentService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}

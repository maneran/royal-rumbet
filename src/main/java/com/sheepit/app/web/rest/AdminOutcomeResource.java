package com.sheepit.app.web.rest;

import com.sheepit.app.domain.AdminOutcome;
import com.sheepit.app.domain.AdminOutcomeId;
import com.sheepit.app.repository.AdminOutcomeRepository;
import com.sheepit.app.service.AdminOutcomeQueryService;
import com.sheepit.app.service.AdminOutcomeService;
import com.sheepit.app.service.criteria.AdminOutcomeCriteria;
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
 * REST controller for managing {@link com.sheepit.app.domain.AdminOutcome}.
 */
@RestController
@RequestMapping("/api")
public class AdminOutcomeResource {

    private final Logger log = LoggerFactory.getLogger(AdminOutcomeResource.class);

    private static final String ENTITY_NAME = "adminOutcome";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AdminOutcomeService adminOutcomeService;

    private final AdminOutcomeRepository adminOutcomeRepository;

    private final AdminOutcomeQueryService adminOutcomeQueryService;

    public AdminOutcomeResource(
        AdminOutcomeService adminOutcomeService,
        AdminOutcomeRepository adminOutcomeRepository,
        AdminOutcomeQueryService adminOutcomeQueryService
    ) {
        this.adminOutcomeService = adminOutcomeService;
        this.adminOutcomeRepository = adminOutcomeRepository;
        this.adminOutcomeQueryService = adminOutcomeQueryService;
    }

    /**
     * {@code POST  /admin-outcomes} : Create a new adminOutcome.
     *
     * @param adminOutcome the adminOutcome to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new adminOutcome, or with status {@code 400 (Bad Request)} if the adminOutcome has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/admin-outcomes")
    public ResponseEntity<AdminOutcome> createAdminOutcome(@Valid @RequestBody AdminOutcome adminOutcome) throws URISyntaxException {
        log.debug("REST request to save AdminOutcome : {}", adminOutcome);
        if (adminOutcome.getId() != null) {
            throw new BadRequestAlertException("A new adminOutcome cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AdminOutcome result = adminOutcomeService.save(adminOutcome);
        StringBuffer sb = new StringBuffer()
            .append(result.getGame().getId())
            .append("_")
            .append(result.getUser().getId())
            .append("_")
            .append(result.getTournament().getId());
        return ResponseEntity
            .created(new URI("/api/admin-outcomes/" + sb.toString()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, sb.toString()))
            .body(result);
    }

    /**
     * {@code PUT  /admin-outcomes/:id} : Updates an existing adminOutcome.
     *
     * @param gameId the id of the adminOutcome to save.
     * @param userId the id of the adminOutcome to save.
     * @param tournamentId the id of the adminOutcome to save.
     * @param adminOutcome the adminOutcome to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated adminOutcome,
     * or with status {@code 400 (Bad Request)} if the adminOutcome is not valid,
     * or with status {@code 500 (Internal Server Error)} if the adminOutcome couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/admin-outcomes/{gameId}_{userId}_{tournamentId}")
    public ResponseEntity<AdminOutcome> updateAdminOutcome(
        @PathVariable(value = "gameId", required = false) final Long gameId,
        @PathVariable(value = "userId", required = false) final String userId,
        @PathVariable(value = "tournamentId", required = false) final Long tournamentId,
        @Valid @RequestBody AdminOutcome adminOutcome
    ) throws URISyntaxException {
        log.debug("REST request to update AdminOutcome : {}, {} {} {}", gameId, userId, tournamentId, adminOutcome);
        if (adminOutcome.getGame() == null || adminOutcome.getUser() == null || adminOutcome.getTournament() == null) {
            throw new BadRequestAlertException("Invalid adminOutcomeId", ENTITY_NAME, "adminoutcomeidnull");
        }
        if (!Objects.equals(gameId, adminOutcome.getGame().getId())) {
            throw new BadRequestAlertException("Invalid AdminOutcomeId", ENTITY_NAME, "gameidinvalid");
        }
        if (!Objects.equals(userId, adminOutcome.getUser().getId())) {
            throw new BadRequestAlertException("Invalid AdminOutcomeId", ENTITY_NAME, "useridinvalid");
        }
        if (!Objects.equals(tournamentId, adminOutcome.getTournament().getId())) {
            throw new BadRequestAlertException("Invalid AdminOutcomeId", ENTITY_NAME, "tournamentidinvalid");
        }

        if (
            !adminOutcomeRepository.existsById(
                new AdminOutcomeId(adminOutcome.getGame().getId(), adminOutcome.getUser().getId(), adminOutcome.getTournament().getId())
            )
        ) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "adminoutcomeidnotfound");
        }

        AdminOutcome result = adminOutcomeService.save(adminOutcome);
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
     * {@code PATCH  /admin-outcomes/:id} : Partial updates given fields of an existing adminOutcome, field will ignore if it is null
     *
     * @param gameId the id of the adminOutcome to save.
     * @param userId the id of the adminOutcome to save.
     * @param tournamentId the id of the adminOutcome to save.
     * @param adminOutcome the adminOutcome to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated adminOutcome,
     * or with status {@code 400 (Bad Request)} if the adminOutcome is not valid,
     * or with status {@code 404 (Not Found)} if the adminOutcome is not found,
     * or with status {@code 500 (Internal Server Error)} if the adminOutcome couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/admin-outcomes/{gameId}_{userId}_{tournamentId}", consumes = "application/merge-patch+json")
    public ResponseEntity<AdminOutcome> partialUpdateAdminOutcome(
        @PathVariable(value = "gameId", required = false) final Long gameId,
        @PathVariable(value = "userId", required = false) final String userId,
        @PathVariable(value = "tournamentId", required = false) final Long tournamentId,
        @NotNull @RequestBody AdminOutcome adminOutcome
    ) throws URISyntaxException {
        log.debug("REST request to partial update AdminOutcome partially : {}, {} {} {}", gameId, userId, tournamentId, adminOutcome);
        if (adminOutcome.getGame() == null || adminOutcome.getUser() == null || adminOutcome.getTournament() == null) {
            throw new BadRequestAlertException("Invalid adminOutcomeId", ENTITY_NAME, "adminoutcomeidnull");
        }
        if (!Objects.equals(gameId, adminOutcome.getGame().getId())) {
            throw new BadRequestAlertException("Invalid adminOutcomeId", ENTITY_NAME, "gameidinvalid");
        }
        if (!Objects.equals(userId, adminOutcome.getUser().getId())) {
            throw new BadRequestAlertException("Invalid adminOutcomeId", ENTITY_NAME, "useridinvalid");
        }
        if (!Objects.equals(tournamentId, adminOutcome.getTournament().getId())) {
            throw new BadRequestAlertException("Invalid adminOutcomeId", ENTITY_NAME, "tournamentidinvalid");
        }

        if (
            !adminOutcomeRepository.existsById(
                new AdminOutcomeId(adminOutcome.getGame().getId(), adminOutcome.getUser().getId(), adminOutcome.getTournament().getId())
            )
        ) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "adminoutcomeidnotfound");
        }

        Optional<AdminOutcome> result = adminOutcomeService.partialUpdate(adminOutcome);

        StringBuffer sb = new StringBuffer()
            .append(result.get().getGame().getId())
            .append("_")
            .append(result.get().getUser().getId())
            .append("_")
            .append(result.get().getTournament().getId());
        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sb.toString()));
    }

    /**
     * {@code GET  /admin-outcomes} : get all the adminOutcomes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of adminOutcomes in body.
     */
    @GetMapping("/admin-outcomes")
    public ResponseEntity<List<AdminOutcome>> getAllAdminOutcomes(AdminOutcomeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get AdminOutcomes by criteria: {}", criteria);
        Page<AdminOutcome> page = adminOutcomeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /admin-outcomes/count} : count all the adminOutcomes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/admin-outcomes/count")
    public ResponseEntity<Long> countAdminOutcomes(AdminOutcomeCriteria criteria) {
        log.debug("REST request to count AdminOutcomes by criteria: {}", criteria);
        return ResponseEntity.ok().body(adminOutcomeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /admin-outcomes/:gameId_userId_tournamentId} : get the composite "id" adminOutcome.
     *
     * @param gameId the id of the adminOutcome to retrieve.
     * @param userId the id of the adminOutcome to retrieve.
     * @param tournamentId the id of the adminOutcome to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the adminOutcome, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/admin-outcomes/{gameId}_{userId}_{tournamentId}")
    public ResponseEntity<AdminOutcome> getAdminOutcome(
        @PathVariable Long gameId,
        @PathVariable String userId,
        @PathVariable Long tournamentId
    ) {
        log.debug("REST request to get AdminOutcome : {} {} {} ", gameId, userId, tournamentId);
        Optional<AdminOutcome> adminOutcome = adminOutcomeService.findOne(new AdminOutcomeId(gameId, userId, tournamentId));
        return ResponseUtil.wrapOrNotFound(adminOutcome);
    }

    /**
     * {@code DELETE  /admin-outcomes/:gameId_userId_tournamentId} : delete the composite "id" adminOutcome.
     *
     * @param gameId the id of the adminOutcome to delete.
     * @param userId the id of the adminOutcome to delete.
     * @param tournamentId the id of the adminOutcome to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/admin-outcomes/{gameId}_{userId}_{tournamentId}")
    public ResponseEntity<Void> deleteAdminOutcome(
        @PathVariable Long gameId,
        @PathVariable String userId,
        @PathVariable Long tournamentId
    ) {
        log.debug("REST request to delete AdminOutcome : {} {} {} ", gameId, userId, tournamentId);
        log.info("############################REST request to delete AdminOutcome : {} {} {} ", gameId, userId, tournamentId);

        adminOutcomeService.delete(new AdminOutcomeId(gameId, userId, tournamentId));

        StringBuffer sb = new StringBuffer().append(gameId).append("_").append(userId).append("_").append(tournamentId);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, sb.toString()))
            .build();
    }
}

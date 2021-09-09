package com.sheepit.app.web.rest;

import com.sheepit.app.domain.UserOutcome;
import com.sheepit.app.domain.UserOutcomeId;
import com.sheepit.app.repository.UserOutcomeRepository;
import com.sheepit.app.service.UserOutcomeQueryService;
import com.sheepit.app.service.UserOutcomeService;
import com.sheepit.app.service.criteria.UserOutcomeCriteria;
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
 * REST controller for managing {@link com.sheepit.app.domain.UserOutcome}.
 */
@RestController
@RequestMapping("/api")
public class UserOutcomeResource {

    private final Logger log = LoggerFactory.getLogger(UserOutcomeResource.class);

    private static final String ENTITY_NAME = "userOutcome";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserOutcomeService userOutcomeService;

    private final UserOutcomeRepository userOutcomeRepository;

    private final UserOutcomeQueryService userOutcomeQueryService;

    public UserOutcomeResource(
        UserOutcomeService userOutcomeService,
        UserOutcomeRepository userOutcomeRepository,
        UserOutcomeQueryService userOutcomeQueryService
    ) {
        this.userOutcomeService = userOutcomeService;
        this.userOutcomeRepository = userOutcomeRepository;
        this.userOutcomeQueryService = userOutcomeQueryService;
    }

    /**
     * {@code POST  /user-outcomes} : Create a new userOutcome.
     *
     * @param userOutcome the userOutcome to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userOutcome, or with status {@code 400 (Bad Request)} if the userOutcome has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-outcomes")
    public ResponseEntity<UserOutcome> createUserOutcome(@Valid @RequestBody UserOutcome userOutcome) throws URISyntaxException {
        log.debug("REST request to save UserOutcome : {}", userOutcome);
        if (userOutcome.getId() != null) {
            throw new BadRequestAlertException("A new userOutcome cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserOutcome result = userOutcomeService.save(userOutcome);
        StringBuffer sb = new StringBuffer()
            .append(result.getGame().getId())
            .append("_")
            .append(result.getUser().getId())
            .append("_")
            .append(result.getTournament().getId());
        return ResponseEntity
            .created(new URI("/api/user-outcomes/" + sb.toString()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, sb.toString()))
            .body(result);
    }

    //    /**
    //     * {@code PUT  /user-outcomes/:id} : Updates an existing userOutcome.
    //     *
    //     * @param id the id of the userOutcome to save.
    //     * @param userOutcome the userOutcome to update.
    //     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userOutcome,
    //     * or with status {@code 400 (Bad Request)} if the userOutcome is not valid,
    //     * or with status {@code 500 (Internal Server Error)} if the userOutcome couldn't be updated.
    //     * @throws URISyntaxException if the Location URI syntax is incorrect.
    //     */
    //    @PutMapping("/user-outcomes/{id}")
    //    public ResponseEntity<UserOutcome> updateUserOutcome(
    //        @PathVariable(value = "id", required = false) final Long id,
    //        @Valid @RequestBody UserOutcome userOutcome
    //    ) throws URISyntaxException {
    //        log.debug("REST request to update UserOutcome : {}, {}", id, userOutcome);
    //        if (userOutcome.getId() == null) {
    //            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    //        }
    //        if (!Objects.equals(id, userOutcome.getId())) {
    //            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
    //        }
    //
    //        if (!userOutcomeRepository.existsById(id)) {
    //            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
    //        }
    //
    //        UserOutcome result = userOutcomeService.save(userOutcome);
    //        return ResponseEntity
    //            .ok()
    //            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userOutcome.getId().toString()))
    //            .body(result);
    //    }

    /**
     * {@code PUT  /user-outcomes/:id} : Updates an existing userOutcome.
     *
     * @param gameId the id of the userOutcome to save.
     * @param userId the id of the userOutcome to save.
     * @param tournamentId the id of the userOutcome to save.
     * @param userOutcome the userOutcome to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userOutcome,
     * or with status {@code 400 (Bad Request)} if the userOutcome is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userOutcome couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-outcomes/{gameId}_{userId}_{tournamentId}")
    public ResponseEntity<UserOutcome> updateUserOutcome(
        @PathVariable(value = "gameId", required = false) final Long gameId,
        @PathVariable(value = "userId", required = false) final String userId,
        @PathVariable(value = "tournamentId", required = false) final Long tournamentId,
        @Valid @RequestBody UserOutcome userOutcome
    ) throws URISyntaxException {
        log.debug("REST request to update UserOutcome : {}, {} {} {}", gameId, userId, tournamentId, userOutcome);
        if (userOutcome.getGame() == null || userOutcome.getUser() == null || userOutcome.getTournament() == null) {
            throw new BadRequestAlertException("Invalid userOutcomeId", ENTITY_NAME, "useroutcomeidnull");
        }
        if (!Objects.equals(gameId, userOutcome.getGame().getId())) {
            throw new BadRequestAlertException("Invalid UserOutcomeId", ENTITY_NAME, "gameidinvalid");
        }
        if (!Objects.equals(userId, userOutcome.getUser().getId())) {
            throw new BadRequestAlertException("Invalid UserOutcomeId", ENTITY_NAME, "useridinvalid");
        }
        if (!Objects.equals(tournamentId, userOutcome.getTournament().getId())) {
            throw new BadRequestAlertException("Invalid UserOutcomeId", ENTITY_NAME, "tournamentidinvalid");
        }

        if (
            !userOutcomeRepository.existsById(
                new UserOutcomeId(userOutcome.getGame().getId(), userOutcome.getUser().getId(), userOutcome.getTournament().getId())
            )
        ) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "useroutcomeidnotfound");
        }

        UserOutcome result = userOutcomeService.save(userOutcome);
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

    //    /**
    //     * {@code PATCH  /user-outcomes/:id} : Partial updates given fields of an existing userOutcome, field will ignore if it is null
    //     *
    //     * @param id the id of the userOutcome to save.
    //     * @param userOutcome the userOutcome to update.
    //     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userOutcome,
    //     * or with status {@code 400 (Bad Request)} if the userOutcome is not valid,
    //     * or with status {@code 404 (Not Found)} if the userOutcome is not found,
    //     * or with status {@code 500 (Internal Server Error)} if the userOutcome couldn't be updated.
    //     * @throws URISyntaxException if the Location URI syntax is incorrect.
    //     */
    //    @PatchMapping(value = "/user-outcomes/{id}", consumes = "application/merge-patch+json")
    //    public ResponseEntity<UserOutcome> partialUpdateUserOutcome(
    //        @PathVariable(value = "id", required = false) final Long id,
    //        @NotNull @RequestBody UserOutcome userOutcome
    //    ) throws URISyntaxException {
    //        log.debug("REST request to partial update UserOutcome partially : {}, {}", id, userOutcome);
    //        if (userOutcome.getId() == null) {
    //            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    //        }
    //        if (!Objects.equals(id, userOutcome.getId())) {
    //            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
    //        }
    //
    //        if (!userOutcomeRepository.existsById(id)) {
    //            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
    //        }
    //
    //        Optional<UserOutcome> result = userOutcomeService.partialUpdate(userOutcome);
    //
    //        return ResponseUtil.wrapOrNotFound(
    //            result,
    //            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userOutcome.getId().toString())
    //        );
    //    }

    /**
     * {@code PATCH  /user-outcomes/:id} : Partial updates given fields of an existing userOutcome, field will ignore if it is null
     *
     * @param gameId the id of the userOutcome to save.
     * @param userId the id of the userOutcome to save.
     * @param tournamentId the id of the userOutcome to save.
     * @param userOutcome the userOutcome to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userOutcome,
     * or with status {@code 400 (Bad Request)} if the userOutcome is not valid,
     * or with status {@code 404 (Not Found)} if the userOutcome is not found,
     * or with status {@code 500 (Internal Server Error)} if the userOutcome couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/user-outcomes/{gameId}_{userId}_{tournamentId}", consumes = "application/merge-patch+json")
    public ResponseEntity<UserOutcome> partialUpdateUserOutcome(
        @PathVariable(value = "gameId", required = false) final Long gameId,
        @PathVariable(value = "userId", required = false) final String userId,
        @PathVariable(value = "tournamentId", required = false) final Long tournamentId,
        @NotNull @RequestBody UserOutcome userOutcome
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserOutcome partially : {}, {} {} {}", gameId, userId, tournamentId, userOutcome);
        if (userOutcome.getGame() == null || userOutcome.getUser() == null || userOutcome.getTournament() == null) {
            throw new BadRequestAlertException("Invalid userOutcomeId", ENTITY_NAME, "useroutcomeidnull");
        }
        if (!Objects.equals(gameId, userOutcome.getGame().getId())) {
            throw new BadRequestAlertException("Invalid UserOutcomeId", ENTITY_NAME, "gameidinvalid");
        }
        if (!Objects.equals(userId, userOutcome.getUser().getId())) {
            throw new BadRequestAlertException("Invalid UserOutcomeId", ENTITY_NAME, "useridinvalid");
        }
        if (!Objects.equals(tournamentId, userOutcome.getTournament().getId())) {
            throw new BadRequestAlertException("Invalid UserOutcomeId", ENTITY_NAME, "tournamentidinvalid");
        }

        if (
            !userOutcomeRepository.existsById(
                new UserOutcomeId(userOutcome.getGame().getId(), userOutcome.getUser().getId(), userOutcome.getTournament().getId())
            )
        ) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "useroutcomeidnotfound");
        }

        Optional<UserOutcome> result = userOutcomeService.partialUpdate(userOutcome);

        //        UserOutcome result = userOutcomeService.save(userOutcome);
        StringBuffer sb = new StringBuffer()
            .append(result.get().getGame().getId())
            .append("_")
            .append(result.get().getUser().getId())
            .append("_")
            .append(result.get().getTournament().getId());
        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, sb.toString()));
    }

    /**
     * {@code GET  /user-outcomes} : get all the userOutcomes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userOutcomes in body.
     */
    @GetMapping("/user-outcomes")
    public ResponseEntity<List<UserOutcome>> getAllUserOutcomes(UserOutcomeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get UserOutcomes by criteria: {}", criteria);
        Page<UserOutcome> page = userOutcomeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /user-outcomes/count} : count all the userOutcomes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/user-outcomes/count")
    public ResponseEntity<Long> countUserOutcomes(UserOutcomeCriteria criteria) {
        log.debug("REST request to count UserOutcomes by criteria: {}", criteria);
        return ResponseEntity.ok().body(userOutcomeQueryService.countByCriteria(criteria));
    }

    //    /**
    //     * {@code GET  /user-outcomes/:id} : get the "id" userOutcome.
    //     *
    //     * @param id the id of the userOutcome to retrieve.
    //     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userOutcome, or with status {@code 404 (Not Found)}.
    //     */
    //    @GetMapping("/user-outcomes/{id}")
    //    public ResponseEntity<UserOutcome> getUserOutcome(@PathVariable Long id) {
    //        log.debug("REST request to get UserOutcome : {}", id);
    //        Optional<UserOutcome> userOutcome = userOutcomeService.findOne(id);
    //        return ResponseUtil.wrapOrNotFound(userOutcome);
    //    }

    /**
     * {@code GET  /user-outcomes/:gameId_userId_tournamentId} : get the composite "id" userOutcome.
     *
     * @param gameId the id of the userOutcome to retrieve.
     * @param userId the id of the userOutcome to retrieve.
     * @param tournamentId the id of the userOutcome to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userOutcome, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-outcomes/{gameId}_{userId}_{tournamentId}")
    public ResponseEntity<UserOutcome> getUserOutcome(
        @PathVariable Long gameId,
        @PathVariable String userId,
        @PathVariable Long tournamentId
    ) {
        log.debug("REST request to get UserOutcome : {} {} {} ", gameId, userId, tournamentId);
        Optional<UserOutcome> userOutcome = userOutcomeService.findOne(new UserOutcomeId(gameId, userId, tournamentId));
        return ResponseUtil.wrapOrNotFound(userOutcome);
    }

    //    /**
    //     * {@code DELETE  /user-outcomes/:id} : delete the "id" userOutcome.
    //     *
    //     * @param id the id of the userOutcome to delete.
    //     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
    //     */
    //    @DeleteMapping("/user-outcomes/{id}")
    //    public ResponseEntity<Void> deleteUserOutcome(@PathVariable Long id) {
    //        log.debug("REST request to delete UserOutcome : {}", id);
    //        userOutcomeService.delete(id);
    //        return ResponseEntity
    //            .noContent()
    //            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
    //            .build();
    //    }

    /**
     * {@code DELETE  /user-outcomes/:gameId_userId_tournamentId} : delete the composite "id" userOutcome.
     *
     * @param gameId the id of the userOutcome to delete.
     * @param userId the id of the userOutcome to delete.
     * @param tournamentId the id of the userOutcome to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-outcomes/{gameId}_{userId}_{tournamentId}")
    public ResponseEntity<Void> deleteUserOutcome(@PathVariable Long gameId, @PathVariable String userId, @PathVariable Long tournamentId) {
        log.debug("REST request to delete UserOutcome : {} {} {} ", gameId, userId, tournamentId);
        log.info("############################REST request to delete UserOutcome : {} {} {} ", gameId, userId, tournamentId);

        userOutcomeService.delete(new UserOutcomeId(gameId, userId, tournamentId));

        StringBuffer sb = new StringBuffer().append(gameId).append("_").append(userId).append("_").append(tournamentId);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, sb.toString()))
            .build();
    }
}

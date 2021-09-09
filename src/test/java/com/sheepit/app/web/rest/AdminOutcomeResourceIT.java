package com.sheepit.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sheepit.app.IntegrationTest;
import com.sheepit.app.domain.*;
import com.sheepit.app.repository.AdminOutcomeRepository;
import com.sheepit.app.service.criteria.AdminOutcomeCriteria;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AdminOutcomeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AdminOutcomeResourceIT {

    private static final String DEFAULT_END_OUTCOME_OPPONENT_A = "450";
    private static final String UPDATED_END_OUTCOME_OPPONENT_A = "93549";

    private static final String DEFAULT_END_OUTCOME_OPPONENT_B = "106";
    private static final String UPDATED_END_OUTCOME_OPPONENT_B = "49";

    private static final String ENTITY_API_URL = "/api/admin-outcomes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AdminOutcomeRepository adminOutcomeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAdminOutcomeMockMvc;

    private AdminOutcome adminOutcome;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AdminOutcome createEntity(EntityManager em) {
        AdminOutcome adminOutcome = new AdminOutcome()
            .endOutcomeOpponentA(DEFAULT_END_OUTCOME_OPPONENT_A)
            .endOutcomeOpponentB(DEFAULT_END_OUTCOME_OPPONENT_B);
        // Add required entity
        Game game;
        if (TestUtil.findAll(em, Game.class).isEmpty()) {
            game = GameResourceIT.createEntity(em);
            em.persist(game);
            em.flush();
        } else {
            game = TestUtil.findAll(em, Game.class).get(0);
        }
        adminOutcome.setGame(game);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        adminOutcome.setUser(user);
        // Add required entity
        Tournament tournament;
        if (TestUtil.findAll(em, Tournament.class).isEmpty()) {
            tournament = TournamentResourceIT.createEntity(em);
            em.persist(tournament);
            em.flush();
        } else {
            tournament = TestUtil.findAll(em, Tournament.class).get(0);
        }
        adminOutcome.setTournament(tournament);
        return adminOutcome;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AdminOutcome createUpdatedEntity(EntityManager em) {
        AdminOutcome adminOutcome = new AdminOutcome()
            .endOutcomeOpponentA(UPDATED_END_OUTCOME_OPPONENT_A)
            .endOutcomeOpponentB(UPDATED_END_OUTCOME_OPPONENT_B);
        // Add required entity
        Game game;
        if (TestUtil.findAll(em, Game.class).isEmpty()) {
            game = GameResourceIT.createUpdatedEntity(em);
            em.persist(game);
            em.flush();
        } else {
            game = TestUtil.findAll(em, Game.class).get(0);
        }
        adminOutcome.setGame(game);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        adminOutcome.setUser(user);
        // Add required entity
        Tournament tournament;
        if (TestUtil.findAll(em, Tournament.class).isEmpty()) {
            tournament = TournamentResourceIT.createUpdatedEntity(em);
            em.persist(tournament);
            em.flush();
        } else {
            tournament = TestUtil.findAll(em, Tournament.class).get(0);
        }
        adminOutcome.setTournament(tournament);
        return adminOutcome;
    }

    @BeforeEach
    public void initTest() {
        adminOutcome = createEntity(em);
    }

    @Test
    @Transactional
    void createAdminOutcome() throws Exception {
        int databaseSizeBeforeCreate = adminOutcomeRepository.findAll().size();
        // Create the AdminOutcome
        restAdminOutcomeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(adminOutcome))
            )
            .andExpect(status().isCreated());

        // Validate the AdminOutcome in the database
        List<AdminOutcome> adminOutcomeList = adminOutcomeRepository.findAll();
        assertThat(adminOutcomeList).hasSize(databaseSizeBeforeCreate + 1);
        AdminOutcome testAdminOutcome = adminOutcomeList.get(adminOutcomeList.size() - 1);
        assertThat(testAdminOutcome.getEndOutcomeOpponentA()).isEqualTo(DEFAULT_END_OUTCOME_OPPONENT_A);
        assertThat(testAdminOutcome.getEndOutcomeOpponentB()).isEqualTo(DEFAULT_END_OUTCOME_OPPONENT_B);
    }

    @Test
    @Transactional
    void createAdminOutcomeWithExistingId() throws Exception {
        // Create the AdminOutcome with an existing ID
        adminOutcome.setId(1L);

        int databaseSizeBeforeCreate = adminOutcomeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAdminOutcomeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(adminOutcome))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdminOutcome in the database
        List<AdminOutcome> adminOutcomeList = adminOutcomeRepository.findAll();
        assertThat(adminOutcomeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAdminOutcomes() throws Exception {
        // Initialize the database
        adminOutcomeRepository.saveAndFlush(adminOutcome);

        // Get all the adminOutcomeList
        restAdminOutcomeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(adminOutcome.getId().intValue())))
            .andExpect(jsonPath("$.[*].endOutcomeOpponentA").value(hasItem(DEFAULT_END_OUTCOME_OPPONENT_A)))
            .andExpect(jsonPath("$.[*].endOutcomeOpponentB").value(hasItem(DEFAULT_END_OUTCOME_OPPONENT_B)));
    }

    @Test
    @Transactional
    void getAdminOutcome() throws Exception {
        // Initialize the database
        adminOutcomeRepository.saveAndFlush(adminOutcome);

        // Get the adminOutcome
        restAdminOutcomeMockMvc
            .perform(get(ENTITY_API_URL_ID, adminOutcome.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(adminOutcome.getId().intValue()))
            .andExpect(jsonPath("$.endOutcomeOpponentA").value(DEFAULT_END_OUTCOME_OPPONENT_A))
            .andExpect(jsonPath("$.endOutcomeOpponentB").value(DEFAULT_END_OUTCOME_OPPONENT_B));
    }

    @Test
    @Transactional
    void getAdminOutcomesByIdFiltering() throws Exception {
        // Initialize the database
        adminOutcomeRepository.saveAndFlush(adminOutcome);

        Long id = adminOutcome.getId();

        defaultAdminOutcomeShouldBeFound("id.equals=" + id);
        defaultAdminOutcomeShouldNotBeFound("id.notEquals=" + id);

        defaultAdminOutcomeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAdminOutcomeShouldNotBeFound("id.greaterThan=" + id);

        defaultAdminOutcomeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAdminOutcomeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAdminOutcomesByEndOutcomeOpponentAIsEqualToSomething() throws Exception {
        // Initialize the database
        adminOutcomeRepository.saveAndFlush(adminOutcome);

        // Get all the adminOutcomeList where endOutcomeOpponentA equals to DEFAULT_END_OUTCOME_OPPONENT_A
        defaultAdminOutcomeShouldBeFound("endOutcomeOpponentA.equals=" + DEFAULT_END_OUTCOME_OPPONENT_A);

        // Get all the adminOutcomeList where endOutcomeOpponentA equals to UPDATED_END_OUTCOME_OPPONENT_A
        defaultAdminOutcomeShouldNotBeFound("endOutcomeOpponentA.equals=" + UPDATED_END_OUTCOME_OPPONENT_A);
    }

    @Test
    @Transactional
    void getAllAdminOutcomesByEndOutcomeOpponentAIsNotEqualToSomething() throws Exception {
        // Initialize the database
        adminOutcomeRepository.saveAndFlush(adminOutcome);

        // Get all the adminOutcomeList where endOutcomeOpponentA not equals to DEFAULT_END_OUTCOME_OPPONENT_A
        defaultAdminOutcomeShouldNotBeFound("endOutcomeOpponentA.notEquals=" + DEFAULT_END_OUTCOME_OPPONENT_A);

        // Get all the adminOutcomeList where endOutcomeOpponentA not equals to UPDATED_END_OUTCOME_OPPONENT_A
        defaultAdminOutcomeShouldBeFound("endOutcomeOpponentA.notEquals=" + UPDATED_END_OUTCOME_OPPONENT_A);
    }

    @Test
    @Transactional
    void getAllAdminOutcomesByEndOutcomeOpponentAIsInShouldWork() throws Exception {
        // Initialize the database
        adminOutcomeRepository.saveAndFlush(adminOutcome);

        // Get all the adminOutcomeList where endOutcomeOpponentA in DEFAULT_END_OUTCOME_OPPONENT_A or UPDATED_END_OUTCOME_OPPONENT_A
        defaultAdminOutcomeShouldBeFound("endOutcomeOpponentA.in=" + DEFAULT_END_OUTCOME_OPPONENT_A + "," + UPDATED_END_OUTCOME_OPPONENT_A);

        // Get all the adminOutcomeList where endOutcomeOpponentA equals to UPDATED_END_OUTCOME_OPPONENT_A
        defaultAdminOutcomeShouldNotBeFound("endOutcomeOpponentA.in=" + UPDATED_END_OUTCOME_OPPONENT_A);
    }

    @Test
    @Transactional
    void getAllAdminOutcomesByEndOutcomeOpponentAIsNullOrNotNull() throws Exception {
        // Initialize the database
        adminOutcomeRepository.saveAndFlush(adminOutcome);

        // Get all the adminOutcomeList where endOutcomeOpponentA is not null
        defaultAdminOutcomeShouldBeFound("endOutcomeOpponentA.specified=true");

        // Get all the adminOutcomeList where endOutcomeOpponentA is null
        defaultAdminOutcomeShouldNotBeFound("endOutcomeOpponentA.specified=false");
    }

    @Test
    @Transactional
    void getAllAdminOutcomesByEndOutcomeOpponentAContainsSomething() throws Exception {
        // Initialize the database
        adminOutcomeRepository.saveAndFlush(adminOutcome);

        // Get all the adminOutcomeList where endOutcomeOpponentA contains DEFAULT_END_OUTCOME_OPPONENT_A
        defaultAdminOutcomeShouldBeFound("endOutcomeOpponentA.contains=" + DEFAULT_END_OUTCOME_OPPONENT_A);

        // Get all the adminOutcomeList where endOutcomeOpponentA contains UPDATED_END_OUTCOME_OPPONENT_A
        defaultAdminOutcomeShouldNotBeFound("endOutcomeOpponentA.contains=" + UPDATED_END_OUTCOME_OPPONENT_A);
    }

    @Test
    @Transactional
    void getAllAdminOutcomesByEndOutcomeOpponentANotContainsSomething() throws Exception {
        // Initialize the database
        adminOutcomeRepository.saveAndFlush(adminOutcome);

        // Get all the adminOutcomeList where endOutcomeOpponentA does not contain DEFAULT_END_OUTCOME_OPPONENT_A
        defaultAdminOutcomeShouldNotBeFound("endOutcomeOpponentA.doesNotContain=" + DEFAULT_END_OUTCOME_OPPONENT_A);

        // Get all the adminOutcomeList where endOutcomeOpponentA does not contain UPDATED_END_OUTCOME_OPPONENT_A
        defaultAdminOutcomeShouldBeFound("endOutcomeOpponentA.doesNotContain=" + UPDATED_END_OUTCOME_OPPONENT_A);
    }

    @Test
    @Transactional
    void getAllAdminOutcomesByEndOutcomeOpponentBIsEqualToSomething() throws Exception {
        // Initialize the database
        adminOutcomeRepository.saveAndFlush(adminOutcome);

        // Get all the adminOutcomeList where endOutcomeOpponentB equals to DEFAULT_END_OUTCOME_OPPONENT_B
        defaultAdminOutcomeShouldBeFound("endOutcomeOpponentB.equals=" + DEFAULT_END_OUTCOME_OPPONENT_B);

        // Get all the adminOutcomeList where endOutcomeOpponentB equals to UPDATED_END_OUTCOME_OPPONENT_B
        defaultAdminOutcomeShouldNotBeFound("endOutcomeOpponentB.equals=" + UPDATED_END_OUTCOME_OPPONENT_B);
    }

    @Test
    @Transactional
    void getAllAdminOutcomesByEndOutcomeOpponentBIsNotEqualToSomething() throws Exception {
        // Initialize the database
        adminOutcomeRepository.saveAndFlush(adminOutcome);

        // Get all the adminOutcomeList where endOutcomeOpponentB not equals to DEFAULT_END_OUTCOME_OPPONENT_B
        defaultAdminOutcomeShouldNotBeFound("endOutcomeOpponentB.notEquals=" + DEFAULT_END_OUTCOME_OPPONENT_B);

        // Get all the adminOutcomeList where endOutcomeOpponentB not equals to UPDATED_END_OUTCOME_OPPONENT_B
        defaultAdminOutcomeShouldBeFound("endOutcomeOpponentB.notEquals=" + UPDATED_END_OUTCOME_OPPONENT_B);
    }

    @Test
    @Transactional
    void getAllAdminOutcomesByEndOutcomeOpponentBIsInShouldWork() throws Exception {
        // Initialize the database
        adminOutcomeRepository.saveAndFlush(adminOutcome);

        // Get all the adminOutcomeList where endOutcomeOpponentB in DEFAULT_END_OUTCOME_OPPONENT_B or UPDATED_END_OUTCOME_OPPONENT_B
        defaultAdminOutcomeShouldBeFound("endOutcomeOpponentB.in=" + DEFAULT_END_OUTCOME_OPPONENT_B + "," + UPDATED_END_OUTCOME_OPPONENT_B);

        // Get all the adminOutcomeList where endOutcomeOpponentB equals to UPDATED_END_OUTCOME_OPPONENT_B
        defaultAdminOutcomeShouldNotBeFound("endOutcomeOpponentB.in=" + UPDATED_END_OUTCOME_OPPONENT_B);
    }

    @Test
    @Transactional
    void getAllAdminOutcomesByEndOutcomeOpponentBIsNullOrNotNull() throws Exception {
        // Initialize the database
        adminOutcomeRepository.saveAndFlush(adminOutcome);

        // Get all the adminOutcomeList where endOutcomeOpponentB is not null
        defaultAdminOutcomeShouldBeFound("endOutcomeOpponentB.specified=true");

        // Get all the adminOutcomeList where endOutcomeOpponentB is null
        defaultAdminOutcomeShouldNotBeFound("endOutcomeOpponentB.specified=false");
    }

    @Test
    @Transactional
    void getAllAdminOutcomesByEndOutcomeOpponentBContainsSomething() throws Exception {
        // Initialize the database
        adminOutcomeRepository.saveAndFlush(adminOutcome);

        // Get all the adminOutcomeList where endOutcomeOpponentB contains DEFAULT_END_OUTCOME_OPPONENT_B
        defaultAdminOutcomeShouldBeFound("endOutcomeOpponentB.contains=" + DEFAULT_END_OUTCOME_OPPONENT_B);

        // Get all the adminOutcomeList where endOutcomeOpponentB contains UPDATED_END_OUTCOME_OPPONENT_B
        defaultAdminOutcomeShouldNotBeFound("endOutcomeOpponentB.contains=" + UPDATED_END_OUTCOME_OPPONENT_B);
    }

    @Test
    @Transactional
    void getAllAdminOutcomesByEndOutcomeOpponentBNotContainsSomething() throws Exception {
        // Initialize the database
        adminOutcomeRepository.saveAndFlush(adminOutcome);

        // Get all the adminOutcomeList where endOutcomeOpponentB does not contain DEFAULT_END_OUTCOME_OPPONENT_B
        defaultAdminOutcomeShouldNotBeFound("endOutcomeOpponentB.doesNotContain=" + DEFAULT_END_OUTCOME_OPPONENT_B);

        // Get all the adminOutcomeList where endOutcomeOpponentB does not contain UPDATED_END_OUTCOME_OPPONENT_B
        defaultAdminOutcomeShouldBeFound("endOutcomeOpponentB.doesNotContain=" + UPDATED_END_OUTCOME_OPPONENT_B);
    }

    @Test
    @Transactional
    void getAllAdminOutcomesByGameIsEqualToSomething() throws Exception {
        // Get already existing entity
        Game game = adminOutcome.getGame();
        adminOutcomeRepository.saveAndFlush(adminOutcome);
        Long gameId = game.getId();

        // Get all the adminOutcomeList where game equals to gameId
        defaultAdminOutcomeShouldBeFound("gameId.equals=" + gameId);

        // Get all the adminOutcomeList where game equals to (gameId + 1)
        defaultAdminOutcomeShouldNotBeFound("gameId.equals=" + (gameId + 1));
    }

    @Test
    @Transactional
    void getAllAdminOutcomesByUserIsEqualToSomething() throws Exception {
        // Get already existing entity
        User user = adminOutcome.getUser();
        adminOutcomeRepository.saveAndFlush(adminOutcome);
        String userId = user.getId();

        // Get all the adminOutcomeList where user equals to userId
        defaultAdminOutcomeShouldBeFound("userId.equals=" + userId);

        // Get all the adminOutcomeList where user equals to "invalid-id"
        defaultAdminOutcomeShouldNotBeFound("userId.equals=" + "invalid-id");
    }

    @Test
    @Transactional
    void getAllAdminOutcomesByTournamentIsEqualToSomething() throws Exception {
        // Get already existing entity
        Tournament tournament = adminOutcome.getTournament();
        adminOutcomeRepository.saveAndFlush(adminOutcome);
        Long tournamentId = tournament.getId();

        // Get all the adminOutcomeList where tournament equals to tournamentId
        defaultAdminOutcomeShouldBeFound("tournamentId.equals=" + tournamentId);

        // Get all the adminOutcomeList where tournament equals to (tournamentId + 1)
        defaultAdminOutcomeShouldNotBeFound("tournamentId.equals=" + (tournamentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAdminOutcomeShouldBeFound(String filter) throws Exception {
        restAdminOutcomeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(adminOutcome.getId().intValue())))
            .andExpect(jsonPath("$.[*].endOutcomeOpponentA").value(hasItem(DEFAULT_END_OUTCOME_OPPONENT_A)))
            .andExpect(jsonPath("$.[*].endOutcomeOpponentB").value(hasItem(DEFAULT_END_OUTCOME_OPPONENT_B)));

        // Check, that the count call also returns 1
        restAdminOutcomeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAdminOutcomeShouldNotBeFound(String filter) throws Exception {
        restAdminOutcomeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAdminOutcomeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAdminOutcome() throws Exception {
        // Get the adminOutcome
        restAdminOutcomeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAdminOutcome() throws Exception {
        // Initialize the database
        adminOutcomeRepository.saveAndFlush(adminOutcome);

        int databaseSizeBeforeUpdate = adminOutcomeRepository.findAll().size();

        // Update the adminOutcome
        AdminOutcome updatedAdminOutcome = adminOutcomeRepository
            .findById(
                new AdminOutcomeId(adminOutcome.getGame().getId(), adminOutcome.getUser().getId(), adminOutcome.getTournament().getId())
            )
            .get();
        // Disconnect from session so that the updates on updatedAdminOutcome are not directly saved in db
        em.detach(updatedAdminOutcome);
        updatedAdminOutcome.endOutcomeOpponentA(UPDATED_END_OUTCOME_OPPONENT_A).endOutcomeOpponentB(UPDATED_END_OUTCOME_OPPONENT_B);

        restAdminOutcomeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAdminOutcome.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAdminOutcome))
            )
            .andExpect(status().isOk());

        // Validate the AdminOutcome in the database
        List<AdminOutcome> adminOutcomeList = adminOutcomeRepository.findAll();
        assertThat(adminOutcomeList).hasSize(databaseSizeBeforeUpdate);
        AdminOutcome testAdminOutcome = adminOutcomeList.get(adminOutcomeList.size() - 1);
        assertThat(testAdminOutcome.getEndOutcomeOpponentA()).isEqualTo(UPDATED_END_OUTCOME_OPPONENT_A);
        assertThat(testAdminOutcome.getEndOutcomeOpponentB()).isEqualTo(UPDATED_END_OUTCOME_OPPONENT_B);
    }

    @Test
    @Transactional
    void putNonExistingAdminOutcome() throws Exception {
        int databaseSizeBeforeUpdate = adminOutcomeRepository.findAll().size();
        adminOutcome.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdminOutcomeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, adminOutcome.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(adminOutcome))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdminOutcome in the database
        List<AdminOutcome> adminOutcomeList = adminOutcomeRepository.findAll();
        assertThat(adminOutcomeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAdminOutcome() throws Exception {
        int databaseSizeBeforeUpdate = adminOutcomeRepository.findAll().size();
        adminOutcome.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdminOutcomeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(adminOutcome))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdminOutcome in the database
        List<AdminOutcome> adminOutcomeList = adminOutcomeRepository.findAll();
        assertThat(adminOutcomeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAdminOutcome() throws Exception {
        int databaseSizeBeforeUpdate = adminOutcomeRepository.findAll().size();
        adminOutcome.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdminOutcomeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(adminOutcome))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AdminOutcome in the database
        List<AdminOutcome> adminOutcomeList = adminOutcomeRepository.findAll();
        assertThat(adminOutcomeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAdminOutcomeWithPatch() throws Exception {
        // Initialize the database
        adminOutcomeRepository.saveAndFlush(adminOutcome);

        int databaseSizeBeforeUpdate = adminOutcomeRepository.findAll().size();

        // Update the adminOutcome using partial update
        AdminOutcome partialUpdatedAdminOutcome = new AdminOutcome();
        partialUpdatedAdminOutcome.setId(adminOutcome.getId());

        partialUpdatedAdminOutcome.endOutcomeOpponentB(UPDATED_END_OUTCOME_OPPONENT_B);

        restAdminOutcomeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdminOutcome.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAdminOutcome))
            )
            .andExpect(status().isOk());

        // Validate the AdminOutcome in the database
        List<AdminOutcome> adminOutcomeList = adminOutcomeRepository.findAll();
        assertThat(adminOutcomeList).hasSize(databaseSizeBeforeUpdate);
        AdminOutcome testAdminOutcome = adminOutcomeList.get(adminOutcomeList.size() - 1);
        assertThat(testAdminOutcome.getEndOutcomeOpponentA()).isEqualTo(DEFAULT_END_OUTCOME_OPPONENT_A);
        assertThat(testAdminOutcome.getEndOutcomeOpponentB()).isEqualTo(UPDATED_END_OUTCOME_OPPONENT_B);
    }

    @Test
    @Transactional
    void fullUpdateAdminOutcomeWithPatch() throws Exception {
        // Initialize the database
        adminOutcomeRepository.saveAndFlush(adminOutcome);

        int databaseSizeBeforeUpdate = adminOutcomeRepository.findAll().size();

        // Update the adminOutcome using partial update
        AdminOutcome partialUpdatedAdminOutcome = new AdminOutcome();
        partialUpdatedAdminOutcome.setId(adminOutcome.getId());

        partialUpdatedAdminOutcome.endOutcomeOpponentA(UPDATED_END_OUTCOME_OPPONENT_A).endOutcomeOpponentB(UPDATED_END_OUTCOME_OPPONENT_B);

        restAdminOutcomeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdminOutcome.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAdminOutcome))
            )
            .andExpect(status().isOk());

        // Validate the AdminOutcome in the database
        List<AdminOutcome> adminOutcomeList = adminOutcomeRepository.findAll();
        assertThat(adminOutcomeList).hasSize(databaseSizeBeforeUpdate);
        AdminOutcome testAdminOutcome = adminOutcomeList.get(adminOutcomeList.size() - 1);
        assertThat(testAdminOutcome.getEndOutcomeOpponentA()).isEqualTo(UPDATED_END_OUTCOME_OPPONENT_A);
        assertThat(testAdminOutcome.getEndOutcomeOpponentB()).isEqualTo(UPDATED_END_OUTCOME_OPPONENT_B);
    }

    @Test
    @Transactional
    void patchNonExistingAdminOutcome() throws Exception {
        int databaseSizeBeforeUpdate = adminOutcomeRepository.findAll().size();
        adminOutcome.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdminOutcomeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, adminOutcome.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(adminOutcome))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdminOutcome in the database
        List<AdminOutcome> adminOutcomeList = adminOutcomeRepository.findAll();
        assertThat(adminOutcomeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAdminOutcome() throws Exception {
        int databaseSizeBeforeUpdate = adminOutcomeRepository.findAll().size();
        adminOutcome.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdminOutcomeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(adminOutcome))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdminOutcome in the database
        List<AdminOutcome> adminOutcomeList = adminOutcomeRepository.findAll();
        assertThat(adminOutcomeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAdminOutcome() throws Exception {
        int databaseSizeBeforeUpdate = adminOutcomeRepository.findAll().size();
        adminOutcome.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdminOutcomeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(adminOutcome))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AdminOutcome in the database
        List<AdminOutcome> adminOutcomeList = adminOutcomeRepository.findAll();
        assertThat(adminOutcomeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAdminOutcome() throws Exception {
        // Initialize the database
        adminOutcomeRepository.saveAndFlush(adminOutcome);

        int databaseSizeBeforeDelete = adminOutcomeRepository.findAll().size();

        // Delete the adminOutcome
        restAdminOutcomeMockMvc
            .perform(delete(ENTITY_API_URL_ID, adminOutcome.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AdminOutcome> adminOutcomeList = adminOutcomeRepository.findAll();
        assertThat(adminOutcomeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

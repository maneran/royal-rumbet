package com.sheepit.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sheepit.app.IntegrationTest;
import com.sheepit.app.domain.*;
import com.sheepit.app.repository.UserOutcomeRepository;
import com.sheepit.app.service.criteria.UserOutcomeCriteria;
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
 * Integration tests for the {@link UserOutcomeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserOutcomeResourceIT {

    private static final String DEFAULT_END_OUTCOME_OPPONENT_A = "04";
    private static final String UPDATED_END_OUTCOME_OPPONENT_A = "085";

    private static final String DEFAULT_END_OUTCOME_OPPONENT_B = "";
    private static final String UPDATED_END_OUTCOME_OPPONENT_B = "0";

    private static final String ENTITY_API_URL = "/api/user-outcomes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserOutcomeRepository userOutcomeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserOutcomeMockMvc;

    private UserOutcome userOutcome;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserOutcome createEntity(EntityManager em) {
        UserOutcome userOutcome = new UserOutcome()
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
        userOutcome.setGame(game);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        userOutcome.setUser(user);
        // Add required entity
        Tournament tournament;
        if (TestUtil.findAll(em, Tournament.class).isEmpty()) {
            tournament = TournamentResourceIT.createEntity(em);
            em.persist(tournament);
            em.flush();
        } else {
            tournament = TestUtil.findAll(em, Tournament.class).get(0);
        }
        userOutcome.setTournament(tournament);
        return userOutcome;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserOutcome createUpdatedEntity(EntityManager em) {
        UserOutcome userOutcome = new UserOutcome()
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
        userOutcome.setGame(game);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        userOutcome.setUser(user);
        // Add required entity
        Tournament tournament;
        if (TestUtil.findAll(em, Tournament.class).isEmpty()) {
            tournament = TournamentResourceIT.createUpdatedEntity(em);
            em.persist(tournament);
            em.flush();
        } else {
            tournament = TestUtil.findAll(em, Tournament.class).get(0);
        }
        userOutcome.setTournament(tournament);
        return userOutcome;
    }

    @BeforeEach
    public void initTest() {
        userOutcome = createEntity(em);
    }

    @Test
    @Transactional
    void createUserOutcome() throws Exception {
        int databaseSizeBeforeCreate = userOutcomeRepository.findAll().size();
        // Create the UserOutcome
        restUserOutcomeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userOutcome))
            )
            .andExpect(status().isCreated());

        // Validate the UserOutcome in the database
        List<UserOutcome> userOutcomeList = userOutcomeRepository.findAll();
        assertThat(userOutcomeList).hasSize(databaseSizeBeforeCreate + 1);
        UserOutcome testUserOutcome = userOutcomeList.get(userOutcomeList.size() - 1);
        assertThat(testUserOutcome.getEndOutcomeOpponentA()).isEqualTo(DEFAULT_END_OUTCOME_OPPONENT_A);
        assertThat(testUserOutcome.getEndOutcomeOpponentB()).isEqualTo(DEFAULT_END_OUTCOME_OPPONENT_B);
    }

    @Test
    @Transactional
    void createUserOutcomeWithExistingId() throws Exception {
        // Create the UserOutcome with an existing ID
        userOutcome.setId(1L);

        int databaseSizeBeforeCreate = userOutcomeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserOutcomeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userOutcome))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserOutcome in the database
        List<UserOutcome> userOutcomeList = userOutcomeRepository.findAll();
        assertThat(userOutcomeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUserOutcomes() throws Exception {
        // Initialize the database
        userOutcomeRepository.saveAndFlush(userOutcome);

        // Get all the userOutcomeList
        restUserOutcomeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userOutcome.getId().intValue())))
            .andExpect(jsonPath("$.[*].endOutcomeOpponentA").value(hasItem(DEFAULT_END_OUTCOME_OPPONENT_A)))
            .andExpect(jsonPath("$.[*].endOutcomeOpponentB").value(hasItem(DEFAULT_END_OUTCOME_OPPONENT_B)));
    }

    @Test
    @Transactional
    void getUserOutcome() throws Exception {
        // Initialize the database
        userOutcomeRepository.saveAndFlush(userOutcome);

        // Get the userOutcome
        restUserOutcomeMockMvc
            .perform(get(ENTITY_API_URL_ID, userOutcome.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userOutcome.getId().intValue()))
            .andExpect(jsonPath("$.endOutcomeOpponentA").value(DEFAULT_END_OUTCOME_OPPONENT_A))
            .andExpect(jsonPath("$.endOutcomeOpponentB").value(DEFAULT_END_OUTCOME_OPPONENT_B));
    }

    @Test
    @Transactional
    void getUserOutcomesByIdFiltering() throws Exception {
        // Initialize the database
        userOutcomeRepository.saveAndFlush(userOutcome);

        Long id = userOutcome.getId();

        defaultUserOutcomeShouldBeFound("id.equals=" + id);
        defaultUserOutcomeShouldNotBeFound("id.notEquals=" + id);

        defaultUserOutcomeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultUserOutcomeShouldNotBeFound("id.greaterThan=" + id);

        defaultUserOutcomeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultUserOutcomeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUserOutcomesByEndOutcomeOpponentAIsEqualToSomething() throws Exception {
        // Initialize the database
        userOutcomeRepository.saveAndFlush(userOutcome);

        // Get all the userOutcomeList where endOutcomeOpponentA equals to DEFAULT_END_OUTCOME_OPPONENT_A
        defaultUserOutcomeShouldBeFound("endOutcomeOpponentA.equals=" + DEFAULT_END_OUTCOME_OPPONENT_A);

        // Get all the userOutcomeList where endOutcomeOpponentA equals to UPDATED_END_OUTCOME_OPPONENT_A
        defaultUserOutcomeShouldNotBeFound("endOutcomeOpponentA.equals=" + UPDATED_END_OUTCOME_OPPONENT_A);
    }

    @Test
    @Transactional
    void getAllUserOutcomesByEndOutcomeOpponentAIsNotEqualToSomething() throws Exception {
        // Initialize the database
        userOutcomeRepository.saveAndFlush(userOutcome);

        // Get all the userOutcomeList where endOutcomeOpponentA not equals to DEFAULT_END_OUTCOME_OPPONENT_A
        defaultUserOutcomeShouldNotBeFound("endOutcomeOpponentA.notEquals=" + DEFAULT_END_OUTCOME_OPPONENT_A);

        // Get all the userOutcomeList where endOutcomeOpponentA not equals to UPDATED_END_OUTCOME_OPPONENT_A
        defaultUserOutcomeShouldBeFound("endOutcomeOpponentA.notEquals=" + UPDATED_END_OUTCOME_OPPONENT_A);
    }

    @Test
    @Transactional
    void getAllUserOutcomesByEndOutcomeOpponentAIsInShouldWork() throws Exception {
        // Initialize the database
        userOutcomeRepository.saveAndFlush(userOutcome);

        // Get all the userOutcomeList where endOutcomeOpponentA in DEFAULT_END_OUTCOME_OPPONENT_A or UPDATED_END_OUTCOME_OPPONENT_A
        defaultUserOutcomeShouldBeFound("endOutcomeOpponentA.in=" + DEFAULT_END_OUTCOME_OPPONENT_A + "," + UPDATED_END_OUTCOME_OPPONENT_A);

        // Get all the userOutcomeList where endOutcomeOpponentA equals to UPDATED_END_OUTCOME_OPPONENT_A
        defaultUserOutcomeShouldNotBeFound("endOutcomeOpponentA.in=" + UPDATED_END_OUTCOME_OPPONENT_A);
    }

    @Test
    @Transactional
    void getAllUserOutcomesByEndOutcomeOpponentAIsNullOrNotNull() throws Exception {
        // Initialize the database
        userOutcomeRepository.saveAndFlush(userOutcome);

        // Get all the userOutcomeList where endOutcomeOpponentA is not null
        defaultUserOutcomeShouldBeFound("endOutcomeOpponentA.specified=true");

        // Get all the userOutcomeList where endOutcomeOpponentA is null
        defaultUserOutcomeShouldNotBeFound("endOutcomeOpponentA.specified=false");
    }

    @Test
    @Transactional
    void getAllUserOutcomesByEndOutcomeOpponentAContainsSomething() throws Exception {
        // Initialize the database
        userOutcomeRepository.saveAndFlush(userOutcome);

        // Get all the userOutcomeList where endOutcomeOpponentA contains DEFAULT_END_OUTCOME_OPPONENT_A
        defaultUserOutcomeShouldBeFound("endOutcomeOpponentA.contains=" + DEFAULT_END_OUTCOME_OPPONENT_A);

        // Get all the userOutcomeList where endOutcomeOpponentA contains UPDATED_END_OUTCOME_OPPONENT_A
        defaultUserOutcomeShouldNotBeFound("endOutcomeOpponentA.contains=" + UPDATED_END_OUTCOME_OPPONENT_A);
    }

    @Test
    @Transactional
    void getAllUserOutcomesByEndOutcomeOpponentANotContainsSomething() throws Exception {
        // Initialize the database
        userOutcomeRepository.saveAndFlush(userOutcome);

        // Get all the userOutcomeList where endOutcomeOpponentA does not contain DEFAULT_END_OUTCOME_OPPONENT_A
        defaultUserOutcomeShouldNotBeFound("endOutcomeOpponentA.doesNotContain=" + DEFAULT_END_OUTCOME_OPPONENT_A);

        // Get all the userOutcomeList where endOutcomeOpponentA does not contain UPDATED_END_OUTCOME_OPPONENT_A
        defaultUserOutcomeShouldBeFound("endOutcomeOpponentA.doesNotContain=" + UPDATED_END_OUTCOME_OPPONENT_A);
    }

    @Test
    @Transactional
    void getAllUserOutcomesByEndOutcomeOpponentBIsEqualToSomething() throws Exception {
        // Initialize the database
        userOutcomeRepository.saveAndFlush(userOutcome);

        // Get all the userOutcomeList where endOutcomeOpponentB equals to DEFAULT_END_OUTCOME_OPPONENT_B
        defaultUserOutcomeShouldBeFound("endOutcomeOpponentB.equals=" + DEFAULT_END_OUTCOME_OPPONENT_B);

        // Get all the userOutcomeList where endOutcomeOpponentB equals to UPDATED_END_OUTCOME_OPPONENT_B
        defaultUserOutcomeShouldNotBeFound("endOutcomeOpponentB.equals=" + UPDATED_END_OUTCOME_OPPONENT_B);
    }

    @Test
    @Transactional
    void getAllUserOutcomesByEndOutcomeOpponentBIsNotEqualToSomething() throws Exception {
        // Initialize the database
        userOutcomeRepository.saveAndFlush(userOutcome);

        // Get all the userOutcomeList where endOutcomeOpponentB not equals to DEFAULT_END_OUTCOME_OPPONENT_B
        defaultUserOutcomeShouldNotBeFound("endOutcomeOpponentB.notEquals=" + DEFAULT_END_OUTCOME_OPPONENT_B);

        // Get all the userOutcomeList where endOutcomeOpponentB not equals to UPDATED_END_OUTCOME_OPPONENT_B
        defaultUserOutcomeShouldBeFound("endOutcomeOpponentB.notEquals=" + UPDATED_END_OUTCOME_OPPONENT_B);
    }

    @Test
    @Transactional
    void getAllUserOutcomesByEndOutcomeOpponentBIsInShouldWork() throws Exception {
        // Initialize the database
        userOutcomeRepository.saveAndFlush(userOutcome);

        // Get all the userOutcomeList where endOutcomeOpponentB in DEFAULT_END_OUTCOME_OPPONENT_B or UPDATED_END_OUTCOME_OPPONENT_B
        defaultUserOutcomeShouldBeFound("endOutcomeOpponentB.in=" + DEFAULT_END_OUTCOME_OPPONENT_B + "," + UPDATED_END_OUTCOME_OPPONENT_B);

        // Get all the userOutcomeList where endOutcomeOpponentB equals to UPDATED_END_OUTCOME_OPPONENT_B
        defaultUserOutcomeShouldNotBeFound("endOutcomeOpponentB.in=" + UPDATED_END_OUTCOME_OPPONENT_B);
    }

    @Test
    @Transactional
    void getAllUserOutcomesByEndOutcomeOpponentBIsNullOrNotNull() throws Exception {
        // Initialize the database
        userOutcomeRepository.saveAndFlush(userOutcome);

        // Get all the userOutcomeList where endOutcomeOpponentB is not null
        defaultUserOutcomeShouldBeFound("endOutcomeOpponentB.specified=true");

        // Get all the userOutcomeList where endOutcomeOpponentB is null
        defaultUserOutcomeShouldNotBeFound("endOutcomeOpponentB.specified=false");
    }

    @Test
    @Transactional
    void getAllUserOutcomesByEndOutcomeOpponentBContainsSomething() throws Exception {
        // Initialize the database
        userOutcomeRepository.saveAndFlush(userOutcome);

        // Get all the userOutcomeList where endOutcomeOpponentB contains DEFAULT_END_OUTCOME_OPPONENT_B
        defaultUserOutcomeShouldBeFound("endOutcomeOpponentB.contains=" + DEFAULT_END_OUTCOME_OPPONENT_B);

        // Get all the userOutcomeList where endOutcomeOpponentB contains UPDATED_END_OUTCOME_OPPONENT_B
        defaultUserOutcomeShouldNotBeFound("endOutcomeOpponentB.contains=" + UPDATED_END_OUTCOME_OPPONENT_B);
    }

    @Test
    @Transactional
    void getAllUserOutcomesByEndOutcomeOpponentBNotContainsSomething() throws Exception {
        // Initialize the database
        userOutcomeRepository.saveAndFlush(userOutcome);

        // Get all the userOutcomeList where endOutcomeOpponentB does not contain DEFAULT_END_OUTCOME_OPPONENT_B
        defaultUserOutcomeShouldNotBeFound("endOutcomeOpponentB.doesNotContain=" + DEFAULT_END_OUTCOME_OPPONENT_B);

        // Get all the userOutcomeList where endOutcomeOpponentB does not contain UPDATED_END_OUTCOME_OPPONENT_B
        defaultUserOutcomeShouldBeFound("endOutcomeOpponentB.doesNotContain=" + UPDATED_END_OUTCOME_OPPONENT_B);
    }

    @Test
    @Transactional
    void getAllUserOutcomesByGameIsEqualToSomething() throws Exception {
        // Get already existing entity
        Game game = userOutcome.getGame();
        userOutcomeRepository.saveAndFlush(userOutcome);
        Long gameId = game.getId();

        // Get all the userOutcomeList where game equals to gameId
        defaultUserOutcomeShouldBeFound("gameId.equals=" + gameId);

        // Get all the userOutcomeList where game equals to (gameId + 1)
        defaultUserOutcomeShouldNotBeFound("gameId.equals=" + (gameId + 1));
    }

    @Test
    @Transactional
    void getAllUserOutcomesByUserIsEqualToSomething() throws Exception {
        // Get already existing entity
        User user = userOutcome.getUser();
        userOutcomeRepository.saveAndFlush(userOutcome);
        String userId = user.getId();

        // Get all the userOutcomeList where user equals to userId
        defaultUserOutcomeShouldBeFound("userId.equals=" + userId);

        // Get all the userOutcomeList where user equals to "invalid-id"
        defaultUserOutcomeShouldNotBeFound("userId.equals=" + "invalid-id");
    }

    @Test
    @Transactional
    void getAllUserOutcomesByTournamentIsEqualToSomething() throws Exception {
        // Get already existing entity
        Tournament tournament = userOutcome.getTournament();
        userOutcomeRepository.saveAndFlush(userOutcome);
        Long tournamentId = tournament.getId();

        // Get all the userOutcomeList where tournament equals to tournamentId
        defaultUserOutcomeShouldBeFound("tournamentId.equals=" + tournamentId);

        // Get all the userOutcomeList where tournament equals to (tournamentId + 1)
        defaultUserOutcomeShouldNotBeFound("tournamentId.equals=" + (tournamentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUserOutcomeShouldBeFound(String filter) throws Exception {
        restUserOutcomeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userOutcome.getId().intValue())))
            .andExpect(jsonPath("$.[*].endOutcomeOpponentA").value(hasItem(DEFAULT_END_OUTCOME_OPPONENT_A)))
            .andExpect(jsonPath("$.[*].endOutcomeOpponentB").value(hasItem(DEFAULT_END_OUTCOME_OPPONENT_B)));

        // Check, that the count call also returns 1
        restUserOutcomeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUserOutcomeShouldNotBeFound(String filter) throws Exception {
        restUserOutcomeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUserOutcomeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUserOutcome() throws Exception {
        // Get the userOutcome
        restUserOutcomeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUserOutcome() throws Exception {
        // Initialize the database
        userOutcomeRepository.saveAndFlush(userOutcome);

        int databaseSizeBeforeUpdate = userOutcomeRepository.findAll().size();

        // Update the userOutcome
        UserOutcome updatedUserOutcome = userOutcomeRepository
            .findById(new UserOutcomeId(userOutcome.getGame().getId(), userOutcome.getUser().getId(), userOutcome.getTournament().getId()))
            .get();
        // Disconnect from session so that the updates on updatedUserOutcome are not directly saved in db
        em.detach(updatedUserOutcome);
        updatedUserOutcome.endOutcomeOpponentA(UPDATED_END_OUTCOME_OPPONENT_A).endOutcomeOpponentB(UPDATED_END_OUTCOME_OPPONENT_B);

        restUserOutcomeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUserOutcome.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedUserOutcome))
            )
            .andExpect(status().isOk());

        // Validate the UserOutcome in the database
        List<UserOutcome> userOutcomeList = userOutcomeRepository.findAll();
        assertThat(userOutcomeList).hasSize(databaseSizeBeforeUpdate);
        UserOutcome testUserOutcome = userOutcomeList.get(userOutcomeList.size() - 1);
        assertThat(testUserOutcome.getEndOutcomeOpponentA()).isEqualTo(UPDATED_END_OUTCOME_OPPONENT_A);
        assertThat(testUserOutcome.getEndOutcomeOpponentB()).isEqualTo(UPDATED_END_OUTCOME_OPPONENT_B);
    }

    @Test
    @Transactional
    void putNonExistingUserOutcome() throws Exception {
        int databaseSizeBeforeUpdate = userOutcomeRepository.findAll().size();
        userOutcome.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserOutcomeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userOutcome.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userOutcome))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserOutcome in the database
        List<UserOutcome> userOutcomeList = userOutcomeRepository.findAll();
        assertThat(userOutcomeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserOutcome() throws Exception {
        int databaseSizeBeforeUpdate = userOutcomeRepository.findAll().size();
        userOutcome.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserOutcomeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userOutcome))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserOutcome in the database
        List<UserOutcome> userOutcomeList = userOutcomeRepository.findAll();
        assertThat(userOutcomeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserOutcome() throws Exception {
        int databaseSizeBeforeUpdate = userOutcomeRepository.findAll().size();
        userOutcome.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserOutcomeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userOutcome))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserOutcome in the database
        List<UserOutcome> userOutcomeList = userOutcomeRepository.findAll();
        assertThat(userOutcomeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserOutcomeWithPatch() throws Exception {
        // Initialize the database
        userOutcomeRepository.saveAndFlush(userOutcome);

        int databaseSizeBeforeUpdate = userOutcomeRepository.findAll().size();

        // Update the userOutcome using partial update
        UserOutcome partialUpdatedUserOutcome = new UserOutcome();
        partialUpdatedUserOutcome.setId(userOutcome.getId());

        restUserOutcomeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserOutcome.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserOutcome))
            )
            .andExpect(status().isOk());

        // Validate the UserOutcome in the database
        List<UserOutcome> userOutcomeList = userOutcomeRepository.findAll();
        assertThat(userOutcomeList).hasSize(databaseSizeBeforeUpdate);
        UserOutcome testUserOutcome = userOutcomeList.get(userOutcomeList.size() - 1);
        assertThat(testUserOutcome.getEndOutcomeOpponentA()).isEqualTo(DEFAULT_END_OUTCOME_OPPONENT_A);
        assertThat(testUserOutcome.getEndOutcomeOpponentB()).isEqualTo(DEFAULT_END_OUTCOME_OPPONENT_B);
    }

    @Test
    @Transactional
    void fullUpdateUserOutcomeWithPatch() throws Exception {
        // Initialize the database
        userOutcomeRepository.saveAndFlush(userOutcome);

        int databaseSizeBeforeUpdate = userOutcomeRepository.findAll().size();

        // Update the userOutcome using partial update
        UserOutcome partialUpdatedUserOutcome = new UserOutcome();
        partialUpdatedUserOutcome.setId(userOutcome.getId());

        partialUpdatedUserOutcome.endOutcomeOpponentA(UPDATED_END_OUTCOME_OPPONENT_A).endOutcomeOpponentB(UPDATED_END_OUTCOME_OPPONENT_B);

        restUserOutcomeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserOutcome.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserOutcome))
            )
            .andExpect(status().isOk());

        // Validate the UserOutcome in the database
        List<UserOutcome> userOutcomeList = userOutcomeRepository.findAll();
        assertThat(userOutcomeList).hasSize(databaseSizeBeforeUpdate);
        UserOutcome testUserOutcome = userOutcomeList.get(userOutcomeList.size() - 1);
        assertThat(testUserOutcome.getEndOutcomeOpponentA()).isEqualTo(UPDATED_END_OUTCOME_OPPONENT_A);
        assertThat(testUserOutcome.getEndOutcomeOpponentB()).isEqualTo(UPDATED_END_OUTCOME_OPPONENT_B);
    }

    @Test
    @Transactional
    void patchNonExistingUserOutcome() throws Exception {
        int databaseSizeBeforeUpdate = userOutcomeRepository.findAll().size();
        userOutcome.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserOutcomeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userOutcome.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userOutcome))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserOutcome in the database
        List<UserOutcome> userOutcomeList = userOutcomeRepository.findAll();
        assertThat(userOutcomeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserOutcome() throws Exception {
        int databaseSizeBeforeUpdate = userOutcomeRepository.findAll().size();
        userOutcome.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserOutcomeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userOutcome))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserOutcome in the database
        List<UserOutcome> userOutcomeList = userOutcomeRepository.findAll();
        assertThat(userOutcomeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserOutcome() throws Exception {
        int databaseSizeBeforeUpdate = userOutcomeRepository.findAll().size();
        userOutcome.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserOutcomeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userOutcome))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserOutcome in the database
        List<UserOutcome> userOutcomeList = userOutcomeRepository.findAll();
        assertThat(userOutcomeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserOutcome() throws Exception {
        // Initialize the database
        userOutcomeRepository.saveAndFlush(userOutcome);

        int databaseSizeBeforeDelete = userOutcomeRepository.findAll().size();

        // Delete the userOutcome
        restUserOutcomeMockMvc
            .perform(delete(ENTITY_API_URL_ID, userOutcome.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserOutcome> userOutcomeList = userOutcomeRepository.findAll();
        assertThat(userOutcomeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

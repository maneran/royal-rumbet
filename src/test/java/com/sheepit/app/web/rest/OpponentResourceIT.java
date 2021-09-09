package com.sheepit.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sheepit.app.IntegrationTest;
import com.sheepit.app.domain.Opponent;
import com.sheepit.app.domain.enumeration.OpponentType;
import com.sheepit.app.repository.OpponentRepository;
import com.sheepit.app.service.criteria.OpponentCriteria;
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
 * Integration tests for the {@link OpponentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OpponentResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final OpponentType DEFAULT_TYPE = OpponentType.SINGLE;
    private static final OpponentType UPDATED_TYPE = OpponentType.TEAM;

    private static final Integer DEFAULT_NUMBER_OF_PLAYERS = 1;
    private static final Integer UPDATED_NUMBER_OF_PLAYERS = 2;
    private static final Integer SMALLER_NUMBER_OF_PLAYERS = 1 - 1;

    private static final String ENTITY_API_URL = "/api/opponents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OpponentRepository opponentRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOpponentMockMvc;

    private Opponent opponent;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Opponent createEntity(EntityManager em) {
        Opponent opponent = new Opponent()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .type(DEFAULT_TYPE)
            .numberOfPlayers(DEFAULT_NUMBER_OF_PLAYERS);
        return opponent;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Opponent createUpdatedEntity(EntityManager em) {
        Opponent opponent = new Opponent()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .numberOfPlayers(UPDATED_NUMBER_OF_PLAYERS);
        return opponent;
    }

    @BeforeEach
    public void initTest() {
        opponent = createEntity(em);
    }

    @Test
    @Transactional
    void createOpponent() throws Exception {
        int databaseSizeBeforeCreate = opponentRepository.findAll().size();
        // Create the Opponent
        restOpponentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(opponent))
            )
            .andExpect(status().isCreated());

        // Validate the Opponent in the database
        List<Opponent> opponentList = opponentRepository.findAll();
        assertThat(opponentList).hasSize(databaseSizeBeforeCreate + 1);
        Opponent testOpponent = opponentList.get(opponentList.size() - 1);
        assertThat(testOpponent.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOpponent.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testOpponent.getOpponentType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testOpponent.getNumberOfPlayers()).isEqualTo(DEFAULT_NUMBER_OF_PLAYERS);
    }

    @Test
    @Transactional
    void createOpponentWithExistingId() throws Exception {
        // Create the Opponent with an existing ID
        opponent.setId(1L);

        int databaseSizeBeforeCreate = opponentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOpponentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(opponent))
            )
            .andExpect(status().isBadRequest());

        // Validate the Opponent in the database
        List<Opponent> opponentList = opponentRepository.findAll();
        assertThat(opponentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = opponentRepository.findAll().size();
        // set the field null
        opponent.setName(null);

        // Create the Opponent, which fails.

        restOpponentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(opponent))
            )
            .andExpect(status().isBadRequest());

        List<Opponent> opponentList = opponentRepository.findAll();
        assertThat(opponentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = opponentRepository.findAll().size();
        // set the field null
        opponent.setType(null);

        // Create the Opponent, which fails.

        restOpponentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(opponent))
            )
            .andExpect(status().isBadRequest());

        List<Opponent> opponentList = opponentRepository.findAll();
        assertThat(opponentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOpponents() throws Exception {
        // Initialize the database
        opponentRepository.saveAndFlush(opponent);

        // Get all the opponentList
        restOpponentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(opponent.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].numberOfPlayers").value(hasItem(DEFAULT_NUMBER_OF_PLAYERS)));
    }

    @Test
    @Transactional
    void getOpponent() throws Exception {
        // Initialize the database
        opponentRepository.saveAndFlush(opponent);

        // Get the opponent
        restOpponentMockMvc
            .perform(get(ENTITY_API_URL_ID, opponent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(opponent.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.numberOfPlayers").value(DEFAULT_NUMBER_OF_PLAYERS));
    }

    @Test
    @Transactional
    void getOpponentsByIdFiltering() throws Exception {
        // Initialize the database
        opponentRepository.saveAndFlush(opponent);

        Long id = opponent.getId();

        defaultOpponentShouldBeFound("id.equals=" + id);
        defaultOpponentShouldNotBeFound("id.notEquals=" + id);

        defaultOpponentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultOpponentShouldNotBeFound("id.greaterThan=" + id);

        defaultOpponentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultOpponentShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOpponentsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        opponentRepository.saveAndFlush(opponent);

        // Get all the opponentList where name equals to DEFAULT_NAME
        defaultOpponentShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the opponentList where name equals to UPDATED_NAME
        defaultOpponentShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOpponentsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        opponentRepository.saveAndFlush(opponent);

        // Get all the opponentList where name not equals to DEFAULT_NAME
        defaultOpponentShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the opponentList where name not equals to UPDATED_NAME
        defaultOpponentShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOpponentsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        opponentRepository.saveAndFlush(opponent);

        // Get all the opponentList where name in DEFAULT_NAME or UPDATED_NAME
        defaultOpponentShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the opponentList where name equals to UPDATED_NAME
        defaultOpponentShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOpponentsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        opponentRepository.saveAndFlush(opponent);

        // Get all the opponentList where name is not null
        defaultOpponentShouldBeFound("name.specified=true");

        // Get all the opponentList where name is null
        defaultOpponentShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllOpponentsByNameContainsSomething() throws Exception {
        // Initialize the database
        opponentRepository.saveAndFlush(opponent);

        // Get all the opponentList where name contains DEFAULT_NAME
        defaultOpponentShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the opponentList where name contains UPDATED_NAME
        defaultOpponentShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOpponentsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        opponentRepository.saveAndFlush(opponent);

        // Get all the opponentList where name does not contain DEFAULT_NAME
        defaultOpponentShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the opponentList where name does not contain UPDATED_NAME
        defaultOpponentShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOpponentsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        opponentRepository.saveAndFlush(opponent);

        // Get all the opponentList where description equals to DEFAULT_DESCRIPTION
        defaultOpponentShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the opponentList where description equals to UPDATED_DESCRIPTION
        defaultOpponentShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllOpponentsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        opponentRepository.saveAndFlush(opponent);

        // Get all the opponentList where description not equals to DEFAULT_DESCRIPTION
        defaultOpponentShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the opponentList where description not equals to UPDATED_DESCRIPTION
        defaultOpponentShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllOpponentsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        opponentRepository.saveAndFlush(opponent);

        // Get all the opponentList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultOpponentShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the opponentList where description equals to UPDATED_DESCRIPTION
        defaultOpponentShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllOpponentsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        opponentRepository.saveAndFlush(opponent);

        // Get all the opponentList where description is not null
        defaultOpponentShouldBeFound("description.specified=true");

        // Get all the opponentList where description is null
        defaultOpponentShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllOpponentsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        opponentRepository.saveAndFlush(opponent);

        // Get all the opponentList where description contains DEFAULT_DESCRIPTION
        defaultOpponentShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the opponentList where description contains UPDATED_DESCRIPTION
        defaultOpponentShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllOpponentsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        opponentRepository.saveAndFlush(opponent);

        // Get all the opponentList where description does not contain DEFAULT_DESCRIPTION
        defaultOpponentShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the opponentList where description does not contain UPDATED_DESCRIPTION
        defaultOpponentShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllOpponentsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        opponentRepository.saveAndFlush(opponent);

        // Get all the opponentList where type equals to DEFAULT_TYPE
        defaultOpponentShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the opponentList where type equals to UPDATED_TYPE
        defaultOpponentShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllOpponentsByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        opponentRepository.saveAndFlush(opponent);

        // Get all the opponentList where type not equals to DEFAULT_TYPE
        defaultOpponentShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the opponentList where type not equals to UPDATED_TYPE
        defaultOpponentShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllOpponentsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        opponentRepository.saveAndFlush(opponent);

        // Get all the opponentList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultOpponentShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the opponentList where type equals to UPDATED_TYPE
        defaultOpponentShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllOpponentsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        opponentRepository.saveAndFlush(opponent);

        // Get all the opponentList where type is not null
        defaultOpponentShouldBeFound("type.specified=true");

        // Get all the opponentList where type is null
        defaultOpponentShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllOpponentsByNumberOfPlayersIsEqualToSomething() throws Exception {
        // Initialize the database
        opponentRepository.saveAndFlush(opponent);

        // Get all the opponentList where numberOfPlayers equals to DEFAULT_NUMBER_OF_PLAYERS
        defaultOpponentShouldBeFound("numberOfPlayers.equals=" + DEFAULT_NUMBER_OF_PLAYERS);

        // Get all the opponentList where numberOfPlayers equals to UPDATED_NUMBER_OF_PLAYERS
        defaultOpponentShouldNotBeFound("numberOfPlayers.equals=" + UPDATED_NUMBER_OF_PLAYERS);
    }

    @Test
    @Transactional
    void getAllOpponentsByNumberOfPlayersIsNotEqualToSomething() throws Exception {
        // Initialize the database
        opponentRepository.saveAndFlush(opponent);

        // Get all the opponentList where numberOfPlayers not equals to DEFAULT_NUMBER_OF_PLAYERS
        defaultOpponentShouldNotBeFound("numberOfPlayers.notEquals=" + DEFAULT_NUMBER_OF_PLAYERS);

        // Get all the opponentList where numberOfPlayers not equals to UPDATED_NUMBER_OF_PLAYERS
        defaultOpponentShouldBeFound("numberOfPlayers.notEquals=" + UPDATED_NUMBER_OF_PLAYERS);
    }

    @Test
    @Transactional
    void getAllOpponentsByNumberOfPlayersIsInShouldWork() throws Exception {
        // Initialize the database
        opponentRepository.saveAndFlush(opponent);

        // Get all the opponentList where numberOfPlayers in DEFAULT_NUMBER_OF_PLAYERS or UPDATED_NUMBER_OF_PLAYERS
        defaultOpponentShouldBeFound("numberOfPlayers.in=" + DEFAULT_NUMBER_OF_PLAYERS + "," + UPDATED_NUMBER_OF_PLAYERS);

        // Get all the opponentList where numberOfPlayers equals to UPDATED_NUMBER_OF_PLAYERS
        defaultOpponentShouldNotBeFound("numberOfPlayers.in=" + UPDATED_NUMBER_OF_PLAYERS);
    }

    @Test
    @Transactional
    void getAllOpponentsByNumberOfPlayersIsNullOrNotNull() throws Exception {
        // Initialize the database
        opponentRepository.saveAndFlush(opponent);

        // Get all the opponentList where numberOfPlayers is not null
        defaultOpponentShouldBeFound("numberOfPlayers.specified=true");

        // Get all the opponentList where numberOfPlayers is null
        defaultOpponentShouldNotBeFound("numberOfPlayers.specified=false");
    }

    @Test
    @Transactional
    void getAllOpponentsByNumberOfPlayersIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        opponentRepository.saveAndFlush(opponent);

        // Get all the opponentList where numberOfPlayers is greater than or equal to DEFAULT_NUMBER_OF_PLAYERS
        defaultOpponentShouldBeFound("numberOfPlayers.greaterThanOrEqual=" + DEFAULT_NUMBER_OF_PLAYERS);

        // Get all the opponentList where numberOfPlayers is greater than or equal to (DEFAULT_NUMBER_OF_PLAYERS + 1)
        defaultOpponentShouldNotBeFound("numberOfPlayers.greaterThanOrEqual=" + (DEFAULT_NUMBER_OF_PLAYERS + 1));
    }

    @Test
    @Transactional
    void getAllOpponentsByNumberOfPlayersIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        opponentRepository.saveAndFlush(opponent);

        // Get all the opponentList where numberOfPlayers is less than or equal to DEFAULT_NUMBER_OF_PLAYERS
        defaultOpponentShouldBeFound("numberOfPlayers.lessThanOrEqual=" + DEFAULT_NUMBER_OF_PLAYERS);

        // Get all the opponentList where numberOfPlayers is less than or equal to SMALLER_NUMBER_OF_PLAYERS
        defaultOpponentShouldNotBeFound("numberOfPlayers.lessThanOrEqual=" + SMALLER_NUMBER_OF_PLAYERS);
    }

    @Test
    @Transactional
    void getAllOpponentsByNumberOfPlayersIsLessThanSomething() throws Exception {
        // Initialize the database
        opponentRepository.saveAndFlush(opponent);

        // Get all the opponentList where numberOfPlayers is less than DEFAULT_NUMBER_OF_PLAYERS
        defaultOpponentShouldNotBeFound("numberOfPlayers.lessThan=" + DEFAULT_NUMBER_OF_PLAYERS);

        // Get all the opponentList where numberOfPlayers is less than (DEFAULT_NUMBER_OF_PLAYERS + 1)
        defaultOpponentShouldBeFound("numberOfPlayers.lessThan=" + (DEFAULT_NUMBER_OF_PLAYERS + 1));
    }

    @Test
    @Transactional
    void getAllOpponentsByNumberOfPlayersIsGreaterThanSomething() throws Exception {
        // Initialize the database
        opponentRepository.saveAndFlush(opponent);

        // Get all the opponentList where numberOfPlayers is greater than DEFAULT_NUMBER_OF_PLAYERS
        defaultOpponentShouldNotBeFound("numberOfPlayers.greaterThan=" + DEFAULT_NUMBER_OF_PLAYERS);

        // Get all the opponentList where numberOfPlayers is greater than SMALLER_NUMBER_OF_PLAYERS
        defaultOpponentShouldBeFound("numberOfPlayers.greaterThan=" + SMALLER_NUMBER_OF_PLAYERS);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOpponentShouldBeFound(String filter) throws Exception {
        restOpponentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(opponent.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].numberOfPlayers").value(hasItem(DEFAULT_NUMBER_OF_PLAYERS)));

        // Check, that the count call also returns 1
        restOpponentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOpponentShouldNotBeFound(String filter) throws Exception {
        restOpponentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOpponentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOpponent() throws Exception {
        // Get the opponent
        restOpponentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOpponent() throws Exception {
        // Initialize the database
        opponentRepository.saveAndFlush(opponent);

        int databaseSizeBeforeUpdate = opponentRepository.findAll().size();

        // Update the opponent
        Opponent updatedOpponent = opponentRepository.findById(opponent.getId()).get();
        // Disconnect from session so that the updates on updatedOpponent are not directly saved in db
        em.detach(updatedOpponent);
        updatedOpponent.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).type(UPDATED_TYPE).numberOfPlayers(UPDATED_NUMBER_OF_PLAYERS);

        restOpponentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOpponent.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOpponent))
            )
            .andExpect(status().isOk());

        // Validate the Opponent in the database
        List<Opponent> opponentList = opponentRepository.findAll();
        assertThat(opponentList).hasSize(databaseSizeBeforeUpdate);
        Opponent testOpponent = opponentList.get(opponentList.size() - 1);
        assertThat(testOpponent.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOpponent.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testOpponent.getOpponentType()).isEqualTo(UPDATED_TYPE);
        assertThat(testOpponent.getNumberOfPlayers()).isEqualTo(UPDATED_NUMBER_OF_PLAYERS);
    }

    @Test
    @Transactional
    void putNonExistingOpponent() throws Exception {
        int databaseSizeBeforeUpdate = opponentRepository.findAll().size();
        opponent.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOpponentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, opponent.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(opponent))
            )
            .andExpect(status().isBadRequest());

        // Validate the Opponent in the database
        List<Opponent> opponentList = opponentRepository.findAll();
        assertThat(opponentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOpponent() throws Exception {
        int databaseSizeBeforeUpdate = opponentRepository.findAll().size();
        opponent.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOpponentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(opponent))
            )
            .andExpect(status().isBadRequest());

        // Validate the Opponent in the database
        List<Opponent> opponentList = opponentRepository.findAll();
        assertThat(opponentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOpponent() throws Exception {
        int databaseSizeBeforeUpdate = opponentRepository.findAll().size();
        opponent.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOpponentMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(opponent))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Opponent in the database
        List<Opponent> opponentList = opponentRepository.findAll();
        assertThat(opponentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOpponentWithPatch() throws Exception {
        // Initialize the database
        opponentRepository.saveAndFlush(opponent);

        int databaseSizeBeforeUpdate = opponentRepository.findAll().size();

        // Update the opponent using partial update
        Opponent partialUpdatedOpponent = new Opponent();
        partialUpdatedOpponent.setId(opponent.getId());

        partialUpdatedOpponent.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).type(UPDATED_TYPE);

        restOpponentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOpponent.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOpponent))
            )
            .andExpect(status().isOk());

        // Validate the Opponent in the database
        List<Opponent> opponentList = opponentRepository.findAll();
        assertThat(opponentList).hasSize(databaseSizeBeforeUpdate);
        Opponent testOpponent = opponentList.get(opponentList.size() - 1);
        assertThat(testOpponent.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOpponent.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testOpponent.getOpponentType()).isEqualTo(UPDATED_TYPE);
        assertThat(testOpponent.getNumberOfPlayers()).isEqualTo(DEFAULT_NUMBER_OF_PLAYERS);
    }

    @Test
    @Transactional
    void fullUpdateOpponentWithPatch() throws Exception {
        // Initialize the database
        opponentRepository.saveAndFlush(opponent);

        int databaseSizeBeforeUpdate = opponentRepository.findAll().size();

        // Update the opponent using partial update
        Opponent partialUpdatedOpponent = new Opponent();
        partialUpdatedOpponent.setId(opponent.getId());

        partialUpdatedOpponent
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .numberOfPlayers(UPDATED_NUMBER_OF_PLAYERS);

        restOpponentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOpponent.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOpponent))
            )
            .andExpect(status().isOk());

        // Validate the Opponent in the database
        List<Opponent> opponentList = opponentRepository.findAll();
        assertThat(opponentList).hasSize(databaseSizeBeforeUpdate);
        Opponent testOpponent = opponentList.get(opponentList.size() - 1);
        assertThat(testOpponent.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOpponent.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testOpponent.getOpponentType()).isEqualTo(UPDATED_TYPE);
        assertThat(testOpponent.getNumberOfPlayers()).isEqualTo(UPDATED_NUMBER_OF_PLAYERS);
    }

    @Test
    @Transactional
    void patchNonExistingOpponent() throws Exception {
        int databaseSizeBeforeUpdate = opponentRepository.findAll().size();
        opponent.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOpponentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, opponent.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(opponent))
            )
            .andExpect(status().isBadRequest());

        // Validate the Opponent in the database
        List<Opponent> opponentList = opponentRepository.findAll();
        assertThat(opponentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOpponent() throws Exception {
        int databaseSizeBeforeUpdate = opponentRepository.findAll().size();
        opponent.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOpponentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(opponent))
            )
            .andExpect(status().isBadRequest());

        // Validate the Opponent in the database
        List<Opponent> opponentList = opponentRepository.findAll();
        assertThat(opponentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOpponent() throws Exception {
        int databaseSizeBeforeUpdate = opponentRepository.findAll().size();
        opponent.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOpponentMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(opponent))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Opponent in the database
        List<Opponent> opponentList = opponentRepository.findAll();
        assertThat(opponentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOpponent() throws Exception {
        // Initialize the database
        opponentRepository.saveAndFlush(opponent);

        int databaseSizeBeforeDelete = opponentRepository.findAll().size();

        // Delete the opponent
        restOpponentMockMvc
            .perform(delete(ENTITY_API_URL_ID, opponent.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Opponent> opponentList = opponentRepository.findAll();
        assertThat(opponentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

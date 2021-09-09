package com.sheepit.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sheepit.app.IntegrationTest;
import com.sheepit.app.domain.Game;
import com.sheepit.app.domain.Opponent;
import com.sheepit.app.domain.Tournament;
import com.sheepit.app.domain.User;
import com.sheepit.app.domain.enumeration.GameStageType;
import com.sheepit.app.domain.enumeration.GameType;
import com.sheepit.app.repository.GameRepository;
import com.sheepit.app.service.GameService;
import com.sheepit.app.service.criteria.GameCriteria;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link GameResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class GameResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_START = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_START = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final GameType DEFAULT_TYPE = GameType.SOCCER;
    private static final GameType UPDATED_TYPE = GameType.BASKETBALL;

    private static final GameStageType DEFAULT_STAGE_TYPE = GameStageType.ROUND_ROBIN;
    private static final GameStageType UPDATED_STAGE_TYPE = GameStageType.FIRST_FOUR;

    private static final String ENTITY_API_URL = "/api/games";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GameRepository gameRepository;

    @Mock
    private GameRepository gameRepositoryMock;

    @Mock
    private GameService gameServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGameMockMvc;

    private Game game;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Game createEntity(EntityManager em) {
        Game game = new Game().name(DEFAULT_NAME).dateStart(DEFAULT_DATE_START).gameType(DEFAULT_TYPE).stageType(DEFAULT_STAGE_TYPE);
        // Add required entity
        Opponent opponent;
        if (TestUtil.findAll(em, Opponent.class).isEmpty()) {
            opponent = OpponentResourceIT.createEntity(em);
            em.persist(opponent);
            em.flush();
        } else {
            opponent = TestUtil.findAll(em, Opponent.class).get(0);
        }
        game.setOpponentA(opponent);
        // Add required entity
        game.setOpponentB(opponent);
        // Add required entity
        Tournament tournament;
        if (TestUtil.findAll(em, Tournament.class).isEmpty()) {
            tournament = TournamentResourceIT.createEntity(em);
            em.persist(tournament);
            em.flush();
        } else {
            tournament = TestUtil.findAll(em, Tournament.class).get(0);
        }
        game.setTournament(tournament);
        return game;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Game createUpdatedEntity(EntityManager em) {
        Game game = new Game().name(UPDATED_NAME).dateStart(UPDATED_DATE_START).gameType(UPDATED_TYPE).stageType(UPDATED_STAGE_TYPE);
        // Add required entity
        Opponent opponent;
        if (TestUtil.findAll(em, Opponent.class).isEmpty()) {
            opponent = OpponentResourceIT.createUpdatedEntity(em);
            em.persist(opponent);
            em.flush();
        } else {
            opponent = TestUtil.findAll(em, Opponent.class).get(0);
        }
        game.setOpponentA(opponent);
        // Add required entity
        game.setOpponentB(opponent);
        // Add required entity
        Tournament tournament;
        if (TestUtil.findAll(em, Tournament.class).isEmpty()) {
            tournament = TournamentResourceIT.createUpdatedEntity(em);
            em.persist(tournament);
            em.flush();
        } else {
            tournament = TestUtil.findAll(em, Tournament.class).get(0);
        }
        game.setTournament(tournament);
        return game;
    }

    @BeforeEach
    public void initTest() {
        game = createEntity(em);
    }

    @Test
    @Transactional
    void createGame() throws Exception {
        int databaseSizeBeforeCreate = gameRepository.findAll().size();
        // Create the Game
        restGameMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(game))
            )
            .andExpect(status().isCreated());

        // Validate the Game in the database
        List<Game> gameList = gameRepository.findAll();
        assertThat(gameList).hasSize(databaseSizeBeforeCreate + 1);
        Game testGame = gameList.get(gameList.size() - 1);
        assertThat(testGame.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testGame.getDateStart()).isEqualTo(DEFAULT_DATE_START);
        assertThat(testGame.getGameType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testGame.getStageType()).isEqualTo(DEFAULT_STAGE_TYPE);
    }

    @Test
    @Transactional
    void createGameWithExistingId() throws Exception {
        // Create the Game with an existing ID
        game.setId(1L);

        int databaseSizeBeforeCreate = gameRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGameMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(game))
            )
            .andExpect(status().isBadRequest());

        // Validate the Game in the database
        List<Game> gameList = gameRepository.findAll();
        assertThat(gameList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateStartIsRequired() throws Exception {
        int databaseSizeBeforeTest = gameRepository.findAll().size();
        // set the field null
        game.setDateStart(null);

        // Create the Game, which fails.

        restGameMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(game))
            )
            .andExpect(status().isBadRequest());

        List<Game> gameList = gameRepository.findAll();
        assertThat(gameList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = gameRepository.findAll().size();
        // set the field null
        game.setGameType(null);

        // Create the Game, which fails.

        restGameMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(game))
            )
            .andExpect(status().isBadRequest());

        List<Game> gameList = gameRepository.findAll();
        assertThat(gameList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStageTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = gameRepository.findAll().size();
        // set the field null
        game.setStageType(null);

        // Create the Game, which fails.

        restGameMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(game))
            )
            .andExpect(status().isBadRequest());

        List<Game> gameList = gameRepository.findAll();
        assertThat(gameList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllGames() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList
        restGameMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(game.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].dateStart").value(hasItem(DEFAULT_DATE_START.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].stageType").value(hasItem(DEFAULT_STAGE_TYPE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllGamesWithEagerRelationshipsIsEnabled() throws Exception {
        when(gameServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restGameMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(gameServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllGamesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(gameServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restGameMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(gameServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getGame() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get the game
        restGameMockMvc
            .perform(get(ENTITY_API_URL_ID, game.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(game.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.dateStart").value(DEFAULT_DATE_START.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.stageType").value(DEFAULT_STAGE_TYPE.toString()));
    }

    @Test
    @Transactional
    void getGamesByIdFiltering() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        Long id = game.getId();

        defaultGameShouldBeFound("id.equals=" + id);
        defaultGameShouldNotBeFound("id.notEquals=" + id);

        defaultGameShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultGameShouldNotBeFound("id.greaterThan=" + id);

        defaultGameShouldBeFound("id.lessThanOrEqual=" + id);
        defaultGameShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllGamesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where name equals to DEFAULT_NAME
        defaultGameShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the gameList where name equals to UPDATED_NAME
        defaultGameShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllGamesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where name not equals to DEFAULT_NAME
        defaultGameShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the gameList where name not equals to UPDATED_NAME
        defaultGameShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllGamesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where name in DEFAULT_NAME or UPDATED_NAME
        defaultGameShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the gameList where name equals to UPDATED_NAME
        defaultGameShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllGamesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where name is not null
        defaultGameShouldBeFound("name.specified=true");

        // Get all the gameList where name is null
        defaultGameShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllGamesByNameContainsSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where name contains DEFAULT_NAME
        defaultGameShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the gameList where name contains UPDATED_NAME
        defaultGameShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllGamesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where name does not contain DEFAULT_NAME
        defaultGameShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the gameList where name does not contain UPDATED_NAME
        defaultGameShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllGamesByDateStartIsEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where dateStart equals to DEFAULT_DATE_START
        defaultGameShouldBeFound("dateStart.equals=" + DEFAULT_DATE_START);

        // Get all the gameList where dateStart equals to UPDATED_DATE_START
        defaultGameShouldNotBeFound("dateStart.equals=" + UPDATED_DATE_START);
    }

    @Test
    @Transactional
    void getAllGamesByDateStartIsNotEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where dateStart not equals to DEFAULT_DATE_START
        defaultGameShouldNotBeFound("dateStart.notEquals=" + DEFAULT_DATE_START);

        // Get all the gameList where dateStart not equals to UPDATED_DATE_START
        defaultGameShouldBeFound("dateStart.notEquals=" + UPDATED_DATE_START);
    }

    @Test
    @Transactional
    void getAllGamesByDateStartIsInShouldWork() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where dateStart in DEFAULT_DATE_START or UPDATED_DATE_START
        defaultGameShouldBeFound("dateStart.in=" + DEFAULT_DATE_START + "," + UPDATED_DATE_START);

        // Get all the gameList where dateStart equals to UPDATED_DATE_START
        defaultGameShouldNotBeFound("dateStart.in=" + UPDATED_DATE_START);
    }

    @Test
    @Transactional
    void getAllGamesByDateStartIsNullOrNotNull() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where dateStart is not null
        defaultGameShouldBeFound("dateStart.specified=true");

        // Get all the gameList where dateStart is null
        defaultGameShouldNotBeFound("dateStart.specified=false");
    }

    @Test
    @Transactional
    void getAllGamesByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where type equals to DEFAULT_TYPE
        defaultGameShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the gameList where type equals to UPDATED_TYPE
        defaultGameShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllGamesByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where type not equals to DEFAULT_TYPE
        defaultGameShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the gameList where type not equals to UPDATED_TYPE
        defaultGameShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllGamesByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultGameShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the gameList where type equals to UPDATED_TYPE
        defaultGameShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllGamesByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where type is not null
        defaultGameShouldBeFound("type.specified=true");

        // Get all the gameList where type is null
        defaultGameShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllGamesByStageTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where stageType equals to DEFAULT_STAGE_TYPE
        defaultGameShouldBeFound("stageType.equals=" + DEFAULT_STAGE_TYPE);

        // Get all the gameList where stageType equals to UPDATED_STAGE_TYPE
        defaultGameShouldNotBeFound("stageType.equals=" + UPDATED_STAGE_TYPE);
    }

    @Test
    @Transactional
    void getAllGamesByStageTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where stageType not equals to DEFAULT_STAGE_TYPE
        defaultGameShouldNotBeFound("stageType.notEquals=" + DEFAULT_STAGE_TYPE);

        // Get all the gameList where stageType not equals to UPDATED_STAGE_TYPE
        defaultGameShouldBeFound("stageType.notEquals=" + UPDATED_STAGE_TYPE);
    }

    @Test
    @Transactional
    void getAllGamesByStageTypeIsInShouldWork() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where stageType in DEFAULT_STAGE_TYPE or UPDATED_STAGE_TYPE
        defaultGameShouldBeFound("stageType.in=" + DEFAULT_STAGE_TYPE + "," + UPDATED_STAGE_TYPE);

        // Get all the gameList where stageType equals to UPDATED_STAGE_TYPE
        defaultGameShouldNotBeFound("stageType.in=" + UPDATED_STAGE_TYPE);
    }

    @Test
    @Transactional
    void getAllGamesByStageTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        // Get all the gameList where stageType is not null
        defaultGameShouldBeFound("stageType.specified=true");

        // Get all the gameList where stageType is null
        defaultGameShouldNotBeFound("stageType.specified=false");
    }

    @Test
    @Transactional
    void getAllGamesByOpponentAIsEqualToSomething() throws Exception {
        // Get already existing entity
        Opponent opponentA = game.getOpponentA();
        gameRepository.saveAndFlush(game);
        Long opponentAId = opponentA.getId();

        // Get all the gameList where opponentA equals to opponentAId
        defaultGameShouldBeFound("opponentAId.equals=" + opponentAId);

        // Get all the gameList where opponentA equals to (opponentAId + 1)
        defaultGameShouldNotBeFound("opponentAId.equals=" + (opponentAId + 1));
    }

    @Test
    @Transactional
    void getAllGamesByOpponentBIsEqualToSomething() throws Exception {
        // Get already existing entity
        Opponent opponentB = game.getOpponentB();
        gameRepository.saveAndFlush(game);
        Long opponentBId = opponentB.getId();

        // Get all the gameList where opponentB equals to opponentBId
        defaultGameShouldBeFound("opponentBId.equals=" + opponentBId);

        // Get all the gameList where opponentB equals to (opponentBId + 1)
        defaultGameShouldNotBeFound("opponentBId.equals=" + (opponentBId + 1));
    }

    @Test
    @Transactional
    void getAllGamesByTournamentIsEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);
        Tournament tournament = TournamentResourceIT.createEntity(em);
        em.persist(tournament);
        em.flush();
        game.setTournament(tournament);
        gameRepository.saveAndFlush(game);
        Long tournamentId = tournament.getId();

        // Get all the gameList where tournament equals to tournamentId
        defaultGameShouldBeFound("tournamentId.equals=" + tournamentId);

        // Get all the gameList where tournament equals to (tournamentId + 1)
        defaultGameShouldNotBeFound("tournamentId.equals=" + (tournamentId + 1));
    }

    @Test
    @Transactional
    void getAllGamesByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        game.addUser(user);
        gameRepository.saveAndFlush(game);
        String userId = user.getId();

        // Get all the gameList where user equals to userId
        defaultGameShouldBeFound("userId.equals=" + userId);

        // Get all the gameList where user equals to "invalid-id"
        defaultGameShouldNotBeFound("userId.equals=" + "invalid-id");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultGameShouldBeFound(String filter) throws Exception {
        restGameMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(game.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].dateStart").value(hasItem(DEFAULT_DATE_START.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].stageType").value(hasItem(DEFAULT_STAGE_TYPE.toString())));

        // Check, that the count call also returns 1
        restGameMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultGameShouldNotBeFound(String filter) throws Exception {
        restGameMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGameMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingGame() throws Exception {
        // Get the game
        restGameMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewGame() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        int databaseSizeBeforeUpdate = gameRepository.findAll().size();

        // Update the game
        Game updatedGame = gameRepository.findById(game.getId()).get();
        // Disconnect from session so that the updates on updatedGame are not directly saved in db
        em.detach(updatedGame);
        updatedGame.name(UPDATED_NAME).dateStart(UPDATED_DATE_START).gameType(UPDATED_TYPE).stageType(UPDATED_STAGE_TYPE);

        restGameMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedGame.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedGame))
            )
            .andExpect(status().isOk());

        // Validate the Game in the database
        List<Game> gameList = gameRepository.findAll();
        assertThat(gameList).hasSize(databaseSizeBeforeUpdate);
        Game testGame = gameList.get(gameList.size() - 1);
        assertThat(testGame.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testGame.getDateStart()).isEqualTo(UPDATED_DATE_START);
        assertThat(testGame.getGameType()).isEqualTo(UPDATED_TYPE);
        assertThat(testGame.getStageType()).isEqualTo(UPDATED_STAGE_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingGame() throws Exception {
        int databaseSizeBeforeUpdate = gameRepository.findAll().size();
        game.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGameMockMvc
            .perform(
                put(ENTITY_API_URL_ID, game.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(game))
            )
            .andExpect(status().isBadRequest());

        // Validate the Game in the database
        List<Game> gameList = gameRepository.findAll();
        assertThat(gameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGame() throws Exception {
        int databaseSizeBeforeUpdate = gameRepository.findAll().size();
        game.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(game))
            )
            .andExpect(status().isBadRequest());

        // Validate the Game in the database
        List<Game> gameList = gameRepository.findAll();
        assertThat(gameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGame() throws Exception {
        int databaseSizeBeforeUpdate = gameRepository.findAll().size();
        game.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(game))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Game in the database
        List<Game> gameList = gameRepository.findAll();
        assertThat(gameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGameWithPatch() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        int databaseSizeBeforeUpdate = gameRepository.findAll().size();

        // Update the game using partial update
        Game partialUpdatedGame = new Game();
        partialUpdatedGame.setId(game.getId());

        restGameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGame.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGame))
            )
            .andExpect(status().isOk());

        // Validate the Game in the database
        List<Game> gameList = gameRepository.findAll();
        assertThat(gameList).hasSize(databaseSizeBeforeUpdate);
        Game testGame = gameList.get(gameList.size() - 1);
        assertThat(testGame.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testGame.getDateStart()).isEqualTo(DEFAULT_DATE_START);
        assertThat(testGame.getGameType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testGame.getStageType()).isEqualTo(DEFAULT_STAGE_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateGameWithPatch() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        int databaseSizeBeforeUpdate = gameRepository.findAll().size();

        // Update the game using partial update
        Game partialUpdatedGame = new Game();
        partialUpdatedGame.setId(game.getId());

        partialUpdatedGame.name(UPDATED_NAME).dateStart(UPDATED_DATE_START).gameType(UPDATED_TYPE).stageType(UPDATED_STAGE_TYPE);

        restGameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGame.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGame))
            )
            .andExpect(status().isOk());

        // Validate the Game in the database
        List<Game> gameList = gameRepository.findAll();
        assertThat(gameList).hasSize(databaseSizeBeforeUpdate);
        Game testGame = gameList.get(gameList.size() - 1);
        assertThat(testGame.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testGame.getDateStart()).isEqualTo(UPDATED_DATE_START);
        assertThat(testGame.getGameType()).isEqualTo(UPDATED_TYPE);
        assertThat(testGame.getStageType()).isEqualTo(UPDATED_STAGE_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingGame() throws Exception {
        int databaseSizeBeforeUpdate = gameRepository.findAll().size();
        game.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, game.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(game))
            )
            .andExpect(status().isBadRequest());

        // Validate the Game in the database
        List<Game> gameList = gameRepository.findAll();
        assertThat(gameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGame() throws Exception {
        int databaseSizeBeforeUpdate = gameRepository.findAll().size();
        game.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(game))
            )
            .andExpect(status().isBadRequest());

        // Validate the Game in the database
        List<Game> gameList = gameRepository.findAll();
        assertThat(gameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGame() throws Exception {
        int databaseSizeBeforeUpdate = gameRepository.findAll().size();
        game.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(game))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Game in the database
        List<Game> gameList = gameRepository.findAll();
        assertThat(gameList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGame() throws Exception {
        // Initialize the database
        gameRepository.saveAndFlush(game);

        int databaseSizeBeforeDelete = gameRepository.findAll().size();

        // Delete the game
        restGameMockMvc
            .perform(delete(ENTITY_API_URL_ID, game.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Game> gameList = gameRepository.findAll();
        assertThat(gameList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

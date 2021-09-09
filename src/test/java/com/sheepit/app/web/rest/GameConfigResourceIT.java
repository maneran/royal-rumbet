package com.sheepit.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sheepit.app.IntegrationTest;
import com.sheepit.app.domain.GameConfig;
import com.sheepit.app.domain.enumeration.GameOutcome;
import com.sheepit.app.domain.enumeration.GameStageType;
import com.sheepit.app.domain.enumeration.GameType;
import com.sheepit.app.repository.GameConfigRepository;
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
 * Integration tests for the {@link GameConfigResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GameConfigResourceIT {

    private static final GameType DEFAULT_GAME_TYPE = GameType.SOCCER;
    private static final GameType UPDATED_GAME_TYPE = GameType.BASKETBALL;

    private static final GameStageType DEFAULT_GAME_STAGE_TYPE = GameStageType.ROUND_ROBIN;
    private static final GameStageType UPDATED_GAME_STAGE_TYPE = GameStageType.FIRST_FOUR;

    private static final GameOutcome DEFAULT_GAME_OUTCOME = GameOutcome.MISSED;
    private static final GameOutcome UPDATED_GAME_OUTCOME = GameOutcome.HIT;

    private static final Integer DEFAULT_POINTS = 0;
    private static final Integer UPDATED_POINTS = 1;

    private static final String ENTITY_API_URL = "/api/game-configs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GameConfigRepository gameConfigRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGameConfigMockMvc;

    private GameConfig gameConfig;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GameConfig createEntity(EntityManager em) {
        GameConfig gameConfig = new GameConfig()
            .gameType(DEFAULT_GAME_TYPE)
            .gameStageType(DEFAULT_GAME_STAGE_TYPE)
            .gameOutcome(DEFAULT_GAME_OUTCOME)
            .points(DEFAULT_POINTS);
        return gameConfig;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GameConfig createUpdatedEntity(EntityManager em) {
        GameConfig gameConfig = new GameConfig()
            .gameType(UPDATED_GAME_TYPE)
            .gameStageType(UPDATED_GAME_STAGE_TYPE)
            .gameOutcome(UPDATED_GAME_OUTCOME)
            .points(UPDATED_POINTS);
        return gameConfig;
    }

    @BeforeEach
    public void initTest() {
        gameConfig = createEntity(em);
    }

    @Test
    @Transactional
    void createGameConfig() throws Exception {
        int databaseSizeBeforeCreate = gameConfigRepository.findAll().size();
        // Create the GameConfig
        restGameConfigMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gameConfig))
            )
            .andExpect(status().isCreated());

        // Validate the GameConfig in the database
        List<GameConfig> gameConfigList = gameConfigRepository.findAll();
        assertThat(gameConfigList).hasSize(databaseSizeBeforeCreate + 1);
        GameConfig testGameConfig = gameConfigList.get(gameConfigList.size() - 1);
        assertThat(testGameConfig.getGameType()).isEqualTo(DEFAULT_GAME_TYPE);
        assertThat(testGameConfig.getGameStageType()).isEqualTo(DEFAULT_GAME_STAGE_TYPE);
        assertThat(testGameConfig.getGameOutcome()).isEqualTo(DEFAULT_GAME_OUTCOME);
        assertThat(testGameConfig.getPoints()).isEqualTo(DEFAULT_POINTS);
    }

    @Test
    @Transactional
    void createGameConfigWithExistingId() throws Exception {
        // Create the GameConfig with an existing ID
        gameConfig.setId(1L);

        int databaseSizeBeforeCreate = gameConfigRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGameConfigMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gameConfig))
            )
            .andExpect(status().isBadRequest());

        // Validate the GameConfig in the database
        List<GameConfig> gameConfigList = gameConfigRepository.findAll();
        assertThat(gameConfigList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkGameTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = gameConfigRepository.findAll().size();
        // set the field null
        gameConfig.setGameType(null);

        // Create the GameConfig, which fails.

        restGameConfigMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gameConfig))
            )
            .andExpect(status().isBadRequest());

        List<GameConfig> gameConfigList = gameConfigRepository.findAll();
        assertThat(gameConfigList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGameStageTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = gameConfigRepository.findAll().size();
        // set the field null
        gameConfig.setGameStageType(null);

        // Create the GameConfig, which fails.

        restGameConfigMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gameConfig))
            )
            .andExpect(status().isBadRequest());

        List<GameConfig> gameConfigList = gameConfigRepository.findAll();
        assertThat(gameConfigList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGameOutcomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = gameConfigRepository.findAll().size();
        // set the field null
        gameConfig.setGameOutcome(null);

        // Create the GameConfig, which fails.

        restGameConfigMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gameConfig))
            )
            .andExpect(status().isBadRequest());

        List<GameConfig> gameConfigList = gameConfigRepository.findAll();
        assertThat(gameConfigList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPointsIsRequired() throws Exception {
        int databaseSizeBeforeTest = gameConfigRepository.findAll().size();
        // set the field null
        gameConfig.setPoints(null);

        // Create the GameConfig, which fails.

        restGameConfigMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gameConfig))
            )
            .andExpect(status().isBadRequest());

        List<GameConfig> gameConfigList = gameConfigRepository.findAll();
        assertThat(gameConfigList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllGameConfigs() throws Exception {
        // Initialize the database
        gameConfigRepository.saveAndFlush(gameConfig);

        // Get all the gameConfigList
        restGameConfigMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gameConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].gameType").value(hasItem(DEFAULT_GAME_TYPE.toString())))
            .andExpect(jsonPath("$.[*].gameStageType").value(hasItem(DEFAULT_GAME_STAGE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].gameOutcome").value(hasItem(DEFAULT_GAME_OUTCOME.toString())))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS)));
    }

    @Test
    @Transactional
    void getGameConfig() throws Exception {
        // Initialize the database
        gameConfigRepository.saveAndFlush(gameConfig);

        // Get the gameConfig
        restGameConfigMockMvc
            .perform(get(ENTITY_API_URL_ID, gameConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(gameConfig.getId().intValue()))
            .andExpect(jsonPath("$.gameType").value(DEFAULT_GAME_TYPE.toString()))
            .andExpect(jsonPath("$.gameStageType").value(DEFAULT_GAME_STAGE_TYPE.toString()))
            .andExpect(jsonPath("$.gameOutcome").value(DEFAULT_GAME_OUTCOME.toString()))
            .andExpect(jsonPath("$.points").value(DEFAULT_POINTS));
    }

    @Test
    @Transactional
    void getNonExistingGameConfig() throws Exception {
        // Get the gameConfig
        restGameConfigMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewGameConfig() throws Exception {
        // Initialize the database
        gameConfigRepository.saveAndFlush(gameConfig);

        int databaseSizeBeforeUpdate = gameConfigRepository.findAll().size();

        // Update the gameConfig
        GameConfig updatedGameConfig = gameConfigRepository.findById(gameConfig.getId()).get();
        // Disconnect from session so that the updates on updatedGameConfig are not directly saved in db
        em.detach(updatedGameConfig);
        updatedGameConfig
            .gameType(UPDATED_GAME_TYPE)
            .gameStageType(UPDATED_GAME_STAGE_TYPE)
            .gameOutcome(UPDATED_GAME_OUTCOME)
            .points(UPDATED_POINTS);

        restGameConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedGameConfig.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedGameConfig))
            )
            .andExpect(status().isOk());

        // Validate the GameConfig in the database
        List<GameConfig> gameConfigList = gameConfigRepository.findAll();
        assertThat(gameConfigList).hasSize(databaseSizeBeforeUpdate);
        GameConfig testGameConfig = gameConfigList.get(gameConfigList.size() - 1);
        assertThat(testGameConfig.getGameType()).isEqualTo(UPDATED_GAME_TYPE);
        assertThat(testGameConfig.getGameStageType()).isEqualTo(UPDATED_GAME_STAGE_TYPE);
        assertThat(testGameConfig.getGameOutcome()).isEqualTo(UPDATED_GAME_OUTCOME);
        assertThat(testGameConfig.getPoints()).isEqualTo(UPDATED_POINTS);
    }

    @Test
    @Transactional
    void putNonExistingGameConfig() throws Exception {
        int databaseSizeBeforeUpdate = gameConfigRepository.findAll().size();
        gameConfig.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGameConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, gameConfig.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gameConfig))
            )
            .andExpect(status().isBadRequest());

        // Validate the GameConfig in the database
        List<GameConfig> gameConfigList = gameConfigRepository.findAll();
        assertThat(gameConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGameConfig() throws Exception {
        int databaseSizeBeforeUpdate = gameConfigRepository.findAll().size();
        gameConfig.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gameConfig))
            )
            .andExpect(status().isBadRequest());

        // Validate the GameConfig in the database
        List<GameConfig> gameConfigList = gameConfigRepository.findAll();
        assertThat(gameConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGameConfig() throws Exception {
        int databaseSizeBeforeUpdate = gameConfigRepository.findAll().size();
        gameConfig.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameConfigMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gameConfig))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GameConfig in the database
        List<GameConfig> gameConfigList = gameConfigRepository.findAll();
        assertThat(gameConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGameConfigWithPatch() throws Exception {
        // Initialize the database
        gameConfigRepository.saveAndFlush(gameConfig);

        int databaseSizeBeforeUpdate = gameConfigRepository.findAll().size();

        // Update the gameConfig using partial update
        GameConfig partialUpdatedGameConfig = new GameConfig();
        partialUpdatedGameConfig.setId(gameConfig.getId());

        partialUpdatedGameConfig.gameStageType(UPDATED_GAME_STAGE_TYPE).gameOutcome(UPDATED_GAME_OUTCOME).points(UPDATED_POINTS);

        restGameConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGameConfig.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGameConfig))
            )
            .andExpect(status().isOk());

        // Validate the GameConfig in the database
        List<GameConfig> gameConfigList = gameConfigRepository.findAll();
        assertThat(gameConfigList).hasSize(databaseSizeBeforeUpdate);
        GameConfig testGameConfig = gameConfigList.get(gameConfigList.size() - 1);
        assertThat(testGameConfig.getGameType()).isEqualTo(DEFAULT_GAME_TYPE);
        assertThat(testGameConfig.getGameStageType()).isEqualTo(UPDATED_GAME_STAGE_TYPE);
        assertThat(testGameConfig.getGameOutcome()).isEqualTo(UPDATED_GAME_OUTCOME);
        assertThat(testGameConfig.getPoints()).isEqualTo(UPDATED_POINTS);
    }

    @Test
    @Transactional
    void fullUpdateGameConfigWithPatch() throws Exception {
        // Initialize the database
        gameConfigRepository.saveAndFlush(gameConfig);

        int databaseSizeBeforeUpdate = gameConfigRepository.findAll().size();

        // Update the gameConfig using partial update
        GameConfig partialUpdatedGameConfig = new GameConfig();
        partialUpdatedGameConfig.setId(gameConfig.getId());

        partialUpdatedGameConfig
            .gameType(UPDATED_GAME_TYPE)
            .gameStageType(UPDATED_GAME_STAGE_TYPE)
            .gameOutcome(UPDATED_GAME_OUTCOME)
            .points(UPDATED_POINTS);

        restGameConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGameConfig.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGameConfig))
            )
            .andExpect(status().isOk());

        // Validate the GameConfig in the database
        List<GameConfig> gameConfigList = gameConfigRepository.findAll();
        assertThat(gameConfigList).hasSize(databaseSizeBeforeUpdate);
        GameConfig testGameConfig = gameConfigList.get(gameConfigList.size() - 1);
        assertThat(testGameConfig.getGameType()).isEqualTo(UPDATED_GAME_TYPE);
        assertThat(testGameConfig.getGameStageType()).isEqualTo(UPDATED_GAME_STAGE_TYPE);
        assertThat(testGameConfig.getGameOutcome()).isEqualTo(UPDATED_GAME_OUTCOME);
        assertThat(testGameConfig.getPoints()).isEqualTo(UPDATED_POINTS);
    }

    @Test
    @Transactional
    void patchNonExistingGameConfig() throws Exception {
        int databaseSizeBeforeUpdate = gameConfigRepository.findAll().size();
        gameConfig.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGameConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, gameConfig.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gameConfig))
            )
            .andExpect(status().isBadRequest());

        // Validate the GameConfig in the database
        List<GameConfig> gameConfigList = gameConfigRepository.findAll();
        assertThat(gameConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGameConfig() throws Exception {
        int databaseSizeBeforeUpdate = gameConfigRepository.findAll().size();
        gameConfig.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gameConfig))
            )
            .andExpect(status().isBadRequest());

        // Validate the GameConfig in the database
        List<GameConfig> gameConfigList = gameConfigRepository.findAll();
        assertThat(gameConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGameConfig() throws Exception {
        int databaseSizeBeforeUpdate = gameConfigRepository.findAll().size();
        gameConfig.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGameConfigMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gameConfig))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GameConfig in the database
        List<GameConfig> gameConfigList = gameConfigRepository.findAll();
        assertThat(gameConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGameConfig() throws Exception {
        // Initialize the database
        gameConfigRepository.saveAndFlush(gameConfig);

        int databaseSizeBeforeDelete = gameConfigRepository.findAll().size();

        // Delete the gameConfig
        restGameConfigMockMvc
            .perform(delete(ENTITY_API_URL_ID, gameConfig.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GameConfig> gameConfigList = gameConfigRepository.findAll();
        assertThat(gameConfigList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

package com.sheepit.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sheepit.app.IntegrationTest;
import com.sheepit.app.domain.*;
import com.sheepit.app.repository.ScoreRepository;
import com.sheepit.app.service.criteria.ScoreCriteria;
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
 * Integration tests for the {@link ScoreResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ScoreResourceIT {

    private static final Integer DEFAULT_POINTS = 1;
    private static final Integer UPDATED_POINTS = 2;
    private static final Integer SMALLER_POINTS = 1 - 1;

    private static final String ENTITY_API_URL = "/api/scores";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restScoreMockMvc;

    private Score score;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Score createEntity(EntityManager em) {
        Score score = new Score().points(DEFAULT_POINTS);
        // Add required entity
        Game game;
        if (TestUtil.findAll(em, Game.class).isEmpty()) {
            game = GameResourceIT.createEntity(em);
            em.persist(game);
            em.flush();
        } else {
            game = TestUtil.findAll(em, Game.class).get(0);
        }
        score.setGame(game);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        score.setUser(user);
        // Add required entity
        Tournament tournament;
        if (TestUtil.findAll(em, Tournament.class).isEmpty()) {
            tournament = TournamentResourceIT.createEntity(em);
            em.persist(tournament);
            em.flush();
        } else {
            tournament = TestUtil.findAll(em, Tournament.class).get(0);
        }
        score.setTournament(tournament);
        return score;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Score createUpdatedEntity(EntityManager em) {
        Score score = new Score().points(UPDATED_POINTS);
        // Add required entity
        Game game;
        if (TestUtil.findAll(em, Game.class).isEmpty()) {
            game = GameResourceIT.createUpdatedEntity(em);
            em.persist(game);
            em.flush();
        } else {
            game = TestUtil.findAll(em, Game.class).get(0);
        }
        score.setGame(game);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        score.setUser(user);
        // Add required entity
        Tournament tournament;
        if (TestUtil.findAll(em, Tournament.class).isEmpty()) {
            tournament = TournamentResourceIT.createUpdatedEntity(em);
            em.persist(tournament);
            em.flush();
        } else {
            tournament = TestUtil.findAll(em, Tournament.class).get(0);
        }
        score.setTournament(tournament);
        return score;
    }

    @BeforeEach
    public void initTest() {
        score = createEntity(em);
    }

    @Test
    @Transactional
    void createScore() throws Exception {
        int databaseSizeBeforeCreate = scoreRepository.findAll().size();
        // Create the Score
        restScoreMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(score))
            )
            .andExpect(status().isCreated());

        // Validate the Score in the database
        List<Score> scoreList = scoreRepository.findAll();
        assertThat(scoreList).hasSize(databaseSizeBeforeCreate + 1);
        Score testScore = scoreList.get(scoreList.size() - 1);
        assertThat(testScore.getPoints()).isEqualTo(DEFAULT_POINTS);
    }

    @Test
    @Transactional
    void createScoreWithExistingId() throws Exception {
        // Create the Score with an existing ID
        score.setId(1L);

        int databaseSizeBeforeCreate = scoreRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restScoreMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(score))
            )
            .andExpect(status().isBadRequest());

        // Validate the Score in the database
        List<Score> scoreList = scoreRepository.findAll();
        assertThat(scoreList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllScores() throws Exception {
        // Initialize the database
        scoreRepository.saveAndFlush(score);

        // Get all the scoreList
        restScoreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(score.getId().intValue())))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS)));
    }

    @Test
    @Transactional
    void getScore() throws Exception {
        // Initialize the database
        scoreRepository.saveAndFlush(score);

        // Get the score
        restScoreMockMvc
            .perform(get(ENTITY_API_URL_ID, score.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(score.getId().intValue()))
            .andExpect(jsonPath("$.points").value(DEFAULT_POINTS));
    }

    @Test
    @Transactional
    void getScoresByIdFiltering() throws Exception {
        // Initialize the database
        scoreRepository.saveAndFlush(score);

        Long id = score.getId();

        defaultScoreShouldBeFound("id.equals=" + id);
        defaultScoreShouldNotBeFound("id.notEquals=" + id);

        defaultScoreShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultScoreShouldNotBeFound("id.greaterThan=" + id);

        defaultScoreShouldBeFound("id.lessThanOrEqual=" + id);
        defaultScoreShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllScoresByPointsIsEqualToSomething() throws Exception {
        // Initialize the database
        scoreRepository.saveAndFlush(score);

        // Get all the scoreList where points equals to DEFAULT_POINTS
        defaultScoreShouldBeFound("points.equals=" + DEFAULT_POINTS);

        // Get all the scoreList where points equals to UPDATED_POINTS
        defaultScoreShouldNotBeFound("points.equals=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    void getAllScoresByPointsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        scoreRepository.saveAndFlush(score);

        // Get all the scoreList where points not equals to DEFAULT_POINTS
        defaultScoreShouldNotBeFound("points.notEquals=" + DEFAULT_POINTS);

        // Get all the scoreList where points not equals to UPDATED_POINTS
        defaultScoreShouldBeFound("points.notEquals=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    void getAllScoresByPointsIsInShouldWork() throws Exception {
        // Initialize the database
        scoreRepository.saveAndFlush(score);

        // Get all the scoreList where points in DEFAULT_POINTS or UPDATED_POINTS
        defaultScoreShouldBeFound("points.in=" + DEFAULT_POINTS + "," + UPDATED_POINTS);

        // Get all the scoreList where points equals to UPDATED_POINTS
        defaultScoreShouldNotBeFound("points.in=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    void getAllScoresByPointsIsNullOrNotNull() throws Exception {
        // Initialize the database
        scoreRepository.saveAndFlush(score);

        // Get all the scoreList where points is not null
        defaultScoreShouldBeFound("points.specified=true");

        // Get all the scoreList where points is null
        defaultScoreShouldNotBeFound("points.specified=false");
    }

    @Test
    @Transactional
    void getAllScoresByPointsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        scoreRepository.saveAndFlush(score);

        // Get all the scoreList where points is greater than or equal to DEFAULT_POINTS
        defaultScoreShouldBeFound("points.greaterThanOrEqual=" + DEFAULT_POINTS);

        // Get all the scoreList where points is greater than or equal to UPDATED_POINTS
        defaultScoreShouldNotBeFound("points.greaterThanOrEqual=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    void getAllScoresByPointsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        scoreRepository.saveAndFlush(score);

        // Get all the scoreList where points is less than or equal to DEFAULT_POINTS
        defaultScoreShouldBeFound("points.lessThanOrEqual=" + DEFAULT_POINTS);

        // Get all the scoreList where points is less than or equal to SMALLER_POINTS
        defaultScoreShouldNotBeFound("points.lessThanOrEqual=" + SMALLER_POINTS);
    }

    @Test
    @Transactional
    void getAllScoresByPointsIsLessThanSomething() throws Exception {
        // Initialize the database
        scoreRepository.saveAndFlush(score);

        // Get all the scoreList where points is less than DEFAULT_POINTS
        defaultScoreShouldNotBeFound("points.lessThan=" + DEFAULT_POINTS);

        // Get all the scoreList where points is less than UPDATED_POINTS
        defaultScoreShouldBeFound("points.lessThan=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    void getAllScoresByPointsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        scoreRepository.saveAndFlush(score);

        // Get all the scoreList where points is greater than DEFAULT_POINTS
        defaultScoreShouldNotBeFound("points.greaterThan=" + DEFAULT_POINTS);

        // Get all the scoreList where points is greater than SMALLER_POINTS
        defaultScoreShouldBeFound("points.greaterThan=" + SMALLER_POINTS);
    }

    @Test
    @Transactional
    void getAllScoresByGameIsEqualToSomething() throws Exception {
        // Get already existing entity
        Game game = score.getGame();
        scoreRepository.saveAndFlush(score);
        Long gameId = game.getId();

        // Get all the scoreList where game equals to gameId
        defaultScoreShouldBeFound("gameId.equals=" + gameId);

        // Get all the scoreList where game equals to (gameId + 1)
        defaultScoreShouldNotBeFound("gameId.equals=" + (gameId + 1));
    }

    @Test
    @Transactional
    void getAllScoresByUserIsEqualToSomething() throws Exception {
        // Get already existing entity
        User user = score.getUser();
        scoreRepository.saveAndFlush(score);
        String userId = user.getId();

        // Get all the scoreList where user equals to userId
        defaultScoreShouldBeFound("userId.equals=" + userId);

        // Get all the scoreList where user equals to "invalid-id"
        defaultScoreShouldNotBeFound("userId.equals=" + "invalid-id");
    }

    @Test
    @Transactional
    void getAllScoresByTournamentIsEqualToSomething() throws Exception {
        // Get already existing entity
        Tournament tournament = score.getTournament();
        scoreRepository.saveAndFlush(score);
        Long tournamentId = tournament.getId();

        // Get all the scoreList where tournament equals to tournamentId
        defaultScoreShouldBeFound("tournamentId.equals=" + tournamentId);

        // Get all the scoreList where tournament equals to (tournamentId + 1)
        defaultScoreShouldNotBeFound("tournamentId.equals=" + (tournamentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultScoreShouldBeFound(String filter) throws Exception {
        restScoreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(score.getId().intValue())))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS)));

        // Check, that the count call also returns 1
        restScoreMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultScoreShouldNotBeFound(String filter) throws Exception {
        restScoreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restScoreMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingScore() throws Exception {
        // Get the score
        restScoreMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewScore() throws Exception {
        // Initialize the database
        scoreRepository.saveAndFlush(score);

        int databaseSizeBeforeUpdate = scoreRepository.findAll().size();

        // Update the score
        Score updatedScore = scoreRepository
            .findById(new ScoreId(score.getGame().getId(), score.getUser().getId(), score.getTournament().getId()))
            .get();
        // Disconnect from session so that the updates on updatedScore are not directly saved in db
        em.detach(updatedScore);
        updatedScore.points(UPDATED_POINTS);

        restScoreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedScore.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedScore))
            )
            .andExpect(status().isOk());

        // Validate the Score in the database
        List<Score> scoreList = scoreRepository.findAll();
        assertThat(scoreList).hasSize(databaseSizeBeforeUpdate);
        Score testScore = scoreList.get(scoreList.size() - 1);
        assertThat(testScore.getPoints()).isEqualTo(UPDATED_POINTS);
    }

    @Test
    @Transactional
    void putNonExistingScore() throws Exception {
        int databaseSizeBeforeUpdate = scoreRepository.findAll().size();
        score.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restScoreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, score.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(score))
            )
            .andExpect(status().isBadRequest());

        // Validate the Score in the database
        List<Score> scoreList = scoreRepository.findAll();
        assertThat(scoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchScore() throws Exception {
        int databaseSizeBeforeUpdate = scoreRepository.findAll().size();
        score.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScoreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(score))
            )
            .andExpect(status().isBadRequest());

        // Validate the Score in the database
        List<Score> scoreList = scoreRepository.findAll();
        assertThat(scoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamScore() throws Exception {
        int databaseSizeBeforeUpdate = scoreRepository.findAll().size();
        score.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScoreMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(score))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Score in the database
        List<Score> scoreList = scoreRepository.findAll();
        assertThat(scoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateScoreWithPatch() throws Exception {
        // Initialize the database
        scoreRepository.saveAndFlush(score);

        int databaseSizeBeforeUpdate = scoreRepository.findAll().size();

        // Update the score using partial update
        Score partialUpdatedScore = new Score();
        partialUpdatedScore.setId(score.getId());

        restScoreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedScore.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedScore))
            )
            .andExpect(status().isOk());

        // Validate the Score in the database
        List<Score> scoreList = scoreRepository.findAll();
        assertThat(scoreList).hasSize(databaseSizeBeforeUpdate);
        Score testScore = scoreList.get(scoreList.size() - 1);
        assertThat(testScore.getPoints()).isEqualTo(DEFAULT_POINTS);
    }

    @Test
    @Transactional
    void fullUpdateScoreWithPatch() throws Exception {
        // Initialize the database
        scoreRepository.saveAndFlush(score);

        int databaseSizeBeforeUpdate = scoreRepository.findAll().size();

        // Update the score using partial update
        Score partialUpdatedScore = new Score();
        partialUpdatedScore.setId(score.getId());

        partialUpdatedScore.points(UPDATED_POINTS);

        restScoreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedScore.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedScore))
            )
            .andExpect(status().isOk());

        // Validate the Score in the database
        List<Score> scoreList = scoreRepository.findAll();
        assertThat(scoreList).hasSize(databaseSizeBeforeUpdate);
        Score testScore = scoreList.get(scoreList.size() - 1);
        assertThat(testScore.getPoints()).isEqualTo(UPDATED_POINTS);
    }

    @Test
    @Transactional
    void patchNonExistingScore() throws Exception {
        int databaseSizeBeforeUpdate = scoreRepository.findAll().size();
        score.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restScoreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, score.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(score))
            )
            .andExpect(status().isBadRequest());

        // Validate the Score in the database
        List<Score> scoreList = scoreRepository.findAll();
        assertThat(scoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchScore() throws Exception {
        int databaseSizeBeforeUpdate = scoreRepository.findAll().size();
        score.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScoreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(score))
            )
            .andExpect(status().isBadRequest());

        // Validate the Score in the database
        List<Score> scoreList = scoreRepository.findAll();
        assertThat(scoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamScore() throws Exception {
        int databaseSizeBeforeUpdate = scoreRepository.findAll().size();
        score.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScoreMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(score))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Score in the database
        List<Score> scoreList = scoreRepository.findAll();
        assertThat(scoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteScore() throws Exception {
        // Initialize the database
        scoreRepository.saveAndFlush(score);

        int databaseSizeBeforeDelete = scoreRepository.findAll().size();

        // Delete the score
        restScoreMockMvc
            .perform(delete(ENTITY_API_URL_ID, score.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Score> scoreList = scoreRepository.findAll();
        assertThat(scoreList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

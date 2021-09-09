package com.sheepit.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sheepit.app.IntegrationTest;
import com.sheepit.app.domain.Group;
import com.sheepit.app.domain.User;
import com.sheepit.app.repository.GroupRepository;
import com.sheepit.app.service.GroupService;
import com.sheepit.app.service.criteria.GroupCriteria;
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
 * Integration tests for the {@link GroupResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class GroupResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/groups";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GroupRepository groupRepository;

    @Mock
    private GroupRepository groupRepositoryMock;

    @Mock
    private GroupService groupServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGroupMockMvc;

    private Group group;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Group createEntity(EntityManager em) {
        Group group = new Group().name(DEFAULT_NAME);
        return group;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Group createUpdatedEntity(EntityManager em) {
        Group group = new Group().name(UPDATED_NAME);
        return group;
    }

    @BeforeEach
    public void initTest() {
        group = createEntity(em);
    }

    @Test
    @Transactional
    void createGroup() throws Exception {
        int databaseSizeBeforeCreate = groupRepository.findAll().size();
        // Create the Group
        restGroupMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(group))
            )
            .andExpect(status().isCreated());

        // Validate the Group in the database
        List<Group> groupList = groupRepository.findAll();
        assertThat(groupList).hasSize(databaseSizeBeforeCreate + 1);
        Group testGroup = groupList.get(groupList.size() - 1);
        assertThat(testGroup.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createGroupWithExistingId() throws Exception {
        // Create the Group with an existing ID
        group.setId(1L);

        int databaseSizeBeforeCreate = groupRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGroupMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(group))
            )
            .andExpect(status().isBadRequest());

        // Validate the Group in the database
        List<Group> groupList = groupRepository.findAll();
        assertThat(groupList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = groupRepository.findAll().size();
        // set the field null
        group.setName(null);

        // Create the Group, which fails.

        restGroupMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(group))
            )
            .andExpect(status().isBadRequest());

        List<Group> groupList = groupRepository.findAll();
        assertThat(groupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllGroups() throws Exception {
        // Initialize the database
        groupRepository.saveAndFlush(group);

        // Get all the groupList
        restGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(group.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllGroupsWithEagerRelationshipsIsEnabled() throws Exception {
        when(groupServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restGroupMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(groupServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllGroupsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(groupServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restGroupMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(groupServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getGroup() throws Exception {
        // Initialize the database
        groupRepository.saveAndFlush(group);

        // Get the group
        restGroupMockMvc
            .perform(get(ENTITY_API_URL_ID, group.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(group.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getGroupsByIdFiltering() throws Exception {
        // Initialize the database
        groupRepository.saveAndFlush(group);

        Long id = group.getId();

        defaultGroupShouldBeFound("id.equals=" + id);
        defaultGroupShouldNotBeFound("id.notEquals=" + id);

        defaultGroupShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultGroupShouldNotBeFound("id.greaterThan=" + id);

        defaultGroupShouldBeFound("id.lessThanOrEqual=" + id);
        defaultGroupShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllGroupsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        groupRepository.saveAndFlush(group);

        // Get all the groupList where name equals to DEFAULT_NAME
        defaultGroupShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the groupList where name equals to UPDATED_NAME
        defaultGroupShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllGroupsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        groupRepository.saveAndFlush(group);

        // Get all the groupList where name not equals to DEFAULT_NAME
        defaultGroupShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the groupList where name not equals to UPDATED_NAME
        defaultGroupShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllGroupsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        groupRepository.saveAndFlush(group);

        // Get all the groupList where name in DEFAULT_NAME or UPDATED_NAME
        defaultGroupShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the groupList where name equals to UPDATED_NAME
        defaultGroupShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllGroupsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        groupRepository.saveAndFlush(group);

        // Get all the groupList where name is not null
        defaultGroupShouldBeFound("name.specified=true");

        // Get all the groupList where name is null
        defaultGroupShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllGroupsByNameContainsSomething() throws Exception {
        // Initialize the database
        groupRepository.saveAndFlush(group);

        // Get all the groupList where name contains DEFAULT_NAME
        defaultGroupShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the groupList where name contains UPDATED_NAME
        defaultGroupShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllGroupsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        groupRepository.saveAndFlush(group);

        // Get all the groupList where name does not contain DEFAULT_NAME
        defaultGroupShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the groupList where name does not contain UPDATED_NAME
        defaultGroupShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllGroupsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        groupRepository.saveAndFlush(group);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        group.addUser(user);
        groupRepository.saveAndFlush(group);
        String userId = user.getId();

        // Get all the groupList where user equals to userId
        defaultGroupShouldBeFound("userId.equals=" + userId);

        // Get all the groupList where user equals to "invalid-id"
        defaultGroupShouldNotBeFound("userId.equals=" + "invalid-id");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultGroupShouldBeFound(String filter) throws Exception {
        restGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(group.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restGroupMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultGroupShouldNotBeFound(String filter) throws Exception {
        restGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restGroupMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingGroup() throws Exception {
        // Get the group
        restGroupMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewGroup() throws Exception {
        // Initialize the database
        groupRepository.saveAndFlush(group);

        int databaseSizeBeforeUpdate = groupRepository.findAll().size();

        // Update the group
        Group updatedGroup = groupRepository.findById(group.getId()).get();
        // Disconnect from session so that the updates on updatedGroup are not directly saved in db
        em.detach(updatedGroup);
        updatedGroup.name(UPDATED_NAME);

        restGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedGroup.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedGroup))
            )
            .andExpect(status().isOk());

        // Validate the Group in the database
        List<Group> groupList = groupRepository.findAll();
        assertThat(groupList).hasSize(databaseSizeBeforeUpdate);
        Group testGroup = groupList.get(groupList.size() - 1);
        assertThat(testGroup.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingGroup() throws Exception {
        int databaseSizeBeforeUpdate = groupRepository.findAll().size();
        group.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, group.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(group))
            )
            .andExpect(status().isBadRequest());

        // Validate the Group in the database
        List<Group> groupList = groupRepository.findAll();
        assertThat(groupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGroup() throws Exception {
        int databaseSizeBeforeUpdate = groupRepository.findAll().size();
        group.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(group))
            )
            .andExpect(status().isBadRequest());

        // Validate the Group in the database
        List<Group> groupList = groupRepository.findAll();
        assertThat(groupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGroup() throws Exception {
        int databaseSizeBeforeUpdate = groupRepository.findAll().size();
        group.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGroupMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(group))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Group in the database
        List<Group> groupList = groupRepository.findAll();
        assertThat(groupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGroupWithPatch() throws Exception {
        // Initialize the database
        groupRepository.saveAndFlush(group);

        int databaseSizeBeforeUpdate = groupRepository.findAll().size();

        // Update the group using partial update
        Group partialUpdatedGroup = new Group();
        partialUpdatedGroup.setId(group.getId());

        restGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGroup.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGroup))
            )
            .andExpect(status().isOk());

        // Validate the Group in the database
        List<Group> groupList = groupRepository.findAll();
        assertThat(groupList).hasSize(databaseSizeBeforeUpdate);
        Group testGroup = groupList.get(groupList.size() - 1);
        assertThat(testGroup.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateGroupWithPatch() throws Exception {
        // Initialize the database
        groupRepository.saveAndFlush(group);

        int databaseSizeBeforeUpdate = groupRepository.findAll().size();

        // Update the group using partial update
        Group partialUpdatedGroup = new Group();
        partialUpdatedGroup.setId(group.getId());

        partialUpdatedGroup.name(UPDATED_NAME);

        restGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGroup.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGroup))
            )
            .andExpect(status().isOk());

        // Validate the Group in the database
        List<Group> groupList = groupRepository.findAll();
        assertThat(groupList).hasSize(databaseSizeBeforeUpdate);
        Group testGroup = groupList.get(groupList.size() - 1);
        assertThat(testGroup.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingGroup() throws Exception {
        int databaseSizeBeforeUpdate = groupRepository.findAll().size();
        group.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, group.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(group))
            )
            .andExpect(status().isBadRequest());

        // Validate the Group in the database
        List<Group> groupList = groupRepository.findAll();
        assertThat(groupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGroup() throws Exception {
        int databaseSizeBeforeUpdate = groupRepository.findAll().size();
        group.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(group))
            )
            .andExpect(status().isBadRequest());

        // Validate the Group in the database
        List<Group> groupList = groupRepository.findAll();
        assertThat(groupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGroup() throws Exception {
        int databaseSizeBeforeUpdate = groupRepository.findAll().size();
        group.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGroupMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(group))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Group in the database
        List<Group> groupList = groupRepository.findAll();
        assertThat(groupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGroup() throws Exception {
        // Initialize the database
        groupRepository.saveAndFlush(group);

        int databaseSizeBeforeDelete = groupRepository.findAll().size();

        // Delete the group
        restGroupMockMvc
            .perform(delete(ENTITY_API_URL_ID, group.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Group> groupList = groupRepository.findAll();
        assertThat(groupList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

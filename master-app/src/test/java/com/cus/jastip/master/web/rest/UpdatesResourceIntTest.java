package com.cus.jastip.master.web.rest;

import com.cus.jastip.master.MasterApp;

import com.cus.jastip.master.config.SecurityBeanOverrideConfiguration;

import com.cus.jastip.master.domain.Updates;
import com.cus.jastip.master.repository.UpdatesRepository;
import com.cus.jastip.master.repository.search.UpdatesSearchRepository;
import com.cus.jastip.master.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.cus.jastip.master.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cus.jastip.master.domain.enumeration.UpdateType;
/**
 * Test class for the UpdatesResource REST controller.
 *
 * @see UpdatesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MasterApp.class, SecurityBeanOverrideConfiguration.class})
public class UpdatesResourceIntTest {

    private static final String DEFAULT_ENTITY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ENTITY_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_ENTITY_ID = 1L;
    private static final Long UPDATED_ENTITY_ID = 2L;

    private static final UpdateType DEFAULT_UPDATE_TYPE = UpdateType.CREATE;
    private static final UpdateType UPDATED_UPDATE_TYPE = UpdateType.UPDATE;

    private static final Instant DEFAULT_UPDATE_DATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATE_DATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private UpdatesRepository updatesRepository;

    @Autowired
    private UpdatesSearchRepository updatesSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restUpdatesMockMvc;

    private Updates updates;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UpdatesResource updatesResource = new UpdatesResource(updatesRepository, updatesSearchRepository);
        this.restUpdatesMockMvc = MockMvcBuilders.standaloneSetup(updatesResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Updates createEntity(EntityManager em) {
        Updates updates = new Updates()
            .entityName(DEFAULT_ENTITY_NAME)
            .entityId(DEFAULT_ENTITY_ID)
            .updateType(DEFAULT_UPDATE_TYPE)
            .updateDateTime(DEFAULT_UPDATE_DATE_TIME);
        return updates;
    }

    @Before
    public void initTest() {
        updatesSearchRepository.deleteAll();
        updates = createEntity(em);
    }

    @Test
    @Transactional
    public void createUpdates() throws Exception {
        int databaseSizeBeforeCreate = updatesRepository.findAll().size();

        // Create the Updates
        restUpdatesMockMvc.perform(post("/api/updates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updates)))
            .andExpect(status().isCreated());

        // Validate the Updates in the database
        List<Updates> updatesList = updatesRepository.findAll();
        assertThat(updatesList).hasSize(databaseSizeBeforeCreate + 1);
        Updates testUpdates = updatesList.get(updatesList.size() - 1);
        assertThat(testUpdates.getEntityName()).isEqualTo(DEFAULT_ENTITY_NAME);
        assertThat(testUpdates.getEntityId()).isEqualTo(DEFAULT_ENTITY_ID);
        assertThat(testUpdates.getUpdateType()).isEqualTo(DEFAULT_UPDATE_TYPE);
        assertThat(testUpdates.getUpdateDateTime()).isEqualTo(DEFAULT_UPDATE_DATE_TIME);

        // Validate the Updates in Elasticsearch
        Updates updatesEs = updatesSearchRepository.findOne(testUpdates.getId());
        assertThat(updatesEs).isEqualToIgnoringGivenFields(testUpdates);
    }

    @Test
    @Transactional
    public void createUpdatesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = updatesRepository.findAll().size();

        // Create the Updates with an existing ID
        updates.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUpdatesMockMvc.perform(post("/api/updates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updates)))
            .andExpect(status().isBadRequest());

        // Validate the Updates in the database
        List<Updates> updatesList = updatesRepository.findAll();
        assertThat(updatesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkEntityNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = updatesRepository.findAll().size();
        // set the field null
        updates.setEntityName(null);

        // Create the Updates, which fails.

        restUpdatesMockMvc.perform(post("/api/updates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updates)))
            .andExpect(status().isBadRequest());

        List<Updates> updatesList = updatesRepository.findAll();
        assertThat(updatesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUpdates() throws Exception {
        // Initialize the database
        updatesRepository.saveAndFlush(updates);

        // Get all the updatesList
        restUpdatesMockMvc.perform(get("/api/updates?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(updates.getId().intValue())))
            .andExpect(jsonPath("$.[*].entityName").value(hasItem(DEFAULT_ENTITY_NAME.toString())))
            .andExpect(jsonPath("$.[*].entityId").value(hasItem(DEFAULT_ENTITY_ID.intValue())))
            .andExpect(jsonPath("$.[*].updateType").value(hasItem(DEFAULT_UPDATE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].updateDateTime").value(hasItem(DEFAULT_UPDATE_DATE_TIME.toString())));
    }

    @Test
    @Transactional
    public void getUpdates() throws Exception {
        // Initialize the database
        updatesRepository.saveAndFlush(updates);

        // Get the updates
        restUpdatesMockMvc.perform(get("/api/updates/{id}", updates.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(updates.getId().intValue()))
            .andExpect(jsonPath("$.entityName").value(DEFAULT_ENTITY_NAME.toString()))
            .andExpect(jsonPath("$.entityId").value(DEFAULT_ENTITY_ID.intValue()))
            .andExpect(jsonPath("$.updateType").value(DEFAULT_UPDATE_TYPE.toString()))
            .andExpect(jsonPath("$.updateDateTime").value(DEFAULT_UPDATE_DATE_TIME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUpdates() throws Exception {
        // Get the updates
        restUpdatesMockMvc.perform(get("/api/updates/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUpdates() throws Exception {
        // Initialize the database
        updatesRepository.saveAndFlush(updates);
        updatesSearchRepository.save(updates);
        int databaseSizeBeforeUpdate = updatesRepository.findAll().size();

        // Update the updates
        Updates updatedUpdates = updatesRepository.findOne(updates.getId());
        // Disconnect from session so that the updates on updatedUpdates are not directly saved in db
        em.detach(updatedUpdates);
        updatedUpdates
            .entityName(UPDATED_ENTITY_NAME)
            .entityId(UPDATED_ENTITY_ID)
            .updateType(UPDATED_UPDATE_TYPE)
            .updateDateTime(UPDATED_UPDATE_DATE_TIME);

        restUpdatesMockMvc.perform(put("/api/updates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUpdates)))
            .andExpect(status().isOk());

        // Validate the Updates in the database
        List<Updates> updatesList = updatesRepository.findAll();
        assertThat(updatesList).hasSize(databaseSizeBeforeUpdate);
        Updates testUpdates = updatesList.get(updatesList.size() - 1);
        assertThat(testUpdates.getEntityName()).isEqualTo(UPDATED_ENTITY_NAME);
        assertThat(testUpdates.getEntityId()).isEqualTo(UPDATED_ENTITY_ID);
        assertThat(testUpdates.getUpdateType()).isEqualTo(UPDATED_UPDATE_TYPE);
        assertThat(testUpdates.getUpdateDateTime()).isEqualTo(UPDATED_UPDATE_DATE_TIME);

        // Validate the Updates in Elasticsearch
        Updates updatesEs = updatesSearchRepository.findOne(testUpdates.getId());
        assertThat(updatesEs).isEqualToIgnoringGivenFields(testUpdates);
    }

    @Test
    @Transactional
    public void updateNonExistingUpdates() throws Exception {
        int databaseSizeBeforeUpdate = updatesRepository.findAll().size();

        // Create the Updates

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restUpdatesMockMvc.perform(put("/api/updates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updates)))
            .andExpect(status().isCreated());

        // Validate the Updates in the database
        List<Updates> updatesList = updatesRepository.findAll();
        assertThat(updatesList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteUpdates() throws Exception {
        // Initialize the database
        updatesRepository.saveAndFlush(updates);
        updatesSearchRepository.save(updates);
        int databaseSizeBeforeDelete = updatesRepository.findAll().size();

        // Get the updates
        restUpdatesMockMvc.perform(delete("/api/updates/{id}", updates.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean updatesExistsInEs = updatesSearchRepository.exists(updates.getId());
        assertThat(updatesExistsInEs).isFalse();

        // Validate the database is empty
        List<Updates> updatesList = updatesRepository.findAll();
        assertThat(updatesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchUpdates() throws Exception {
        // Initialize the database
        updatesRepository.saveAndFlush(updates);
        updatesSearchRepository.save(updates);

        // Search the updates
        restUpdatesMockMvc.perform(get("/api/_search/updates?query=id:" + updates.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(updates.getId().intValue())))
            .andExpect(jsonPath("$.[*].entityName").value(hasItem(DEFAULT_ENTITY_NAME.toString())))
            .andExpect(jsonPath("$.[*].entityId").value(hasItem(DEFAULT_ENTITY_ID.intValue())))
            .andExpect(jsonPath("$.[*].updateType").value(hasItem(DEFAULT_UPDATE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].updateDateTime").value(hasItem(DEFAULT_UPDATE_DATE_TIME.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Updates.class);
        Updates updates1 = new Updates();
        updates1.setId(1L);
        Updates updates2 = new Updates();
        updates2.setId(updates1.getId());
        assertThat(updates1).isEqualTo(updates2);
        updates2.setId(2L);
        assertThat(updates1).isNotEqualTo(updates2);
        updates1.setId(null);
        assertThat(updates1).isNotEqualTo(updates2);
    }
}

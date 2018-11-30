package com.cus.jastip.master.web.rest;

import com.cus.jastip.master.MasterApp;

import com.cus.jastip.master.config.SecurityBeanOverrideConfiguration;

import com.cus.jastip.master.domain.MasterAuditConfig;
import com.cus.jastip.master.repository.MasterAuditConfigRepository;
import com.cus.jastip.master.repository.search.MasterAuditConfigSearchRepository;
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
import java.util.List;

import static com.cus.jastip.master.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the MasterAuditConfigResource REST controller.
 *
 * @see MasterAuditConfigResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MasterApp.class, SecurityBeanOverrideConfiguration.class})
public class MasterAuditConfigResourceIntTest {

    private static final String DEFAULT_ENTITY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ENTITY_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE_STATUS = false;
    private static final Boolean UPDATED_ACTIVE_STATUS = true;

    @Autowired
    private MasterAuditConfigRepository masterAuditConfigRepository;

    @Autowired
    private MasterAuditConfigSearchRepository masterAuditConfigSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMasterAuditConfigMockMvc;

    private MasterAuditConfig masterAuditConfig;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MasterAuditConfigResource masterAuditConfigResource = new MasterAuditConfigResource(masterAuditConfigRepository, masterAuditConfigSearchRepository);
        this.restMasterAuditConfigMockMvc = MockMvcBuilders.standaloneSetup(masterAuditConfigResource)
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
    public static MasterAuditConfig createEntity(EntityManager em) {
        MasterAuditConfig masterAuditConfig = new MasterAuditConfig()
            .entityName(DEFAULT_ENTITY_NAME)
            .activeStatus(DEFAULT_ACTIVE_STATUS);
        return masterAuditConfig;
    }

    @Before
    public void initTest() {
        masterAuditConfigSearchRepository.deleteAll();
        masterAuditConfig = createEntity(em);
    }

    @Test
    @Transactional
    public void createMasterAuditConfig() throws Exception {
        int databaseSizeBeforeCreate = masterAuditConfigRepository.findAll().size();

        // Create the MasterAuditConfig
        restMasterAuditConfigMockMvc.perform(post("/api/master-audit-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(masterAuditConfig)))
            .andExpect(status().isCreated());

        // Validate the MasterAuditConfig in the database
        List<MasterAuditConfig> masterAuditConfigList = masterAuditConfigRepository.findAll();
        assertThat(masterAuditConfigList).hasSize(databaseSizeBeforeCreate + 1);
        MasterAuditConfig testMasterAuditConfig = masterAuditConfigList.get(masterAuditConfigList.size() - 1);
        assertThat(testMasterAuditConfig.getEntityName()).isEqualTo(DEFAULT_ENTITY_NAME);
        assertThat(testMasterAuditConfig.isActiveStatus()).isEqualTo(DEFAULT_ACTIVE_STATUS);

        // Validate the MasterAuditConfig in Elasticsearch
        MasterAuditConfig masterAuditConfigEs = masterAuditConfigSearchRepository.findOne(testMasterAuditConfig.getId());
        assertThat(masterAuditConfigEs).isEqualToIgnoringGivenFields(testMasterAuditConfig);
    }

    @Test
    @Transactional
    public void createMasterAuditConfigWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = masterAuditConfigRepository.findAll().size();

        // Create the MasterAuditConfig with an existing ID
        masterAuditConfig.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMasterAuditConfigMockMvc.perform(post("/api/master-audit-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(masterAuditConfig)))
            .andExpect(status().isBadRequest());

        // Validate the MasterAuditConfig in the database
        List<MasterAuditConfig> masterAuditConfigList = masterAuditConfigRepository.findAll();
        assertThat(masterAuditConfigList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkEntityNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = masterAuditConfigRepository.findAll().size();
        // set the field null
        masterAuditConfig.setEntityName(null);

        // Create the MasterAuditConfig, which fails.

        restMasterAuditConfigMockMvc.perform(post("/api/master-audit-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(masterAuditConfig)))
            .andExpect(status().isBadRequest());

        List<MasterAuditConfig> masterAuditConfigList = masterAuditConfigRepository.findAll();
        assertThat(masterAuditConfigList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMasterAuditConfigs() throws Exception {
        // Initialize the database
        masterAuditConfigRepository.saveAndFlush(masterAuditConfig);

        // Get all the masterAuditConfigList
        restMasterAuditConfigMockMvc.perform(get("/api/master-audit-configs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(masterAuditConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].entityName").value(hasItem(DEFAULT_ENTITY_NAME.toString())))
            .andExpect(jsonPath("$.[*].activeStatus").value(hasItem(DEFAULT_ACTIVE_STATUS.booleanValue())));
    }

    @Test
    @Transactional
    public void getMasterAuditConfig() throws Exception {
        // Initialize the database
        masterAuditConfigRepository.saveAndFlush(masterAuditConfig);

        // Get the masterAuditConfig
        restMasterAuditConfigMockMvc.perform(get("/api/master-audit-configs/{id}", masterAuditConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(masterAuditConfig.getId().intValue()))
            .andExpect(jsonPath("$.entityName").value(DEFAULT_ENTITY_NAME.toString()))
            .andExpect(jsonPath("$.activeStatus").value(DEFAULT_ACTIVE_STATUS.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingMasterAuditConfig() throws Exception {
        // Get the masterAuditConfig
        restMasterAuditConfigMockMvc.perform(get("/api/master-audit-configs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMasterAuditConfig() throws Exception {
        // Initialize the database
        masterAuditConfigRepository.saveAndFlush(masterAuditConfig);
        masterAuditConfigSearchRepository.save(masterAuditConfig);
        int databaseSizeBeforeUpdate = masterAuditConfigRepository.findAll().size();

        // Update the masterAuditConfig
        MasterAuditConfig updatedMasterAuditConfig = masterAuditConfigRepository.findOne(masterAuditConfig.getId());
        // Disconnect from session so that the updates on updatedMasterAuditConfig are not directly saved in db
        em.detach(updatedMasterAuditConfig);
        updatedMasterAuditConfig
            .entityName(UPDATED_ENTITY_NAME)
            .activeStatus(UPDATED_ACTIVE_STATUS);

        restMasterAuditConfigMockMvc.perform(put("/api/master-audit-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMasterAuditConfig)))
            .andExpect(status().isOk());

        // Validate the MasterAuditConfig in the database
        List<MasterAuditConfig> masterAuditConfigList = masterAuditConfigRepository.findAll();
        assertThat(masterAuditConfigList).hasSize(databaseSizeBeforeUpdate);
        MasterAuditConfig testMasterAuditConfig = masterAuditConfigList.get(masterAuditConfigList.size() - 1);
        assertThat(testMasterAuditConfig.getEntityName()).isEqualTo(UPDATED_ENTITY_NAME);
        assertThat(testMasterAuditConfig.isActiveStatus()).isEqualTo(UPDATED_ACTIVE_STATUS);

        // Validate the MasterAuditConfig in Elasticsearch
        MasterAuditConfig masterAuditConfigEs = masterAuditConfigSearchRepository.findOne(testMasterAuditConfig.getId());
        assertThat(masterAuditConfigEs).isEqualToIgnoringGivenFields(testMasterAuditConfig);
    }

    @Test
    @Transactional
    public void updateNonExistingMasterAuditConfig() throws Exception {
        int databaseSizeBeforeUpdate = masterAuditConfigRepository.findAll().size();

        // Create the MasterAuditConfig

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMasterAuditConfigMockMvc.perform(put("/api/master-audit-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(masterAuditConfig)))
            .andExpect(status().isCreated());

        // Validate the MasterAuditConfig in the database
        List<MasterAuditConfig> masterAuditConfigList = masterAuditConfigRepository.findAll();
        assertThat(masterAuditConfigList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMasterAuditConfig() throws Exception {
        // Initialize the database
        masterAuditConfigRepository.saveAndFlush(masterAuditConfig);
        masterAuditConfigSearchRepository.save(masterAuditConfig);
        int databaseSizeBeforeDelete = masterAuditConfigRepository.findAll().size();

        // Get the masterAuditConfig
        restMasterAuditConfigMockMvc.perform(delete("/api/master-audit-configs/{id}", masterAuditConfig.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean masterAuditConfigExistsInEs = masterAuditConfigSearchRepository.exists(masterAuditConfig.getId());
        assertThat(masterAuditConfigExistsInEs).isFalse();

        // Validate the database is empty
        List<MasterAuditConfig> masterAuditConfigList = masterAuditConfigRepository.findAll();
        assertThat(masterAuditConfigList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMasterAuditConfig() throws Exception {
        // Initialize the database
        masterAuditConfigRepository.saveAndFlush(masterAuditConfig);
        masterAuditConfigSearchRepository.save(masterAuditConfig);

        // Search the masterAuditConfig
        restMasterAuditConfigMockMvc.perform(get("/api/_search/master-audit-configs?query=id:" + masterAuditConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(masterAuditConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].entityName").value(hasItem(DEFAULT_ENTITY_NAME.toString())))
            .andExpect(jsonPath("$.[*].activeStatus").value(hasItem(DEFAULT_ACTIVE_STATUS.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MasterAuditConfig.class);
        MasterAuditConfig masterAuditConfig1 = new MasterAuditConfig();
        masterAuditConfig1.setId(1L);
        MasterAuditConfig masterAuditConfig2 = new MasterAuditConfig();
        masterAuditConfig2.setId(masterAuditConfig1.getId());
        assertThat(masterAuditConfig1).isEqualTo(masterAuditConfig2);
        masterAuditConfig2.setId(2L);
        assertThat(masterAuditConfig1).isNotEqualTo(masterAuditConfig2);
        masterAuditConfig1.setId(null);
        assertThat(masterAuditConfig1).isNotEqualTo(masterAuditConfig2);
    }
}

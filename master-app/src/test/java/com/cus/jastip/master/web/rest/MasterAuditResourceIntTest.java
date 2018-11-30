package com.cus.jastip.master.web.rest;

import com.cus.jastip.master.MasterApp;

import com.cus.jastip.master.config.SecurityBeanOverrideConfiguration;

import com.cus.jastip.master.domain.MasterAudit;
import com.cus.jastip.master.repository.MasterAuditRepository;
import com.cus.jastip.master.repository.search.MasterAuditSearchRepository;
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
 * Test class for the MasterAuditResource REST controller.
 *
 * @see MasterAuditResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MasterApp.class, SecurityBeanOverrideConfiguration.class})
public class MasterAuditResourceIntTest {

    private static final String DEFAULT_ENTITY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ENTITY_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_ENTITY_ID = 1L;
    private static final Long UPDATED_ENTITY_ID = 2L;

    @Autowired
    private MasterAuditRepository masterAuditRepository;

    @Autowired
    private MasterAuditSearchRepository masterAuditSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMasterAuditMockMvc;

    private MasterAudit masterAudit;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MasterAuditResource masterAuditResource = new MasterAuditResource(masterAuditRepository, masterAuditSearchRepository);
        this.restMasterAuditMockMvc = MockMvcBuilders.standaloneSetup(masterAuditResource)
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
    public static MasterAudit createEntity(EntityManager em) {
        MasterAudit masterAudit = new MasterAudit()
            .entityName(DEFAULT_ENTITY_NAME)
            .entityId(DEFAULT_ENTITY_ID);
        return masterAudit;
    }

    @Before
    public void initTest() {
        masterAuditSearchRepository.deleteAll();
        masterAudit = createEntity(em);
    }

    @Test
    @Transactional
    public void createMasterAudit() throws Exception {
        int databaseSizeBeforeCreate = masterAuditRepository.findAll().size();

        // Create the MasterAudit
        restMasterAuditMockMvc.perform(post("/api/master-audits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(masterAudit)))
            .andExpect(status().isCreated());

        // Validate the MasterAudit in the database
        List<MasterAudit> masterAuditList = masterAuditRepository.findAll();
        assertThat(masterAuditList).hasSize(databaseSizeBeforeCreate + 1);
        MasterAudit testMasterAudit = masterAuditList.get(masterAuditList.size() - 1);
        assertThat(testMasterAudit.getEntityName()).isEqualTo(DEFAULT_ENTITY_NAME);
        assertThat(testMasterAudit.getEntityId()).isEqualTo(DEFAULT_ENTITY_ID);

        // Validate the MasterAudit in Elasticsearch
        MasterAudit masterAuditEs = masterAuditSearchRepository.findOne(testMasterAudit.getId());
        assertThat(masterAuditEs).isEqualToIgnoringGivenFields(testMasterAudit);
    }

    @Test
    @Transactional
    public void createMasterAuditWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = masterAuditRepository.findAll().size();

        // Create the MasterAudit with an existing ID
        masterAudit.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMasterAuditMockMvc.perform(post("/api/master-audits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(masterAudit)))
            .andExpect(status().isBadRequest());

        // Validate the MasterAudit in the database
        List<MasterAudit> masterAuditList = masterAuditRepository.findAll();
        assertThat(masterAuditList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkEntityNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = masterAuditRepository.findAll().size();
        // set the field null
        masterAudit.setEntityName(null);

        // Create the MasterAudit, which fails.

        restMasterAuditMockMvc.perform(post("/api/master-audits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(masterAudit)))
            .andExpect(status().isBadRequest());

        List<MasterAudit> masterAuditList = masterAuditRepository.findAll();
        assertThat(masterAuditList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEntityIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = masterAuditRepository.findAll().size();
        // set the field null
        masterAudit.setEntityId(null);

        // Create the MasterAudit, which fails.

        restMasterAuditMockMvc.perform(post("/api/master-audits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(masterAudit)))
            .andExpect(status().isBadRequest());

        List<MasterAudit> masterAuditList = masterAuditRepository.findAll();
        assertThat(masterAuditList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMasterAudits() throws Exception {
        // Initialize the database
        masterAuditRepository.saveAndFlush(masterAudit);

        // Get all the masterAuditList
        restMasterAuditMockMvc.perform(get("/api/master-audits?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(masterAudit.getId().intValue())))
            .andExpect(jsonPath("$.[*].entityName").value(hasItem(DEFAULT_ENTITY_NAME.toString())))
            .andExpect(jsonPath("$.[*].entityId").value(hasItem(DEFAULT_ENTITY_ID.intValue())));
    }

    @Test
    @Transactional
    public void getMasterAudit() throws Exception {
        // Initialize the database
        masterAuditRepository.saveAndFlush(masterAudit);

        // Get the masterAudit
        restMasterAuditMockMvc.perform(get("/api/master-audits/{id}", masterAudit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(masterAudit.getId().intValue()))
            .andExpect(jsonPath("$.entityName").value(DEFAULT_ENTITY_NAME.toString()))
            .andExpect(jsonPath("$.entityId").value(DEFAULT_ENTITY_ID.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingMasterAudit() throws Exception {
        // Get the masterAudit
        restMasterAuditMockMvc.perform(get("/api/master-audits/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMasterAudit() throws Exception {
        // Initialize the database
        masterAuditRepository.saveAndFlush(masterAudit);
        masterAuditSearchRepository.save(masterAudit);
        int databaseSizeBeforeUpdate = masterAuditRepository.findAll().size();

        // Update the masterAudit
        MasterAudit updatedMasterAudit = masterAuditRepository.findOne(masterAudit.getId());
        // Disconnect from session so that the updates on updatedMasterAudit are not directly saved in db
        em.detach(updatedMasterAudit);
        updatedMasterAudit
            .entityName(UPDATED_ENTITY_NAME)
            .entityId(UPDATED_ENTITY_ID);

        restMasterAuditMockMvc.perform(put("/api/master-audits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMasterAudit)))
            .andExpect(status().isOk());

        // Validate the MasterAudit in the database
        List<MasterAudit> masterAuditList = masterAuditRepository.findAll();
        assertThat(masterAuditList).hasSize(databaseSizeBeforeUpdate);
        MasterAudit testMasterAudit = masterAuditList.get(masterAuditList.size() - 1);
        assertThat(testMasterAudit.getEntityName()).isEqualTo(UPDATED_ENTITY_NAME);
        assertThat(testMasterAudit.getEntityId()).isEqualTo(UPDATED_ENTITY_ID);

        // Validate the MasterAudit in Elasticsearch
        MasterAudit masterAuditEs = masterAuditSearchRepository.findOne(testMasterAudit.getId());
        assertThat(masterAuditEs).isEqualToIgnoringGivenFields(testMasterAudit);
    }

    @Test
    @Transactional
    public void updateNonExistingMasterAudit() throws Exception {
        int databaseSizeBeforeUpdate = masterAuditRepository.findAll().size();

        // Create the MasterAudit

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMasterAuditMockMvc.perform(put("/api/master-audits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(masterAudit)))
            .andExpect(status().isCreated());

        // Validate the MasterAudit in the database
        List<MasterAudit> masterAuditList = masterAuditRepository.findAll();
        assertThat(masterAuditList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMasterAudit() throws Exception {
        // Initialize the database
        masterAuditRepository.saveAndFlush(masterAudit);
        masterAuditSearchRepository.save(masterAudit);
        int databaseSizeBeforeDelete = masterAuditRepository.findAll().size();

        // Get the masterAudit
        restMasterAuditMockMvc.perform(delete("/api/master-audits/{id}", masterAudit.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean masterAuditExistsInEs = masterAuditSearchRepository.exists(masterAudit.getId());
        assertThat(masterAuditExistsInEs).isFalse();

        // Validate the database is empty
        List<MasterAudit> masterAuditList = masterAuditRepository.findAll();
        assertThat(masterAuditList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMasterAudit() throws Exception {
        // Initialize the database
        masterAuditRepository.saveAndFlush(masterAudit);
        masterAuditSearchRepository.save(masterAudit);

        // Search the masterAudit
        restMasterAuditMockMvc.perform(get("/api/_search/master-audits?query=id:" + masterAudit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(masterAudit.getId().intValue())))
            .andExpect(jsonPath("$.[*].entityName").value(hasItem(DEFAULT_ENTITY_NAME.toString())))
            .andExpect(jsonPath("$.[*].entityId").value(hasItem(DEFAULT_ENTITY_ID.intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MasterAudit.class);
        MasterAudit masterAudit1 = new MasterAudit();
        masterAudit1.setId(1L);
        MasterAudit masterAudit2 = new MasterAudit();
        masterAudit2.setId(masterAudit1.getId());
        assertThat(masterAudit1).isEqualTo(masterAudit2);
        masterAudit2.setId(2L);
        assertThat(masterAudit1).isNotEqualTo(masterAudit2);
        masterAudit1.setId(null);
        assertThat(masterAudit1).isNotEqualTo(masterAudit2);
    }
}

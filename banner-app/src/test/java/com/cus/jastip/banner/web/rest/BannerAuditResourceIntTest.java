package com.cus.jastip.banner.web.rest;

import com.cus.jastip.banner.BannerApp;

import com.cus.jastip.banner.config.SecurityBeanOverrideConfiguration;

import com.cus.jastip.banner.domain.BannerAudit;
import com.cus.jastip.banner.repository.BannerAuditRepository;
import com.cus.jastip.banner.repository.search.BannerAuditSearchRepository;
import com.cus.jastip.banner.web.rest.errors.ExceptionTranslator;

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

import static com.cus.jastip.banner.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the BannerAuditResource REST controller.
 *
 * @see BannerAuditResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {BannerApp.class, SecurityBeanOverrideConfiguration.class})
public class BannerAuditResourceIntTest {

    private static final String DEFAULT_ENTITY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ENTITY_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_ENTITY_ID = 1L;
    private static final Long UPDATED_ENTITY_ID = 2L;

    @Autowired
    private BannerAuditRepository bannerAuditRepository;

    @Autowired
    private BannerAuditSearchRepository bannerAuditSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBannerAuditMockMvc;

    private BannerAudit bannerAudit;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BannerAuditResource bannerAuditResource = new BannerAuditResource(bannerAuditRepository, bannerAuditSearchRepository);
        this.restBannerAuditMockMvc = MockMvcBuilders.standaloneSetup(bannerAuditResource)
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
    public static BannerAudit createEntity(EntityManager em) {
        BannerAudit bannerAudit = new BannerAudit()
            .entityName(DEFAULT_ENTITY_NAME)
            .entityId(DEFAULT_ENTITY_ID);
        return bannerAudit;
    }

    @Before
    public void initTest() {
        bannerAuditSearchRepository.deleteAll();
        bannerAudit = createEntity(em);
    }

    @Test
    @Transactional
    public void createBannerAudit() throws Exception {
        int databaseSizeBeforeCreate = bannerAuditRepository.findAll().size();

        // Create the BannerAudit
        restBannerAuditMockMvc.perform(post("/api/banner-audits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bannerAudit)))
            .andExpect(status().isCreated());

        // Validate the BannerAudit in the database
        List<BannerAudit> bannerAuditList = bannerAuditRepository.findAll();
        assertThat(bannerAuditList).hasSize(databaseSizeBeforeCreate + 1);
        BannerAudit testBannerAudit = bannerAuditList.get(bannerAuditList.size() - 1);
        assertThat(testBannerAudit.getEntityName()).isEqualTo(DEFAULT_ENTITY_NAME);
        assertThat(testBannerAudit.getEntityId()).isEqualTo(DEFAULT_ENTITY_ID);

        // Validate the BannerAudit in Elasticsearch
        BannerAudit bannerAuditEs = bannerAuditSearchRepository.findOne(testBannerAudit.getId());
        assertThat(bannerAuditEs).isEqualToIgnoringGivenFields(testBannerAudit);
    }

    @Test
    @Transactional
    public void createBannerAuditWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = bannerAuditRepository.findAll().size();

        // Create the BannerAudit with an existing ID
        bannerAudit.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBannerAuditMockMvc.perform(post("/api/banner-audits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bannerAudit)))
            .andExpect(status().isBadRequest());

        // Validate the BannerAudit in the database
        List<BannerAudit> bannerAuditList = bannerAuditRepository.findAll();
        assertThat(bannerAuditList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkEntityNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = bannerAuditRepository.findAll().size();
        // set the field null
        bannerAudit.setEntityName(null);

        // Create the BannerAudit, which fails.

        restBannerAuditMockMvc.perform(post("/api/banner-audits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bannerAudit)))
            .andExpect(status().isBadRequest());

        List<BannerAudit> bannerAuditList = bannerAuditRepository.findAll();
        assertThat(bannerAuditList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBannerAudits() throws Exception {
        // Initialize the database
        bannerAuditRepository.saveAndFlush(bannerAudit);

        // Get all the bannerAuditList
        restBannerAuditMockMvc.perform(get("/api/banner-audits?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bannerAudit.getId().intValue())))
            .andExpect(jsonPath("$.[*].entityName").value(hasItem(DEFAULT_ENTITY_NAME.toString())))
            .andExpect(jsonPath("$.[*].entityId").value(hasItem(DEFAULT_ENTITY_ID.intValue())));
    }

    @Test
    @Transactional
    public void getBannerAudit() throws Exception {
        // Initialize the database
        bannerAuditRepository.saveAndFlush(bannerAudit);

        // Get the bannerAudit
        restBannerAuditMockMvc.perform(get("/api/banner-audits/{id}", bannerAudit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(bannerAudit.getId().intValue()))
            .andExpect(jsonPath("$.entityName").value(DEFAULT_ENTITY_NAME.toString()))
            .andExpect(jsonPath("$.entityId").value(DEFAULT_ENTITY_ID.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingBannerAudit() throws Exception {
        // Get the bannerAudit
        restBannerAuditMockMvc.perform(get("/api/banner-audits/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBannerAudit() throws Exception {
        // Initialize the database
        bannerAuditRepository.saveAndFlush(bannerAudit);
        bannerAuditSearchRepository.save(bannerAudit);
        int databaseSizeBeforeUpdate = bannerAuditRepository.findAll().size();

        // Update the bannerAudit
        BannerAudit updatedBannerAudit = bannerAuditRepository.findOne(bannerAudit.getId());
        // Disconnect from session so that the updates on updatedBannerAudit are not directly saved in db
        em.detach(updatedBannerAudit);
        updatedBannerAudit
            .entityName(UPDATED_ENTITY_NAME)
            .entityId(UPDATED_ENTITY_ID);

        restBannerAuditMockMvc.perform(put("/api/banner-audits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBannerAudit)))
            .andExpect(status().isOk());

        // Validate the BannerAudit in the database
        List<BannerAudit> bannerAuditList = bannerAuditRepository.findAll();
        assertThat(bannerAuditList).hasSize(databaseSizeBeforeUpdate);
        BannerAudit testBannerAudit = bannerAuditList.get(bannerAuditList.size() - 1);
        assertThat(testBannerAudit.getEntityName()).isEqualTo(UPDATED_ENTITY_NAME);
        assertThat(testBannerAudit.getEntityId()).isEqualTo(UPDATED_ENTITY_ID);

        // Validate the BannerAudit in Elasticsearch
        BannerAudit bannerAuditEs = bannerAuditSearchRepository.findOne(testBannerAudit.getId());
        assertThat(bannerAuditEs).isEqualToIgnoringGivenFields(testBannerAudit);
    }

    @Test
    @Transactional
    public void updateNonExistingBannerAudit() throws Exception {
        int databaseSizeBeforeUpdate = bannerAuditRepository.findAll().size();

        // Create the BannerAudit

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBannerAuditMockMvc.perform(put("/api/banner-audits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bannerAudit)))
            .andExpect(status().isCreated());

        // Validate the BannerAudit in the database
        List<BannerAudit> bannerAuditList = bannerAuditRepository.findAll();
        assertThat(bannerAuditList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBannerAudit() throws Exception {
        // Initialize the database
        bannerAuditRepository.saveAndFlush(bannerAudit);
        bannerAuditSearchRepository.save(bannerAudit);
        int databaseSizeBeforeDelete = bannerAuditRepository.findAll().size();

        // Get the bannerAudit
        restBannerAuditMockMvc.perform(delete("/api/banner-audits/{id}", bannerAudit.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean bannerAuditExistsInEs = bannerAuditSearchRepository.exists(bannerAudit.getId());
        assertThat(bannerAuditExistsInEs).isFalse();

        // Validate the database is empty
        List<BannerAudit> bannerAuditList = bannerAuditRepository.findAll();
        assertThat(bannerAuditList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchBannerAudit() throws Exception {
        // Initialize the database
        bannerAuditRepository.saveAndFlush(bannerAudit);
        bannerAuditSearchRepository.save(bannerAudit);

        // Search the bannerAudit
        restBannerAuditMockMvc.perform(get("/api/_search/banner-audits?query=id:" + bannerAudit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bannerAudit.getId().intValue())))
            .andExpect(jsonPath("$.[*].entityName").value(hasItem(DEFAULT_ENTITY_NAME.toString())))
            .andExpect(jsonPath("$.[*].entityId").value(hasItem(DEFAULT_ENTITY_ID.intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BannerAudit.class);
        BannerAudit bannerAudit1 = new BannerAudit();
        bannerAudit1.setId(1L);
        BannerAudit bannerAudit2 = new BannerAudit();
        bannerAudit2.setId(bannerAudit1.getId());
        assertThat(bannerAudit1).isEqualTo(bannerAudit2);
        bannerAudit2.setId(2L);
        assertThat(bannerAudit1).isNotEqualTo(bannerAudit2);
        bannerAudit1.setId(null);
        assertThat(bannerAudit1).isNotEqualTo(bannerAudit2);
    }
}

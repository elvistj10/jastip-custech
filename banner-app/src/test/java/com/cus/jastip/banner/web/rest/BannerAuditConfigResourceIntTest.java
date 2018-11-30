package com.cus.jastip.banner.web.rest;

import com.cus.jastip.banner.BannerApp;

import com.cus.jastip.banner.config.SecurityBeanOverrideConfiguration;

import com.cus.jastip.banner.domain.BannerAuditConfig;
import com.cus.jastip.banner.repository.BannerAuditConfigRepository;
import com.cus.jastip.banner.repository.search.BannerAuditConfigSearchRepository;
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
 * Test class for the BannerAuditConfigResource REST controller.
 *
 * @see BannerAuditConfigResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {BannerApp.class, SecurityBeanOverrideConfiguration.class})
public class BannerAuditConfigResourceIntTest {

    private static final String DEFAULT_ENTITY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ENTITY_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE_STATUS = false;
    private static final Boolean UPDATED_ACTIVE_STATUS = true;

    @Autowired
    private BannerAuditConfigRepository bannerAuditConfigRepository;

    @Autowired
    private BannerAuditConfigSearchRepository bannerAuditConfigSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBannerAuditConfigMockMvc;

    private BannerAuditConfig bannerAuditConfig;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BannerAuditConfigResource bannerAuditConfigResource = new BannerAuditConfigResource(bannerAuditConfigRepository, bannerAuditConfigSearchRepository);
        this.restBannerAuditConfigMockMvc = MockMvcBuilders.standaloneSetup(bannerAuditConfigResource)
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
    public static BannerAuditConfig createEntity(EntityManager em) {
        BannerAuditConfig bannerAuditConfig = new BannerAuditConfig()
            .entityName(DEFAULT_ENTITY_NAME)
            .activeStatus(DEFAULT_ACTIVE_STATUS);
        return bannerAuditConfig;
    }

    @Before
    public void initTest() {
        bannerAuditConfigSearchRepository.deleteAll();
        bannerAuditConfig = createEntity(em);
    }

    @Test
    @Transactional
    public void createBannerAuditConfig() throws Exception {
        int databaseSizeBeforeCreate = bannerAuditConfigRepository.findAll().size();

        // Create the BannerAuditConfig
        restBannerAuditConfigMockMvc.perform(post("/api/banner-audit-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bannerAuditConfig)))
            .andExpect(status().isCreated());

        // Validate the BannerAuditConfig in the database
        List<BannerAuditConfig> bannerAuditConfigList = bannerAuditConfigRepository.findAll();
        assertThat(bannerAuditConfigList).hasSize(databaseSizeBeforeCreate + 1);
        BannerAuditConfig testBannerAuditConfig = bannerAuditConfigList.get(bannerAuditConfigList.size() - 1);
        assertThat(testBannerAuditConfig.getEntityName()).isEqualTo(DEFAULT_ENTITY_NAME);
        assertThat(testBannerAuditConfig.isActiveStatus()).isEqualTo(DEFAULT_ACTIVE_STATUS);

        // Validate the BannerAuditConfig in Elasticsearch
        BannerAuditConfig bannerAuditConfigEs = bannerAuditConfigSearchRepository.findOne(testBannerAuditConfig.getId());
        assertThat(bannerAuditConfigEs).isEqualToIgnoringGivenFields(testBannerAuditConfig);
    }

    @Test
    @Transactional
    public void createBannerAuditConfigWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = bannerAuditConfigRepository.findAll().size();

        // Create the BannerAuditConfig with an existing ID
        bannerAuditConfig.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBannerAuditConfigMockMvc.perform(post("/api/banner-audit-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bannerAuditConfig)))
            .andExpect(status().isBadRequest());

        // Validate the BannerAuditConfig in the database
        List<BannerAuditConfig> bannerAuditConfigList = bannerAuditConfigRepository.findAll();
        assertThat(bannerAuditConfigList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkEntityNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = bannerAuditConfigRepository.findAll().size();
        // set the field null
        bannerAuditConfig.setEntityName(null);

        // Create the BannerAuditConfig, which fails.

        restBannerAuditConfigMockMvc.perform(post("/api/banner-audit-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bannerAuditConfig)))
            .andExpect(status().isBadRequest());

        List<BannerAuditConfig> bannerAuditConfigList = bannerAuditConfigRepository.findAll();
        assertThat(bannerAuditConfigList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBannerAuditConfigs() throws Exception {
        // Initialize the database
        bannerAuditConfigRepository.saveAndFlush(bannerAuditConfig);

        // Get all the bannerAuditConfigList
        restBannerAuditConfigMockMvc.perform(get("/api/banner-audit-configs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bannerAuditConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].entityName").value(hasItem(DEFAULT_ENTITY_NAME.toString())))
            .andExpect(jsonPath("$.[*].activeStatus").value(hasItem(DEFAULT_ACTIVE_STATUS.booleanValue())));
    }

    @Test
    @Transactional
    public void getBannerAuditConfig() throws Exception {
        // Initialize the database
        bannerAuditConfigRepository.saveAndFlush(bannerAuditConfig);

        // Get the bannerAuditConfig
        restBannerAuditConfigMockMvc.perform(get("/api/banner-audit-configs/{id}", bannerAuditConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(bannerAuditConfig.getId().intValue()))
            .andExpect(jsonPath("$.entityName").value(DEFAULT_ENTITY_NAME.toString()))
            .andExpect(jsonPath("$.activeStatus").value(DEFAULT_ACTIVE_STATUS.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingBannerAuditConfig() throws Exception {
        // Get the bannerAuditConfig
        restBannerAuditConfigMockMvc.perform(get("/api/banner-audit-configs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBannerAuditConfig() throws Exception {
        // Initialize the database
        bannerAuditConfigRepository.saveAndFlush(bannerAuditConfig);
        bannerAuditConfigSearchRepository.save(bannerAuditConfig);
        int databaseSizeBeforeUpdate = bannerAuditConfigRepository.findAll().size();

        // Update the bannerAuditConfig
        BannerAuditConfig updatedBannerAuditConfig = bannerAuditConfigRepository.findOne(bannerAuditConfig.getId());
        // Disconnect from session so that the updates on updatedBannerAuditConfig are not directly saved in db
        em.detach(updatedBannerAuditConfig);
        updatedBannerAuditConfig
            .entityName(UPDATED_ENTITY_NAME)
            .activeStatus(UPDATED_ACTIVE_STATUS);

        restBannerAuditConfigMockMvc.perform(put("/api/banner-audit-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBannerAuditConfig)))
            .andExpect(status().isOk());

        // Validate the BannerAuditConfig in the database
        List<BannerAuditConfig> bannerAuditConfigList = bannerAuditConfigRepository.findAll();
        assertThat(bannerAuditConfigList).hasSize(databaseSizeBeforeUpdate);
        BannerAuditConfig testBannerAuditConfig = bannerAuditConfigList.get(bannerAuditConfigList.size() - 1);
        assertThat(testBannerAuditConfig.getEntityName()).isEqualTo(UPDATED_ENTITY_NAME);
        assertThat(testBannerAuditConfig.isActiveStatus()).isEqualTo(UPDATED_ACTIVE_STATUS);

        // Validate the BannerAuditConfig in Elasticsearch
        BannerAuditConfig bannerAuditConfigEs = bannerAuditConfigSearchRepository.findOne(testBannerAuditConfig.getId());
        assertThat(bannerAuditConfigEs).isEqualToIgnoringGivenFields(testBannerAuditConfig);
    }

    @Test
    @Transactional
    public void updateNonExistingBannerAuditConfig() throws Exception {
        int databaseSizeBeforeUpdate = bannerAuditConfigRepository.findAll().size();

        // Create the BannerAuditConfig

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBannerAuditConfigMockMvc.perform(put("/api/banner-audit-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bannerAuditConfig)))
            .andExpect(status().isCreated());

        // Validate the BannerAuditConfig in the database
        List<BannerAuditConfig> bannerAuditConfigList = bannerAuditConfigRepository.findAll();
        assertThat(bannerAuditConfigList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBannerAuditConfig() throws Exception {
        // Initialize the database
        bannerAuditConfigRepository.saveAndFlush(bannerAuditConfig);
        bannerAuditConfigSearchRepository.save(bannerAuditConfig);
        int databaseSizeBeforeDelete = bannerAuditConfigRepository.findAll().size();

        // Get the bannerAuditConfig
        restBannerAuditConfigMockMvc.perform(delete("/api/banner-audit-configs/{id}", bannerAuditConfig.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean bannerAuditConfigExistsInEs = bannerAuditConfigSearchRepository.exists(bannerAuditConfig.getId());
        assertThat(bannerAuditConfigExistsInEs).isFalse();

        // Validate the database is empty
        List<BannerAuditConfig> bannerAuditConfigList = bannerAuditConfigRepository.findAll();
        assertThat(bannerAuditConfigList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchBannerAuditConfig() throws Exception {
        // Initialize the database
        bannerAuditConfigRepository.saveAndFlush(bannerAuditConfig);
        bannerAuditConfigSearchRepository.save(bannerAuditConfig);

        // Search the bannerAuditConfig
        restBannerAuditConfigMockMvc.perform(get("/api/_search/banner-audit-configs?query=id:" + bannerAuditConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bannerAuditConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].entityName").value(hasItem(DEFAULT_ENTITY_NAME.toString())))
            .andExpect(jsonPath("$.[*].activeStatus").value(hasItem(DEFAULT_ACTIVE_STATUS.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BannerAuditConfig.class);
        BannerAuditConfig bannerAuditConfig1 = new BannerAuditConfig();
        bannerAuditConfig1.setId(1L);
        BannerAuditConfig bannerAuditConfig2 = new BannerAuditConfig();
        bannerAuditConfig2.setId(bannerAuditConfig1.getId());
        assertThat(bannerAuditConfig1).isEqualTo(bannerAuditConfig2);
        bannerAuditConfig2.setId(2L);
        assertThat(bannerAuditConfig1).isNotEqualTo(bannerAuditConfig2);
        bannerAuditConfig1.setId(null);
        assertThat(bannerAuditConfig1).isNotEqualTo(bannerAuditConfig2);
    }
}

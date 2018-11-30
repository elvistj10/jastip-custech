package com.cus.jastip.wallet.web.rest;

import com.cus.jastip.wallet.WalletApp;

import com.cus.jastip.wallet.config.SecurityBeanOverrideConfiguration;

import com.cus.jastip.wallet.domain.WalletAuditConfig;
import com.cus.jastip.wallet.repository.WalletAuditConfigRepository;
import com.cus.jastip.wallet.repository.search.WalletAuditConfigSearchRepository;
import com.cus.jastip.wallet.web.rest.errors.ExceptionTranslator;

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

import static com.cus.jastip.wallet.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the WalletAuditConfigResource REST controller.
 *
 * @see WalletAuditConfigResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {WalletApp.class, SecurityBeanOverrideConfiguration.class})
public class WalletAuditConfigResourceIntTest {

    private static final String DEFAULT_ENTITY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ENTITY_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE_STATUS = false;
    private static final Boolean UPDATED_ACTIVE_STATUS = true;

    @Autowired
    private WalletAuditConfigRepository walletAuditConfigRepository;

    @Autowired
    private WalletAuditConfigSearchRepository walletAuditConfigSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restWalletAuditConfigMockMvc;

    private WalletAuditConfig walletAuditConfig;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final WalletAuditConfigResource walletAuditConfigResource = new WalletAuditConfigResource(walletAuditConfigRepository, walletAuditConfigSearchRepository);
        this.restWalletAuditConfigMockMvc = MockMvcBuilders.standaloneSetup(walletAuditConfigResource)
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
    public static WalletAuditConfig createEntity(EntityManager em) {
        WalletAuditConfig walletAuditConfig = new WalletAuditConfig()
            .entityName(DEFAULT_ENTITY_NAME)
            .activeStatus(DEFAULT_ACTIVE_STATUS);
        return walletAuditConfig;
    }

    @Before
    public void initTest() {
        walletAuditConfigSearchRepository.deleteAll();
        walletAuditConfig = createEntity(em);
    }

    @Test
    @Transactional
    public void createWalletAuditConfig() throws Exception {
        int databaseSizeBeforeCreate = walletAuditConfigRepository.findAll().size();

        // Create the WalletAuditConfig
        restWalletAuditConfigMockMvc.perform(post("/api/wallet-audit-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(walletAuditConfig)))
            .andExpect(status().isCreated());

        // Validate the WalletAuditConfig in the database
        List<WalletAuditConfig> walletAuditConfigList = walletAuditConfigRepository.findAll();
        assertThat(walletAuditConfigList).hasSize(databaseSizeBeforeCreate + 1);
        WalletAuditConfig testWalletAuditConfig = walletAuditConfigList.get(walletAuditConfigList.size() - 1);
        assertThat(testWalletAuditConfig.getEntityName()).isEqualTo(DEFAULT_ENTITY_NAME);
        assertThat(testWalletAuditConfig.isActiveStatus()).isEqualTo(DEFAULT_ACTIVE_STATUS);

        // Validate the WalletAuditConfig in Elasticsearch
        WalletAuditConfig walletAuditConfigEs = walletAuditConfigSearchRepository.findOne(testWalletAuditConfig.getId());
        assertThat(walletAuditConfigEs).isEqualToIgnoringGivenFields(testWalletAuditConfig);
    }

    @Test
    @Transactional
    public void createWalletAuditConfigWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = walletAuditConfigRepository.findAll().size();

        // Create the WalletAuditConfig with an existing ID
        walletAuditConfig.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWalletAuditConfigMockMvc.perform(post("/api/wallet-audit-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(walletAuditConfig)))
            .andExpect(status().isBadRequest());

        // Validate the WalletAuditConfig in the database
        List<WalletAuditConfig> walletAuditConfigList = walletAuditConfigRepository.findAll();
        assertThat(walletAuditConfigList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkEntityNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = walletAuditConfigRepository.findAll().size();
        // set the field null
        walletAuditConfig.setEntityName(null);

        // Create the WalletAuditConfig, which fails.

        restWalletAuditConfigMockMvc.perform(post("/api/wallet-audit-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(walletAuditConfig)))
            .andExpect(status().isBadRequest());

        List<WalletAuditConfig> walletAuditConfigList = walletAuditConfigRepository.findAll();
        assertThat(walletAuditConfigList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllWalletAuditConfigs() throws Exception {
        // Initialize the database
        walletAuditConfigRepository.saveAndFlush(walletAuditConfig);

        // Get all the walletAuditConfigList
        restWalletAuditConfigMockMvc.perform(get("/api/wallet-audit-configs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(walletAuditConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].entityName").value(hasItem(DEFAULT_ENTITY_NAME.toString())))
            .andExpect(jsonPath("$.[*].activeStatus").value(hasItem(DEFAULT_ACTIVE_STATUS.booleanValue())));
    }

    @Test
    @Transactional
    public void getWalletAuditConfig() throws Exception {
        // Initialize the database
        walletAuditConfigRepository.saveAndFlush(walletAuditConfig);

        // Get the walletAuditConfig
        restWalletAuditConfigMockMvc.perform(get("/api/wallet-audit-configs/{id}", walletAuditConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(walletAuditConfig.getId().intValue()))
            .andExpect(jsonPath("$.entityName").value(DEFAULT_ENTITY_NAME.toString()))
            .andExpect(jsonPath("$.activeStatus").value(DEFAULT_ACTIVE_STATUS.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingWalletAuditConfig() throws Exception {
        // Get the walletAuditConfig
        restWalletAuditConfigMockMvc.perform(get("/api/wallet-audit-configs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWalletAuditConfig() throws Exception {
        // Initialize the database
        walletAuditConfigRepository.saveAndFlush(walletAuditConfig);
        walletAuditConfigSearchRepository.save(walletAuditConfig);
        int databaseSizeBeforeUpdate = walletAuditConfigRepository.findAll().size();

        // Update the walletAuditConfig
        WalletAuditConfig updatedWalletAuditConfig = walletAuditConfigRepository.findOne(walletAuditConfig.getId());
        // Disconnect from session so that the updates on updatedWalletAuditConfig are not directly saved in db
        em.detach(updatedWalletAuditConfig);
        updatedWalletAuditConfig
            .entityName(UPDATED_ENTITY_NAME)
            .activeStatus(UPDATED_ACTIVE_STATUS);

        restWalletAuditConfigMockMvc.perform(put("/api/wallet-audit-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedWalletAuditConfig)))
            .andExpect(status().isOk());

        // Validate the WalletAuditConfig in the database
        List<WalletAuditConfig> walletAuditConfigList = walletAuditConfigRepository.findAll();
        assertThat(walletAuditConfigList).hasSize(databaseSizeBeforeUpdate);
        WalletAuditConfig testWalletAuditConfig = walletAuditConfigList.get(walletAuditConfigList.size() - 1);
        assertThat(testWalletAuditConfig.getEntityName()).isEqualTo(UPDATED_ENTITY_NAME);
        assertThat(testWalletAuditConfig.isActiveStatus()).isEqualTo(UPDATED_ACTIVE_STATUS);

        // Validate the WalletAuditConfig in Elasticsearch
        WalletAuditConfig walletAuditConfigEs = walletAuditConfigSearchRepository.findOne(testWalletAuditConfig.getId());
        assertThat(walletAuditConfigEs).isEqualToIgnoringGivenFields(testWalletAuditConfig);
    }

    @Test
    @Transactional
    public void updateNonExistingWalletAuditConfig() throws Exception {
        int databaseSizeBeforeUpdate = walletAuditConfigRepository.findAll().size();

        // Create the WalletAuditConfig

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restWalletAuditConfigMockMvc.perform(put("/api/wallet-audit-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(walletAuditConfig)))
            .andExpect(status().isCreated());

        // Validate the WalletAuditConfig in the database
        List<WalletAuditConfig> walletAuditConfigList = walletAuditConfigRepository.findAll();
        assertThat(walletAuditConfigList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteWalletAuditConfig() throws Exception {
        // Initialize the database
        walletAuditConfigRepository.saveAndFlush(walletAuditConfig);
        walletAuditConfigSearchRepository.save(walletAuditConfig);
        int databaseSizeBeforeDelete = walletAuditConfigRepository.findAll().size();

        // Get the walletAuditConfig
        restWalletAuditConfigMockMvc.perform(delete("/api/wallet-audit-configs/{id}", walletAuditConfig.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean walletAuditConfigExistsInEs = walletAuditConfigSearchRepository.exists(walletAuditConfig.getId());
        assertThat(walletAuditConfigExistsInEs).isFalse();

        // Validate the database is empty
        List<WalletAuditConfig> walletAuditConfigList = walletAuditConfigRepository.findAll();
        assertThat(walletAuditConfigList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchWalletAuditConfig() throws Exception {
        // Initialize the database
        walletAuditConfigRepository.saveAndFlush(walletAuditConfig);
        walletAuditConfigSearchRepository.save(walletAuditConfig);

        // Search the walletAuditConfig
        restWalletAuditConfigMockMvc.perform(get("/api/_search/wallet-audit-configs?query=id:" + walletAuditConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(walletAuditConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].entityName").value(hasItem(DEFAULT_ENTITY_NAME.toString())))
            .andExpect(jsonPath("$.[*].activeStatus").value(hasItem(DEFAULT_ACTIVE_STATUS.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WalletAuditConfig.class);
        WalletAuditConfig walletAuditConfig1 = new WalletAuditConfig();
        walletAuditConfig1.setId(1L);
        WalletAuditConfig walletAuditConfig2 = new WalletAuditConfig();
        walletAuditConfig2.setId(walletAuditConfig1.getId());
        assertThat(walletAuditConfig1).isEqualTo(walletAuditConfig2);
        walletAuditConfig2.setId(2L);
        assertThat(walletAuditConfig1).isNotEqualTo(walletAuditConfig2);
        walletAuditConfig1.setId(null);
        assertThat(walletAuditConfig1).isNotEqualTo(walletAuditConfig2);
    }
}

package com.cus.jastip.wallet.web.rest;

import com.cus.jastip.wallet.WalletApp;

import com.cus.jastip.wallet.config.SecurityBeanOverrideConfiguration;

import com.cus.jastip.wallet.domain.WalletAudit;
import com.cus.jastip.wallet.repository.WalletAuditRepository;
import com.cus.jastip.wallet.repository.search.WalletAuditSearchRepository;
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
 * Test class for the WalletAuditResource REST controller.
 *
 * @see WalletAuditResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {WalletApp.class, SecurityBeanOverrideConfiguration.class})
public class WalletAuditResourceIntTest {

    private static final String DEFAULT_ENTITY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ENTITY_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_ENTITY_ID = 1L;
    private static final Long UPDATED_ENTITY_ID = 2L;

    @Autowired
    private WalletAuditRepository walletAuditRepository;

    @Autowired
    private WalletAuditSearchRepository walletAuditSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restWalletAuditMockMvc;

    private WalletAudit walletAudit;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final WalletAuditResource walletAuditResource = new WalletAuditResource(walletAuditRepository, walletAuditSearchRepository);
        this.restWalletAuditMockMvc = MockMvcBuilders.standaloneSetup(walletAuditResource)
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
    public static WalletAudit createEntity(EntityManager em) {
        WalletAudit walletAudit = new WalletAudit()
            .entityName(DEFAULT_ENTITY_NAME)
            .entityId(DEFAULT_ENTITY_ID);
        return walletAudit;
    }

    @Before
    public void initTest() {
        walletAuditSearchRepository.deleteAll();
        walletAudit = createEntity(em);
    }

    @Test
    @Transactional
    public void createWalletAudit() throws Exception {
        int databaseSizeBeforeCreate = walletAuditRepository.findAll().size();

        // Create the WalletAudit
        restWalletAuditMockMvc.perform(post("/api/wallet-audits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(walletAudit)))
            .andExpect(status().isCreated());

        // Validate the WalletAudit in the database
        List<WalletAudit> walletAuditList = walletAuditRepository.findAll();
        assertThat(walletAuditList).hasSize(databaseSizeBeforeCreate + 1);
        WalletAudit testWalletAudit = walletAuditList.get(walletAuditList.size() - 1);
        assertThat(testWalletAudit.getEntityName()).isEqualTo(DEFAULT_ENTITY_NAME);
        assertThat(testWalletAudit.getEntityId()).isEqualTo(DEFAULT_ENTITY_ID);

        // Validate the WalletAudit in Elasticsearch
        WalletAudit walletAuditEs = walletAuditSearchRepository.findOne(testWalletAudit.getId());
        assertThat(walletAuditEs).isEqualToIgnoringGivenFields(testWalletAudit);
    }

    @Test
    @Transactional
    public void createWalletAuditWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = walletAuditRepository.findAll().size();

        // Create the WalletAudit with an existing ID
        walletAudit.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWalletAuditMockMvc.perform(post("/api/wallet-audits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(walletAudit)))
            .andExpect(status().isBadRequest());

        // Validate the WalletAudit in the database
        List<WalletAudit> walletAuditList = walletAuditRepository.findAll();
        assertThat(walletAuditList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkEntityNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = walletAuditRepository.findAll().size();
        // set the field null
        walletAudit.setEntityName(null);

        // Create the WalletAudit, which fails.

        restWalletAuditMockMvc.perform(post("/api/wallet-audits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(walletAudit)))
            .andExpect(status().isBadRequest());

        List<WalletAudit> walletAuditList = walletAuditRepository.findAll();
        assertThat(walletAuditList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllWalletAudits() throws Exception {
        // Initialize the database
        walletAuditRepository.saveAndFlush(walletAudit);

        // Get all the walletAuditList
        restWalletAuditMockMvc.perform(get("/api/wallet-audits?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(walletAudit.getId().intValue())))
            .andExpect(jsonPath("$.[*].entityName").value(hasItem(DEFAULT_ENTITY_NAME.toString())))
            .andExpect(jsonPath("$.[*].entityId").value(hasItem(DEFAULT_ENTITY_ID.intValue())));
    }

    @Test
    @Transactional
    public void getWalletAudit() throws Exception {
        // Initialize the database
        walletAuditRepository.saveAndFlush(walletAudit);

        // Get the walletAudit
        restWalletAuditMockMvc.perform(get("/api/wallet-audits/{id}", walletAudit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(walletAudit.getId().intValue()))
            .andExpect(jsonPath("$.entityName").value(DEFAULT_ENTITY_NAME.toString()))
            .andExpect(jsonPath("$.entityId").value(DEFAULT_ENTITY_ID.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingWalletAudit() throws Exception {
        // Get the walletAudit
        restWalletAuditMockMvc.perform(get("/api/wallet-audits/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWalletAudit() throws Exception {
        // Initialize the database
        walletAuditRepository.saveAndFlush(walletAudit);
        walletAuditSearchRepository.save(walletAudit);
        int databaseSizeBeforeUpdate = walletAuditRepository.findAll().size();

        // Update the walletAudit
        WalletAudit updatedWalletAudit = walletAuditRepository.findOne(walletAudit.getId());
        // Disconnect from session so that the updates on updatedWalletAudit are not directly saved in db
        em.detach(updatedWalletAudit);
        updatedWalletAudit
            .entityName(UPDATED_ENTITY_NAME)
            .entityId(UPDATED_ENTITY_ID);

        restWalletAuditMockMvc.perform(put("/api/wallet-audits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedWalletAudit)))
            .andExpect(status().isOk());

        // Validate the WalletAudit in the database
        List<WalletAudit> walletAuditList = walletAuditRepository.findAll();
        assertThat(walletAuditList).hasSize(databaseSizeBeforeUpdate);
        WalletAudit testWalletAudit = walletAuditList.get(walletAuditList.size() - 1);
        assertThat(testWalletAudit.getEntityName()).isEqualTo(UPDATED_ENTITY_NAME);
        assertThat(testWalletAudit.getEntityId()).isEqualTo(UPDATED_ENTITY_ID);

        // Validate the WalletAudit in Elasticsearch
        WalletAudit walletAuditEs = walletAuditSearchRepository.findOne(testWalletAudit.getId());
        assertThat(walletAuditEs).isEqualToIgnoringGivenFields(testWalletAudit);
    }

    @Test
    @Transactional
    public void updateNonExistingWalletAudit() throws Exception {
        int databaseSizeBeforeUpdate = walletAuditRepository.findAll().size();

        // Create the WalletAudit

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restWalletAuditMockMvc.perform(put("/api/wallet-audits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(walletAudit)))
            .andExpect(status().isCreated());

        // Validate the WalletAudit in the database
        List<WalletAudit> walletAuditList = walletAuditRepository.findAll();
        assertThat(walletAuditList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteWalletAudit() throws Exception {
        // Initialize the database
        walletAuditRepository.saveAndFlush(walletAudit);
        walletAuditSearchRepository.save(walletAudit);
        int databaseSizeBeforeDelete = walletAuditRepository.findAll().size();

        // Get the walletAudit
        restWalletAuditMockMvc.perform(delete("/api/wallet-audits/{id}", walletAudit.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean walletAuditExistsInEs = walletAuditSearchRepository.exists(walletAudit.getId());
        assertThat(walletAuditExistsInEs).isFalse();

        // Validate the database is empty
        List<WalletAudit> walletAuditList = walletAuditRepository.findAll();
        assertThat(walletAuditList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchWalletAudit() throws Exception {
        // Initialize the database
        walletAuditRepository.saveAndFlush(walletAudit);
        walletAuditSearchRepository.save(walletAudit);

        // Search the walletAudit
        restWalletAuditMockMvc.perform(get("/api/_search/wallet-audits?query=id:" + walletAudit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(walletAudit.getId().intValue())))
            .andExpect(jsonPath("$.[*].entityName").value(hasItem(DEFAULT_ENTITY_NAME.toString())))
            .andExpect(jsonPath("$.[*].entityId").value(hasItem(DEFAULT_ENTITY_ID.intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WalletAudit.class);
        WalletAudit walletAudit1 = new WalletAudit();
        walletAudit1.setId(1L);
        WalletAudit walletAudit2 = new WalletAudit();
        walletAudit2.setId(walletAudit1.getId());
        assertThat(walletAudit1).isEqualTo(walletAudit2);
        walletAudit2.setId(2L);
        assertThat(walletAudit1).isNotEqualTo(walletAudit2);
        walletAudit1.setId(null);
        assertThat(walletAudit1).isNotEqualTo(walletAudit2);
    }
}

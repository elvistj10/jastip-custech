package com.cus.jastip.wallet.web.rest;

import com.cus.jastip.wallet.WalletApp;

import com.cus.jastip.wallet.config.SecurityBeanOverrideConfiguration;

import com.cus.jastip.wallet.domain.WalletTransaction;
import com.cus.jastip.wallet.repository.WalletTransactionRepository;
import com.cus.jastip.wallet.repository.search.WalletTransactionSearchRepository;
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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.cus.jastip.wallet.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cus.jastip.wallet.domain.enumeration.WalletTransactionType;
/**
 * Test class for the WalletTransactionResource REST controller.
 *
 * @see WalletTransactionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {WalletApp.class, SecurityBeanOverrideConfiguration.class})
public class WalletTransactionResourceIntTest {

    private static final Instant DEFAULT_TRANSACTION_DATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TRANSACTION_DATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final WalletTransactionType DEFAULT_TRANSACTION_TYPE = WalletTransactionType.WITHDRAWAL;
    private static final WalletTransactionType UPDATED_TRANSACTION_TYPE = WalletTransactionType.INCOME;

    private static final Double DEFAULT_NOMINAL = 1D;
    private static final Double UPDATED_NOMINAL = 2D;

    private static final Long DEFAULT_POSTING_ID = 1L;
    private static final Long UPDATED_POSTING_ID = 2L;

    private static final Long DEFAULT_OFFERING_ID = 1L;
    private static final Long UPDATED_OFFERING_ID = 2L;

    private static final Long DEFAULT_WITHDRAWAL_ID = 1L;
    private static final Long UPDATED_WITHDRAWAL_ID = 2L;

    @Autowired
    private WalletTransactionRepository walletTransactionRepository;

    @Autowired
    private WalletTransactionSearchRepository walletTransactionSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restWalletTransactionMockMvc;

    private WalletTransaction walletTransaction;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final WalletTransactionResource walletTransactionResource = new WalletTransactionResource(walletTransactionRepository, walletTransactionSearchRepository);
        this.restWalletTransactionMockMvc = MockMvcBuilders.standaloneSetup(walletTransactionResource)
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
    public static WalletTransaction createEntity(EntityManager em) {
        WalletTransaction walletTransaction = new WalletTransaction()
            .transactionDateTime(DEFAULT_TRANSACTION_DATE_TIME)
            .transactionType(DEFAULT_TRANSACTION_TYPE)
            .nominal(DEFAULT_NOMINAL)
            .postingId(DEFAULT_POSTING_ID)
            .offeringId(DEFAULT_OFFERING_ID)
            .withdrawalId(DEFAULT_WITHDRAWAL_ID);
        return walletTransaction;
    }

    @Before
    public void initTest() {
        walletTransactionSearchRepository.deleteAll();
        walletTransaction = createEntity(em);
    }

    @Test
    @Transactional
    public void createWalletTransaction() throws Exception {
        int databaseSizeBeforeCreate = walletTransactionRepository.findAll().size();

        // Create the WalletTransaction
        restWalletTransactionMockMvc.perform(post("/api/wallet-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(walletTransaction)))
            .andExpect(status().isCreated());

        // Validate the WalletTransaction in the database
        List<WalletTransaction> walletTransactionList = walletTransactionRepository.findAll();
        assertThat(walletTransactionList).hasSize(databaseSizeBeforeCreate + 1);
        WalletTransaction testWalletTransaction = walletTransactionList.get(walletTransactionList.size() - 1);
        assertThat(testWalletTransaction.getTransactionDateTime()).isEqualTo(DEFAULT_TRANSACTION_DATE_TIME);
        assertThat(testWalletTransaction.getTransactionType()).isEqualTo(DEFAULT_TRANSACTION_TYPE);
        assertThat(testWalletTransaction.getNominal()).isEqualTo(DEFAULT_NOMINAL);
        assertThat(testWalletTransaction.getPostingId()).isEqualTo(DEFAULT_POSTING_ID);
        assertThat(testWalletTransaction.getOfferingId()).isEqualTo(DEFAULT_OFFERING_ID);
        assertThat(testWalletTransaction.getWithdrawalId()).isEqualTo(DEFAULT_WITHDRAWAL_ID);

        // Validate the WalletTransaction in Elasticsearch
        WalletTransaction walletTransactionEs = walletTransactionSearchRepository.findOne(testWalletTransaction.getId());
        assertThat(walletTransactionEs).isEqualToIgnoringGivenFields(testWalletTransaction);
    }

    @Test
    @Transactional
    public void createWalletTransactionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = walletTransactionRepository.findAll().size();

        // Create the WalletTransaction with an existing ID
        walletTransaction.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWalletTransactionMockMvc.perform(post("/api/wallet-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(walletTransaction)))
            .andExpect(status().isBadRequest());

        // Validate the WalletTransaction in the database
        List<WalletTransaction> walletTransactionList = walletTransactionRepository.findAll();
        assertThat(walletTransactionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllWalletTransactions() throws Exception {
        // Initialize the database
        walletTransactionRepository.saveAndFlush(walletTransaction);

        // Get all the walletTransactionList
        restWalletTransactionMockMvc.perform(get("/api/wallet-transactions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(walletTransaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].transactionDateTime").value(hasItem(DEFAULT_TRANSACTION_DATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].transactionType").value(hasItem(DEFAULT_TRANSACTION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].nominal").value(hasItem(DEFAULT_NOMINAL.doubleValue())))
            .andExpect(jsonPath("$.[*].postingId").value(hasItem(DEFAULT_POSTING_ID.intValue())))
            .andExpect(jsonPath("$.[*].offeringId").value(hasItem(DEFAULT_OFFERING_ID.intValue())))
            .andExpect(jsonPath("$.[*].withdrawalId").value(hasItem(DEFAULT_WITHDRAWAL_ID.intValue())));
    }

    @Test
    @Transactional
    public void getWalletTransaction() throws Exception {
        // Initialize the database
        walletTransactionRepository.saveAndFlush(walletTransaction);

        // Get the walletTransaction
        restWalletTransactionMockMvc.perform(get("/api/wallet-transactions/{id}", walletTransaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(walletTransaction.getId().intValue()))
            .andExpect(jsonPath("$.transactionDateTime").value(DEFAULT_TRANSACTION_DATE_TIME.toString()))
            .andExpect(jsonPath("$.transactionType").value(DEFAULT_TRANSACTION_TYPE.toString()))
            .andExpect(jsonPath("$.nominal").value(DEFAULT_NOMINAL.doubleValue()))
            .andExpect(jsonPath("$.postingId").value(DEFAULT_POSTING_ID.intValue()))
            .andExpect(jsonPath("$.offeringId").value(DEFAULT_OFFERING_ID.intValue()))
            .andExpect(jsonPath("$.withdrawalId").value(DEFAULT_WITHDRAWAL_ID.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingWalletTransaction() throws Exception {
        // Get the walletTransaction
        restWalletTransactionMockMvc.perform(get("/api/wallet-transactions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWalletTransaction() throws Exception {
        // Initialize the database
        walletTransactionRepository.saveAndFlush(walletTransaction);
        walletTransactionSearchRepository.save(walletTransaction);
        int databaseSizeBeforeUpdate = walletTransactionRepository.findAll().size();

        // Update the walletTransaction
        WalletTransaction updatedWalletTransaction = walletTransactionRepository.findOne(walletTransaction.getId());
        // Disconnect from session so that the updates on updatedWalletTransaction are not directly saved in db
        em.detach(updatedWalletTransaction);
        updatedWalletTransaction
            .transactionDateTime(UPDATED_TRANSACTION_DATE_TIME)
            .transactionType(UPDATED_TRANSACTION_TYPE)
            .nominal(UPDATED_NOMINAL)
            .postingId(UPDATED_POSTING_ID)
            .offeringId(UPDATED_OFFERING_ID)
            .withdrawalId(UPDATED_WITHDRAWAL_ID);

        restWalletTransactionMockMvc.perform(put("/api/wallet-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedWalletTransaction)))
            .andExpect(status().isOk());

        // Validate the WalletTransaction in the database
        List<WalletTransaction> walletTransactionList = walletTransactionRepository.findAll();
        assertThat(walletTransactionList).hasSize(databaseSizeBeforeUpdate);
        WalletTransaction testWalletTransaction = walletTransactionList.get(walletTransactionList.size() - 1);
        assertThat(testWalletTransaction.getTransactionDateTime()).isEqualTo(UPDATED_TRANSACTION_DATE_TIME);
        assertThat(testWalletTransaction.getTransactionType()).isEqualTo(UPDATED_TRANSACTION_TYPE);
        assertThat(testWalletTransaction.getNominal()).isEqualTo(UPDATED_NOMINAL);
        assertThat(testWalletTransaction.getPostingId()).isEqualTo(UPDATED_POSTING_ID);
        assertThat(testWalletTransaction.getOfferingId()).isEqualTo(UPDATED_OFFERING_ID);
        assertThat(testWalletTransaction.getWithdrawalId()).isEqualTo(UPDATED_WITHDRAWAL_ID);

        // Validate the WalletTransaction in Elasticsearch
        WalletTransaction walletTransactionEs = walletTransactionSearchRepository.findOne(testWalletTransaction.getId());
        assertThat(walletTransactionEs).isEqualToIgnoringGivenFields(testWalletTransaction);
    }

    @Test
    @Transactional
    public void updateNonExistingWalletTransaction() throws Exception {
        int databaseSizeBeforeUpdate = walletTransactionRepository.findAll().size();

        // Create the WalletTransaction

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restWalletTransactionMockMvc.perform(put("/api/wallet-transactions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(walletTransaction)))
            .andExpect(status().isCreated());

        // Validate the WalletTransaction in the database
        List<WalletTransaction> walletTransactionList = walletTransactionRepository.findAll();
        assertThat(walletTransactionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteWalletTransaction() throws Exception {
        // Initialize the database
        walletTransactionRepository.saveAndFlush(walletTransaction);
        walletTransactionSearchRepository.save(walletTransaction);
        int databaseSizeBeforeDelete = walletTransactionRepository.findAll().size();

        // Get the walletTransaction
        restWalletTransactionMockMvc.perform(delete("/api/wallet-transactions/{id}", walletTransaction.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean walletTransactionExistsInEs = walletTransactionSearchRepository.exists(walletTransaction.getId());
        assertThat(walletTransactionExistsInEs).isFalse();

        // Validate the database is empty
        List<WalletTransaction> walletTransactionList = walletTransactionRepository.findAll();
        assertThat(walletTransactionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchWalletTransaction() throws Exception {
        // Initialize the database
        walletTransactionRepository.saveAndFlush(walletTransaction);
        walletTransactionSearchRepository.save(walletTransaction);

        // Search the walletTransaction
        restWalletTransactionMockMvc.perform(get("/api/_search/wallet-transactions?query=id:" + walletTransaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(walletTransaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].transactionDateTime").value(hasItem(DEFAULT_TRANSACTION_DATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].transactionType").value(hasItem(DEFAULT_TRANSACTION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].nominal").value(hasItem(DEFAULT_NOMINAL.doubleValue())))
            .andExpect(jsonPath("$.[*].postingId").value(hasItem(DEFAULT_POSTING_ID.intValue())))
            .andExpect(jsonPath("$.[*].offeringId").value(hasItem(DEFAULT_OFFERING_ID.intValue())))
            .andExpect(jsonPath("$.[*].withdrawalId").value(hasItem(DEFAULT_WITHDRAWAL_ID.intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WalletTransaction.class);
        WalletTransaction walletTransaction1 = new WalletTransaction();
        walletTransaction1.setId(1L);
        WalletTransaction walletTransaction2 = new WalletTransaction();
        walletTransaction2.setId(walletTransaction1.getId());
        assertThat(walletTransaction1).isEqualTo(walletTransaction2);
        walletTransaction2.setId(2L);
        assertThat(walletTransaction1).isNotEqualTo(walletTransaction2);
        walletTransaction1.setId(null);
        assertThat(walletTransaction1).isNotEqualTo(walletTransaction2);
    }
}

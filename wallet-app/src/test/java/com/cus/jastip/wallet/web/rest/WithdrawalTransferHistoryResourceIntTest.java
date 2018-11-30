package com.cus.jastip.wallet.web.rest;

import com.cus.jastip.wallet.WalletApp;

import com.cus.jastip.wallet.config.SecurityBeanOverrideConfiguration;

import com.cus.jastip.wallet.domain.WithdrawalTransferHistory;
import com.cus.jastip.wallet.repository.WithdrawalTransferHistoryRepository;
import com.cus.jastip.wallet.repository.search.WithdrawalTransferHistorySearchRepository;
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
 * Test class for the WithdrawalTransferHistoryResource REST controller.
 *
 * @see WithdrawalTransferHistoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {WalletApp.class, SecurityBeanOverrideConfiguration.class})
public class WithdrawalTransferHistoryResourceIntTest {

    private static final Long DEFAULT_WITHDRAWAL_ID = 1L;
    private static final Long UPDATED_WITHDRAWAL_ID = 2L;

    private static final Double DEFAULT_NOMINAL = 1D;
    private static final Double UPDATED_NOMINAL = 2D;

    private static final String DEFAULT_DEST_BANK_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DEST_BANK_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DEST_BANK_ACCOUNT = "AAAAAAAAAA";
    private static final String UPDATED_DEST_BANK_ACCOUNT = "BBBBBBBBBB";

    @Autowired
    private WithdrawalTransferHistoryRepository withdrawalTransferHistoryRepository;

    @Autowired
    private WithdrawalTransferHistorySearchRepository withdrawalTransferHistorySearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restWithdrawalTransferHistoryMockMvc;

    private WithdrawalTransferHistory withdrawalTransferHistory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final WithdrawalTransferHistoryResource withdrawalTransferHistoryResource = new WithdrawalTransferHistoryResource(withdrawalTransferHistoryRepository, withdrawalTransferHistorySearchRepository);
        this.restWithdrawalTransferHistoryMockMvc = MockMvcBuilders.standaloneSetup(withdrawalTransferHistoryResource)
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
    public static WithdrawalTransferHistory createEntity(EntityManager em) {
        WithdrawalTransferHistory withdrawalTransferHistory = new WithdrawalTransferHistory()
            .withdrawalId(DEFAULT_WITHDRAWAL_ID)
            .nominal(DEFAULT_NOMINAL)
            .destBankName(DEFAULT_DEST_BANK_NAME)
            .destBankAccount(DEFAULT_DEST_BANK_ACCOUNT);
        return withdrawalTransferHistory;
    }

    @Before
    public void initTest() {
        withdrawalTransferHistorySearchRepository.deleteAll();
        withdrawalTransferHistory = createEntity(em);
    }

    @Test
    @Transactional
    public void createWithdrawalTransferHistory() throws Exception {
        int databaseSizeBeforeCreate = withdrawalTransferHistoryRepository.findAll().size();

        // Create the WithdrawalTransferHistory
        restWithdrawalTransferHistoryMockMvc.perform(post("/api/withdrawal-transfer-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(withdrawalTransferHistory)))
            .andExpect(status().isCreated());

        // Validate the WithdrawalTransferHistory in the database
        List<WithdrawalTransferHistory> withdrawalTransferHistoryList = withdrawalTransferHistoryRepository.findAll();
        assertThat(withdrawalTransferHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        WithdrawalTransferHistory testWithdrawalTransferHistory = withdrawalTransferHistoryList.get(withdrawalTransferHistoryList.size() - 1);
        assertThat(testWithdrawalTransferHistory.getWithdrawalId()).isEqualTo(DEFAULT_WITHDRAWAL_ID);
        assertThat(testWithdrawalTransferHistory.getNominal()).isEqualTo(DEFAULT_NOMINAL);
        assertThat(testWithdrawalTransferHistory.getDestBankName()).isEqualTo(DEFAULT_DEST_BANK_NAME);
        assertThat(testWithdrawalTransferHistory.getDestBankAccount()).isEqualTo(DEFAULT_DEST_BANK_ACCOUNT);

        // Validate the WithdrawalTransferHistory in Elasticsearch
        WithdrawalTransferHistory withdrawalTransferHistoryEs = withdrawalTransferHistorySearchRepository.findOne(testWithdrawalTransferHistory.getId());
        assertThat(withdrawalTransferHistoryEs).isEqualToIgnoringGivenFields(testWithdrawalTransferHistory);
    }

    @Test
    @Transactional
    public void createWithdrawalTransferHistoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = withdrawalTransferHistoryRepository.findAll().size();

        // Create the WithdrawalTransferHistory with an existing ID
        withdrawalTransferHistory.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWithdrawalTransferHistoryMockMvc.perform(post("/api/withdrawal-transfer-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(withdrawalTransferHistory)))
            .andExpect(status().isBadRequest());

        // Validate the WithdrawalTransferHistory in the database
        List<WithdrawalTransferHistory> withdrawalTransferHistoryList = withdrawalTransferHistoryRepository.findAll();
        assertThat(withdrawalTransferHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllWithdrawalTransferHistories() throws Exception {
        // Initialize the database
        withdrawalTransferHistoryRepository.saveAndFlush(withdrawalTransferHistory);

        // Get all the withdrawalTransferHistoryList
        restWithdrawalTransferHistoryMockMvc.perform(get("/api/withdrawal-transfer-histories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(withdrawalTransferHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].withdrawalId").value(hasItem(DEFAULT_WITHDRAWAL_ID.intValue())))
            .andExpect(jsonPath("$.[*].nominal").value(hasItem(DEFAULT_NOMINAL.doubleValue())))
            .andExpect(jsonPath("$.[*].destBankName").value(hasItem(DEFAULT_DEST_BANK_NAME.toString())))
            .andExpect(jsonPath("$.[*].destBankAccount").value(hasItem(DEFAULT_DEST_BANK_ACCOUNT.toString())));
    }

    @Test
    @Transactional
    public void getWithdrawalTransferHistory() throws Exception {
        // Initialize the database
        withdrawalTransferHistoryRepository.saveAndFlush(withdrawalTransferHistory);

        // Get the withdrawalTransferHistory
        restWithdrawalTransferHistoryMockMvc.perform(get("/api/withdrawal-transfer-histories/{id}", withdrawalTransferHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(withdrawalTransferHistory.getId().intValue()))
            .andExpect(jsonPath("$.withdrawalId").value(DEFAULT_WITHDRAWAL_ID.intValue()))
            .andExpect(jsonPath("$.nominal").value(DEFAULT_NOMINAL.doubleValue()))
            .andExpect(jsonPath("$.destBankName").value(DEFAULT_DEST_BANK_NAME.toString()))
            .andExpect(jsonPath("$.destBankAccount").value(DEFAULT_DEST_BANK_ACCOUNT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingWithdrawalTransferHistory() throws Exception {
        // Get the withdrawalTransferHistory
        restWithdrawalTransferHistoryMockMvc.perform(get("/api/withdrawal-transfer-histories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWithdrawalTransferHistory() throws Exception {
        // Initialize the database
        withdrawalTransferHistoryRepository.saveAndFlush(withdrawalTransferHistory);
        withdrawalTransferHistorySearchRepository.save(withdrawalTransferHistory);
        int databaseSizeBeforeUpdate = withdrawalTransferHistoryRepository.findAll().size();

        // Update the withdrawalTransferHistory
        WithdrawalTransferHistory updatedWithdrawalTransferHistory = withdrawalTransferHistoryRepository.findOne(withdrawalTransferHistory.getId());
        // Disconnect from session so that the updates on updatedWithdrawalTransferHistory are not directly saved in db
        em.detach(updatedWithdrawalTransferHistory);
        updatedWithdrawalTransferHistory
            .withdrawalId(UPDATED_WITHDRAWAL_ID)
            .nominal(UPDATED_NOMINAL)
            .destBankName(UPDATED_DEST_BANK_NAME)
            .destBankAccount(UPDATED_DEST_BANK_ACCOUNT);

        restWithdrawalTransferHistoryMockMvc.perform(put("/api/withdrawal-transfer-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedWithdrawalTransferHistory)))
            .andExpect(status().isOk());

        // Validate the WithdrawalTransferHistory in the database
        List<WithdrawalTransferHistory> withdrawalTransferHistoryList = withdrawalTransferHistoryRepository.findAll();
        assertThat(withdrawalTransferHistoryList).hasSize(databaseSizeBeforeUpdate);
        WithdrawalTransferHistory testWithdrawalTransferHistory = withdrawalTransferHistoryList.get(withdrawalTransferHistoryList.size() - 1);
        assertThat(testWithdrawalTransferHistory.getWithdrawalId()).isEqualTo(UPDATED_WITHDRAWAL_ID);
        assertThat(testWithdrawalTransferHistory.getNominal()).isEqualTo(UPDATED_NOMINAL);
        assertThat(testWithdrawalTransferHistory.getDestBankName()).isEqualTo(UPDATED_DEST_BANK_NAME);
        assertThat(testWithdrawalTransferHistory.getDestBankAccount()).isEqualTo(UPDATED_DEST_BANK_ACCOUNT);

        // Validate the WithdrawalTransferHistory in Elasticsearch
        WithdrawalTransferHistory withdrawalTransferHistoryEs = withdrawalTransferHistorySearchRepository.findOne(testWithdrawalTransferHistory.getId());
        assertThat(withdrawalTransferHistoryEs).isEqualToIgnoringGivenFields(testWithdrawalTransferHistory);
    }

    @Test
    @Transactional
    public void updateNonExistingWithdrawalTransferHistory() throws Exception {
        int databaseSizeBeforeUpdate = withdrawalTransferHistoryRepository.findAll().size();

        // Create the WithdrawalTransferHistory

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restWithdrawalTransferHistoryMockMvc.perform(put("/api/withdrawal-transfer-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(withdrawalTransferHistory)))
            .andExpect(status().isCreated());

        // Validate the WithdrawalTransferHistory in the database
        List<WithdrawalTransferHistory> withdrawalTransferHistoryList = withdrawalTransferHistoryRepository.findAll();
        assertThat(withdrawalTransferHistoryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteWithdrawalTransferHistory() throws Exception {
        // Initialize the database
        withdrawalTransferHistoryRepository.saveAndFlush(withdrawalTransferHistory);
        withdrawalTransferHistorySearchRepository.save(withdrawalTransferHistory);
        int databaseSizeBeforeDelete = withdrawalTransferHistoryRepository.findAll().size();

        // Get the withdrawalTransferHistory
        restWithdrawalTransferHistoryMockMvc.perform(delete("/api/withdrawal-transfer-histories/{id}", withdrawalTransferHistory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean withdrawalTransferHistoryExistsInEs = withdrawalTransferHistorySearchRepository.exists(withdrawalTransferHistory.getId());
        assertThat(withdrawalTransferHistoryExistsInEs).isFalse();

        // Validate the database is empty
        List<WithdrawalTransferHistory> withdrawalTransferHistoryList = withdrawalTransferHistoryRepository.findAll();
        assertThat(withdrawalTransferHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchWithdrawalTransferHistory() throws Exception {
        // Initialize the database
        withdrawalTransferHistoryRepository.saveAndFlush(withdrawalTransferHistory);
        withdrawalTransferHistorySearchRepository.save(withdrawalTransferHistory);

        // Search the withdrawalTransferHistory
        restWithdrawalTransferHistoryMockMvc.perform(get("/api/_search/withdrawal-transfer-histories?query=id:" + withdrawalTransferHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(withdrawalTransferHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].withdrawalId").value(hasItem(DEFAULT_WITHDRAWAL_ID.intValue())))
            .andExpect(jsonPath("$.[*].nominal").value(hasItem(DEFAULT_NOMINAL.doubleValue())))
            .andExpect(jsonPath("$.[*].destBankName").value(hasItem(DEFAULT_DEST_BANK_NAME.toString())))
            .andExpect(jsonPath("$.[*].destBankAccount").value(hasItem(DEFAULT_DEST_BANK_ACCOUNT.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WithdrawalTransferHistory.class);
        WithdrawalTransferHistory withdrawalTransferHistory1 = new WithdrawalTransferHistory();
        withdrawalTransferHistory1.setId(1L);
        WithdrawalTransferHistory withdrawalTransferHistory2 = new WithdrawalTransferHistory();
        withdrawalTransferHistory2.setId(withdrawalTransferHistory1.getId());
        assertThat(withdrawalTransferHistory1).isEqualTo(withdrawalTransferHistory2);
        withdrawalTransferHistory2.setId(2L);
        assertThat(withdrawalTransferHistory1).isNotEqualTo(withdrawalTransferHistory2);
        withdrawalTransferHistory1.setId(null);
        assertThat(withdrawalTransferHistory1).isNotEqualTo(withdrawalTransferHistory2);
    }
}

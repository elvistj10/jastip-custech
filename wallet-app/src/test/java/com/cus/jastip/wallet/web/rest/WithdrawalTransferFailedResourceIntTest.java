package com.cus.jastip.wallet.web.rest;

import com.cus.jastip.wallet.WalletApp;

import com.cus.jastip.wallet.config.SecurityBeanOverrideConfiguration;

import com.cus.jastip.wallet.domain.WithdrawalTransferFailed;
import com.cus.jastip.wallet.repository.WithdrawalTransferFailedRepository;
import com.cus.jastip.wallet.repository.search.WithdrawalTransferFailedSearchRepository;
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
 * Test class for the WithdrawalTransferFailedResource REST controller.
 *
 * @see WithdrawalTransferFailedResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {WalletApp.class, SecurityBeanOverrideConfiguration.class})
public class WithdrawalTransferFailedResourceIntTest {

    private static final Long DEFAULT_WITHDRAWAL_ID = 1L;
    private static final Long UPDATED_WITHDRAWAL_ID = 2L;

    private static final Double DEFAULT_NOMINAL = 1D;
    private static final Double UPDATED_NOMINAL = 2D;

    private static final String DEFAULT_DEST_BANK_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DEST_BANK_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DEST_BANK_ACCOUNT = "AAAAAAAAAA";
    private static final String UPDATED_DEST_BANK_ACCOUNT = "BBBBBBBBBB";

    @Autowired
    private WithdrawalTransferFailedRepository withdrawalTransferFailedRepository;

    @Autowired
    private WithdrawalTransferFailedSearchRepository withdrawalTransferFailedSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restWithdrawalTransferFailedMockMvc;

    private WithdrawalTransferFailed withdrawalTransferFailed;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final WithdrawalTransferFailedResource withdrawalTransferFailedResource = new WithdrawalTransferFailedResource(withdrawalTransferFailedRepository, withdrawalTransferFailedSearchRepository);
        this.restWithdrawalTransferFailedMockMvc = MockMvcBuilders.standaloneSetup(withdrawalTransferFailedResource)
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
    public static WithdrawalTransferFailed createEntity(EntityManager em) {
        WithdrawalTransferFailed withdrawalTransferFailed = new WithdrawalTransferFailed()
            .withdrawalId(DEFAULT_WITHDRAWAL_ID)
            .nominal(DEFAULT_NOMINAL)
            .destBankName(DEFAULT_DEST_BANK_NAME)
            .destBankAccount(DEFAULT_DEST_BANK_ACCOUNT);
        return withdrawalTransferFailed;
    }

    @Before
    public void initTest() {
        withdrawalTransferFailedSearchRepository.deleteAll();
        withdrawalTransferFailed = createEntity(em);
    }

    @Test
    @Transactional
    public void createWithdrawalTransferFailed() throws Exception {
        int databaseSizeBeforeCreate = withdrawalTransferFailedRepository.findAll().size();

        // Create the WithdrawalTransferFailed
        restWithdrawalTransferFailedMockMvc.perform(post("/api/withdrawal-transfer-faileds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(withdrawalTransferFailed)))
            .andExpect(status().isCreated());

        // Validate the WithdrawalTransferFailed in the database
        List<WithdrawalTransferFailed> withdrawalTransferFailedList = withdrawalTransferFailedRepository.findAll();
        assertThat(withdrawalTransferFailedList).hasSize(databaseSizeBeforeCreate + 1);
        WithdrawalTransferFailed testWithdrawalTransferFailed = withdrawalTransferFailedList.get(withdrawalTransferFailedList.size() - 1);
        assertThat(testWithdrawalTransferFailed.getWithdrawalId()).isEqualTo(DEFAULT_WITHDRAWAL_ID);
        assertThat(testWithdrawalTransferFailed.getNominal()).isEqualTo(DEFAULT_NOMINAL);
        assertThat(testWithdrawalTransferFailed.getDestBankName()).isEqualTo(DEFAULT_DEST_BANK_NAME);
        assertThat(testWithdrawalTransferFailed.getDestBankAccount()).isEqualTo(DEFAULT_DEST_BANK_ACCOUNT);

        // Validate the WithdrawalTransferFailed in Elasticsearch
        WithdrawalTransferFailed withdrawalTransferFailedEs = withdrawalTransferFailedSearchRepository.findOne(testWithdrawalTransferFailed.getId());
        assertThat(withdrawalTransferFailedEs).isEqualToIgnoringGivenFields(testWithdrawalTransferFailed);
    }

    @Test
    @Transactional
    public void createWithdrawalTransferFailedWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = withdrawalTransferFailedRepository.findAll().size();

        // Create the WithdrawalTransferFailed with an existing ID
        withdrawalTransferFailed.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWithdrawalTransferFailedMockMvc.perform(post("/api/withdrawal-transfer-faileds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(withdrawalTransferFailed)))
            .andExpect(status().isBadRequest());

        // Validate the WithdrawalTransferFailed in the database
        List<WithdrawalTransferFailed> withdrawalTransferFailedList = withdrawalTransferFailedRepository.findAll();
        assertThat(withdrawalTransferFailedList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllWithdrawalTransferFaileds() throws Exception {
        // Initialize the database
        withdrawalTransferFailedRepository.saveAndFlush(withdrawalTransferFailed);

        // Get all the withdrawalTransferFailedList
        restWithdrawalTransferFailedMockMvc.perform(get("/api/withdrawal-transfer-faileds?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(withdrawalTransferFailed.getId().intValue())))
            .andExpect(jsonPath("$.[*].withdrawalId").value(hasItem(DEFAULT_WITHDRAWAL_ID.intValue())))
            .andExpect(jsonPath("$.[*].nominal").value(hasItem(DEFAULT_NOMINAL.doubleValue())))
            .andExpect(jsonPath("$.[*].destBankName").value(hasItem(DEFAULT_DEST_BANK_NAME.toString())))
            .andExpect(jsonPath("$.[*].destBankAccount").value(hasItem(DEFAULT_DEST_BANK_ACCOUNT.toString())));
    }

    @Test
    @Transactional
    public void getWithdrawalTransferFailed() throws Exception {
        // Initialize the database
        withdrawalTransferFailedRepository.saveAndFlush(withdrawalTransferFailed);

        // Get the withdrawalTransferFailed
        restWithdrawalTransferFailedMockMvc.perform(get("/api/withdrawal-transfer-faileds/{id}", withdrawalTransferFailed.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(withdrawalTransferFailed.getId().intValue()))
            .andExpect(jsonPath("$.withdrawalId").value(DEFAULT_WITHDRAWAL_ID.intValue()))
            .andExpect(jsonPath("$.nominal").value(DEFAULT_NOMINAL.doubleValue()))
            .andExpect(jsonPath("$.destBankName").value(DEFAULT_DEST_BANK_NAME.toString()))
            .andExpect(jsonPath("$.destBankAccount").value(DEFAULT_DEST_BANK_ACCOUNT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingWithdrawalTransferFailed() throws Exception {
        // Get the withdrawalTransferFailed
        restWithdrawalTransferFailedMockMvc.perform(get("/api/withdrawal-transfer-faileds/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWithdrawalTransferFailed() throws Exception {
        // Initialize the database
        withdrawalTransferFailedRepository.saveAndFlush(withdrawalTransferFailed);
        withdrawalTransferFailedSearchRepository.save(withdrawalTransferFailed);
        int databaseSizeBeforeUpdate = withdrawalTransferFailedRepository.findAll().size();

        // Update the withdrawalTransferFailed
        WithdrawalTransferFailed updatedWithdrawalTransferFailed = withdrawalTransferFailedRepository.findOne(withdrawalTransferFailed.getId());
        // Disconnect from session so that the updates on updatedWithdrawalTransferFailed are not directly saved in db
        em.detach(updatedWithdrawalTransferFailed);
        updatedWithdrawalTransferFailed
            .withdrawalId(UPDATED_WITHDRAWAL_ID)
            .nominal(UPDATED_NOMINAL)
            .destBankName(UPDATED_DEST_BANK_NAME)
            .destBankAccount(UPDATED_DEST_BANK_ACCOUNT);

        restWithdrawalTransferFailedMockMvc.perform(put("/api/withdrawal-transfer-faileds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedWithdrawalTransferFailed)))
            .andExpect(status().isOk());

        // Validate the WithdrawalTransferFailed in the database
        List<WithdrawalTransferFailed> withdrawalTransferFailedList = withdrawalTransferFailedRepository.findAll();
        assertThat(withdrawalTransferFailedList).hasSize(databaseSizeBeforeUpdate);
        WithdrawalTransferFailed testWithdrawalTransferFailed = withdrawalTransferFailedList.get(withdrawalTransferFailedList.size() - 1);
        assertThat(testWithdrawalTransferFailed.getWithdrawalId()).isEqualTo(UPDATED_WITHDRAWAL_ID);
        assertThat(testWithdrawalTransferFailed.getNominal()).isEqualTo(UPDATED_NOMINAL);
        assertThat(testWithdrawalTransferFailed.getDestBankName()).isEqualTo(UPDATED_DEST_BANK_NAME);
        assertThat(testWithdrawalTransferFailed.getDestBankAccount()).isEqualTo(UPDATED_DEST_BANK_ACCOUNT);

        // Validate the WithdrawalTransferFailed in Elasticsearch
        WithdrawalTransferFailed withdrawalTransferFailedEs = withdrawalTransferFailedSearchRepository.findOne(testWithdrawalTransferFailed.getId());
        assertThat(withdrawalTransferFailedEs).isEqualToIgnoringGivenFields(testWithdrawalTransferFailed);
    }

    @Test
    @Transactional
    public void updateNonExistingWithdrawalTransferFailed() throws Exception {
        int databaseSizeBeforeUpdate = withdrawalTransferFailedRepository.findAll().size();

        // Create the WithdrawalTransferFailed

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restWithdrawalTransferFailedMockMvc.perform(put("/api/withdrawal-transfer-faileds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(withdrawalTransferFailed)))
            .andExpect(status().isCreated());

        // Validate the WithdrawalTransferFailed in the database
        List<WithdrawalTransferFailed> withdrawalTransferFailedList = withdrawalTransferFailedRepository.findAll();
        assertThat(withdrawalTransferFailedList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteWithdrawalTransferFailed() throws Exception {
        // Initialize the database
        withdrawalTransferFailedRepository.saveAndFlush(withdrawalTransferFailed);
        withdrawalTransferFailedSearchRepository.save(withdrawalTransferFailed);
        int databaseSizeBeforeDelete = withdrawalTransferFailedRepository.findAll().size();

        // Get the withdrawalTransferFailed
        restWithdrawalTransferFailedMockMvc.perform(delete("/api/withdrawal-transfer-faileds/{id}", withdrawalTransferFailed.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean withdrawalTransferFailedExistsInEs = withdrawalTransferFailedSearchRepository.exists(withdrawalTransferFailed.getId());
        assertThat(withdrawalTransferFailedExistsInEs).isFalse();

        // Validate the database is empty
        List<WithdrawalTransferFailed> withdrawalTransferFailedList = withdrawalTransferFailedRepository.findAll();
        assertThat(withdrawalTransferFailedList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchWithdrawalTransferFailed() throws Exception {
        // Initialize the database
        withdrawalTransferFailedRepository.saveAndFlush(withdrawalTransferFailed);
        withdrawalTransferFailedSearchRepository.save(withdrawalTransferFailed);

        // Search the withdrawalTransferFailed
        restWithdrawalTransferFailedMockMvc.perform(get("/api/_search/withdrawal-transfer-faileds?query=id:" + withdrawalTransferFailed.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(withdrawalTransferFailed.getId().intValue())))
            .andExpect(jsonPath("$.[*].withdrawalId").value(hasItem(DEFAULT_WITHDRAWAL_ID.intValue())))
            .andExpect(jsonPath("$.[*].nominal").value(hasItem(DEFAULT_NOMINAL.doubleValue())))
            .andExpect(jsonPath("$.[*].destBankName").value(hasItem(DEFAULT_DEST_BANK_NAME.toString())))
            .andExpect(jsonPath("$.[*].destBankAccount").value(hasItem(DEFAULT_DEST_BANK_ACCOUNT.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WithdrawalTransferFailed.class);
        WithdrawalTransferFailed withdrawalTransferFailed1 = new WithdrawalTransferFailed();
        withdrawalTransferFailed1.setId(1L);
        WithdrawalTransferFailed withdrawalTransferFailed2 = new WithdrawalTransferFailed();
        withdrawalTransferFailed2.setId(withdrawalTransferFailed1.getId());
        assertThat(withdrawalTransferFailed1).isEqualTo(withdrawalTransferFailed2);
        withdrawalTransferFailed2.setId(2L);
        assertThat(withdrawalTransferFailed1).isNotEqualTo(withdrawalTransferFailed2);
        withdrawalTransferFailed1.setId(null);
        assertThat(withdrawalTransferFailed1).isNotEqualTo(withdrawalTransferFailed2);
    }
}

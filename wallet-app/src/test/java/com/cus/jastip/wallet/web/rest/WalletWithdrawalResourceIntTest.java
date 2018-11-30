package com.cus.jastip.wallet.web.rest;

import com.cus.jastip.wallet.WalletApp;

import com.cus.jastip.wallet.config.SecurityBeanOverrideConfiguration;

import com.cus.jastip.wallet.domain.WalletWithdrawal;
import com.cus.jastip.wallet.repository.WalletWithdrawalRepository;
import com.cus.jastip.wallet.repository.search.WalletWithdrawalSearchRepository;
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

import com.cus.jastip.wallet.domain.enumeration.WithdrawalStatus;
/**
 * Test class for the WalletWithdrawalResource REST controller.
 *
 * @see WalletWithdrawalResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {WalletApp.class, SecurityBeanOverrideConfiguration.class})
public class WalletWithdrawalResourceIntTest {

    private static final Long DEFAULT_OWNER_ID = 1L;
    private static final Long UPDATED_OWNER_ID = 2L;

    private static final Instant DEFAULT_REQUEST_DATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REQUEST_DATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Double DEFAULT_NOMINAL = 1D;
    private static final Double UPDATED_NOMINAL = 2D;

    private static final String DEFAULT_DEST_BANK_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DEST_BANK_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DEST_BANK_ACCOUNT = "AAAAAAAAAA";
    private static final String UPDATED_DEST_BANK_ACCOUNT = "BBBBBBBBBB";

    private static final WithdrawalStatus DEFAULT_STATUS = WithdrawalStatus.IN_PROCESS;
    private static final WithdrawalStatus UPDATED_STATUS = WithdrawalStatus.SUCCESS;

    private static final Instant DEFAULT_COMPLETED_DATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_COMPLETED_DATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private WalletWithdrawalRepository walletWithdrawalRepository;

    @Autowired
    private WalletWithdrawalSearchRepository walletWithdrawalSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restWalletWithdrawalMockMvc;

    private WalletWithdrawal walletWithdrawal;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final WalletWithdrawalResource walletWithdrawalResource = new WalletWithdrawalResource(walletWithdrawalRepository, walletWithdrawalSearchRepository);
        this.restWalletWithdrawalMockMvc = MockMvcBuilders.standaloneSetup(walletWithdrawalResource)
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
    public static WalletWithdrawal createEntity(EntityManager em) {
        WalletWithdrawal walletWithdrawal = new WalletWithdrawal()
            .ownerId(DEFAULT_OWNER_ID)
            .requestDateTime(DEFAULT_REQUEST_DATE_TIME)
            .nominal(DEFAULT_NOMINAL)
            .destBankName(DEFAULT_DEST_BANK_NAME)
            .destBankAccount(DEFAULT_DEST_BANK_ACCOUNT)
            .status(DEFAULT_STATUS)
            .completedDateTime(DEFAULT_COMPLETED_DATE_TIME);
        return walletWithdrawal;
    }

    @Before
    public void initTest() {
        walletWithdrawalSearchRepository.deleteAll();
        walletWithdrawal = createEntity(em);
    }

    @Test
    @Transactional
    public void createWalletWithdrawal() throws Exception {
        int databaseSizeBeforeCreate = walletWithdrawalRepository.findAll().size();

        // Create the WalletWithdrawal
        restWalletWithdrawalMockMvc.perform(post("/api/wallet-withdrawals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(walletWithdrawal)))
            .andExpect(status().isCreated());

        // Validate the WalletWithdrawal in the database
        List<WalletWithdrawal> walletWithdrawalList = walletWithdrawalRepository.findAll();
        assertThat(walletWithdrawalList).hasSize(databaseSizeBeforeCreate + 1);
        WalletWithdrawal testWalletWithdrawal = walletWithdrawalList.get(walletWithdrawalList.size() - 1);
        assertThat(testWalletWithdrawal.getOwnerId()).isEqualTo(DEFAULT_OWNER_ID);
        assertThat(testWalletWithdrawal.getRequestDateTime()).isEqualTo(DEFAULT_REQUEST_DATE_TIME);
        assertThat(testWalletWithdrawal.getNominal()).isEqualTo(DEFAULT_NOMINAL);
        assertThat(testWalletWithdrawal.getDestBankName()).isEqualTo(DEFAULT_DEST_BANK_NAME);
        assertThat(testWalletWithdrawal.getDestBankAccount()).isEqualTo(DEFAULT_DEST_BANK_ACCOUNT);
        assertThat(testWalletWithdrawal.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testWalletWithdrawal.getCompletedDateTime()).isEqualTo(DEFAULT_COMPLETED_DATE_TIME);

        // Validate the WalletWithdrawal in Elasticsearch
        WalletWithdrawal walletWithdrawalEs = walletWithdrawalSearchRepository.findOne(testWalletWithdrawal.getId());
        assertThat(walletWithdrawalEs).isEqualToIgnoringGivenFields(testWalletWithdrawal);
    }

    @Test
    @Transactional
    public void createWalletWithdrawalWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = walletWithdrawalRepository.findAll().size();

        // Create the WalletWithdrawal with an existing ID
        walletWithdrawal.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWalletWithdrawalMockMvc.perform(post("/api/wallet-withdrawals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(walletWithdrawal)))
            .andExpect(status().isBadRequest());

        // Validate the WalletWithdrawal in the database
        List<WalletWithdrawal> walletWithdrawalList = walletWithdrawalRepository.findAll();
        assertThat(walletWithdrawalList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkOwnerIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = walletWithdrawalRepository.findAll().size();
        // set the field null
        walletWithdrawal.setOwnerId(null);

        // Create the WalletWithdrawal, which fails.

        restWalletWithdrawalMockMvc.perform(post("/api/wallet-withdrawals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(walletWithdrawal)))
            .andExpect(status().isBadRequest());

        List<WalletWithdrawal> walletWithdrawalList = walletWithdrawalRepository.findAll();
        assertThat(walletWithdrawalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllWalletWithdrawals() throws Exception {
        // Initialize the database
        walletWithdrawalRepository.saveAndFlush(walletWithdrawal);

        // Get all the walletWithdrawalList
        restWalletWithdrawalMockMvc.perform(get("/api/wallet-withdrawals?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(walletWithdrawal.getId().intValue())))
            .andExpect(jsonPath("$.[*].ownerId").value(hasItem(DEFAULT_OWNER_ID.intValue())))
            .andExpect(jsonPath("$.[*].requestDateTime").value(hasItem(DEFAULT_REQUEST_DATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].nominal").value(hasItem(DEFAULT_NOMINAL.doubleValue())))
            .andExpect(jsonPath("$.[*].destBankName").value(hasItem(DEFAULT_DEST_BANK_NAME.toString())))
            .andExpect(jsonPath("$.[*].destBankAccount").value(hasItem(DEFAULT_DEST_BANK_ACCOUNT.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].completedDateTime").value(hasItem(DEFAULT_COMPLETED_DATE_TIME.toString())));
    }

    @Test
    @Transactional
    public void getWalletWithdrawal() throws Exception {
        // Initialize the database
        walletWithdrawalRepository.saveAndFlush(walletWithdrawal);

        // Get the walletWithdrawal
        restWalletWithdrawalMockMvc.perform(get("/api/wallet-withdrawals/{id}", walletWithdrawal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(walletWithdrawal.getId().intValue()))
            .andExpect(jsonPath("$.ownerId").value(DEFAULT_OWNER_ID.intValue()))
            .andExpect(jsonPath("$.requestDateTime").value(DEFAULT_REQUEST_DATE_TIME.toString()))
            .andExpect(jsonPath("$.nominal").value(DEFAULT_NOMINAL.doubleValue()))
            .andExpect(jsonPath("$.destBankName").value(DEFAULT_DEST_BANK_NAME.toString()))
            .andExpect(jsonPath("$.destBankAccount").value(DEFAULT_DEST_BANK_ACCOUNT.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.completedDateTime").value(DEFAULT_COMPLETED_DATE_TIME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingWalletWithdrawal() throws Exception {
        // Get the walletWithdrawal
        restWalletWithdrawalMockMvc.perform(get("/api/wallet-withdrawals/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWalletWithdrawal() throws Exception {
        // Initialize the database
        walletWithdrawalRepository.saveAndFlush(walletWithdrawal);
        walletWithdrawalSearchRepository.save(walletWithdrawal);
        int databaseSizeBeforeUpdate = walletWithdrawalRepository.findAll().size();

        // Update the walletWithdrawal
        WalletWithdrawal updatedWalletWithdrawal = walletWithdrawalRepository.findOne(walletWithdrawal.getId());
        // Disconnect from session so that the updates on updatedWalletWithdrawal are not directly saved in db
        em.detach(updatedWalletWithdrawal);
        updatedWalletWithdrawal
            .ownerId(UPDATED_OWNER_ID)
            .requestDateTime(UPDATED_REQUEST_DATE_TIME)
            .nominal(UPDATED_NOMINAL)
            .destBankName(UPDATED_DEST_BANK_NAME)
            .destBankAccount(UPDATED_DEST_BANK_ACCOUNT)
            .status(UPDATED_STATUS)
            .completedDateTime(UPDATED_COMPLETED_DATE_TIME);

        restWalletWithdrawalMockMvc.perform(put("/api/wallet-withdrawals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedWalletWithdrawal)))
            .andExpect(status().isOk());

        // Validate the WalletWithdrawal in the database
        List<WalletWithdrawal> walletWithdrawalList = walletWithdrawalRepository.findAll();
        assertThat(walletWithdrawalList).hasSize(databaseSizeBeforeUpdate);
        WalletWithdrawal testWalletWithdrawal = walletWithdrawalList.get(walletWithdrawalList.size() - 1);
        assertThat(testWalletWithdrawal.getOwnerId()).isEqualTo(UPDATED_OWNER_ID);
        assertThat(testWalletWithdrawal.getRequestDateTime()).isEqualTo(UPDATED_REQUEST_DATE_TIME);
        assertThat(testWalletWithdrawal.getNominal()).isEqualTo(UPDATED_NOMINAL);
        assertThat(testWalletWithdrawal.getDestBankName()).isEqualTo(UPDATED_DEST_BANK_NAME);
        assertThat(testWalletWithdrawal.getDestBankAccount()).isEqualTo(UPDATED_DEST_BANK_ACCOUNT);
        assertThat(testWalletWithdrawal.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testWalletWithdrawal.getCompletedDateTime()).isEqualTo(UPDATED_COMPLETED_DATE_TIME);

        // Validate the WalletWithdrawal in Elasticsearch
        WalletWithdrawal walletWithdrawalEs = walletWithdrawalSearchRepository.findOne(testWalletWithdrawal.getId());
        assertThat(walletWithdrawalEs).isEqualToIgnoringGivenFields(testWalletWithdrawal);
    }

    @Test
    @Transactional
    public void updateNonExistingWalletWithdrawal() throws Exception {
        int databaseSizeBeforeUpdate = walletWithdrawalRepository.findAll().size();

        // Create the WalletWithdrawal

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restWalletWithdrawalMockMvc.perform(put("/api/wallet-withdrawals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(walletWithdrawal)))
            .andExpect(status().isCreated());

        // Validate the WalletWithdrawal in the database
        List<WalletWithdrawal> walletWithdrawalList = walletWithdrawalRepository.findAll();
        assertThat(walletWithdrawalList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteWalletWithdrawal() throws Exception {
        // Initialize the database
        walletWithdrawalRepository.saveAndFlush(walletWithdrawal);
        walletWithdrawalSearchRepository.save(walletWithdrawal);
        int databaseSizeBeforeDelete = walletWithdrawalRepository.findAll().size();

        // Get the walletWithdrawal
        restWalletWithdrawalMockMvc.perform(delete("/api/wallet-withdrawals/{id}", walletWithdrawal.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean walletWithdrawalExistsInEs = walletWithdrawalSearchRepository.exists(walletWithdrawal.getId());
        assertThat(walletWithdrawalExistsInEs).isFalse();

        // Validate the database is empty
        List<WalletWithdrawal> walletWithdrawalList = walletWithdrawalRepository.findAll();
        assertThat(walletWithdrawalList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchWalletWithdrawal() throws Exception {
        // Initialize the database
        walletWithdrawalRepository.saveAndFlush(walletWithdrawal);
        walletWithdrawalSearchRepository.save(walletWithdrawal);

        // Search the walletWithdrawal
        restWalletWithdrawalMockMvc.perform(get("/api/_search/wallet-withdrawals?query=id:" + walletWithdrawal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(walletWithdrawal.getId().intValue())))
            .andExpect(jsonPath("$.[*].ownerId").value(hasItem(DEFAULT_OWNER_ID.intValue())))
            .andExpect(jsonPath("$.[*].requestDateTime").value(hasItem(DEFAULT_REQUEST_DATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].nominal").value(hasItem(DEFAULT_NOMINAL.doubleValue())))
            .andExpect(jsonPath("$.[*].destBankName").value(hasItem(DEFAULT_DEST_BANK_NAME.toString())))
            .andExpect(jsonPath("$.[*].destBankAccount").value(hasItem(DEFAULT_DEST_BANK_ACCOUNT.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].completedDateTime").value(hasItem(DEFAULT_COMPLETED_DATE_TIME.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WalletWithdrawal.class);
        WalletWithdrawal walletWithdrawal1 = new WalletWithdrawal();
        walletWithdrawal1.setId(1L);
        WalletWithdrawal walletWithdrawal2 = new WalletWithdrawal();
        walletWithdrawal2.setId(walletWithdrawal1.getId());
        assertThat(walletWithdrawal1).isEqualTo(walletWithdrawal2);
        walletWithdrawal2.setId(2L);
        assertThat(walletWithdrawal1).isNotEqualTo(walletWithdrawal2);
        walletWithdrawal1.setId(null);
        assertThat(walletWithdrawal1).isNotEqualTo(walletWithdrawal2);
    }
}

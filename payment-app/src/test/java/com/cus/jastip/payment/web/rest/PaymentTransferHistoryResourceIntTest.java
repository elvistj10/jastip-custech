package com.cus.jastip.payment.web.rest;

import com.cus.jastip.payment.PaymentApp;

import com.cus.jastip.payment.config.SecurityBeanOverrideConfiguration;

import com.cus.jastip.payment.domain.PaymentTransferHistory;
import com.cus.jastip.payment.repository.PaymentTransferHistoryRepository;
import com.cus.jastip.payment.repository.search.PaymentTransferHistorySearchRepository;
import com.cus.jastip.payment.web.rest.errors.ExceptionTranslator;

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

import static com.cus.jastip.payment.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PaymentTransferHistoryResource REST controller.
 *
 * @see PaymentTransferHistoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {PaymentApp.class, SecurityBeanOverrideConfiguration.class})
public class PaymentTransferHistoryResourceIntTest {

    private static final Long DEFAULT_POSTING_ID = 1L;
    private static final Long UPDATED_POSTING_ID = 2L;

    private static final Long DEFAULT_OFFERING_ID = 1L;
    private static final Long UPDATED_OFFERING_ID = 2L;

    private static final Double DEFAULT_NOMINAL = 0D;
    private static final Double UPDATED_NOMINAL = 1D;

    private static final Instant DEFAULT_PAYMENT_CONFIRM_DATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PAYMENT_CONFIRM_DATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_CHECK_DATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CHECK_DATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_EXPIRED_DATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXPIRED_DATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private PaymentTransferHistoryRepository paymentTransferHistoryRepository;

    @Autowired
    private PaymentTransferHistorySearchRepository paymentTransferHistorySearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPaymentTransferHistoryMockMvc;

    private PaymentTransferHistory paymentTransferHistory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PaymentTransferHistoryResource paymentTransferHistoryResource = new PaymentTransferHistoryResource(paymentTransferHistoryRepository, paymentTransferHistorySearchRepository);
        this.restPaymentTransferHistoryMockMvc = MockMvcBuilders.standaloneSetup(paymentTransferHistoryResource)
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
    public static PaymentTransferHistory createEntity(EntityManager em) {
        PaymentTransferHistory paymentTransferHistory = new PaymentTransferHistory()
            .postingId(DEFAULT_POSTING_ID)
            .offeringId(DEFAULT_OFFERING_ID)
            .nominal(DEFAULT_NOMINAL)
            .paymentConfirmDateTime(DEFAULT_PAYMENT_CONFIRM_DATE_TIME)
            .checkDateTime(DEFAULT_CHECK_DATE_TIME)
            .expiredDateTime(DEFAULT_EXPIRED_DATE_TIME);
        return paymentTransferHistory;
    }

    @Before
    public void initTest() {
        paymentTransferHistorySearchRepository.deleteAll();
        paymentTransferHistory = createEntity(em);
    }

    @Test
    @Transactional
    public void createPaymentTransferHistory() throws Exception {
        int databaseSizeBeforeCreate = paymentTransferHistoryRepository.findAll().size();

        // Create the PaymentTransferHistory
        restPaymentTransferHistoryMockMvc.perform(post("/api/payment-transfer-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentTransferHistory)))
            .andExpect(status().isCreated());

        // Validate the PaymentTransferHistory in the database
        List<PaymentTransferHistory> paymentTransferHistoryList = paymentTransferHistoryRepository.findAll();
        assertThat(paymentTransferHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        PaymentTransferHistory testPaymentTransferHistory = paymentTransferHistoryList.get(paymentTransferHistoryList.size() - 1);
        assertThat(testPaymentTransferHistory.getPostingId()).isEqualTo(DEFAULT_POSTING_ID);
        assertThat(testPaymentTransferHistory.getOfferingId()).isEqualTo(DEFAULT_OFFERING_ID);
        assertThat(testPaymentTransferHistory.getNominal()).isEqualTo(DEFAULT_NOMINAL);
        assertThat(testPaymentTransferHistory.getPaymentConfirmDateTime()).isEqualTo(DEFAULT_PAYMENT_CONFIRM_DATE_TIME);
        assertThat(testPaymentTransferHistory.getCheckDateTime()).isEqualTo(DEFAULT_CHECK_DATE_TIME);
        assertThat(testPaymentTransferHistory.getExpiredDateTime()).isEqualTo(DEFAULT_EXPIRED_DATE_TIME);

        // Validate the PaymentTransferHistory in Elasticsearch
        PaymentTransferHistory paymentTransferHistoryEs = paymentTransferHistorySearchRepository.findOne(testPaymentTransferHistory.getId());
        assertThat(paymentTransferHistoryEs).isEqualToIgnoringGivenFields(testPaymentTransferHistory);
    }

    @Test
    @Transactional
    public void createPaymentTransferHistoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = paymentTransferHistoryRepository.findAll().size();

        // Create the PaymentTransferHistory with an existing ID
        paymentTransferHistory.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentTransferHistoryMockMvc.perform(post("/api/payment-transfer-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentTransferHistory)))
            .andExpect(status().isBadRequest());

        // Validate the PaymentTransferHistory in the database
        List<PaymentTransferHistory> paymentTransferHistoryList = paymentTransferHistoryRepository.findAll();
        assertThat(paymentTransferHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkPostingIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentTransferHistoryRepository.findAll().size();
        // set the field null
        paymentTransferHistory.setPostingId(null);

        // Create the PaymentTransferHistory, which fails.

        restPaymentTransferHistoryMockMvc.perform(post("/api/payment-transfer-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentTransferHistory)))
            .andExpect(status().isBadRequest());

        List<PaymentTransferHistory> paymentTransferHistoryList = paymentTransferHistoryRepository.findAll();
        assertThat(paymentTransferHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOfferingIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentTransferHistoryRepository.findAll().size();
        // set the field null
        paymentTransferHistory.setOfferingId(null);

        // Create the PaymentTransferHistory, which fails.

        restPaymentTransferHistoryMockMvc.perform(post("/api/payment-transfer-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentTransferHistory)))
            .andExpect(status().isBadRequest());

        List<PaymentTransferHistory> paymentTransferHistoryList = paymentTransferHistoryRepository.findAll();
        assertThat(paymentTransferHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNominalIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentTransferHistoryRepository.findAll().size();
        // set the field null
        paymentTransferHistory.setNominal(null);

        // Create the PaymentTransferHistory, which fails.

        restPaymentTransferHistoryMockMvc.perform(post("/api/payment-transfer-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentTransferHistory)))
            .andExpect(status().isBadRequest());

        List<PaymentTransferHistory> paymentTransferHistoryList = paymentTransferHistoryRepository.findAll();
        assertThat(paymentTransferHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPaymentConfirmDateTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentTransferHistoryRepository.findAll().size();
        // set the field null
        paymentTransferHistory.setPaymentConfirmDateTime(null);

        // Create the PaymentTransferHistory, which fails.

        restPaymentTransferHistoryMockMvc.perform(post("/api/payment-transfer-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentTransferHistory)))
            .andExpect(status().isBadRequest());

        List<PaymentTransferHistory> paymentTransferHistoryList = paymentTransferHistoryRepository.findAll();
        assertThat(paymentTransferHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCheckDateTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentTransferHistoryRepository.findAll().size();
        // set the field null
        paymentTransferHistory.setCheckDateTime(null);

        // Create the PaymentTransferHistory, which fails.

        restPaymentTransferHistoryMockMvc.perform(post("/api/payment-transfer-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentTransferHistory)))
            .andExpect(status().isBadRequest());

        List<PaymentTransferHistory> paymentTransferHistoryList = paymentTransferHistoryRepository.findAll();
        assertThat(paymentTransferHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkExpiredDateTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentTransferHistoryRepository.findAll().size();
        // set the field null
        paymentTransferHistory.setExpiredDateTime(null);

        // Create the PaymentTransferHistory, which fails.

        restPaymentTransferHistoryMockMvc.perform(post("/api/payment-transfer-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentTransferHistory)))
            .andExpect(status().isBadRequest());

        List<PaymentTransferHistory> paymentTransferHistoryList = paymentTransferHistoryRepository.findAll();
        assertThat(paymentTransferHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPaymentTransferHistories() throws Exception {
        // Initialize the database
        paymentTransferHistoryRepository.saveAndFlush(paymentTransferHistory);

        // Get all the paymentTransferHistoryList
        restPaymentTransferHistoryMockMvc.perform(get("/api/payment-transfer-histories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentTransferHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].postingId").value(hasItem(DEFAULT_POSTING_ID.intValue())))
            .andExpect(jsonPath("$.[*].offeringId").value(hasItem(DEFAULT_OFFERING_ID.intValue())))
            .andExpect(jsonPath("$.[*].nominal").value(hasItem(DEFAULT_NOMINAL.doubleValue())))
            .andExpect(jsonPath("$.[*].paymentConfirmDateTime").value(hasItem(DEFAULT_PAYMENT_CONFIRM_DATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].checkDateTime").value(hasItem(DEFAULT_CHECK_DATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].expiredDateTime").value(hasItem(DEFAULT_EXPIRED_DATE_TIME.toString())));
    }

    @Test
    @Transactional
    public void getPaymentTransferHistory() throws Exception {
        // Initialize the database
        paymentTransferHistoryRepository.saveAndFlush(paymentTransferHistory);

        // Get the paymentTransferHistory
        restPaymentTransferHistoryMockMvc.perform(get("/api/payment-transfer-histories/{id}", paymentTransferHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(paymentTransferHistory.getId().intValue()))
            .andExpect(jsonPath("$.postingId").value(DEFAULT_POSTING_ID.intValue()))
            .andExpect(jsonPath("$.offeringId").value(DEFAULT_OFFERING_ID.intValue()))
            .andExpect(jsonPath("$.nominal").value(DEFAULT_NOMINAL.doubleValue()))
            .andExpect(jsonPath("$.paymentConfirmDateTime").value(DEFAULT_PAYMENT_CONFIRM_DATE_TIME.toString()))
            .andExpect(jsonPath("$.checkDateTime").value(DEFAULT_CHECK_DATE_TIME.toString()))
            .andExpect(jsonPath("$.expiredDateTime").value(DEFAULT_EXPIRED_DATE_TIME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPaymentTransferHistory() throws Exception {
        // Get the paymentTransferHistory
        restPaymentTransferHistoryMockMvc.perform(get("/api/payment-transfer-histories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePaymentTransferHistory() throws Exception {
        // Initialize the database
        paymentTransferHistoryRepository.saveAndFlush(paymentTransferHistory);
        paymentTransferHistorySearchRepository.save(paymentTransferHistory);
        int databaseSizeBeforeUpdate = paymentTransferHistoryRepository.findAll().size();

        // Update the paymentTransferHistory
        PaymentTransferHistory updatedPaymentTransferHistory = paymentTransferHistoryRepository.findOne(paymentTransferHistory.getId());
        // Disconnect from session so that the updates on updatedPaymentTransferHistory are not directly saved in db
        em.detach(updatedPaymentTransferHistory);
        updatedPaymentTransferHistory
            .postingId(UPDATED_POSTING_ID)
            .offeringId(UPDATED_OFFERING_ID)
            .nominal(UPDATED_NOMINAL)
            .paymentConfirmDateTime(UPDATED_PAYMENT_CONFIRM_DATE_TIME)
            .checkDateTime(UPDATED_CHECK_DATE_TIME)
            .expiredDateTime(UPDATED_EXPIRED_DATE_TIME);

        restPaymentTransferHistoryMockMvc.perform(put("/api/payment-transfer-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPaymentTransferHistory)))
            .andExpect(status().isOk());

        // Validate the PaymentTransferHistory in the database
        List<PaymentTransferHistory> paymentTransferHistoryList = paymentTransferHistoryRepository.findAll();
        assertThat(paymentTransferHistoryList).hasSize(databaseSizeBeforeUpdate);
        PaymentTransferHistory testPaymentTransferHistory = paymentTransferHistoryList.get(paymentTransferHistoryList.size() - 1);
        assertThat(testPaymentTransferHistory.getPostingId()).isEqualTo(UPDATED_POSTING_ID);
        assertThat(testPaymentTransferHistory.getOfferingId()).isEqualTo(UPDATED_OFFERING_ID);
        assertThat(testPaymentTransferHistory.getNominal()).isEqualTo(UPDATED_NOMINAL);
        assertThat(testPaymentTransferHistory.getPaymentConfirmDateTime()).isEqualTo(UPDATED_PAYMENT_CONFIRM_DATE_TIME);
        assertThat(testPaymentTransferHistory.getCheckDateTime()).isEqualTo(UPDATED_CHECK_DATE_TIME);
        assertThat(testPaymentTransferHistory.getExpiredDateTime()).isEqualTo(UPDATED_EXPIRED_DATE_TIME);

        // Validate the PaymentTransferHistory in Elasticsearch
        PaymentTransferHistory paymentTransferHistoryEs = paymentTransferHistorySearchRepository.findOne(testPaymentTransferHistory.getId());
        assertThat(paymentTransferHistoryEs).isEqualToIgnoringGivenFields(testPaymentTransferHistory);
    }

    @Test
    @Transactional
    public void updateNonExistingPaymentTransferHistory() throws Exception {
        int databaseSizeBeforeUpdate = paymentTransferHistoryRepository.findAll().size();

        // Create the PaymentTransferHistory

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPaymentTransferHistoryMockMvc.perform(put("/api/payment-transfer-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentTransferHistory)))
            .andExpect(status().isCreated());

        // Validate the PaymentTransferHistory in the database
        List<PaymentTransferHistory> paymentTransferHistoryList = paymentTransferHistoryRepository.findAll();
        assertThat(paymentTransferHistoryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePaymentTransferHistory() throws Exception {
        // Initialize the database
        paymentTransferHistoryRepository.saveAndFlush(paymentTransferHistory);
        paymentTransferHistorySearchRepository.save(paymentTransferHistory);
        int databaseSizeBeforeDelete = paymentTransferHistoryRepository.findAll().size();

        // Get the paymentTransferHistory
        restPaymentTransferHistoryMockMvc.perform(delete("/api/payment-transfer-histories/{id}", paymentTransferHistory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean paymentTransferHistoryExistsInEs = paymentTransferHistorySearchRepository.exists(paymentTransferHistory.getId());
        assertThat(paymentTransferHistoryExistsInEs).isFalse();

        // Validate the database is empty
        List<PaymentTransferHistory> paymentTransferHistoryList = paymentTransferHistoryRepository.findAll();
        assertThat(paymentTransferHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPaymentTransferHistory() throws Exception {
        // Initialize the database
        paymentTransferHistoryRepository.saveAndFlush(paymentTransferHistory);
        paymentTransferHistorySearchRepository.save(paymentTransferHistory);

        // Search the paymentTransferHistory
        restPaymentTransferHistoryMockMvc.perform(get("/api/_search/payment-transfer-histories?query=id:" + paymentTransferHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentTransferHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].postingId").value(hasItem(DEFAULT_POSTING_ID.intValue())))
            .andExpect(jsonPath("$.[*].offeringId").value(hasItem(DEFAULT_OFFERING_ID.intValue())))
            .andExpect(jsonPath("$.[*].nominal").value(hasItem(DEFAULT_NOMINAL.doubleValue())))
            .andExpect(jsonPath("$.[*].paymentConfirmDateTime").value(hasItem(DEFAULT_PAYMENT_CONFIRM_DATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].checkDateTime").value(hasItem(DEFAULT_CHECK_DATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].expiredDateTime").value(hasItem(DEFAULT_EXPIRED_DATE_TIME.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentTransferHistory.class);
        PaymentTransferHistory paymentTransferHistory1 = new PaymentTransferHistory();
        paymentTransferHistory1.setId(1L);
        PaymentTransferHistory paymentTransferHistory2 = new PaymentTransferHistory();
        paymentTransferHistory2.setId(paymentTransferHistory1.getId());
        assertThat(paymentTransferHistory1).isEqualTo(paymentTransferHistory2);
        paymentTransferHistory2.setId(2L);
        assertThat(paymentTransferHistory1).isNotEqualTo(paymentTransferHistory2);
        paymentTransferHistory1.setId(null);
        assertThat(paymentTransferHistory1).isNotEqualTo(paymentTransferHistory2);
    }
}

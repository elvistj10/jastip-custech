package com.cus.jastip.payment.web.rest;

import com.cus.jastip.payment.PaymentApp;

import com.cus.jastip.payment.config.SecurityBeanOverrideConfiguration;

import com.cus.jastip.payment.domain.PaymentTransferUnmatched;
import com.cus.jastip.payment.repository.PaymentTransferUnmatchedRepository;
import com.cus.jastip.payment.repository.search.PaymentTransferUnmatchedSearchRepository;
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
 * Test class for the PaymentTransferUnmatchedResource REST controller.
 *
 * @see PaymentTransferUnmatchedResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {PaymentApp.class, SecurityBeanOverrideConfiguration.class})
public class PaymentTransferUnmatchedResourceIntTest {

    private static final Long DEFAULT_POSTING_ID = 1L;
    private static final Long UPDATED_POSTING_ID = 2L;

    private static final Long DEFAULT_OFFERING_ID = 1L;
    private static final Long UPDATED_OFFERING_ID = 2L;

    private static final Double DEFAULT_NOMINAL = 0D;
    private static final Double UPDATED_NOMINAL = 1D;

    private static final Instant DEFAULT_PAYMENT_UNMATCHED_DATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PAYMENT_UNMATCHED_DATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_CHECK_DATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CHECK_DATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_EXPIRED_DATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXPIRED_DATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private PaymentTransferUnmatchedRepository paymentTransferUnmatchedRepository;

    @Autowired
    private PaymentTransferUnmatchedSearchRepository paymentTransferUnmatchedSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPaymentTransferUnmatchedMockMvc;

    private PaymentTransferUnmatched paymentTransferUnmatched;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PaymentTransferUnmatchedResource paymentTransferUnmatchedResource = new PaymentTransferUnmatchedResource(paymentTransferUnmatchedRepository, paymentTransferUnmatchedSearchRepository);
        this.restPaymentTransferUnmatchedMockMvc = MockMvcBuilders.standaloneSetup(paymentTransferUnmatchedResource)
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
    public static PaymentTransferUnmatched createEntity(EntityManager em) {
        PaymentTransferUnmatched paymentTransferUnmatched = new PaymentTransferUnmatched()
            .postingId(DEFAULT_POSTING_ID)
            .offeringId(DEFAULT_OFFERING_ID)
            .nominal(DEFAULT_NOMINAL)
            .paymentUnmatchedDateTime(DEFAULT_PAYMENT_UNMATCHED_DATE_TIME)
            .checkDateTime(DEFAULT_CHECK_DATE_TIME)
            .expiredDateTime(DEFAULT_EXPIRED_DATE_TIME);
        return paymentTransferUnmatched;
    }

    @Before
    public void initTest() {
        paymentTransferUnmatchedSearchRepository.deleteAll();
        paymentTransferUnmatched = createEntity(em);
    }

    @Test
    @Transactional
    public void createPaymentTransferUnmatched() throws Exception {
        int databaseSizeBeforeCreate = paymentTransferUnmatchedRepository.findAll().size();

        // Create the PaymentTransferUnmatched
        restPaymentTransferUnmatchedMockMvc.perform(post("/api/payment-transfer-unmatcheds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentTransferUnmatched)))
            .andExpect(status().isCreated());

        // Validate the PaymentTransferUnmatched in the database
        List<PaymentTransferUnmatched> paymentTransferUnmatchedList = paymentTransferUnmatchedRepository.findAll();
        assertThat(paymentTransferUnmatchedList).hasSize(databaseSizeBeforeCreate + 1);
        PaymentTransferUnmatched testPaymentTransferUnmatched = paymentTransferUnmatchedList.get(paymentTransferUnmatchedList.size() - 1);
        assertThat(testPaymentTransferUnmatched.getPostingId()).isEqualTo(DEFAULT_POSTING_ID);
        assertThat(testPaymentTransferUnmatched.getOfferingId()).isEqualTo(DEFAULT_OFFERING_ID);
        assertThat(testPaymentTransferUnmatched.getNominal()).isEqualTo(DEFAULT_NOMINAL);
        assertThat(testPaymentTransferUnmatched.getPaymentUnmatchedDateTime()).isEqualTo(DEFAULT_PAYMENT_UNMATCHED_DATE_TIME);
        assertThat(testPaymentTransferUnmatched.getCheckDateTime()).isEqualTo(DEFAULT_CHECK_DATE_TIME);
        assertThat(testPaymentTransferUnmatched.getExpiredDateTime()).isEqualTo(DEFAULT_EXPIRED_DATE_TIME);

        // Validate the PaymentTransferUnmatched in Elasticsearch
        PaymentTransferUnmatched paymentTransferUnmatchedEs = paymentTransferUnmatchedSearchRepository.findOne(testPaymentTransferUnmatched.getId());
        assertThat(paymentTransferUnmatchedEs).isEqualToIgnoringGivenFields(testPaymentTransferUnmatched);
    }

    @Test
    @Transactional
    public void createPaymentTransferUnmatchedWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = paymentTransferUnmatchedRepository.findAll().size();

        // Create the PaymentTransferUnmatched with an existing ID
        paymentTransferUnmatched.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentTransferUnmatchedMockMvc.perform(post("/api/payment-transfer-unmatcheds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentTransferUnmatched)))
            .andExpect(status().isBadRequest());

        // Validate the PaymentTransferUnmatched in the database
        List<PaymentTransferUnmatched> paymentTransferUnmatchedList = paymentTransferUnmatchedRepository.findAll();
        assertThat(paymentTransferUnmatchedList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkPostingIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentTransferUnmatchedRepository.findAll().size();
        // set the field null
        paymentTransferUnmatched.setPostingId(null);

        // Create the PaymentTransferUnmatched, which fails.

        restPaymentTransferUnmatchedMockMvc.perform(post("/api/payment-transfer-unmatcheds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentTransferUnmatched)))
            .andExpect(status().isBadRequest());

        List<PaymentTransferUnmatched> paymentTransferUnmatchedList = paymentTransferUnmatchedRepository.findAll();
        assertThat(paymentTransferUnmatchedList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOfferingIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentTransferUnmatchedRepository.findAll().size();
        // set the field null
        paymentTransferUnmatched.setOfferingId(null);

        // Create the PaymentTransferUnmatched, which fails.

        restPaymentTransferUnmatchedMockMvc.perform(post("/api/payment-transfer-unmatcheds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentTransferUnmatched)))
            .andExpect(status().isBadRequest());

        List<PaymentTransferUnmatched> paymentTransferUnmatchedList = paymentTransferUnmatchedRepository.findAll();
        assertThat(paymentTransferUnmatchedList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNominalIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentTransferUnmatchedRepository.findAll().size();
        // set the field null
        paymentTransferUnmatched.setNominal(null);

        // Create the PaymentTransferUnmatched, which fails.

        restPaymentTransferUnmatchedMockMvc.perform(post("/api/payment-transfer-unmatcheds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentTransferUnmatched)))
            .andExpect(status().isBadRequest());

        List<PaymentTransferUnmatched> paymentTransferUnmatchedList = paymentTransferUnmatchedRepository.findAll();
        assertThat(paymentTransferUnmatchedList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPaymentUnmatchedDateTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentTransferUnmatchedRepository.findAll().size();
        // set the field null
        paymentTransferUnmatched.setPaymentUnmatchedDateTime(null);

        // Create the PaymentTransferUnmatched, which fails.

        restPaymentTransferUnmatchedMockMvc.perform(post("/api/payment-transfer-unmatcheds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentTransferUnmatched)))
            .andExpect(status().isBadRequest());

        List<PaymentTransferUnmatched> paymentTransferUnmatchedList = paymentTransferUnmatchedRepository.findAll();
        assertThat(paymentTransferUnmatchedList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCheckDateTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentTransferUnmatchedRepository.findAll().size();
        // set the field null
        paymentTransferUnmatched.setCheckDateTime(null);

        // Create the PaymentTransferUnmatched, which fails.

        restPaymentTransferUnmatchedMockMvc.perform(post("/api/payment-transfer-unmatcheds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentTransferUnmatched)))
            .andExpect(status().isBadRequest());

        List<PaymentTransferUnmatched> paymentTransferUnmatchedList = paymentTransferUnmatchedRepository.findAll();
        assertThat(paymentTransferUnmatchedList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkExpiredDateTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentTransferUnmatchedRepository.findAll().size();
        // set the field null
        paymentTransferUnmatched.setExpiredDateTime(null);

        // Create the PaymentTransferUnmatched, which fails.

        restPaymentTransferUnmatchedMockMvc.perform(post("/api/payment-transfer-unmatcheds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentTransferUnmatched)))
            .andExpect(status().isBadRequest());

        List<PaymentTransferUnmatched> paymentTransferUnmatchedList = paymentTransferUnmatchedRepository.findAll();
        assertThat(paymentTransferUnmatchedList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPaymentTransferUnmatcheds() throws Exception {
        // Initialize the database
        paymentTransferUnmatchedRepository.saveAndFlush(paymentTransferUnmatched);

        // Get all the paymentTransferUnmatchedList
        restPaymentTransferUnmatchedMockMvc.perform(get("/api/payment-transfer-unmatcheds?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentTransferUnmatched.getId().intValue())))
            .andExpect(jsonPath("$.[*].postingId").value(hasItem(DEFAULT_POSTING_ID.intValue())))
            .andExpect(jsonPath("$.[*].offeringId").value(hasItem(DEFAULT_OFFERING_ID.intValue())))
            .andExpect(jsonPath("$.[*].nominal").value(hasItem(DEFAULT_NOMINAL.doubleValue())))
            .andExpect(jsonPath("$.[*].paymentUnmatchedDateTime").value(hasItem(DEFAULT_PAYMENT_UNMATCHED_DATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].checkDateTime").value(hasItem(DEFAULT_CHECK_DATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].expiredDateTime").value(hasItem(DEFAULT_EXPIRED_DATE_TIME.toString())));
    }

    @Test
    @Transactional
    public void getPaymentTransferUnmatched() throws Exception {
        // Initialize the database
        paymentTransferUnmatchedRepository.saveAndFlush(paymentTransferUnmatched);

        // Get the paymentTransferUnmatched
        restPaymentTransferUnmatchedMockMvc.perform(get("/api/payment-transfer-unmatcheds/{id}", paymentTransferUnmatched.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(paymentTransferUnmatched.getId().intValue()))
            .andExpect(jsonPath("$.postingId").value(DEFAULT_POSTING_ID.intValue()))
            .andExpect(jsonPath("$.offeringId").value(DEFAULT_OFFERING_ID.intValue()))
            .andExpect(jsonPath("$.nominal").value(DEFAULT_NOMINAL.doubleValue()))
            .andExpect(jsonPath("$.paymentUnmatchedDateTime").value(DEFAULT_PAYMENT_UNMATCHED_DATE_TIME.toString()))
            .andExpect(jsonPath("$.checkDateTime").value(DEFAULT_CHECK_DATE_TIME.toString()))
            .andExpect(jsonPath("$.expiredDateTime").value(DEFAULT_EXPIRED_DATE_TIME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPaymentTransferUnmatched() throws Exception {
        // Get the paymentTransferUnmatched
        restPaymentTransferUnmatchedMockMvc.perform(get("/api/payment-transfer-unmatcheds/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePaymentTransferUnmatched() throws Exception {
        // Initialize the database
        paymentTransferUnmatchedRepository.saveAndFlush(paymentTransferUnmatched);
        paymentTransferUnmatchedSearchRepository.save(paymentTransferUnmatched);
        int databaseSizeBeforeUpdate = paymentTransferUnmatchedRepository.findAll().size();

        // Update the paymentTransferUnmatched
        PaymentTransferUnmatched updatedPaymentTransferUnmatched = paymentTransferUnmatchedRepository.findOne(paymentTransferUnmatched.getId());
        // Disconnect from session so that the updates on updatedPaymentTransferUnmatched are not directly saved in db
        em.detach(updatedPaymentTransferUnmatched);
        updatedPaymentTransferUnmatched
            .postingId(UPDATED_POSTING_ID)
            .offeringId(UPDATED_OFFERING_ID)
            .nominal(UPDATED_NOMINAL)
            .paymentUnmatchedDateTime(UPDATED_PAYMENT_UNMATCHED_DATE_TIME)
            .checkDateTime(UPDATED_CHECK_DATE_TIME)
            .expiredDateTime(UPDATED_EXPIRED_DATE_TIME);

        restPaymentTransferUnmatchedMockMvc.perform(put("/api/payment-transfer-unmatcheds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPaymentTransferUnmatched)))
            .andExpect(status().isOk());

        // Validate the PaymentTransferUnmatched in the database
        List<PaymentTransferUnmatched> paymentTransferUnmatchedList = paymentTransferUnmatchedRepository.findAll();
        assertThat(paymentTransferUnmatchedList).hasSize(databaseSizeBeforeUpdate);
        PaymentTransferUnmatched testPaymentTransferUnmatched = paymentTransferUnmatchedList.get(paymentTransferUnmatchedList.size() - 1);
        assertThat(testPaymentTransferUnmatched.getPostingId()).isEqualTo(UPDATED_POSTING_ID);
        assertThat(testPaymentTransferUnmatched.getOfferingId()).isEqualTo(UPDATED_OFFERING_ID);
        assertThat(testPaymentTransferUnmatched.getNominal()).isEqualTo(UPDATED_NOMINAL);
        assertThat(testPaymentTransferUnmatched.getPaymentUnmatchedDateTime()).isEqualTo(UPDATED_PAYMENT_UNMATCHED_DATE_TIME);
        assertThat(testPaymentTransferUnmatched.getCheckDateTime()).isEqualTo(UPDATED_CHECK_DATE_TIME);
        assertThat(testPaymentTransferUnmatched.getExpiredDateTime()).isEqualTo(UPDATED_EXPIRED_DATE_TIME);

        // Validate the PaymentTransferUnmatched in Elasticsearch
        PaymentTransferUnmatched paymentTransferUnmatchedEs = paymentTransferUnmatchedSearchRepository.findOne(testPaymentTransferUnmatched.getId());
        assertThat(paymentTransferUnmatchedEs).isEqualToIgnoringGivenFields(testPaymentTransferUnmatched);
    }

    @Test
    @Transactional
    public void updateNonExistingPaymentTransferUnmatched() throws Exception {
        int databaseSizeBeforeUpdate = paymentTransferUnmatchedRepository.findAll().size();

        // Create the PaymentTransferUnmatched

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPaymentTransferUnmatchedMockMvc.perform(put("/api/payment-transfer-unmatcheds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentTransferUnmatched)))
            .andExpect(status().isCreated());

        // Validate the PaymentTransferUnmatched in the database
        List<PaymentTransferUnmatched> paymentTransferUnmatchedList = paymentTransferUnmatchedRepository.findAll();
        assertThat(paymentTransferUnmatchedList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePaymentTransferUnmatched() throws Exception {
        // Initialize the database
        paymentTransferUnmatchedRepository.saveAndFlush(paymentTransferUnmatched);
        paymentTransferUnmatchedSearchRepository.save(paymentTransferUnmatched);
        int databaseSizeBeforeDelete = paymentTransferUnmatchedRepository.findAll().size();

        // Get the paymentTransferUnmatched
        restPaymentTransferUnmatchedMockMvc.perform(delete("/api/payment-transfer-unmatcheds/{id}", paymentTransferUnmatched.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean paymentTransferUnmatchedExistsInEs = paymentTransferUnmatchedSearchRepository.exists(paymentTransferUnmatched.getId());
        assertThat(paymentTransferUnmatchedExistsInEs).isFalse();

        // Validate the database is empty
        List<PaymentTransferUnmatched> paymentTransferUnmatchedList = paymentTransferUnmatchedRepository.findAll();
        assertThat(paymentTransferUnmatchedList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPaymentTransferUnmatched() throws Exception {
        // Initialize the database
        paymentTransferUnmatchedRepository.saveAndFlush(paymentTransferUnmatched);
        paymentTransferUnmatchedSearchRepository.save(paymentTransferUnmatched);

        // Search the paymentTransferUnmatched
        restPaymentTransferUnmatchedMockMvc.perform(get("/api/_search/payment-transfer-unmatcheds?query=id:" + paymentTransferUnmatched.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentTransferUnmatched.getId().intValue())))
            .andExpect(jsonPath("$.[*].postingId").value(hasItem(DEFAULT_POSTING_ID.intValue())))
            .andExpect(jsonPath("$.[*].offeringId").value(hasItem(DEFAULT_OFFERING_ID.intValue())))
            .andExpect(jsonPath("$.[*].nominal").value(hasItem(DEFAULT_NOMINAL.doubleValue())))
            .andExpect(jsonPath("$.[*].paymentUnmatchedDateTime").value(hasItem(DEFAULT_PAYMENT_UNMATCHED_DATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].checkDateTime").value(hasItem(DEFAULT_CHECK_DATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].expiredDateTime").value(hasItem(DEFAULT_EXPIRED_DATE_TIME.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentTransferUnmatched.class);
        PaymentTransferUnmatched paymentTransferUnmatched1 = new PaymentTransferUnmatched();
        paymentTransferUnmatched1.setId(1L);
        PaymentTransferUnmatched paymentTransferUnmatched2 = new PaymentTransferUnmatched();
        paymentTransferUnmatched2.setId(paymentTransferUnmatched1.getId());
        assertThat(paymentTransferUnmatched1).isEqualTo(paymentTransferUnmatched2);
        paymentTransferUnmatched2.setId(2L);
        assertThat(paymentTransferUnmatched1).isNotEqualTo(paymentTransferUnmatched2);
        paymentTransferUnmatched1.setId(null);
        assertThat(paymentTransferUnmatched1).isNotEqualTo(paymentTransferUnmatched2);
    }
}

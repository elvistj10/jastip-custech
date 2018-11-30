package com.cus.jastip.payment.web.rest;

import com.cus.jastip.payment.PaymentApp;

import com.cus.jastip.payment.config.SecurityBeanOverrideConfiguration;

import com.cus.jastip.payment.domain.PaymentTransferCheckList;
import com.cus.jastip.payment.repository.PaymentTransferCheckListRepository;
import com.cus.jastip.payment.repository.search.PaymentTransferCheckListSearchRepository;
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
 * Test class for the PaymentTransferCheckListResource REST controller.
 *
 * @see PaymentTransferCheckListResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {PaymentApp.class, SecurityBeanOverrideConfiguration.class})
public class PaymentTransferCheckListResourceIntTest {

    private static final Long DEFAULT_POSTING_ID = 1L;
    private static final Long UPDATED_POSTING_ID = 2L;

    private static final Long DEFAULT_OFFERING_ID = 1L;
    private static final Long UPDATED_OFFERING_ID = 2L;

    private static final Double DEFAULT_NOMINAL = 0D;
    private static final Double UPDATED_NOMINAL = 1D;

    private static final Instant DEFAULT_PAYMENT_CONFIRM_DATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PAYMENT_CONFIRM_DATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_EXPIRED_DATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXPIRED_DATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private PaymentTransferCheckListRepository paymentTransferCheckListRepository;

    @Autowired
    private PaymentTransferCheckListSearchRepository paymentTransferCheckListSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPaymentTransferCheckListMockMvc;

    private PaymentTransferCheckList paymentTransferCheckList;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PaymentTransferCheckListResource paymentTransferCheckListResource = new PaymentTransferCheckListResource(paymentTransferCheckListRepository, paymentTransferCheckListSearchRepository);
        this.restPaymentTransferCheckListMockMvc = MockMvcBuilders.standaloneSetup(paymentTransferCheckListResource)
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
    public static PaymentTransferCheckList createEntity(EntityManager em) {
        PaymentTransferCheckList paymentTransferCheckList = new PaymentTransferCheckList()
            .postingId(DEFAULT_POSTING_ID)
            .offeringId(DEFAULT_OFFERING_ID)
            .nominal(DEFAULT_NOMINAL)
            .paymentConfirmDateTime(DEFAULT_PAYMENT_CONFIRM_DATE_TIME)
            .expiredDateTime(DEFAULT_EXPIRED_DATE_TIME);
        return paymentTransferCheckList;
    }

    @Before
    public void initTest() {
        paymentTransferCheckListSearchRepository.deleteAll();
        paymentTransferCheckList = createEntity(em);
    }

    @Test
    @Transactional
    public void createPaymentTransferCheckList() throws Exception {
        int databaseSizeBeforeCreate = paymentTransferCheckListRepository.findAll().size();

        // Create the PaymentTransferCheckList
        restPaymentTransferCheckListMockMvc.perform(post("/api/payment-transfer-check-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentTransferCheckList)))
            .andExpect(status().isCreated());

        // Validate the PaymentTransferCheckList in the database
        List<PaymentTransferCheckList> paymentTransferCheckListList = paymentTransferCheckListRepository.findAll();
        assertThat(paymentTransferCheckListList).hasSize(databaseSizeBeforeCreate + 1);
        PaymentTransferCheckList testPaymentTransferCheckList = paymentTransferCheckListList.get(paymentTransferCheckListList.size() - 1);
        assertThat(testPaymentTransferCheckList.getPostingId()).isEqualTo(DEFAULT_POSTING_ID);
        assertThat(testPaymentTransferCheckList.getOfferingId()).isEqualTo(DEFAULT_OFFERING_ID);
        assertThat(testPaymentTransferCheckList.getNominal()).isEqualTo(DEFAULT_NOMINAL);
        assertThat(testPaymentTransferCheckList.getPaymentConfirmDateTime()).isEqualTo(DEFAULT_PAYMENT_CONFIRM_DATE_TIME);
        assertThat(testPaymentTransferCheckList.getExpiredDateTime()).isEqualTo(DEFAULT_EXPIRED_DATE_TIME);

        // Validate the PaymentTransferCheckList in Elasticsearch
        PaymentTransferCheckList paymentTransferCheckListEs = paymentTransferCheckListSearchRepository.findOne(testPaymentTransferCheckList.getId());
        assertThat(paymentTransferCheckListEs).isEqualToIgnoringGivenFields(testPaymentTransferCheckList);
    }

    @Test
    @Transactional
    public void createPaymentTransferCheckListWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = paymentTransferCheckListRepository.findAll().size();

        // Create the PaymentTransferCheckList with an existing ID
        paymentTransferCheckList.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentTransferCheckListMockMvc.perform(post("/api/payment-transfer-check-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentTransferCheckList)))
            .andExpect(status().isBadRequest());

        // Validate the PaymentTransferCheckList in the database
        List<PaymentTransferCheckList> paymentTransferCheckListList = paymentTransferCheckListRepository.findAll();
        assertThat(paymentTransferCheckListList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkPostingIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentTransferCheckListRepository.findAll().size();
        // set the field null
        paymentTransferCheckList.setPostingId(null);

        // Create the PaymentTransferCheckList, which fails.

        restPaymentTransferCheckListMockMvc.perform(post("/api/payment-transfer-check-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentTransferCheckList)))
            .andExpect(status().isBadRequest());

        List<PaymentTransferCheckList> paymentTransferCheckListList = paymentTransferCheckListRepository.findAll();
        assertThat(paymentTransferCheckListList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOfferingIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentTransferCheckListRepository.findAll().size();
        // set the field null
        paymentTransferCheckList.setOfferingId(null);

        // Create the PaymentTransferCheckList, which fails.

        restPaymentTransferCheckListMockMvc.perform(post("/api/payment-transfer-check-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentTransferCheckList)))
            .andExpect(status().isBadRequest());

        List<PaymentTransferCheckList> paymentTransferCheckListList = paymentTransferCheckListRepository.findAll();
        assertThat(paymentTransferCheckListList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNominalIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentTransferCheckListRepository.findAll().size();
        // set the field null
        paymentTransferCheckList.setNominal(null);

        // Create the PaymentTransferCheckList, which fails.

        restPaymentTransferCheckListMockMvc.perform(post("/api/payment-transfer-check-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentTransferCheckList)))
            .andExpect(status().isBadRequest());

        List<PaymentTransferCheckList> paymentTransferCheckListList = paymentTransferCheckListRepository.findAll();
        assertThat(paymentTransferCheckListList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPaymentConfirmDateTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentTransferCheckListRepository.findAll().size();
        // set the field null
        paymentTransferCheckList.setPaymentConfirmDateTime(null);

        // Create the PaymentTransferCheckList, which fails.

        restPaymentTransferCheckListMockMvc.perform(post("/api/payment-transfer-check-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentTransferCheckList)))
            .andExpect(status().isBadRequest());

        List<PaymentTransferCheckList> paymentTransferCheckListList = paymentTransferCheckListRepository.findAll();
        assertThat(paymentTransferCheckListList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkExpiredDateTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentTransferCheckListRepository.findAll().size();
        // set the field null
        paymentTransferCheckList.setExpiredDateTime(null);

        // Create the PaymentTransferCheckList, which fails.

        restPaymentTransferCheckListMockMvc.perform(post("/api/payment-transfer-check-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentTransferCheckList)))
            .andExpect(status().isBadRequest());

        List<PaymentTransferCheckList> paymentTransferCheckListList = paymentTransferCheckListRepository.findAll();
        assertThat(paymentTransferCheckListList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPaymentTransferCheckLists() throws Exception {
        // Initialize the database
        paymentTransferCheckListRepository.saveAndFlush(paymentTransferCheckList);

        // Get all the paymentTransferCheckListList
        restPaymentTransferCheckListMockMvc.perform(get("/api/payment-transfer-check-lists?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentTransferCheckList.getId().intValue())))
            .andExpect(jsonPath("$.[*].postingId").value(hasItem(DEFAULT_POSTING_ID.intValue())))
            .andExpect(jsonPath("$.[*].offeringId").value(hasItem(DEFAULT_OFFERING_ID.intValue())))
            .andExpect(jsonPath("$.[*].nominal").value(hasItem(DEFAULT_NOMINAL.doubleValue())))
            .andExpect(jsonPath("$.[*].paymentConfirmDateTime").value(hasItem(DEFAULT_PAYMENT_CONFIRM_DATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].expiredDateTime").value(hasItem(DEFAULT_EXPIRED_DATE_TIME.toString())));
    }

    @Test
    @Transactional
    public void getPaymentTransferCheckList() throws Exception {
        // Initialize the database
        paymentTransferCheckListRepository.saveAndFlush(paymentTransferCheckList);

        // Get the paymentTransferCheckList
        restPaymentTransferCheckListMockMvc.perform(get("/api/payment-transfer-check-lists/{id}", paymentTransferCheckList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(paymentTransferCheckList.getId().intValue()))
            .andExpect(jsonPath("$.postingId").value(DEFAULT_POSTING_ID.intValue()))
            .andExpect(jsonPath("$.offeringId").value(DEFAULT_OFFERING_ID.intValue()))
            .andExpect(jsonPath("$.nominal").value(DEFAULT_NOMINAL.doubleValue()))
            .andExpect(jsonPath("$.paymentConfirmDateTime").value(DEFAULT_PAYMENT_CONFIRM_DATE_TIME.toString()))
            .andExpect(jsonPath("$.expiredDateTime").value(DEFAULT_EXPIRED_DATE_TIME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPaymentTransferCheckList() throws Exception {
        // Get the paymentTransferCheckList
        restPaymentTransferCheckListMockMvc.perform(get("/api/payment-transfer-check-lists/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePaymentTransferCheckList() throws Exception {
        // Initialize the database
        paymentTransferCheckListRepository.saveAndFlush(paymentTransferCheckList);
        paymentTransferCheckListSearchRepository.save(paymentTransferCheckList);
        int databaseSizeBeforeUpdate = paymentTransferCheckListRepository.findAll().size();

        // Update the paymentTransferCheckList
        PaymentTransferCheckList updatedPaymentTransferCheckList = paymentTransferCheckListRepository.findOne(paymentTransferCheckList.getId());
        // Disconnect from session so that the updates on updatedPaymentTransferCheckList are not directly saved in db
        em.detach(updatedPaymentTransferCheckList);
        updatedPaymentTransferCheckList
            .postingId(UPDATED_POSTING_ID)
            .offeringId(UPDATED_OFFERING_ID)
            .nominal(UPDATED_NOMINAL)
            .paymentConfirmDateTime(UPDATED_PAYMENT_CONFIRM_DATE_TIME)
            .expiredDateTime(UPDATED_EXPIRED_DATE_TIME);

        restPaymentTransferCheckListMockMvc.perform(put("/api/payment-transfer-check-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPaymentTransferCheckList)))
            .andExpect(status().isOk());

        // Validate the PaymentTransferCheckList in the database
        List<PaymentTransferCheckList> paymentTransferCheckListList = paymentTransferCheckListRepository.findAll();
        assertThat(paymentTransferCheckListList).hasSize(databaseSizeBeforeUpdate);
        PaymentTransferCheckList testPaymentTransferCheckList = paymentTransferCheckListList.get(paymentTransferCheckListList.size() - 1);
        assertThat(testPaymentTransferCheckList.getPostingId()).isEqualTo(UPDATED_POSTING_ID);
        assertThat(testPaymentTransferCheckList.getOfferingId()).isEqualTo(UPDATED_OFFERING_ID);
        assertThat(testPaymentTransferCheckList.getNominal()).isEqualTo(UPDATED_NOMINAL);
        assertThat(testPaymentTransferCheckList.getPaymentConfirmDateTime()).isEqualTo(UPDATED_PAYMENT_CONFIRM_DATE_TIME);
        assertThat(testPaymentTransferCheckList.getExpiredDateTime()).isEqualTo(UPDATED_EXPIRED_DATE_TIME);

        // Validate the PaymentTransferCheckList in Elasticsearch
        PaymentTransferCheckList paymentTransferCheckListEs = paymentTransferCheckListSearchRepository.findOne(testPaymentTransferCheckList.getId());
        assertThat(paymentTransferCheckListEs).isEqualToIgnoringGivenFields(testPaymentTransferCheckList);
    }

    @Test
    @Transactional
    public void updateNonExistingPaymentTransferCheckList() throws Exception {
        int databaseSizeBeforeUpdate = paymentTransferCheckListRepository.findAll().size();

        // Create the PaymentTransferCheckList

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPaymentTransferCheckListMockMvc.perform(put("/api/payment-transfer-check-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentTransferCheckList)))
            .andExpect(status().isCreated());

        // Validate the PaymentTransferCheckList in the database
        List<PaymentTransferCheckList> paymentTransferCheckListList = paymentTransferCheckListRepository.findAll();
        assertThat(paymentTransferCheckListList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePaymentTransferCheckList() throws Exception {
        // Initialize the database
        paymentTransferCheckListRepository.saveAndFlush(paymentTransferCheckList);
        paymentTransferCheckListSearchRepository.save(paymentTransferCheckList);
        int databaseSizeBeforeDelete = paymentTransferCheckListRepository.findAll().size();

        // Get the paymentTransferCheckList
        restPaymentTransferCheckListMockMvc.perform(delete("/api/payment-transfer-check-lists/{id}", paymentTransferCheckList.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean paymentTransferCheckListExistsInEs = paymentTransferCheckListSearchRepository.exists(paymentTransferCheckList.getId());
        assertThat(paymentTransferCheckListExistsInEs).isFalse();

        // Validate the database is empty
        List<PaymentTransferCheckList> paymentTransferCheckListList = paymentTransferCheckListRepository.findAll();
        assertThat(paymentTransferCheckListList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPaymentTransferCheckList() throws Exception {
        // Initialize the database
        paymentTransferCheckListRepository.saveAndFlush(paymentTransferCheckList);
        paymentTransferCheckListSearchRepository.save(paymentTransferCheckList);

        // Search the paymentTransferCheckList
        restPaymentTransferCheckListMockMvc.perform(get("/api/_search/payment-transfer-check-lists?query=id:" + paymentTransferCheckList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentTransferCheckList.getId().intValue())))
            .andExpect(jsonPath("$.[*].postingId").value(hasItem(DEFAULT_POSTING_ID.intValue())))
            .andExpect(jsonPath("$.[*].offeringId").value(hasItem(DEFAULT_OFFERING_ID.intValue())))
            .andExpect(jsonPath("$.[*].nominal").value(hasItem(DEFAULT_NOMINAL.doubleValue())))
            .andExpect(jsonPath("$.[*].paymentConfirmDateTime").value(hasItem(DEFAULT_PAYMENT_CONFIRM_DATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].expiredDateTime").value(hasItem(DEFAULT_EXPIRED_DATE_TIME.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentTransferCheckList.class);
        PaymentTransferCheckList paymentTransferCheckList1 = new PaymentTransferCheckList();
        paymentTransferCheckList1.setId(1L);
        PaymentTransferCheckList paymentTransferCheckList2 = new PaymentTransferCheckList();
        paymentTransferCheckList2.setId(paymentTransferCheckList1.getId());
        assertThat(paymentTransferCheckList1).isEqualTo(paymentTransferCheckList2);
        paymentTransferCheckList2.setId(2L);
        assertThat(paymentTransferCheckList1).isNotEqualTo(paymentTransferCheckList2);
        paymentTransferCheckList1.setId(null);
        assertThat(paymentTransferCheckList1).isNotEqualTo(paymentTransferCheckList2);
    }
}

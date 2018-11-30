package com.cus.jastip.payment.web.rest;

import com.cus.jastip.payment.PaymentApp;

import com.cus.jastip.payment.config.SecurityBeanOverrideConfiguration;

import com.cus.jastip.payment.domain.PaymentAudit;
import com.cus.jastip.payment.repository.PaymentAuditRepository;
import com.cus.jastip.payment.repository.search.PaymentAuditSearchRepository;
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
import java.util.List;

import static com.cus.jastip.payment.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PaymentAuditResource REST controller.
 *
 * @see PaymentAuditResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {PaymentApp.class, SecurityBeanOverrideConfiguration.class})
public class PaymentAuditResourceIntTest {

    private static final String DEFAULT_ENTITY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ENTITY_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_ENTITY_ID = 1L;
    private static final Long UPDATED_ENTITY_ID = 2L;

    @Autowired
    private PaymentAuditRepository paymentAuditRepository;

    @Autowired
    private PaymentAuditSearchRepository paymentAuditSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPaymentAuditMockMvc;

    private PaymentAudit paymentAudit;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PaymentAuditResource paymentAuditResource = new PaymentAuditResource(paymentAuditRepository, paymentAuditSearchRepository);
        this.restPaymentAuditMockMvc = MockMvcBuilders.standaloneSetup(paymentAuditResource)
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
    public static PaymentAudit createEntity(EntityManager em) {
        PaymentAudit paymentAudit = new PaymentAudit()
            .entityName(DEFAULT_ENTITY_NAME)
            .entityId(DEFAULT_ENTITY_ID);
        return paymentAudit;
    }

    @Before
    public void initTest() {
        paymentAuditSearchRepository.deleteAll();
        paymentAudit = createEntity(em);
    }

    @Test
    @Transactional
    public void createPaymentAudit() throws Exception {
        int databaseSizeBeforeCreate = paymentAuditRepository.findAll().size();

        // Create the PaymentAudit
        restPaymentAuditMockMvc.perform(post("/api/payment-audits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentAudit)))
            .andExpect(status().isCreated());

        // Validate the PaymentAudit in the database
        List<PaymentAudit> paymentAuditList = paymentAuditRepository.findAll();
        assertThat(paymentAuditList).hasSize(databaseSizeBeforeCreate + 1);
        PaymentAudit testPaymentAudit = paymentAuditList.get(paymentAuditList.size() - 1);
        assertThat(testPaymentAudit.getEntityName()).isEqualTo(DEFAULT_ENTITY_NAME);
        assertThat(testPaymentAudit.getEntityId()).isEqualTo(DEFAULT_ENTITY_ID);

        // Validate the PaymentAudit in Elasticsearch
        PaymentAudit paymentAuditEs = paymentAuditSearchRepository.findOne(testPaymentAudit.getId());
        assertThat(paymentAuditEs).isEqualToIgnoringGivenFields(testPaymentAudit);
    }

    @Test
    @Transactional
    public void createPaymentAuditWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = paymentAuditRepository.findAll().size();

        // Create the PaymentAudit with an existing ID
        paymentAudit.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentAuditMockMvc.perform(post("/api/payment-audits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentAudit)))
            .andExpect(status().isBadRequest());

        // Validate the PaymentAudit in the database
        List<PaymentAudit> paymentAuditList = paymentAuditRepository.findAll();
        assertThat(paymentAuditList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkEntityNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentAuditRepository.findAll().size();
        // set the field null
        paymentAudit.setEntityName(null);

        // Create the PaymentAudit, which fails.

        restPaymentAuditMockMvc.perform(post("/api/payment-audits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentAudit)))
            .andExpect(status().isBadRequest());

        List<PaymentAudit> paymentAuditList = paymentAuditRepository.findAll();
        assertThat(paymentAuditList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPaymentAudits() throws Exception {
        // Initialize the database
        paymentAuditRepository.saveAndFlush(paymentAudit);

        // Get all the paymentAuditList
        restPaymentAuditMockMvc.perform(get("/api/payment-audits?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentAudit.getId().intValue())))
            .andExpect(jsonPath("$.[*].entityName").value(hasItem(DEFAULT_ENTITY_NAME.toString())))
            .andExpect(jsonPath("$.[*].entityId").value(hasItem(DEFAULT_ENTITY_ID.intValue())));
    }

    @Test
    @Transactional
    public void getPaymentAudit() throws Exception {
        // Initialize the database
        paymentAuditRepository.saveAndFlush(paymentAudit);

        // Get the paymentAudit
        restPaymentAuditMockMvc.perform(get("/api/payment-audits/{id}", paymentAudit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(paymentAudit.getId().intValue()))
            .andExpect(jsonPath("$.entityName").value(DEFAULT_ENTITY_NAME.toString()))
            .andExpect(jsonPath("$.entityId").value(DEFAULT_ENTITY_ID.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPaymentAudit() throws Exception {
        // Get the paymentAudit
        restPaymentAuditMockMvc.perform(get("/api/payment-audits/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePaymentAudit() throws Exception {
        // Initialize the database
        paymentAuditRepository.saveAndFlush(paymentAudit);
        paymentAuditSearchRepository.save(paymentAudit);
        int databaseSizeBeforeUpdate = paymentAuditRepository.findAll().size();

        // Update the paymentAudit
        PaymentAudit updatedPaymentAudit = paymentAuditRepository.findOne(paymentAudit.getId());
        // Disconnect from session so that the updates on updatedPaymentAudit are not directly saved in db
        em.detach(updatedPaymentAudit);
        updatedPaymentAudit
            .entityName(UPDATED_ENTITY_NAME)
            .entityId(UPDATED_ENTITY_ID);

        restPaymentAuditMockMvc.perform(put("/api/payment-audits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPaymentAudit)))
            .andExpect(status().isOk());

        // Validate the PaymentAudit in the database
        List<PaymentAudit> paymentAuditList = paymentAuditRepository.findAll();
        assertThat(paymentAuditList).hasSize(databaseSizeBeforeUpdate);
        PaymentAudit testPaymentAudit = paymentAuditList.get(paymentAuditList.size() - 1);
        assertThat(testPaymentAudit.getEntityName()).isEqualTo(UPDATED_ENTITY_NAME);
        assertThat(testPaymentAudit.getEntityId()).isEqualTo(UPDATED_ENTITY_ID);

        // Validate the PaymentAudit in Elasticsearch
        PaymentAudit paymentAuditEs = paymentAuditSearchRepository.findOne(testPaymentAudit.getId());
        assertThat(paymentAuditEs).isEqualToIgnoringGivenFields(testPaymentAudit);
    }

    @Test
    @Transactional
    public void updateNonExistingPaymentAudit() throws Exception {
        int databaseSizeBeforeUpdate = paymentAuditRepository.findAll().size();

        // Create the PaymentAudit

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPaymentAuditMockMvc.perform(put("/api/payment-audits")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentAudit)))
            .andExpect(status().isCreated());

        // Validate the PaymentAudit in the database
        List<PaymentAudit> paymentAuditList = paymentAuditRepository.findAll();
        assertThat(paymentAuditList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePaymentAudit() throws Exception {
        // Initialize the database
        paymentAuditRepository.saveAndFlush(paymentAudit);
        paymentAuditSearchRepository.save(paymentAudit);
        int databaseSizeBeforeDelete = paymentAuditRepository.findAll().size();

        // Get the paymentAudit
        restPaymentAuditMockMvc.perform(delete("/api/payment-audits/{id}", paymentAudit.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean paymentAuditExistsInEs = paymentAuditSearchRepository.exists(paymentAudit.getId());
        assertThat(paymentAuditExistsInEs).isFalse();

        // Validate the database is empty
        List<PaymentAudit> paymentAuditList = paymentAuditRepository.findAll();
        assertThat(paymentAuditList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPaymentAudit() throws Exception {
        // Initialize the database
        paymentAuditRepository.saveAndFlush(paymentAudit);
        paymentAuditSearchRepository.save(paymentAudit);

        // Search the paymentAudit
        restPaymentAuditMockMvc.perform(get("/api/_search/payment-audits?query=id:" + paymentAudit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentAudit.getId().intValue())))
            .andExpect(jsonPath("$.[*].entityName").value(hasItem(DEFAULT_ENTITY_NAME.toString())))
            .andExpect(jsonPath("$.[*].entityId").value(hasItem(DEFAULT_ENTITY_ID.intValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentAudit.class);
        PaymentAudit paymentAudit1 = new PaymentAudit();
        paymentAudit1.setId(1L);
        PaymentAudit paymentAudit2 = new PaymentAudit();
        paymentAudit2.setId(paymentAudit1.getId());
        assertThat(paymentAudit1).isEqualTo(paymentAudit2);
        paymentAudit2.setId(2L);
        assertThat(paymentAudit1).isNotEqualTo(paymentAudit2);
        paymentAudit1.setId(null);
        assertThat(paymentAudit1).isNotEqualTo(paymentAudit2);
    }
}

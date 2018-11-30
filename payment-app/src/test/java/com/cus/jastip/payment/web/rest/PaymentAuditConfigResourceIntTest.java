package com.cus.jastip.payment.web.rest;

import com.cus.jastip.payment.PaymentApp;

import com.cus.jastip.payment.config.SecurityBeanOverrideConfiguration;

import com.cus.jastip.payment.domain.PaymentAuditConfig;
import com.cus.jastip.payment.repository.PaymentAuditConfigRepository;
import com.cus.jastip.payment.repository.search.PaymentAuditConfigSearchRepository;
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
 * Test class for the PaymentAuditConfigResource REST controller.
 *
 * @see PaymentAuditConfigResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {PaymentApp.class, SecurityBeanOverrideConfiguration.class})
public class PaymentAuditConfigResourceIntTest {

    private static final String DEFAULT_ENTITY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ENTITY_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE_STATUS = false;
    private static final Boolean UPDATED_ACTIVE_STATUS = true;

    @Autowired
    private PaymentAuditConfigRepository paymentAuditConfigRepository;

    @Autowired
    private PaymentAuditConfigSearchRepository paymentAuditConfigSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPaymentAuditConfigMockMvc;

    private PaymentAuditConfig paymentAuditConfig;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PaymentAuditConfigResource paymentAuditConfigResource = new PaymentAuditConfigResource(paymentAuditConfigRepository, paymentAuditConfigSearchRepository);
        this.restPaymentAuditConfigMockMvc = MockMvcBuilders.standaloneSetup(paymentAuditConfigResource)
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
    public static PaymentAuditConfig createEntity(EntityManager em) {
        PaymentAuditConfig paymentAuditConfig = new PaymentAuditConfig()
            .entityName(DEFAULT_ENTITY_NAME)
            .activeStatus(DEFAULT_ACTIVE_STATUS);
        return paymentAuditConfig;
    }

    @Before
    public void initTest() {
        paymentAuditConfigSearchRepository.deleteAll();
        paymentAuditConfig = createEntity(em);
    }

    @Test
    @Transactional
    public void createPaymentAuditConfig() throws Exception {
        int databaseSizeBeforeCreate = paymentAuditConfigRepository.findAll().size();

        // Create the PaymentAuditConfig
        restPaymentAuditConfigMockMvc.perform(post("/api/payment-audit-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentAuditConfig)))
            .andExpect(status().isCreated());

        // Validate the PaymentAuditConfig in the database
        List<PaymentAuditConfig> paymentAuditConfigList = paymentAuditConfigRepository.findAll();
        assertThat(paymentAuditConfigList).hasSize(databaseSizeBeforeCreate + 1);
        PaymentAuditConfig testPaymentAuditConfig = paymentAuditConfigList.get(paymentAuditConfigList.size() - 1);
        assertThat(testPaymentAuditConfig.getEntityName()).isEqualTo(DEFAULT_ENTITY_NAME);
        assertThat(testPaymentAuditConfig.isActiveStatus()).isEqualTo(DEFAULT_ACTIVE_STATUS);

        // Validate the PaymentAuditConfig in Elasticsearch
        PaymentAuditConfig paymentAuditConfigEs = paymentAuditConfigSearchRepository.findOne(testPaymentAuditConfig.getId());
        assertThat(paymentAuditConfigEs).isEqualToIgnoringGivenFields(testPaymentAuditConfig);
    }

    @Test
    @Transactional
    public void createPaymentAuditConfigWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = paymentAuditConfigRepository.findAll().size();

        // Create the PaymentAuditConfig with an existing ID
        paymentAuditConfig.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentAuditConfigMockMvc.perform(post("/api/payment-audit-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentAuditConfig)))
            .andExpect(status().isBadRequest());

        // Validate the PaymentAuditConfig in the database
        List<PaymentAuditConfig> paymentAuditConfigList = paymentAuditConfigRepository.findAll();
        assertThat(paymentAuditConfigList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkEntityNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentAuditConfigRepository.findAll().size();
        // set the field null
        paymentAuditConfig.setEntityName(null);

        // Create the PaymentAuditConfig, which fails.

        restPaymentAuditConfigMockMvc.perform(post("/api/payment-audit-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentAuditConfig)))
            .andExpect(status().isBadRequest());

        List<PaymentAuditConfig> paymentAuditConfigList = paymentAuditConfigRepository.findAll();
        assertThat(paymentAuditConfigList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPaymentAuditConfigs() throws Exception {
        // Initialize the database
        paymentAuditConfigRepository.saveAndFlush(paymentAuditConfig);

        // Get all the paymentAuditConfigList
        restPaymentAuditConfigMockMvc.perform(get("/api/payment-audit-configs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentAuditConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].entityName").value(hasItem(DEFAULT_ENTITY_NAME.toString())))
            .andExpect(jsonPath("$.[*].activeStatus").value(hasItem(DEFAULT_ACTIVE_STATUS.booleanValue())));
    }

    @Test
    @Transactional
    public void getPaymentAuditConfig() throws Exception {
        // Initialize the database
        paymentAuditConfigRepository.saveAndFlush(paymentAuditConfig);

        // Get the paymentAuditConfig
        restPaymentAuditConfigMockMvc.perform(get("/api/payment-audit-configs/{id}", paymentAuditConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(paymentAuditConfig.getId().intValue()))
            .andExpect(jsonPath("$.entityName").value(DEFAULT_ENTITY_NAME.toString()))
            .andExpect(jsonPath("$.activeStatus").value(DEFAULT_ACTIVE_STATUS.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPaymentAuditConfig() throws Exception {
        // Get the paymentAuditConfig
        restPaymentAuditConfigMockMvc.perform(get("/api/payment-audit-configs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePaymentAuditConfig() throws Exception {
        // Initialize the database
        paymentAuditConfigRepository.saveAndFlush(paymentAuditConfig);
        paymentAuditConfigSearchRepository.save(paymentAuditConfig);
        int databaseSizeBeforeUpdate = paymentAuditConfigRepository.findAll().size();

        // Update the paymentAuditConfig
        PaymentAuditConfig updatedPaymentAuditConfig = paymentAuditConfigRepository.findOne(paymentAuditConfig.getId());
        // Disconnect from session so that the updates on updatedPaymentAuditConfig are not directly saved in db
        em.detach(updatedPaymentAuditConfig);
        updatedPaymentAuditConfig
            .entityName(UPDATED_ENTITY_NAME)
            .activeStatus(UPDATED_ACTIVE_STATUS);

        restPaymentAuditConfigMockMvc.perform(put("/api/payment-audit-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPaymentAuditConfig)))
            .andExpect(status().isOk());

        // Validate the PaymentAuditConfig in the database
        List<PaymentAuditConfig> paymentAuditConfigList = paymentAuditConfigRepository.findAll();
        assertThat(paymentAuditConfigList).hasSize(databaseSizeBeforeUpdate);
        PaymentAuditConfig testPaymentAuditConfig = paymentAuditConfigList.get(paymentAuditConfigList.size() - 1);
        assertThat(testPaymentAuditConfig.getEntityName()).isEqualTo(UPDATED_ENTITY_NAME);
        assertThat(testPaymentAuditConfig.isActiveStatus()).isEqualTo(UPDATED_ACTIVE_STATUS);

        // Validate the PaymentAuditConfig in Elasticsearch
        PaymentAuditConfig paymentAuditConfigEs = paymentAuditConfigSearchRepository.findOne(testPaymentAuditConfig.getId());
        assertThat(paymentAuditConfigEs).isEqualToIgnoringGivenFields(testPaymentAuditConfig);
    }

    @Test
    @Transactional
    public void updateNonExistingPaymentAuditConfig() throws Exception {
        int databaseSizeBeforeUpdate = paymentAuditConfigRepository.findAll().size();

        // Create the PaymentAuditConfig

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPaymentAuditConfigMockMvc.perform(put("/api/payment-audit-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paymentAuditConfig)))
            .andExpect(status().isCreated());

        // Validate the PaymentAuditConfig in the database
        List<PaymentAuditConfig> paymentAuditConfigList = paymentAuditConfigRepository.findAll();
        assertThat(paymentAuditConfigList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePaymentAuditConfig() throws Exception {
        // Initialize the database
        paymentAuditConfigRepository.saveAndFlush(paymentAuditConfig);
        paymentAuditConfigSearchRepository.save(paymentAuditConfig);
        int databaseSizeBeforeDelete = paymentAuditConfigRepository.findAll().size();

        // Get the paymentAuditConfig
        restPaymentAuditConfigMockMvc.perform(delete("/api/payment-audit-configs/{id}", paymentAuditConfig.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean paymentAuditConfigExistsInEs = paymentAuditConfigSearchRepository.exists(paymentAuditConfig.getId());
        assertThat(paymentAuditConfigExistsInEs).isFalse();

        // Validate the database is empty
        List<PaymentAuditConfig> paymentAuditConfigList = paymentAuditConfigRepository.findAll();
        assertThat(paymentAuditConfigList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPaymentAuditConfig() throws Exception {
        // Initialize the database
        paymentAuditConfigRepository.saveAndFlush(paymentAuditConfig);
        paymentAuditConfigSearchRepository.save(paymentAuditConfig);

        // Search the paymentAuditConfig
        restPaymentAuditConfigMockMvc.perform(get("/api/_search/payment-audit-configs?query=id:" + paymentAuditConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paymentAuditConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].entityName").value(hasItem(DEFAULT_ENTITY_NAME.toString())))
            .andExpect(jsonPath("$.[*].activeStatus").value(hasItem(DEFAULT_ACTIVE_STATUS.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentAuditConfig.class);
        PaymentAuditConfig paymentAuditConfig1 = new PaymentAuditConfig();
        paymentAuditConfig1.setId(1L);
        PaymentAuditConfig paymentAuditConfig2 = new PaymentAuditConfig();
        paymentAuditConfig2.setId(paymentAuditConfig1.getId());
        assertThat(paymentAuditConfig1).isEqualTo(paymentAuditConfig2);
        paymentAuditConfig2.setId(2L);
        assertThat(paymentAuditConfig1).isNotEqualTo(paymentAuditConfig2);
        paymentAuditConfig1.setId(null);
        assertThat(paymentAuditConfig1).isNotEqualTo(paymentAuditConfig2);
    }
}

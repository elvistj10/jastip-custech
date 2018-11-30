package com.cus.jastip.master.web.rest;

import com.cus.jastip.master.MasterApp;

import com.cus.jastip.master.config.SecurityBeanOverrideConfiguration;

import com.cus.jastip.master.domain.ServiceFee;
import com.cus.jastip.master.repository.ServiceFeeRepository;
import com.cus.jastip.master.repository.search.ServiceFeeSearchRepository;
import com.cus.jastip.master.web.rest.errors.ExceptionTranslator;

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

import static com.cus.jastip.master.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ServiceFeeResource REST controller.
 *
 * @see ServiceFeeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MasterApp.class, SecurityBeanOverrideConfiguration.class})
public class ServiceFeeResourceIntTest {

    private static final Double DEFAULT_PERCENTAGE_FEE = 1D;
    private static final Double UPDATED_PERCENTAGE_FEE = 2D;

    private static final Double DEFAULT_FIX_NOMINAL_FEE = 1D;
    private static final Double UPDATED_FIX_NOMINAL_FEE = 2D;

    private static final Double DEFAULT_MINIMUM_NOMINAL_FEE = 1D;
    private static final Double UPDATED_MINIMUM_NOMINAL_FEE = 2D;

    private static final Double DEFAULT_MINIMUM_NOMINAL_PRICE = 1D;
    private static final Double UPDATED_MINIMUM_NOMINAL_PRICE = 2D;

    private static final Double DEFAULT_MAXIMUM_NOMINAL_PRICE = 1D;
    private static final Double UPDATED_MAXIMUM_NOMINAL_PRICE = 2D;

    private static final Instant DEFAULT_START_DATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_ACTIVE_STATUS = false;
    private static final Boolean UPDATED_ACTIVE_STATUS = true;

    @Autowired
    private ServiceFeeRepository serviceFeeRepository;

    @Autowired
    private ServiceFeeSearchRepository serviceFeeSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restServiceFeeMockMvc;

    private ServiceFee serviceFee;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ServiceFeeResource serviceFeeResource = new ServiceFeeResource(serviceFeeRepository, serviceFeeSearchRepository);
        this.restServiceFeeMockMvc = MockMvcBuilders.standaloneSetup(serviceFeeResource)
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
    public static ServiceFee createEntity(EntityManager em) {
        ServiceFee serviceFee = new ServiceFee()
            .percentageFee(DEFAULT_PERCENTAGE_FEE)
            .fixNominalFee(DEFAULT_FIX_NOMINAL_FEE)
            .minimumNominalFee(DEFAULT_MINIMUM_NOMINAL_FEE)
            .minimumNominalPrice(DEFAULT_MINIMUM_NOMINAL_PRICE)
            .maximumNominalPrice(DEFAULT_MAXIMUM_NOMINAL_PRICE)
            .startDateTime(DEFAULT_START_DATE_TIME)
            .endDateTime(DEFAULT_END_DATE_TIME)
            .activeStatus(DEFAULT_ACTIVE_STATUS);
        return serviceFee;
    }

    @Before
    public void initTest() {
        serviceFeeSearchRepository.deleteAll();
        serviceFee = createEntity(em);
    }

    @Test
    @Transactional
    public void createServiceFee() throws Exception {
        int databaseSizeBeforeCreate = serviceFeeRepository.findAll().size();

        // Create the ServiceFee
        restServiceFeeMockMvc.perform(post("/api/service-fees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceFee)))
            .andExpect(status().isCreated());

        // Validate the ServiceFee in the database
        List<ServiceFee> serviceFeeList = serviceFeeRepository.findAll();
        assertThat(serviceFeeList).hasSize(databaseSizeBeforeCreate + 1);
        ServiceFee testServiceFee = serviceFeeList.get(serviceFeeList.size() - 1);
        assertThat(testServiceFee.getPercentageFee()).isEqualTo(DEFAULT_PERCENTAGE_FEE);
        assertThat(testServiceFee.getFixNominalFee()).isEqualTo(DEFAULT_FIX_NOMINAL_FEE);
        assertThat(testServiceFee.getMinimumNominalFee()).isEqualTo(DEFAULT_MINIMUM_NOMINAL_FEE);
        assertThat(testServiceFee.getMinimumNominalPrice()).isEqualTo(DEFAULT_MINIMUM_NOMINAL_PRICE);
        assertThat(testServiceFee.getMaximumNominalPrice()).isEqualTo(DEFAULT_MAXIMUM_NOMINAL_PRICE);
        assertThat(testServiceFee.getStartDateTime()).isEqualTo(DEFAULT_START_DATE_TIME);
        assertThat(testServiceFee.getEndDateTime()).isEqualTo(DEFAULT_END_DATE_TIME);
        assertThat(testServiceFee.isActiveStatus()).isEqualTo(DEFAULT_ACTIVE_STATUS);

        // Validate the ServiceFee in Elasticsearch
        ServiceFee serviceFeeEs = serviceFeeSearchRepository.findOne(testServiceFee.getId());
        assertThat(serviceFeeEs).isEqualToIgnoringGivenFields(testServiceFee);
    }

    @Test
    @Transactional
    public void createServiceFeeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = serviceFeeRepository.findAll().size();

        // Create the ServiceFee with an existing ID
        serviceFee.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restServiceFeeMockMvc.perform(post("/api/service-fees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceFee)))
            .andExpect(status().isBadRequest());

        // Validate the ServiceFee in the database
        List<ServiceFee> serviceFeeList = serviceFeeRepository.findAll();
        assertThat(serviceFeeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkStartDateTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = serviceFeeRepository.findAll().size();
        // set the field null
        serviceFee.setStartDateTime(null);

        // Create the ServiceFee, which fails.

        restServiceFeeMockMvc.perform(post("/api/service-fees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceFee)))
            .andExpect(status().isBadRequest());

        List<ServiceFee> serviceFeeList = serviceFeeRepository.findAll();
        assertThat(serviceFeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllServiceFees() throws Exception {
        // Initialize the database
        serviceFeeRepository.saveAndFlush(serviceFee);

        // Get all the serviceFeeList
        restServiceFeeMockMvc.perform(get("/api/service-fees?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceFee.getId().intValue())))
            .andExpect(jsonPath("$.[*].percentageFee").value(hasItem(DEFAULT_PERCENTAGE_FEE.doubleValue())))
            .andExpect(jsonPath("$.[*].fixNominalFee").value(hasItem(DEFAULT_FIX_NOMINAL_FEE.doubleValue())))
            .andExpect(jsonPath("$.[*].minimumNominalFee").value(hasItem(DEFAULT_MINIMUM_NOMINAL_FEE.doubleValue())))
            .andExpect(jsonPath("$.[*].minimumNominalPrice").value(hasItem(DEFAULT_MINIMUM_NOMINAL_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].maximumNominalPrice").value(hasItem(DEFAULT_MAXIMUM_NOMINAL_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].startDateTime").value(hasItem(DEFAULT_START_DATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].endDateTime").value(hasItem(DEFAULT_END_DATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].activeStatus").value(hasItem(DEFAULT_ACTIVE_STATUS.booleanValue())));
    }

    @Test
    @Transactional
    public void getServiceFee() throws Exception {
        // Initialize the database
        serviceFeeRepository.saveAndFlush(serviceFee);

        // Get the serviceFee
        restServiceFeeMockMvc.perform(get("/api/service-fees/{id}", serviceFee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(serviceFee.getId().intValue()))
            .andExpect(jsonPath("$.percentageFee").value(DEFAULT_PERCENTAGE_FEE.doubleValue()))
            .andExpect(jsonPath("$.fixNominalFee").value(DEFAULT_FIX_NOMINAL_FEE.doubleValue()))
            .andExpect(jsonPath("$.minimumNominalFee").value(DEFAULT_MINIMUM_NOMINAL_FEE.doubleValue()))
            .andExpect(jsonPath("$.minimumNominalPrice").value(DEFAULT_MINIMUM_NOMINAL_PRICE.doubleValue()))
            .andExpect(jsonPath("$.maximumNominalPrice").value(DEFAULT_MAXIMUM_NOMINAL_PRICE.doubleValue()))
            .andExpect(jsonPath("$.startDateTime").value(DEFAULT_START_DATE_TIME.toString()))
            .andExpect(jsonPath("$.endDateTime").value(DEFAULT_END_DATE_TIME.toString()))
            .andExpect(jsonPath("$.activeStatus").value(DEFAULT_ACTIVE_STATUS.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingServiceFee() throws Exception {
        // Get the serviceFee
        restServiceFeeMockMvc.perform(get("/api/service-fees/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateServiceFee() throws Exception {
        // Initialize the database
        serviceFeeRepository.saveAndFlush(serviceFee);
        serviceFeeSearchRepository.save(serviceFee);
        int databaseSizeBeforeUpdate = serviceFeeRepository.findAll().size();

        // Update the serviceFee
        ServiceFee updatedServiceFee = serviceFeeRepository.findOne(serviceFee.getId());
        // Disconnect from session so that the updates on updatedServiceFee are not directly saved in db
        em.detach(updatedServiceFee);
        updatedServiceFee
            .percentageFee(UPDATED_PERCENTAGE_FEE)
            .fixNominalFee(UPDATED_FIX_NOMINAL_FEE)
            .minimumNominalFee(UPDATED_MINIMUM_NOMINAL_FEE)
            .minimumNominalPrice(UPDATED_MINIMUM_NOMINAL_PRICE)
            .maximumNominalPrice(UPDATED_MAXIMUM_NOMINAL_PRICE)
            .startDateTime(UPDATED_START_DATE_TIME)
            .endDateTime(UPDATED_END_DATE_TIME)
            .activeStatus(UPDATED_ACTIVE_STATUS);

        restServiceFeeMockMvc.perform(put("/api/service-fees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedServiceFee)))
            .andExpect(status().isOk());

        // Validate the ServiceFee in the database
        List<ServiceFee> serviceFeeList = serviceFeeRepository.findAll();
        assertThat(serviceFeeList).hasSize(databaseSizeBeforeUpdate);
        ServiceFee testServiceFee = serviceFeeList.get(serviceFeeList.size() - 1);
        assertThat(testServiceFee.getPercentageFee()).isEqualTo(UPDATED_PERCENTAGE_FEE);
        assertThat(testServiceFee.getFixNominalFee()).isEqualTo(UPDATED_FIX_NOMINAL_FEE);
        assertThat(testServiceFee.getMinimumNominalFee()).isEqualTo(UPDATED_MINIMUM_NOMINAL_FEE);
        assertThat(testServiceFee.getMinimumNominalPrice()).isEqualTo(UPDATED_MINIMUM_NOMINAL_PRICE);
        assertThat(testServiceFee.getMaximumNominalPrice()).isEqualTo(UPDATED_MAXIMUM_NOMINAL_PRICE);
        assertThat(testServiceFee.getStartDateTime()).isEqualTo(UPDATED_START_DATE_TIME);
        assertThat(testServiceFee.getEndDateTime()).isEqualTo(UPDATED_END_DATE_TIME);
        assertThat(testServiceFee.isActiveStatus()).isEqualTo(UPDATED_ACTIVE_STATUS);

        // Validate the ServiceFee in Elasticsearch
        ServiceFee serviceFeeEs = serviceFeeSearchRepository.findOne(testServiceFee.getId());
        assertThat(serviceFeeEs).isEqualToIgnoringGivenFields(testServiceFee);
    }

    @Test
    @Transactional
    public void updateNonExistingServiceFee() throws Exception {
        int databaseSizeBeforeUpdate = serviceFeeRepository.findAll().size();

        // Create the ServiceFee

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restServiceFeeMockMvc.perform(put("/api/service-fees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serviceFee)))
            .andExpect(status().isCreated());

        // Validate the ServiceFee in the database
        List<ServiceFee> serviceFeeList = serviceFeeRepository.findAll();
        assertThat(serviceFeeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteServiceFee() throws Exception {
        // Initialize the database
        serviceFeeRepository.saveAndFlush(serviceFee);
        serviceFeeSearchRepository.save(serviceFee);
        int databaseSizeBeforeDelete = serviceFeeRepository.findAll().size();

        // Get the serviceFee
        restServiceFeeMockMvc.perform(delete("/api/service-fees/{id}", serviceFee.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean serviceFeeExistsInEs = serviceFeeSearchRepository.exists(serviceFee.getId());
        assertThat(serviceFeeExistsInEs).isFalse();

        // Validate the database is empty
        List<ServiceFee> serviceFeeList = serviceFeeRepository.findAll();
        assertThat(serviceFeeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchServiceFee() throws Exception {
        // Initialize the database
        serviceFeeRepository.saveAndFlush(serviceFee);
        serviceFeeSearchRepository.save(serviceFee);

        // Search the serviceFee
        restServiceFeeMockMvc.perform(get("/api/_search/service-fees?query=id:" + serviceFee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceFee.getId().intValue())))
            .andExpect(jsonPath("$.[*].percentageFee").value(hasItem(DEFAULT_PERCENTAGE_FEE.doubleValue())))
            .andExpect(jsonPath("$.[*].fixNominalFee").value(hasItem(DEFAULT_FIX_NOMINAL_FEE.doubleValue())))
            .andExpect(jsonPath("$.[*].minimumNominalFee").value(hasItem(DEFAULT_MINIMUM_NOMINAL_FEE.doubleValue())))
            .andExpect(jsonPath("$.[*].minimumNominalPrice").value(hasItem(DEFAULT_MINIMUM_NOMINAL_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].maximumNominalPrice").value(hasItem(DEFAULT_MAXIMUM_NOMINAL_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].startDateTime").value(hasItem(DEFAULT_START_DATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].endDateTime").value(hasItem(DEFAULT_END_DATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].activeStatus").value(hasItem(DEFAULT_ACTIVE_STATUS.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServiceFee.class);
        ServiceFee serviceFee1 = new ServiceFee();
        serviceFee1.setId(1L);
        ServiceFee serviceFee2 = new ServiceFee();
        serviceFee2.setId(serviceFee1.getId());
        assertThat(serviceFee1).isEqualTo(serviceFee2);
        serviceFee2.setId(2L);
        assertThat(serviceFee1).isNotEqualTo(serviceFee2);
        serviceFee1.setId(null);
        assertThat(serviceFee1).isNotEqualTo(serviceFee2);
    }
}

package com.cus.jastip.master.web.rest;

import com.cus.jastip.master.MasterApp;

import com.cus.jastip.master.config.SecurityBeanOverrideConfiguration;

import com.cus.jastip.master.domain.BusinessAccount;
import com.cus.jastip.master.repository.BusinessAccountRepository;
import com.cus.jastip.master.repository.search.BusinessAccountSearchRepository;
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
import java.util.List;

import static com.cus.jastip.master.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the BusinessAccountResource REST controller.
 *
 * @see BusinessAccountResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MasterApp.class, SecurityBeanOverrideConfiguration.class})
public class BusinessAccountResourceIntTest {

    private static final String DEFAULT_COORPERATE_ID = "AAAAAAAAAA";
    private static final String UPDATED_COORPERATE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_ACCOUNT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_BANK_NAME = "AAAAAAAAAA";
    private static final String UPDATED_BANK_NAME = "BBBBBBBBBB";

    @Autowired
    private BusinessAccountRepository businessAccountRepository;

    @Autowired
    private BusinessAccountSearchRepository businessAccountSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBusinessAccountMockMvc;

    private BusinessAccount businessAccount;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BusinessAccountResource businessAccountResource = new BusinessAccountResource(businessAccountRepository, businessAccountSearchRepository);
        this.restBusinessAccountMockMvc = MockMvcBuilders.standaloneSetup(businessAccountResource)
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
    public static BusinessAccount createEntity(EntityManager em) {
        BusinessAccount businessAccount = new BusinessAccount()
            .coorperateId(DEFAULT_COORPERATE_ID)
            .accountNumber(DEFAULT_ACCOUNT_NUMBER)
            .bankName(DEFAULT_BANK_NAME);
        return businessAccount;
    }

    @Before
    public void initTest() {
        businessAccountSearchRepository.deleteAll();
        businessAccount = createEntity(em);
    }

    @Test
    @Transactional
    public void createBusinessAccount() throws Exception {
        int databaseSizeBeforeCreate = businessAccountRepository.findAll().size();

        // Create the BusinessAccount
        restBusinessAccountMockMvc.perform(post("/api/business-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(businessAccount)))
            .andExpect(status().isCreated());

        // Validate the BusinessAccount in the database
        List<BusinessAccount> businessAccountList = businessAccountRepository.findAll();
        assertThat(businessAccountList).hasSize(databaseSizeBeforeCreate + 1);
        BusinessAccount testBusinessAccount = businessAccountList.get(businessAccountList.size() - 1);
        assertThat(testBusinessAccount.getCoorperateId()).isEqualTo(DEFAULT_COORPERATE_ID);
        assertThat(testBusinessAccount.getAccountNumber()).isEqualTo(DEFAULT_ACCOUNT_NUMBER);
        assertThat(testBusinessAccount.getBankName()).isEqualTo(DEFAULT_BANK_NAME);

        // Validate the BusinessAccount in Elasticsearch
        BusinessAccount businessAccountEs = businessAccountSearchRepository.findOne(testBusinessAccount.getId());
        assertThat(businessAccountEs).isEqualToIgnoringGivenFields(testBusinessAccount);
    }

    @Test
    @Transactional
    public void createBusinessAccountWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = businessAccountRepository.findAll().size();

        // Create the BusinessAccount with an existing ID
        businessAccount.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBusinessAccountMockMvc.perform(post("/api/business-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(businessAccount)))
            .andExpect(status().isBadRequest());

        // Validate the BusinessAccount in the database
        List<BusinessAccount> businessAccountList = businessAccountRepository.findAll();
        assertThat(businessAccountList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCoorperateIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = businessAccountRepository.findAll().size();
        // set the field null
        businessAccount.setCoorperateId(null);

        // Create the BusinessAccount, which fails.

        restBusinessAccountMockMvc.perform(post("/api/business-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(businessAccount)))
            .andExpect(status().isBadRequest());

        List<BusinessAccount> businessAccountList = businessAccountRepository.findAll();
        assertThat(businessAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAccountNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = businessAccountRepository.findAll().size();
        // set the field null
        businessAccount.setAccountNumber(null);

        // Create the BusinessAccount, which fails.

        restBusinessAccountMockMvc.perform(post("/api/business-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(businessAccount)))
            .andExpect(status().isBadRequest());

        List<BusinessAccount> businessAccountList = businessAccountRepository.findAll();
        assertThat(businessAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBusinessAccounts() throws Exception {
        // Initialize the database
        businessAccountRepository.saveAndFlush(businessAccount);

        // Get all the businessAccountList
        restBusinessAccountMockMvc.perform(get("/api/business-accounts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(businessAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].coorperateId").value(hasItem(DEFAULT_COORPERATE_ID.toString())))
            .andExpect(jsonPath("$.[*].accountNumber").value(hasItem(DEFAULT_ACCOUNT_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].bankName").value(hasItem(DEFAULT_BANK_NAME.toString())));
    }

    @Test
    @Transactional
    public void getBusinessAccount() throws Exception {
        // Initialize the database
        businessAccountRepository.saveAndFlush(businessAccount);

        // Get the businessAccount
        restBusinessAccountMockMvc.perform(get("/api/business-accounts/{id}", businessAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(businessAccount.getId().intValue()))
            .andExpect(jsonPath("$.coorperateId").value(DEFAULT_COORPERATE_ID.toString()))
            .andExpect(jsonPath("$.accountNumber").value(DEFAULT_ACCOUNT_NUMBER.toString()))
            .andExpect(jsonPath("$.bankName").value(DEFAULT_BANK_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBusinessAccount() throws Exception {
        // Get the businessAccount
        restBusinessAccountMockMvc.perform(get("/api/business-accounts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBusinessAccount() throws Exception {
        // Initialize the database
        businessAccountRepository.saveAndFlush(businessAccount);
        businessAccountSearchRepository.save(businessAccount);
        int databaseSizeBeforeUpdate = businessAccountRepository.findAll().size();

        // Update the businessAccount
        BusinessAccount updatedBusinessAccount = businessAccountRepository.findOne(businessAccount.getId());
        // Disconnect from session so that the updates on updatedBusinessAccount are not directly saved in db
        em.detach(updatedBusinessAccount);
        updatedBusinessAccount
            .coorperateId(UPDATED_COORPERATE_ID)
            .accountNumber(UPDATED_ACCOUNT_NUMBER)
            .bankName(UPDATED_BANK_NAME);

        restBusinessAccountMockMvc.perform(put("/api/business-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBusinessAccount)))
            .andExpect(status().isOk());

        // Validate the BusinessAccount in the database
        List<BusinessAccount> businessAccountList = businessAccountRepository.findAll();
        assertThat(businessAccountList).hasSize(databaseSizeBeforeUpdate);
        BusinessAccount testBusinessAccount = businessAccountList.get(businessAccountList.size() - 1);
        assertThat(testBusinessAccount.getCoorperateId()).isEqualTo(UPDATED_COORPERATE_ID);
        assertThat(testBusinessAccount.getAccountNumber()).isEqualTo(UPDATED_ACCOUNT_NUMBER);
        assertThat(testBusinessAccount.getBankName()).isEqualTo(UPDATED_BANK_NAME);

        // Validate the BusinessAccount in Elasticsearch
        BusinessAccount businessAccountEs = businessAccountSearchRepository.findOne(testBusinessAccount.getId());
        assertThat(businessAccountEs).isEqualToIgnoringGivenFields(testBusinessAccount);
    }

    @Test
    @Transactional
    public void updateNonExistingBusinessAccount() throws Exception {
        int databaseSizeBeforeUpdate = businessAccountRepository.findAll().size();

        // Create the BusinessAccount

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBusinessAccountMockMvc.perform(put("/api/business-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(businessAccount)))
            .andExpect(status().isCreated());

        // Validate the BusinessAccount in the database
        List<BusinessAccount> businessAccountList = businessAccountRepository.findAll();
        assertThat(businessAccountList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBusinessAccount() throws Exception {
        // Initialize the database
        businessAccountRepository.saveAndFlush(businessAccount);
        businessAccountSearchRepository.save(businessAccount);
        int databaseSizeBeforeDelete = businessAccountRepository.findAll().size();

        // Get the businessAccount
        restBusinessAccountMockMvc.perform(delete("/api/business-accounts/{id}", businessAccount.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean businessAccountExistsInEs = businessAccountSearchRepository.exists(businessAccount.getId());
        assertThat(businessAccountExistsInEs).isFalse();

        // Validate the database is empty
        List<BusinessAccount> businessAccountList = businessAccountRepository.findAll();
        assertThat(businessAccountList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchBusinessAccount() throws Exception {
        // Initialize the database
        businessAccountRepository.saveAndFlush(businessAccount);
        businessAccountSearchRepository.save(businessAccount);

        // Search the businessAccount
        restBusinessAccountMockMvc.perform(get("/api/_search/business-accounts?query=id:" + businessAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(businessAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].coorperateId").value(hasItem(DEFAULT_COORPERATE_ID.toString())))
            .andExpect(jsonPath("$.[*].accountNumber").value(hasItem(DEFAULT_ACCOUNT_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].bankName").value(hasItem(DEFAULT_BANK_NAME.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BusinessAccount.class);
        BusinessAccount businessAccount1 = new BusinessAccount();
        businessAccount1.setId(1L);
        BusinessAccount businessAccount2 = new BusinessAccount();
        businessAccount2.setId(businessAccount1.getId());
        assertThat(businessAccount1).isEqualTo(businessAccount2);
        businessAccount2.setId(2L);
        assertThat(businessAccount1).isNotEqualTo(businessAccount2);
        businessAccount1.setId(null);
        assertThat(businessAccount1).isNotEqualTo(businessAccount2);
    }
}

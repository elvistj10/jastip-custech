package com.cus.jastip.wallet.web.rest;

import com.cus.jastip.wallet.WalletApp;

import com.cus.jastip.wallet.config.SecurityBeanOverrideConfiguration;

import com.cus.jastip.wallet.domain.WithdrawalTransferList;
import com.cus.jastip.wallet.repository.WithdrawalTransferListRepository;
import com.cus.jastip.wallet.repository.search.WithdrawalTransferListSearchRepository;
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
 * Test class for the WithdrawalTransferListResource REST controller.
 *
 * @see WithdrawalTransferListResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {WalletApp.class, SecurityBeanOverrideConfiguration.class})
public class WithdrawalTransferListResourceIntTest {

    private static final Long DEFAULT_WITHDRAWAL_ID = 1L;
    private static final Long UPDATED_WITHDRAWAL_ID = 2L;

    private static final Double DEFAULT_NOMINAL = 1D;
    private static final Double UPDATED_NOMINAL = 2D;

    private static final String DEFAULT_DEST_BANK_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DEST_BANK_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DEST_BANK_ACCOUNT = "AAAAAAAAAA";
    private static final String UPDATED_DEST_BANK_ACCOUNT = "BBBBBBBBBB";

    @Autowired
    private WithdrawalTransferListRepository withdrawalTransferListRepository;

    @Autowired
    private WithdrawalTransferListSearchRepository withdrawalTransferListSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restWithdrawalTransferListMockMvc;

    private WithdrawalTransferList withdrawalTransferList;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final WithdrawalTransferListResource withdrawalTransferListResource = new WithdrawalTransferListResource(withdrawalTransferListRepository, withdrawalTransferListSearchRepository);
        this.restWithdrawalTransferListMockMvc = MockMvcBuilders.standaloneSetup(withdrawalTransferListResource)
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
    public static WithdrawalTransferList createEntity(EntityManager em) {
        WithdrawalTransferList withdrawalTransferList = new WithdrawalTransferList()
            .withdrawalId(DEFAULT_WITHDRAWAL_ID)
            .nominal(DEFAULT_NOMINAL)
            .destBankName(DEFAULT_DEST_BANK_NAME)
            .destBankAccount(DEFAULT_DEST_BANK_ACCOUNT);
        return withdrawalTransferList;
    }

    @Before
    public void initTest() {
        withdrawalTransferListSearchRepository.deleteAll();
        withdrawalTransferList = createEntity(em);
    }

    @Test
    @Transactional
    public void createWithdrawalTransferList() throws Exception {
        int databaseSizeBeforeCreate = withdrawalTransferListRepository.findAll().size();

        // Create the WithdrawalTransferList
        restWithdrawalTransferListMockMvc.perform(post("/api/withdrawal-transfer-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(withdrawalTransferList)))
            .andExpect(status().isCreated());

        // Validate the WithdrawalTransferList in the database
        List<WithdrawalTransferList> withdrawalTransferListList = withdrawalTransferListRepository.findAll();
        assertThat(withdrawalTransferListList).hasSize(databaseSizeBeforeCreate + 1);
        WithdrawalTransferList testWithdrawalTransferList = withdrawalTransferListList.get(withdrawalTransferListList.size() - 1);
        assertThat(testWithdrawalTransferList.getWithdrawalId()).isEqualTo(DEFAULT_WITHDRAWAL_ID);
        assertThat(testWithdrawalTransferList.getNominal()).isEqualTo(DEFAULT_NOMINAL);
        assertThat(testWithdrawalTransferList.getDestBankName()).isEqualTo(DEFAULT_DEST_BANK_NAME);
        assertThat(testWithdrawalTransferList.getDestBankAccount()).isEqualTo(DEFAULT_DEST_BANK_ACCOUNT);

        // Validate the WithdrawalTransferList in Elasticsearch
        WithdrawalTransferList withdrawalTransferListEs = withdrawalTransferListSearchRepository.findOne(testWithdrawalTransferList.getId());
        assertThat(withdrawalTransferListEs).isEqualToIgnoringGivenFields(testWithdrawalTransferList);
    }

    @Test
    @Transactional
    public void createWithdrawalTransferListWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = withdrawalTransferListRepository.findAll().size();

        // Create the WithdrawalTransferList with an existing ID
        withdrawalTransferList.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWithdrawalTransferListMockMvc.perform(post("/api/withdrawal-transfer-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(withdrawalTransferList)))
            .andExpect(status().isBadRequest());

        // Validate the WithdrawalTransferList in the database
        List<WithdrawalTransferList> withdrawalTransferListList = withdrawalTransferListRepository.findAll();
        assertThat(withdrawalTransferListList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllWithdrawalTransferLists() throws Exception {
        // Initialize the database
        withdrawalTransferListRepository.saveAndFlush(withdrawalTransferList);

        // Get all the withdrawalTransferListList
        restWithdrawalTransferListMockMvc.perform(get("/api/withdrawal-transfer-lists?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(withdrawalTransferList.getId().intValue())))
            .andExpect(jsonPath("$.[*].withdrawalId").value(hasItem(DEFAULT_WITHDRAWAL_ID.intValue())))
            .andExpect(jsonPath("$.[*].nominal").value(hasItem(DEFAULT_NOMINAL.doubleValue())))
            .andExpect(jsonPath("$.[*].destBankName").value(hasItem(DEFAULT_DEST_BANK_NAME.toString())))
            .andExpect(jsonPath("$.[*].destBankAccount").value(hasItem(DEFAULT_DEST_BANK_ACCOUNT.toString())));
    }

    @Test
    @Transactional
    public void getWithdrawalTransferList() throws Exception {
        // Initialize the database
        withdrawalTransferListRepository.saveAndFlush(withdrawalTransferList);

        // Get the withdrawalTransferList
        restWithdrawalTransferListMockMvc.perform(get("/api/withdrawal-transfer-lists/{id}", withdrawalTransferList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(withdrawalTransferList.getId().intValue()))
            .andExpect(jsonPath("$.withdrawalId").value(DEFAULT_WITHDRAWAL_ID.intValue()))
            .andExpect(jsonPath("$.nominal").value(DEFAULT_NOMINAL.doubleValue()))
            .andExpect(jsonPath("$.destBankName").value(DEFAULT_DEST_BANK_NAME.toString()))
            .andExpect(jsonPath("$.destBankAccount").value(DEFAULT_DEST_BANK_ACCOUNT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingWithdrawalTransferList() throws Exception {
        // Get the withdrawalTransferList
        restWithdrawalTransferListMockMvc.perform(get("/api/withdrawal-transfer-lists/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWithdrawalTransferList() throws Exception {
        // Initialize the database
        withdrawalTransferListRepository.saveAndFlush(withdrawalTransferList);
        withdrawalTransferListSearchRepository.save(withdrawalTransferList);
        int databaseSizeBeforeUpdate = withdrawalTransferListRepository.findAll().size();

        // Update the withdrawalTransferList
        WithdrawalTransferList updatedWithdrawalTransferList = withdrawalTransferListRepository.findOne(withdrawalTransferList.getId());
        // Disconnect from session so that the updates on updatedWithdrawalTransferList are not directly saved in db
        em.detach(updatedWithdrawalTransferList);
        updatedWithdrawalTransferList
            .withdrawalId(UPDATED_WITHDRAWAL_ID)
            .nominal(UPDATED_NOMINAL)
            .destBankName(UPDATED_DEST_BANK_NAME)
            .destBankAccount(UPDATED_DEST_BANK_ACCOUNT);

        restWithdrawalTransferListMockMvc.perform(put("/api/withdrawal-transfer-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedWithdrawalTransferList)))
            .andExpect(status().isOk());

        // Validate the WithdrawalTransferList in the database
        List<WithdrawalTransferList> withdrawalTransferListList = withdrawalTransferListRepository.findAll();
        assertThat(withdrawalTransferListList).hasSize(databaseSizeBeforeUpdate);
        WithdrawalTransferList testWithdrawalTransferList = withdrawalTransferListList.get(withdrawalTransferListList.size() - 1);
        assertThat(testWithdrawalTransferList.getWithdrawalId()).isEqualTo(UPDATED_WITHDRAWAL_ID);
        assertThat(testWithdrawalTransferList.getNominal()).isEqualTo(UPDATED_NOMINAL);
        assertThat(testWithdrawalTransferList.getDestBankName()).isEqualTo(UPDATED_DEST_BANK_NAME);
        assertThat(testWithdrawalTransferList.getDestBankAccount()).isEqualTo(UPDATED_DEST_BANK_ACCOUNT);

        // Validate the WithdrawalTransferList in Elasticsearch
        WithdrawalTransferList withdrawalTransferListEs = withdrawalTransferListSearchRepository.findOne(testWithdrawalTransferList.getId());
        assertThat(withdrawalTransferListEs).isEqualToIgnoringGivenFields(testWithdrawalTransferList);
    }

    @Test
    @Transactional
    public void updateNonExistingWithdrawalTransferList() throws Exception {
        int databaseSizeBeforeUpdate = withdrawalTransferListRepository.findAll().size();

        // Create the WithdrawalTransferList

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restWithdrawalTransferListMockMvc.perform(put("/api/withdrawal-transfer-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(withdrawalTransferList)))
            .andExpect(status().isCreated());

        // Validate the WithdrawalTransferList in the database
        List<WithdrawalTransferList> withdrawalTransferListList = withdrawalTransferListRepository.findAll();
        assertThat(withdrawalTransferListList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteWithdrawalTransferList() throws Exception {
        // Initialize the database
        withdrawalTransferListRepository.saveAndFlush(withdrawalTransferList);
        withdrawalTransferListSearchRepository.save(withdrawalTransferList);
        int databaseSizeBeforeDelete = withdrawalTransferListRepository.findAll().size();

        // Get the withdrawalTransferList
        restWithdrawalTransferListMockMvc.perform(delete("/api/withdrawal-transfer-lists/{id}", withdrawalTransferList.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean withdrawalTransferListExistsInEs = withdrawalTransferListSearchRepository.exists(withdrawalTransferList.getId());
        assertThat(withdrawalTransferListExistsInEs).isFalse();

        // Validate the database is empty
        List<WithdrawalTransferList> withdrawalTransferListList = withdrawalTransferListRepository.findAll();
        assertThat(withdrawalTransferListList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchWithdrawalTransferList() throws Exception {
        // Initialize the database
        withdrawalTransferListRepository.saveAndFlush(withdrawalTransferList);
        withdrawalTransferListSearchRepository.save(withdrawalTransferList);

        // Search the withdrawalTransferList
        restWithdrawalTransferListMockMvc.perform(get("/api/_search/withdrawal-transfer-lists?query=id:" + withdrawalTransferList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(withdrawalTransferList.getId().intValue())))
            .andExpect(jsonPath("$.[*].withdrawalId").value(hasItem(DEFAULT_WITHDRAWAL_ID.intValue())))
            .andExpect(jsonPath("$.[*].nominal").value(hasItem(DEFAULT_NOMINAL.doubleValue())))
            .andExpect(jsonPath("$.[*].destBankName").value(hasItem(DEFAULT_DEST_BANK_NAME.toString())))
            .andExpect(jsonPath("$.[*].destBankAccount").value(hasItem(DEFAULT_DEST_BANK_ACCOUNT.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WithdrawalTransferList.class);
        WithdrawalTransferList withdrawalTransferList1 = new WithdrawalTransferList();
        withdrawalTransferList1.setId(1L);
        WithdrawalTransferList withdrawalTransferList2 = new WithdrawalTransferList();
        withdrawalTransferList2.setId(withdrawalTransferList1.getId());
        assertThat(withdrawalTransferList1).isEqualTo(withdrawalTransferList2);
        withdrawalTransferList2.setId(2L);
        assertThat(withdrawalTransferList1).isNotEqualTo(withdrawalTransferList2);
        withdrawalTransferList1.setId(null);
        assertThat(withdrawalTransferList1).isNotEqualTo(withdrawalTransferList2);
    }
}

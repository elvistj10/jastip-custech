package com.cus.jastip.master.web.rest;

import com.cus.jastip.master.MasterApp;

import com.cus.jastip.master.config.SecurityBeanOverrideConfiguration;

import com.cus.jastip.master.domain.PostalCode;
import com.cus.jastip.master.repository.PostalCodeRepository;
import com.cus.jastip.master.repository.search.PostalCodeSearchRepository;
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
 * Test class for the PostalCodeResource REST controller.
 *
 * @see PostalCodeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MasterApp.class, SecurityBeanOverrideConfiguration.class})
public class PostalCodeResourceIntTest {

    private static final String DEFAULT_POSTAL_CODE = "AAAAAAAAAA";
    private static final String UPDATED_POSTAL_CODE = "BBBBBBBBBB";

    @Autowired
    private PostalCodeRepository postalCodeRepository;

    @Autowired
    private PostalCodeSearchRepository postalCodeSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPostalCodeMockMvc;

    private PostalCode postalCode;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PostalCodeResource postalCodeResource = new PostalCodeResource(postalCodeRepository, postalCodeSearchRepository);
        this.restPostalCodeMockMvc = MockMvcBuilders.standaloneSetup(postalCodeResource)
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
    public static PostalCode createEntity(EntityManager em) {
        PostalCode postalCode = new PostalCode()
            .postalCode(DEFAULT_POSTAL_CODE);
        return postalCode;
    }

    @Before
    public void initTest() {
        postalCodeSearchRepository.deleteAll();
        postalCode = createEntity(em);
    }

    @Test
    @Transactional
    public void createPostalCode() throws Exception {
        int databaseSizeBeforeCreate = postalCodeRepository.findAll().size();

        // Create the PostalCode
        restPostalCodeMockMvc.perform(post("/api/postal-codes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(postalCode)))
            .andExpect(status().isCreated());

        // Validate the PostalCode in the database
        List<PostalCode> postalCodeList = postalCodeRepository.findAll();
        assertThat(postalCodeList).hasSize(databaseSizeBeforeCreate + 1);
        PostalCode testPostalCode = postalCodeList.get(postalCodeList.size() - 1);
        assertThat(testPostalCode.getPostalCode()).isEqualTo(DEFAULT_POSTAL_CODE);

        // Validate the PostalCode in Elasticsearch
        PostalCode postalCodeEs = postalCodeSearchRepository.findOne(testPostalCode.getId());
        assertThat(postalCodeEs).isEqualToIgnoringGivenFields(testPostalCode);
    }

    @Test
    @Transactional
    public void createPostalCodeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = postalCodeRepository.findAll().size();

        // Create the PostalCode with an existing ID
        postalCode.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPostalCodeMockMvc.perform(post("/api/postal-codes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(postalCode)))
            .andExpect(status().isBadRequest());

        // Validate the PostalCode in the database
        List<PostalCode> postalCodeList = postalCodeRepository.findAll();
        assertThat(postalCodeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkPostalCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = postalCodeRepository.findAll().size();
        // set the field null
        postalCode.setPostalCode(null);

        // Create the PostalCode, which fails.

        restPostalCodeMockMvc.perform(post("/api/postal-codes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(postalCode)))
            .andExpect(status().isBadRequest());

        List<PostalCode> postalCodeList = postalCodeRepository.findAll();
        assertThat(postalCodeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPostalCodes() throws Exception {
        // Initialize the database
        postalCodeRepository.saveAndFlush(postalCode);

        // Get all the postalCodeList
        restPostalCodeMockMvc.perform(get("/api/postal-codes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(postalCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE.toString())));
    }

    @Test
    @Transactional
    public void getPostalCode() throws Exception {
        // Initialize the database
        postalCodeRepository.saveAndFlush(postalCode);

        // Get the postalCode
        restPostalCodeMockMvc.perform(get("/api/postal-codes/{id}", postalCode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(postalCode.getId().intValue()))
            .andExpect(jsonPath("$.postalCode").value(DEFAULT_POSTAL_CODE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPostalCode() throws Exception {
        // Get the postalCode
        restPostalCodeMockMvc.perform(get("/api/postal-codes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePostalCode() throws Exception {
        // Initialize the database
        postalCodeRepository.saveAndFlush(postalCode);
        postalCodeSearchRepository.save(postalCode);
        int databaseSizeBeforeUpdate = postalCodeRepository.findAll().size();

        // Update the postalCode
        PostalCode updatedPostalCode = postalCodeRepository.findOne(postalCode.getId());
        // Disconnect from session so that the updates on updatedPostalCode are not directly saved in db
        em.detach(updatedPostalCode);
        updatedPostalCode
            .postalCode(UPDATED_POSTAL_CODE);

        restPostalCodeMockMvc.perform(put("/api/postal-codes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPostalCode)))
            .andExpect(status().isOk());

        // Validate the PostalCode in the database
        List<PostalCode> postalCodeList = postalCodeRepository.findAll();
        assertThat(postalCodeList).hasSize(databaseSizeBeforeUpdate);
        PostalCode testPostalCode = postalCodeList.get(postalCodeList.size() - 1);
        assertThat(testPostalCode.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);

        // Validate the PostalCode in Elasticsearch
        PostalCode postalCodeEs = postalCodeSearchRepository.findOne(testPostalCode.getId());
        assertThat(postalCodeEs).isEqualToIgnoringGivenFields(testPostalCode);
    }

    @Test
    @Transactional
    public void updateNonExistingPostalCode() throws Exception {
        int databaseSizeBeforeUpdate = postalCodeRepository.findAll().size();

        // Create the PostalCode

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPostalCodeMockMvc.perform(put("/api/postal-codes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(postalCode)))
            .andExpect(status().isCreated());

        // Validate the PostalCode in the database
        List<PostalCode> postalCodeList = postalCodeRepository.findAll();
        assertThat(postalCodeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePostalCode() throws Exception {
        // Initialize the database
        postalCodeRepository.saveAndFlush(postalCode);
        postalCodeSearchRepository.save(postalCode);
        int databaseSizeBeforeDelete = postalCodeRepository.findAll().size();

        // Get the postalCode
        restPostalCodeMockMvc.perform(delete("/api/postal-codes/{id}", postalCode.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean postalCodeExistsInEs = postalCodeSearchRepository.exists(postalCode.getId());
        assertThat(postalCodeExistsInEs).isFalse();

        // Validate the database is empty
        List<PostalCode> postalCodeList = postalCodeRepository.findAll();
        assertThat(postalCodeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPostalCode() throws Exception {
        // Initialize the database
        postalCodeRepository.saveAndFlush(postalCode);
        postalCodeSearchRepository.save(postalCode);

        // Search the postalCode
        restPostalCodeMockMvc.perform(get("/api/_search/postal-codes?query=id:" + postalCode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(postalCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PostalCode.class);
        PostalCode postalCode1 = new PostalCode();
        postalCode1.setId(1L);
        PostalCode postalCode2 = new PostalCode();
        postalCode2.setId(postalCode1.getId());
        assertThat(postalCode1).isEqualTo(postalCode2);
        postalCode2.setId(2L);
        assertThat(postalCode1).isNotEqualTo(postalCode2);
        postalCode1.setId(null);
        assertThat(postalCode1).isNotEqualTo(postalCode2);
    }
}

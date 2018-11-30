package com.cus.jastip.master.web.rest;

import com.cus.jastip.master.MasterApp;

import com.cus.jastip.master.config.SecurityBeanOverrideConfiguration;

import com.cus.jastip.master.domain.PackageSize;
import com.cus.jastip.master.repository.PackageSizeRepository;
import com.cus.jastip.master.repository.search.PackageSizeSearchRepository;
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
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.util.List;

import static com.cus.jastip.master.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PackageSizeResource REST controller.
 *
 * @see PackageSizeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MasterApp.class, SecurityBeanOverrideConfiguration.class})
public class PackageSizeResourceIntTest {

    private static final String DEFAULT_PACKAGE_SIZE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PACKAGE_SIZE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PACKAGE_SIZE_DESCIPTION = "AAAAAAAAAA";
    private static final String UPDATED_PACKAGE_SIZE_DESCIPTION = "BBBBBBBBBB";

    private static final byte[] DEFAULT_PACKAGE_SIZE_ICON = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PACKAGE_SIZE_ICON = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_PACKAGE_SIZE_ICON_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PACKAGE_SIZE_ICON_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_PACKAGE_SIZE_ICON_URL = "AAAAAAAAAA";
    private static final String UPDATED_PACKAGE_SIZE_ICON_URL = "BBBBBBBBBB";

    @Autowired
    private PackageSizeRepository packageSizeRepository;

    @Autowired
    private PackageSizeSearchRepository packageSizeSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPackageSizeMockMvc;

    private PackageSize packageSize;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PackageSizeResource packageSizeResource = new PackageSizeResource(packageSizeRepository, packageSizeSearchRepository);
        this.restPackageSizeMockMvc = MockMvcBuilders.standaloneSetup(packageSizeResource)
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
    public static PackageSize createEntity(EntityManager em) {
        PackageSize packageSize = new PackageSize()
            .packageSizeName(DEFAULT_PACKAGE_SIZE_NAME)
            .packageSizeDesciption(DEFAULT_PACKAGE_SIZE_DESCIPTION)
            .packageSizeIcon(DEFAULT_PACKAGE_SIZE_ICON)
            .packageSizeIconContentType(DEFAULT_PACKAGE_SIZE_ICON_CONTENT_TYPE)
            .packageSizeIconUrl(DEFAULT_PACKAGE_SIZE_ICON_URL);
        return packageSize;
    }

    @Before
    public void initTest() {
        packageSizeSearchRepository.deleteAll();
        packageSize = createEntity(em);
    }

    @Test
    @Transactional
    public void createPackageSize() throws Exception {
        int databaseSizeBeforeCreate = packageSizeRepository.findAll().size();

        // Create the PackageSize
        restPackageSizeMockMvc.perform(post("/api/package-sizes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(packageSize)))
            .andExpect(status().isCreated());

        // Validate the PackageSize in the database
        List<PackageSize> packageSizeList = packageSizeRepository.findAll();
        assertThat(packageSizeList).hasSize(databaseSizeBeforeCreate + 1);
        PackageSize testPackageSize = packageSizeList.get(packageSizeList.size() - 1);
        assertThat(testPackageSize.getPackageSizeName()).isEqualTo(DEFAULT_PACKAGE_SIZE_NAME);
        assertThat(testPackageSize.getPackageSizeDesciption()).isEqualTo(DEFAULT_PACKAGE_SIZE_DESCIPTION);
        assertThat(testPackageSize.getPackageSizeIcon()).isEqualTo(DEFAULT_PACKAGE_SIZE_ICON);
        assertThat(testPackageSize.getPackageSizeIconContentType()).isEqualTo(DEFAULT_PACKAGE_SIZE_ICON_CONTENT_TYPE);
        assertThat(testPackageSize.getPackageSizeIconUrl()).isEqualTo(DEFAULT_PACKAGE_SIZE_ICON_URL);

        // Validate the PackageSize in Elasticsearch
        PackageSize packageSizeEs = packageSizeSearchRepository.findOne(testPackageSize.getId());
        assertThat(packageSizeEs).isEqualToIgnoringGivenFields(testPackageSize);
    }

    @Test
    @Transactional
    public void createPackageSizeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = packageSizeRepository.findAll().size();

        // Create the PackageSize with an existing ID
        packageSize.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPackageSizeMockMvc.perform(post("/api/package-sizes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(packageSize)))
            .andExpect(status().isBadRequest());

        // Validate the PackageSize in the database
        List<PackageSize> packageSizeList = packageSizeRepository.findAll();
        assertThat(packageSizeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkPackageSizeNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = packageSizeRepository.findAll().size();
        // set the field null
        packageSize.setPackageSizeName(null);

        // Create the PackageSize, which fails.

        restPackageSizeMockMvc.perform(post("/api/package-sizes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(packageSize)))
            .andExpect(status().isBadRequest());

        List<PackageSize> packageSizeList = packageSizeRepository.findAll();
        assertThat(packageSizeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPackageSizeDesciptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = packageSizeRepository.findAll().size();
        // set the field null
        packageSize.setPackageSizeDesciption(null);

        // Create the PackageSize, which fails.

        restPackageSizeMockMvc.perform(post("/api/package-sizes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(packageSize)))
            .andExpect(status().isBadRequest());

        List<PackageSize> packageSizeList = packageSizeRepository.findAll();
        assertThat(packageSizeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPackageSizes() throws Exception {
        // Initialize the database
        packageSizeRepository.saveAndFlush(packageSize);

        // Get all the packageSizeList
        restPackageSizeMockMvc.perform(get("/api/package-sizes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(packageSize.getId().intValue())))
            .andExpect(jsonPath("$.[*].packageSizeName").value(hasItem(DEFAULT_PACKAGE_SIZE_NAME.toString())))
            .andExpect(jsonPath("$.[*].packageSizeDesciption").value(hasItem(DEFAULT_PACKAGE_SIZE_DESCIPTION.toString())))
            .andExpect(jsonPath("$.[*].packageSizeIconContentType").value(hasItem(DEFAULT_PACKAGE_SIZE_ICON_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].packageSizeIcon").value(hasItem(Base64Utils.encodeToString(DEFAULT_PACKAGE_SIZE_ICON))))
            .andExpect(jsonPath("$.[*].packageSizeIconUrl").value(hasItem(DEFAULT_PACKAGE_SIZE_ICON_URL.toString())));
    }

    @Test
    @Transactional
    public void getPackageSize() throws Exception {
        // Initialize the database
        packageSizeRepository.saveAndFlush(packageSize);

        // Get the packageSize
        restPackageSizeMockMvc.perform(get("/api/package-sizes/{id}", packageSize.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(packageSize.getId().intValue()))
            .andExpect(jsonPath("$.packageSizeName").value(DEFAULT_PACKAGE_SIZE_NAME.toString()))
            .andExpect(jsonPath("$.packageSizeDesciption").value(DEFAULT_PACKAGE_SIZE_DESCIPTION.toString()))
            .andExpect(jsonPath("$.packageSizeIconContentType").value(DEFAULT_PACKAGE_SIZE_ICON_CONTENT_TYPE))
            .andExpect(jsonPath("$.packageSizeIcon").value(Base64Utils.encodeToString(DEFAULT_PACKAGE_SIZE_ICON)))
            .andExpect(jsonPath("$.packageSizeIconUrl").value(DEFAULT_PACKAGE_SIZE_ICON_URL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPackageSize() throws Exception {
        // Get the packageSize
        restPackageSizeMockMvc.perform(get("/api/package-sizes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePackageSize() throws Exception {
        // Initialize the database
        packageSizeRepository.saveAndFlush(packageSize);
        packageSizeSearchRepository.save(packageSize);
        int databaseSizeBeforeUpdate = packageSizeRepository.findAll().size();

        // Update the packageSize
        PackageSize updatedPackageSize = packageSizeRepository.findOne(packageSize.getId());
        // Disconnect from session so that the updates on updatedPackageSize are not directly saved in db
        em.detach(updatedPackageSize);
        updatedPackageSize
            .packageSizeName(UPDATED_PACKAGE_SIZE_NAME)
            .packageSizeDesciption(UPDATED_PACKAGE_SIZE_DESCIPTION)
            .packageSizeIcon(UPDATED_PACKAGE_SIZE_ICON)
            .packageSizeIconContentType(UPDATED_PACKAGE_SIZE_ICON_CONTENT_TYPE)
            .packageSizeIconUrl(UPDATED_PACKAGE_SIZE_ICON_URL);

        restPackageSizeMockMvc.perform(put("/api/package-sizes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPackageSize)))
            .andExpect(status().isOk());

        // Validate the PackageSize in the database
        List<PackageSize> packageSizeList = packageSizeRepository.findAll();
        assertThat(packageSizeList).hasSize(databaseSizeBeforeUpdate);
        PackageSize testPackageSize = packageSizeList.get(packageSizeList.size() - 1);
        assertThat(testPackageSize.getPackageSizeName()).isEqualTo(UPDATED_PACKAGE_SIZE_NAME);
        assertThat(testPackageSize.getPackageSizeDesciption()).isEqualTo(UPDATED_PACKAGE_SIZE_DESCIPTION);
        assertThat(testPackageSize.getPackageSizeIcon()).isEqualTo(UPDATED_PACKAGE_SIZE_ICON);
        assertThat(testPackageSize.getPackageSizeIconContentType()).isEqualTo(UPDATED_PACKAGE_SIZE_ICON_CONTENT_TYPE);
        assertThat(testPackageSize.getPackageSizeIconUrl()).isEqualTo(UPDATED_PACKAGE_SIZE_ICON_URL);

        // Validate the PackageSize in Elasticsearch
        PackageSize packageSizeEs = packageSizeSearchRepository.findOne(testPackageSize.getId());
        assertThat(packageSizeEs).isEqualToIgnoringGivenFields(testPackageSize);
    }

    @Test
    @Transactional
    public void updateNonExistingPackageSize() throws Exception {
        int databaseSizeBeforeUpdate = packageSizeRepository.findAll().size();

        // Create the PackageSize

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPackageSizeMockMvc.perform(put("/api/package-sizes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(packageSize)))
            .andExpect(status().isCreated());

        // Validate the PackageSize in the database
        List<PackageSize> packageSizeList = packageSizeRepository.findAll();
        assertThat(packageSizeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePackageSize() throws Exception {
        // Initialize the database
        packageSizeRepository.saveAndFlush(packageSize);
        packageSizeSearchRepository.save(packageSize);
        int databaseSizeBeforeDelete = packageSizeRepository.findAll().size();

        // Get the packageSize
        restPackageSizeMockMvc.perform(delete("/api/package-sizes/{id}", packageSize.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean packageSizeExistsInEs = packageSizeSearchRepository.exists(packageSize.getId());
        assertThat(packageSizeExistsInEs).isFalse();

        // Validate the database is empty
        List<PackageSize> packageSizeList = packageSizeRepository.findAll();
        assertThat(packageSizeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPackageSize() throws Exception {
        // Initialize the database
        packageSizeRepository.saveAndFlush(packageSize);
        packageSizeSearchRepository.save(packageSize);

        // Search the packageSize
        restPackageSizeMockMvc.perform(get("/api/_search/package-sizes?query=id:" + packageSize.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(packageSize.getId().intValue())))
            .andExpect(jsonPath("$.[*].packageSizeName").value(hasItem(DEFAULT_PACKAGE_SIZE_NAME.toString())))
            .andExpect(jsonPath("$.[*].packageSizeDesciption").value(hasItem(DEFAULT_PACKAGE_SIZE_DESCIPTION.toString())))
            .andExpect(jsonPath("$.[*].packageSizeIconContentType").value(hasItem(DEFAULT_PACKAGE_SIZE_ICON_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].packageSizeIcon").value(hasItem(Base64Utils.encodeToString(DEFAULT_PACKAGE_SIZE_ICON))))
            .andExpect(jsonPath("$.[*].packageSizeIconUrl").value(hasItem(DEFAULT_PACKAGE_SIZE_ICON_URL.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PackageSize.class);
        PackageSize packageSize1 = new PackageSize();
        packageSize1.setId(1L);
        PackageSize packageSize2 = new PackageSize();
        packageSize2.setId(packageSize1.getId());
        assertThat(packageSize1).isEqualTo(packageSize2);
        packageSize2.setId(2L);
        assertThat(packageSize1).isNotEqualTo(packageSize2);
        packageSize1.setId(null);
        assertThat(packageSize1).isNotEqualTo(packageSize2);
    }
}

package com.cus.jastip.master.web.rest;

import com.cus.jastip.master.MasterApp;

import com.cus.jastip.master.config.SecurityBeanOverrideConfiguration;

import com.cus.jastip.master.domain.ItemSubCategory;
import com.cus.jastip.master.repository.ItemSubCategoryRepository;
import com.cus.jastip.master.repository.search.ItemSubCategorySearchRepository;
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
 * Test class for the ItemSubCategoryResource REST controller.
 *
 * @see ItemSubCategoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MasterApp.class, SecurityBeanOverrideConfiguration.class})
public class ItemSubCategoryResourceIntTest {

    private static final String DEFAULT_ITEM_SUB_CATEGORY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ITEM_SUB_CATEGORY_NAME = "BBBBBBBBBB";

    private static final byte[] DEFAULT_ITEM_SUB_CATEGORY_ICON = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_ITEM_SUB_CATEGORY_ICON = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_ITEM_SUB_CATEGORY_ICON_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_ITEM_SUB_CATEGORY_ICON_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_ITEM_SUB_CATEGORY_ICON_URL = "AAAAAAAAAA";
    private static final String UPDATED_ITEM_SUB_CATEGORY_ICON_URL = "BBBBBBBBBB";

    @Autowired
    private ItemSubCategoryRepository itemSubCategoryRepository;

    @Autowired
    private ItemSubCategorySearchRepository itemSubCategorySearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restItemSubCategoryMockMvc;

    private ItemSubCategory itemSubCategory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ItemSubCategoryResource itemSubCategoryResource = new ItemSubCategoryResource(itemSubCategoryRepository, itemSubCategorySearchRepository);
        this.restItemSubCategoryMockMvc = MockMvcBuilders.standaloneSetup(itemSubCategoryResource)
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
    public static ItemSubCategory createEntity(EntityManager em) {
        ItemSubCategory itemSubCategory = new ItemSubCategory()
            .itemSubCategoryName(DEFAULT_ITEM_SUB_CATEGORY_NAME)
            .itemSubCategoryIcon(DEFAULT_ITEM_SUB_CATEGORY_ICON)
            .itemSubCategoryIconContentType(DEFAULT_ITEM_SUB_CATEGORY_ICON_CONTENT_TYPE)
            .itemSubCategoryIconUrl(DEFAULT_ITEM_SUB_CATEGORY_ICON_URL);
        return itemSubCategory;
    }

    @Before
    public void initTest() {
        itemSubCategorySearchRepository.deleteAll();
        itemSubCategory = createEntity(em);
    }

    @Test
    @Transactional
    public void createItemSubCategory() throws Exception {
        int databaseSizeBeforeCreate = itemSubCategoryRepository.findAll().size();

        // Create the ItemSubCategory
        restItemSubCategoryMockMvc.perform(post("/api/item-sub-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itemSubCategory)))
            .andExpect(status().isCreated());

        // Validate the ItemSubCategory in the database
        List<ItemSubCategory> itemSubCategoryList = itemSubCategoryRepository.findAll();
        assertThat(itemSubCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        ItemSubCategory testItemSubCategory = itemSubCategoryList.get(itemSubCategoryList.size() - 1);
        assertThat(testItemSubCategory.getItemSubCategoryName()).isEqualTo(DEFAULT_ITEM_SUB_CATEGORY_NAME);
        assertThat(testItemSubCategory.getItemSubCategoryIcon()).isEqualTo(DEFAULT_ITEM_SUB_CATEGORY_ICON);
        assertThat(testItemSubCategory.getItemSubCategoryIconContentType()).isEqualTo(DEFAULT_ITEM_SUB_CATEGORY_ICON_CONTENT_TYPE);
        assertThat(testItemSubCategory.getItemSubCategoryIconUrl()).isEqualTo(DEFAULT_ITEM_SUB_CATEGORY_ICON_URL);

        // Validate the ItemSubCategory in Elasticsearch
        ItemSubCategory itemSubCategoryEs = itemSubCategorySearchRepository.findOne(testItemSubCategory.getId());
        assertThat(itemSubCategoryEs).isEqualToIgnoringGivenFields(testItemSubCategory);
    }

    @Test
    @Transactional
    public void createItemSubCategoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = itemSubCategoryRepository.findAll().size();

        // Create the ItemSubCategory with an existing ID
        itemSubCategory.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restItemSubCategoryMockMvc.perform(post("/api/item-sub-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itemSubCategory)))
            .andExpect(status().isBadRequest());

        // Validate the ItemSubCategory in the database
        List<ItemSubCategory> itemSubCategoryList = itemSubCategoryRepository.findAll();
        assertThat(itemSubCategoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkItemSubCategoryNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemSubCategoryRepository.findAll().size();
        // set the field null
        itemSubCategory.setItemSubCategoryName(null);

        // Create the ItemSubCategory, which fails.

        restItemSubCategoryMockMvc.perform(post("/api/item-sub-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itemSubCategory)))
            .andExpect(status().isBadRequest());

        List<ItemSubCategory> itemSubCategoryList = itemSubCategoryRepository.findAll();
        assertThat(itemSubCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllItemSubCategories() throws Exception {
        // Initialize the database
        itemSubCategoryRepository.saveAndFlush(itemSubCategory);

        // Get all the itemSubCategoryList
        restItemSubCategoryMockMvc.perform(get("/api/item-sub-categories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itemSubCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].itemSubCategoryName").value(hasItem(DEFAULT_ITEM_SUB_CATEGORY_NAME.toString())))
            .andExpect(jsonPath("$.[*].itemSubCategoryIconContentType").value(hasItem(DEFAULT_ITEM_SUB_CATEGORY_ICON_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].itemSubCategoryIcon").value(hasItem(Base64Utils.encodeToString(DEFAULT_ITEM_SUB_CATEGORY_ICON))))
            .andExpect(jsonPath("$.[*].itemSubCategoryIconUrl").value(hasItem(DEFAULT_ITEM_SUB_CATEGORY_ICON_URL.toString())));
    }

    @Test
    @Transactional
    public void getItemSubCategory() throws Exception {
        // Initialize the database
        itemSubCategoryRepository.saveAndFlush(itemSubCategory);

        // Get the itemSubCategory
        restItemSubCategoryMockMvc.perform(get("/api/item-sub-categories/{id}", itemSubCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(itemSubCategory.getId().intValue()))
            .andExpect(jsonPath("$.itemSubCategoryName").value(DEFAULT_ITEM_SUB_CATEGORY_NAME.toString()))
            .andExpect(jsonPath("$.itemSubCategoryIconContentType").value(DEFAULT_ITEM_SUB_CATEGORY_ICON_CONTENT_TYPE))
            .andExpect(jsonPath("$.itemSubCategoryIcon").value(Base64Utils.encodeToString(DEFAULT_ITEM_SUB_CATEGORY_ICON)))
            .andExpect(jsonPath("$.itemSubCategoryIconUrl").value(DEFAULT_ITEM_SUB_CATEGORY_ICON_URL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingItemSubCategory() throws Exception {
        // Get the itemSubCategory
        restItemSubCategoryMockMvc.perform(get("/api/item-sub-categories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateItemSubCategory() throws Exception {
        // Initialize the database
        itemSubCategoryRepository.saveAndFlush(itemSubCategory);
        itemSubCategorySearchRepository.save(itemSubCategory);
        int databaseSizeBeforeUpdate = itemSubCategoryRepository.findAll().size();

        // Update the itemSubCategory
        ItemSubCategory updatedItemSubCategory = itemSubCategoryRepository.findOne(itemSubCategory.getId());
        // Disconnect from session so that the updates on updatedItemSubCategory are not directly saved in db
        em.detach(updatedItemSubCategory);
        updatedItemSubCategory
            .itemSubCategoryName(UPDATED_ITEM_SUB_CATEGORY_NAME)
            .itemSubCategoryIcon(UPDATED_ITEM_SUB_CATEGORY_ICON)
            .itemSubCategoryIconContentType(UPDATED_ITEM_SUB_CATEGORY_ICON_CONTENT_TYPE)
            .itemSubCategoryIconUrl(UPDATED_ITEM_SUB_CATEGORY_ICON_URL);

        restItemSubCategoryMockMvc.perform(put("/api/item-sub-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedItemSubCategory)))
            .andExpect(status().isOk());

        // Validate the ItemSubCategory in the database
        List<ItemSubCategory> itemSubCategoryList = itemSubCategoryRepository.findAll();
        assertThat(itemSubCategoryList).hasSize(databaseSizeBeforeUpdate);
        ItemSubCategory testItemSubCategory = itemSubCategoryList.get(itemSubCategoryList.size() - 1);
        assertThat(testItemSubCategory.getItemSubCategoryName()).isEqualTo(UPDATED_ITEM_SUB_CATEGORY_NAME);
        assertThat(testItemSubCategory.getItemSubCategoryIcon()).isEqualTo(UPDATED_ITEM_SUB_CATEGORY_ICON);
        assertThat(testItemSubCategory.getItemSubCategoryIconContentType()).isEqualTo(UPDATED_ITEM_SUB_CATEGORY_ICON_CONTENT_TYPE);
        assertThat(testItemSubCategory.getItemSubCategoryIconUrl()).isEqualTo(UPDATED_ITEM_SUB_CATEGORY_ICON_URL);

        // Validate the ItemSubCategory in Elasticsearch
        ItemSubCategory itemSubCategoryEs = itemSubCategorySearchRepository.findOne(testItemSubCategory.getId());
        assertThat(itemSubCategoryEs).isEqualToIgnoringGivenFields(testItemSubCategory);
    }

    @Test
    @Transactional
    public void updateNonExistingItemSubCategory() throws Exception {
        int databaseSizeBeforeUpdate = itemSubCategoryRepository.findAll().size();

        // Create the ItemSubCategory

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restItemSubCategoryMockMvc.perform(put("/api/item-sub-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itemSubCategory)))
            .andExpect(status().isCreated());

        // Validate the ItemSubCategory in the database
        List<ItemSubCategory> itemSubCategoryList = itemSubCategoryRepository.findAll();
        assertThat(itemSubCategoryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteItemSubCategory() throws Exception {
        // Initialize the database
        itemSubCategoryRepository.saveAndFlush(itemSubCategory);
        itemSubCategorySearchRepository.save(itemSubCategory);
        int databaseSizeBeforeDelete = itemSubCategoryRepository.findAll().size();

        // Get the itemSubCategory
        restItemSubCategoryMockMvc.perform(delete("/api/item-sub-categories/{id}", itemSubCategory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean itemSubCategoryExistsInEs = itemSubCategorySearchRepository.exists(itemSubCategory.getId());
        assertThat(itemSubCategoryExistsInEs).isFalse();

        // Validate the database is empty
        List<ItemSubCategory> itemSubCategoryList = itemSubCategoryRepository.findAll();
        assertThat(itemSubCategoryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchItemSubCategory() throws Exception {
        // Initialize the database
        itemSubCategoryRepository.saveAndFlush(itemSubCategory);
        itemSubCategorySearchRepository.save(itemSubCategory);

        // Search the itemSubCategory
        restItemSubCategoryMockMvc.perform(get("/api/_search/item-sub-categories?query=id:" + itemSubCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itemSubCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].itemSubCategoryName").value(hasItem(DEFAULT_ITEM_SUB_CATEGORY_NAME.toString())))
            .andExpect(jsonPath("$.[*].itemSubCategoryIconContentType").value(hasItem(DEFAULT_ITEM_SUB_CATEGORY_ICON_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].itemSubCategoryIcon").value(hasItem(Base64Utils.encodeToString(DEFAULT_ITEM_SUB_CATEGORY_ICON))))
            .andExpect(jsonPath("$.[*].itemSubCategoryIconUrl").value(hasItem(DEFAULT_ITEM_SUB_CATEGORY_ICON_URL.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ItemSubCategory.class);
        ItemSubCategory itemSubCategory1 = new ItemSubCategory();
        itemSubCategory1.setId(1L);
        ItemSubCategory itemSubCategory2 = new ItemSubCategory();
        itemSubCategory2.setId(itemSubCategory1.getId());
        assertThat(itemSubCategory1).isEqualTo(itemSubCategory2);
        itemSubCategory2.setId(2L);
        assertThat(itemSubCategory1).isNotEqualTo(itemSubCategory2);
        itemSubCategory1.setId(null);
        assertThat(itemSubCategory1).isNotEqualTo(itemSubCategory2);
    }
}

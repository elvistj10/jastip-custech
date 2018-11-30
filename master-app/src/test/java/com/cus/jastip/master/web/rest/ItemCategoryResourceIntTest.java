package com.cus.jastip.master.web.rest;

import com.cus.jastip.master.MasterApp;

import com.cus.jastip.master.config.SecurityBeanOverrideConfiguration;

import com.cus.jastip.master.domain.ItemCategory;
import com.cus.jastip.master.repository.ItemCategoryRepository;
import com.cus.jastip.master.repository.search.ItemCategorySearchRepository;
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
 * Test class for the ItemCategoryResource REST controller.
 *
 * @see ItemCategoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MasterApp.class, SecurityBeanOverrideConfiguration.class})
public class ItemCategoryResourceIntTest {

    private static final String DEFAULT_ITEM_CATEGORY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ITEM_CATEGORY_NAME = "BBBBBBBBBB";

    private static final byte[] DEFAULT_ITEM_CATEGORY_ICON = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_ITEM_CATEGORY_ICON = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_ITEM_CATEGORY_ICON_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_ITEM_CATEGORY_ICON_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_ITEM_CATEGORY_ICON_URL = "AAAAAAAAAA";
    private static final String UPDATED_ITEM_CATEGORY_ICON_URL = "BBBBBBBBBB";

    @Autowired
    private ItemCategoryRepository itemCategoryRepository;

    @Autowired
    private ItemCategorySearchRepository itemCategorySearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restItemCategoryMockMvc;

    private ItemCategory itemCategory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ItemCategoryResource itemCategoryResource = new ItemCategoryResource(itemCategoryRepository, itemCategorySearchRepository);
        this.restItemCategoryMockMvc = MockMvcBuilders.standaloneSetup(itemCategoryResource)
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
    public static ItemCategory createEntity(EntityManager em) {
        ItemCategory itemCategory = new ItemCategory()
            .itemCategoryName(DEFAULT_ITEM_CATEGORY_NAME)
            .itemCategoryIcon(DEFAULT_ITEM_CATEGORY_ICON)
            .itemCategoryIconContentType(DEFAULT_ITEM_CATEGORY_ICON_CONTENT_TYPE)
            .itemCategoryIconUrl(DEFAULT_ITEM_CATEGORY_ICON_URL);
        return itemCategory;
    }

    @Before
    public void initTest() {
        itemCategorySearchRepository.deleteAll();
        itemCategory = createEntity(em);
    }

    @Test
    @Transactional
    public void createItemCategory() throws Exception {
        int databaseSizeBeforeCreate = itemCategoryRepository.findAll().size();

        // Create the ItemCategory
        restItemCategoryMockMvc.perform(post("/api/item-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itemCategory)))
            .andExpect(status().isCreated());

        // Validate the ItemCategory in the database
        List<ItemCategory> itemCategoryList = itemCategoryRepository.findAll();
        assertThat(itemCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        ItemCategory testItemCategory = itemCategoryList.get(itemCategoryList.size() - 1);
        assertThat(testItemCategory.getItemCategoryName()).isEqualTo(DEFAULT_ITEM_CATEGORY_NAME);
        assertThat(testItemCategory.getItemCategoryIcon()).isEqualTo(DEFAULT_ITEM_CATEGORY_ICON);
        assertThat(testItemCategory.getItemCategoryIconContentType()).isEqualTo(DEFAULT_ITEM_CATEGORY_ICON_CONTENT_TYPE);
        assertThat(testItemCategory.getItemCategoryIconUrl()).isEqualTo(DEFAULT_ITEM_CATEGORY_ICON_URL);

        // Validate the ItemCategory in Elasticsearch
        ItemCategory itemCategoryEs = itemCategorySearchRepository.findOne(testItemCategory.getId());
        assertThat(itemCategoryEs).isEqualToIgnoringGivenFields(testItemCategory);
    }

    @Test
    @Transactional
    public void createItemCategoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = itemCategoryRepository.findAll().size();

        // Create the ItemCategory with an existing ID
        itemCategory.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restItemCategoryMockMvc.perform(post("/api/item-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itemCategory)))
            .andExpect(status().isBadRequest());

        // Validate the ItemCategory in the database
        List<ItemCategory> itemCategoryList = itemCategoryRepository.findAll();
        assertThat(itemCategoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkItemCategoryNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemCategoryRepository.findAll().size();
        // set the field null
        itemCategory.setItemCategoryName(null);

        // Create the ItemCategory, which fails.

        restItemCategoryMockMvc.perform(post("/api/item-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itemCategory)))
            .andExpect(status().isBadRequest());

        List<ItemCategory> itemCategoryList = itemCategoryRepository.findAll();
        assertThat(itemCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllItemCategories() throws Exception {
        // Initialize the database
        itemCategoryRepository.saveAndFlush(itemCategory);

        // Get all the itemCategoryList
        restItemCategoryMockMvc.perform(get("/api/item-categories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itemCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].itemCategoryName").value(hasItem(DEFAULT_ITEM_CATEGORY_NAME.toString())))
            .andExpect(jsonPath("$.[*].itemCategoryIconContentType").value(hasItem(DEFAULT_ITEM_CATEGORY_ICON_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].itemCategoryIcon").value(hasItem(Base64Utils.encodeToString(DEFAULT_ITEM_CATEGORY_ICON))))
            .andExpect(jsonPath("$.[*].itemCategoryIconUrl").value(hasItem(DEFAULT_ITEM_CATEGORY_ICON_URL.toString())));
    }

    @Test
    @Transactional
    public void getItemCategory() throws Exception {
        // Initialize the database
        itemCategoryRepository.saveAndFlush(itemCategory);

        // Get the itemCategory
        restItemCategoryMockMvc.perform(get("/api/item-categories/{id}", itemCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(itemCategory.getId().intValue()))
            .andExpect(jsonPath("$.itemCategoryName").value(DEFAULT_ITEM_CATEGORY_NAME.toString()))
            .andExpect(jsonPath("$.itemCategoryIconContentType").value(DEFAULT_ITEM_CATEGORY_ICON_CONTENT_TYPE))
            .andExpect(jsonPath("$.itemCategoryIcon").value(Base64Utils.encodeToString(DEFAULT_ITEM_CATEGORY_ICON)))
            .andExpect(jsonPath("$.itemCategoryIconUrl").value(DEFAULT_ITEM_CATEGORY_ICON_URL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingItemCategory() throws Exception {
        // Get the itemCategory
        restItemCategoryMockMvc.perform(get("/api/item-categories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateItemCategory() throws Exception {
        // Initialize the database
        itemCategoryRepository.saveAndFlush(itemCategory);
        itemCategorySearchRepository.save(itemCategory);
        int databaseSizeBeforeUpdate = itemCategoryRepository.findAll().size();

        // Update the itemCategory
        ItemCategory updatedItemCategory = itemCategoryRepository.findOne(itemCategory.getId());
        // Disconnect from session so that the updates on updatedItemCategory are not directly saved in db
        em.detach(updatedItemCategory);
        updatedItemCategory
            .itemCategoryName(UPDATED_ITEM_CATEGORY_NAME)
            .itemCategoryIcon(UPDATED_ITEM_CATEGORY_ICON)
            .itemCategoryIconContentType(UPDATED_ITEM_CATEGORY_ICON_CONTENT_TYPE)
            .itemCategoryIconUrl(UPDATED_ITEM_CATEGORY_ICON_URL);

        restItemCategoryMockMvc.perform(put("/api/item-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedItemCategory)))
            .andExpect(status().isOk());

        // Validate the ItemCategory in the database
        List<ItemCategory> itemCategoryList = itemCategoryRepository.findAll();
        assertThat(itemCategoryList).hasSize(databaseSizeBeforeUpdate);
        ItemCategory testItemCategory = itemCategoryList.get(itemCategoryList.size() - 1);
        assertThat(testItemCategory.getItemCategoryName()).isEqualTo(UPDATED_ITEM_CATEGORY_NAME);
        assertThat(testItemCategory.getItemCategoryIcon()).isEqualTo(UPDATED_ITEM_CATEGORY_ICON);
        assertThat(testItemCategory.getItemCategoryIconContentType()).isEqualTo(UPDATED_ITEM_CATEGORY_ICON_CONTENT_TYPE);
        assertThat(testItemCategory.getItemCategoryIconUrl()).isEqualTo(UPDATED_ITEM_CATEGORY_ICON_URL);

        // Validate the ItemCategory in Elasticsearch
        ItemCategory itemCategoryEs = itemCategorySearchRepository.findOne(testItemCategory.getId());
        assertThat(itemCategoryEs).isEqualToIgnoringGivenFields(testItemCategory);
    }

    @Test
    @Transactional
    public void updateNonExistingItemCategory() throws Exception {
        int databaseSizeBeforeUpdate = itemCategoryRepository.findAll().size();

        // Create the ItemCategory

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restItemCategoryMockMvc.perform(put("/api/item-categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itemCategory)))
            .andExpect(status().isCreated());

        // Validate the ItemCategory in the database
        List<ItemCategory> itemCategoryList = itemCategoryRepository.findAll();
        assertThat(itemCategoryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteItemCategory() throws Exception {
        // Initialize the database
        itemCategoryRepository.saveAndFlush(itemCategory);
        itemCategorySearchRepository.save(itemCategory);
        int databaseSizeBeforeDelete = itemCategoryRepository.findAll().size();

        // Get the itemCategory
        restItemCategoryMockMvc.perform(delete("/api/item-categories/{id}", itemCategory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean itemCategoryExistsInEs = itemCategorySearchRepository.exists(itemCategory.getId());
        assertThat(itemCategoryExistsInEs).isFalse();

        // Validate the database is empty
        List<ItemCategory> itemCategoryList = itemCategoryRepository.findAll();
        assertThat(itemCategoryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchItemCategory() throws Exception {
        // Initialize the database
        itemCategoryRepository.saveAndFlush(itemCategory);
        itemCategorySearchRepository.save(itemCategory);

        // Search the itemCategory
        restItemCategoryMockMvc.perform(get("/api/_search/item-categories?query=id:" + itemCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itemCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].itemCategoryName").value(hasItem(DEFAULT_ITEM_CATEGORY_NAME.toString())))
            .andExpect(jsonPath("$.[*].itemCategoryIconContentType").value(hasItem(DEFAULT_ITEM_CATEGORY_ICON_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].itemCategoryIcon").value(hasItem(Base64Utils.encodeToString(DEFAULT_ITEM_CATEGORY_ICON))))
            .andExpect(jsonPath("$.[*].itemCategoryIconUrl").value(hasItem(DEFAULT_ITEM_CATEGORY_ICON_URL.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ItemCategory.class);
        ItemCategory itemCategory1 = new ItemCategory();
        itemCategory1.setId(1L);
        ItemCategory itemCategory2 = new ItemCategory();
        itemCategory2.setId(itemCategory1.getId());
        assertThat(itemCategory1).isEqualTo(itemCategory2);
        itemCategory2.setId(2L);
        assertThat(itemCategory1).isNotEqualTo(itemCategory2);
        itemCategory1.setId(null);
        assertThat(itemCategory1).isNotEqualTo(itemCategory2);
    }
}

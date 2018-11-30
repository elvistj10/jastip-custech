package com.cus.jastip.banner.web.rest;

import com.cus.jastip.banner.BannerApp;

import com.cus.jastip.banner.config.SecurityBeanOverrideConfiguration;

import com.cus.jastip.banner.domain.Banner;
import com.cus.jastip.banner.repository.BannerRepository;
import com.cus.jastip.banner.repository.search.BannerSearchRepository;
import com.cus.jastip.banner.web.rest.errors.ExceptionTranslator;

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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.cus.jastip.banner.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cus.jastip.banner.domain.enumeration.BannerType;
import com.cus.jastip.banner.domain.enumeration.PostingType;
/**
 * Test class for the BannerResource REST controller.
 *
 * @see BannerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {BannerApp.class, SecurityBeanOverrideConfiguration.class})
public class BannerResourceIntTest {

    private static final String DEFAULT_BANNER_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_BANNER_TITLE = "BBBBBBBBBB";

    private static final BannerType DEFAULT_BANNER_TYPE = BannerType.INTERNAL;
    private static final BannerType UPDATED_BANNER_TYPE = BannerType.EXTERNAL;

    private static final PostingType DEFAULT_POSTING_TYPE = PostingType.TRAVELLER;
    private static final PostingType UPDATED_POSTING_TYPE = PostingType.REQUESTOR;

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_URL = "BBBBBBBBBB";

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String DEFAULT_QUERY = "AAAAAAAAAA";
    private static final String UPDATED_QUERY = "BBBBBBBBBB";

    @Autowired
    private BannerRepository bannerRepository;

    @Autowired
    private BannerSearchRepository bannerSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBannerMockMvc;

    private Banner banner;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BannerResource bannerResource = new BannerResource(bannerRepository, bannerSearchRepository);
        this.restBannerMockMvc = MockMvcBuilders.standaloneSetup(bannerResource)
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
    public static Banner createEntity(EntityManager em) {
        Banner banner = new Banner()
            .bannerTitle(DEFAULT_BANNER_TITLE)
            .bannerType(DEFAULT_BANNER_TYPE)
            .postingType(DEFAULT_POSTING_TYPE)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .imageUrl(DEFAULT_IMAGE_URL)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .description(DEFAULT_DESCRIPTION)
            .notes(DEFAULT_NOTES)
            .query(DEFAULT_QUERY);
        return banner;
    }

    @Before
    public void initTest() {
        bannerSearchRepository.deleteAll();
        banner = createEntity(em);
    }

    @Test
    @Transactional
    public void createBanner() throws Exception {
        int databaseSizeBeforeCreate = bannerRepository.findAll().size();

        // Create the Banner
        restBannerMockMvc.perform(post("/api/banners")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(banner)))
            .andExpect(status().isCreated());

        // Validate the Banner in the database
        List<Banner> bannerList = bannerRepository.findAll();
        assertThat(bannerList).hasSize(databaseSizeBeforeCreate + 1);
        Banner testBanner = bannerList.get(bannerList.size() - 1);
        assertThat(testBanner.getBannerTitle()).isEqualTo(DEFAULT_BANNER_TITLE);
        assertThat(testBanner.getBannerType()).isEqualTo(DEFAULT_BANNER_TYPE);
        assertThat(testBanner.getPostingType()).isEqualTo(DEFAULT_POSTING_TYPE);
        assertThat(testBanner.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testBanner.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testBanner.getImageUrl()).isEqualTo(DEFAULT_IMAGE_URL);
        assertThat(testBanner.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testBanner.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testBanner.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testBanner.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testBanner.getQuery()).isEqualTo(DEFAULT_QUERY);

        // Validate the Banner in Elasticsearch
        Banner bannerEs = bannerSearchRepository.findOne(testBanner.getId());
        assertThat(bannerEs).isEqualToIgnoringGivenFields(testBanner);
    }

    @Test
    @Transactional
    public void createBannerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = bannerRepository.findAll().size();

        // Create the Banner with an existing ID
        banner.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBannerMockMvc.perform(post("/api/banners")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(banner)))
            .andExpect(status().isBadRequest());

        // Validate the Banner in the database
        List<Banner> bannerList = bannerRepository.findAll();
        assertThat(bannerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllBanners() throws Exception {
        // Initialize the database
        bannerRepository.saveAndFlush(banner);

        // Get all the bannerList
        restBannerMockMvc.perform(get("/api/banners?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(banner.getId().intValue())))
            .andExpect(jsonPath("$.[*].bannerTitle").value(hasItem(DEFAULT_BANNER_TITLE.toString())))
            .andExpect(jsonPath("$.[*].bannerType").value(hasItem(DEFAULT_BANNER_TYPE.toString())))
            .andExpect(jsonPath("$.[*].postingType").value(hasItem(DEFAULT_POSTING_TYPE.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())))
            .andExpect(jsonPath("$.[*].query").value(hasItem(DEFAULT_QUERY.toString())));
    }

    @Test
    @Transactional
    public void getBanner() throws Exception {
        // Initialize the database
        bannerRepository.saveAndFlush(banner);

        // Get the banner
        restBannerMockMvc.perform(get("/api/banners/{id}", banner.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(banner.getId().intValue()))
            .andExpect(jsonPath("$.bannerTitle").value(DEFAULT_BANNER_TITLE.toString()))
            .andExpect(jsonPath("$.bannerType").value(DEFAULT_BANNER_TYPE.toString()))
            .andExpect(jsonPath("$.postingType").value(DEFAULT_POSTING_TYPE.toString()))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.imageUrl").value(DEFAULT_IMAGE_URL.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES.toString()))
            .andExpect(jsonPath("$.query").value(DEFAULT_QUERY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBanner() throws Exception {
        // Get the banner
        restBannerMockMvc.perform(get("/api/banners/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBanner() throws Exception {
        // Initialize the database
        bannerRepository.saveAndFlush(banner);
        bannerSearchRepository.save(banner);
        int databaseSizeBeforeUpdate = bannerRepository.findAll().size();

        // Update the banner
        Banner updatedBanner = bannerRepository.findOne(banner.getId());
        // Disconnect from session so that the updates on updatedBanner are not directly saved in db
        em.detach(updatedBanner);
        updatedBanner
            .bannerTitle(UPDATED_BANNER_TITLE)
            .bannerType(UPDATED_BANNER_TYPE)
            .postingType(UPDATED_POSTING_TYPE)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .imageUrl(UPDATED_IMAGE_URL)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .description(UPDATED_DESCRIPTION)
            .notes(UPDATED_NOTES)
            .query(UPDATED_QUERY);

        restBannerMockMvc.perform(put("/api/banners")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBanner)))
            .andExpect(status().isOk());

        // Validate the Banner in the database
        List<Banner> bannerList = bannerRepository.findAll();
        assertThat(bannerList).hasSize(databaseSizeBeforeUpdate);
        Banner testBanner = bannerList.get(bannerList.size() - 1);
        assertThat(testBanner.getBannerTitle()).isEqualTo(UPDATED_BANNER_TITLE);
        assertThat(testBanner.getBannerType()).isEqualTo(UPDATED_BANNER_TYPE);
        assertThat(testBanner.getPostingType()).isEqualTo(UPDATED_POSTING_TYPE);
        assertThat(testBanner.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testBanner.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testBanner.getImageUrl()).isEqualTo(UPDATED_IMAGE_URL);
        assertThat(testBanner.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testBanner.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testBanner.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testBanner.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testBanner.getQuery()).isEqualTo(UPDATED_QUERY);

        // Validate the Banner in Elasticsearch
        Banner bannerEs = bannerSearchRepository.findOne(testBanner.getId());
        assertThat(bannerEs).isEqualToIgnoringGivenFields(testBanner);
    }

    @Test
    @Transactional
    public void updateNonExistingBanner() throws Exception {
        int databaseSizeBeforeUpdate = bannerRepository.findAll().size();

        // Create the Banner

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBannerMockMvc.perform(put("/api/banners")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(banner)))
            .andExpect(status().isCreated());

        // Validate the Banner in the database
        List<Banner> bannerList = bannerRepository.findAll();
        assertThat(bannerList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBanner() throws Exception {
        // Initialize the database
        bannerRepository.saveAndFlush(banner);
        bannerSearchRepository.save(banner);
        int databaseSizeBeforeDelete = bannerRepository.findAll().size();

        // Get the banner
        restBannerMockMvc.perform(delete("/api/banners/{id}", banner.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean bannerExistsInEs = bannerSearchRepository.exists(banner.getId());
        assertThat(bannerExistsInEs).isFalse();

        // Validate the database is empty
        List<Banner> bannerList = bannerRepository.findAll();
        assertThat(bannerList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchBanner() throws Exception {
        // Initialize the database
        bannerRepository.saveAndFlush(banner);
        bannerSearchRepository.save(banner);

        // Search the banner
        restBannerMockMvc.perform(get("/api/_search/banners?query=id:" + banner.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(banner.getId().intValue())))
            .andExpect(jsonPath("$.[*].bannerTitle").value(hasItem(DEFAULT_BANNER_TITLE.toString())))
            .andExpect(jsonPath("$.[*].bannerType").value(hasItem(DEFAULT_BANNER_TYPE.toString())))
            .andExpect(jsonPath("$.[*].postingType").value(hasItem(DEFAULT_POSTING_TYPE.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].imageUrl").value(hasItem(DEFAULT_IMAGE_URL.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())))
            .andExpect(jsonPath("$.[*].query").value(hasItem(DEFAULT_QUERY.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Banner.class);
        Banner banner1 = new Banner();
        banner1.setId(1L);
        Banner banner2 = new Banner();
        banner2.setId(banner1.getId());
        assertThat(banner1).isEqualTo(banner2);
        banner2.setId(2L);
        assertThat(banner1).isNotEqualTo(banner2);
        banner1.setId(null);
        assertThat(banner1).isNotEqualTo(banner2);
    }
}

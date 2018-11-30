package com.cus.jastip.banner.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cus.jastip.banner.domain.BannerAuditConfig;

import com.cus.jastip.banner.repository.BannerAuditConfigRepository;
import com.cus.jastip.banner.repository.search.BannerAuditConfigSearchRepository;
import com.cus.jastip.banner.web.rest.errors.BadRequestAlertException;
import com.cus.jastip.banner.web.rest.util.HeaderUtil;
import com.cus.jastip.banner.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing BannerAuditConfig.
 */
@RestController
@RequestMapping("/api")
public class BannerAuditConfigResource {

    private final Logger log = LoggerFactory.getLogger(BannerAuditConfigResource.class);

    private static final String ENTITY_NAME = "bannerAuditConfig";

    private final BannerAuditConfigRepository bannerAuditConfigRepository;

    private final BannerAuditConfigSearchRepository bannerAuditConfigSearchRepository;

    public BannerAuditConfigResource(BannerAuditConfigRepository bannerAuditConfigRepository, BannerAuditConfigSearchRepository bannerAuditConfigSearchRepository) {
        this.bannerAuditConfigRepository = bannerAuditConfigRepository;
        this.bannerAuditConfigSearchRepository = bannerAuditConfigSearchRepository;
    }

    /**
     * POST  /banner-audit-configs : Create a new bannerAuditConfig.
     *
     * @param bannerAuditConfig the bannerAuditConfig to create
     * @return the ResponseEntity with status 201 (Created) and with body the new bannerAuditConfig, or with status 400 (Bad Request) if the bannerAuditConfig has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/banner-audit-configs")
    @Timed
    public ResponseEntity<BannerAuditConfig> createBannerAuditConfig(@Valid @RequestBody BannerAuditConfig bannerAuditConfig) throws URISyntaxException {
        log.debug("REST request to save BannerAuditConfig : {}", bannerAuditConfig);
        if (bannerAuditConfig.getId() != null) {
            throw new BadRequestAlertException("A new bannerAuditConfig cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BannerAuditConfig result = bannerAuditConfigRepository.save(bannerAuditConfig);
        bannerAuditConfigSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/banner-audit-configs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /banner-audit-configs : Updates an existing bannerAuditConfig.
     *
     * @param bannerAuditConfig the bannerAuditConfig to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated bannerAuditConfig,
     * or with status 400 (Bad Request) if the bannerAuditConfig is not valid,
     * or with status 500 (Internal Server Error) if the bannerAuditConfig couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/banner-audit-configs")
    @Timed
    public ResponseEntity<BannerAuditConfig> updateBannerAuditConfig(@Valid @RequestBody BannerAuditConfig bannerAuditConfig) throws URISyntaxException {
        log.debug("REST request to update BannerAuditConfig : {}", bannerAuditConfig);
        if (bannerAuditConfig.getId() == null) {
            return createBannerAuditConfig(bannerAuditConfig);
        }
        BannerAuditConfig result = bannerAuditConfigRepository.save(bannerAuditConfig);
        bannerAuditConfigSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, bannerAuditConfig.getId().toString()))
            .body(result);
    }

    /**
     * GET  /banner-audit-configs : get all the bannerAuditConfigs.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of bannerAuditConfigs in body
     */
    @GetMapping("/banner-audit-configs")
    @Timed
    public ResponseEntity<List<BannerAuditConfig>> getAllBannerAuditConfigs(Pageable pageable) {
        log.debug("REST request to get a page of BannerAuditConfigs");
        Page<BannerAuditConfig> page = bannerAuditConfigRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/banner-audit-configs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /banner-audit-configs/:id : get the "id" bannerAuditConfig.
     *
     * @param id the id of the bannerAuditConfig to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the bannerAuditConfig, or with status 404 (Not Found)
     */
    @GetMapping("/banner-audit-configs/{id}")
    @Timed
    public ResponseEntity<BannerAuditConfig> getBannerAuditConfig(@PathVariable Long id) {
        log.debug("REST request to get BannerAuditConfig : {}", id);
        BannerAuditConfig bannerAuditConfig = bannerAuditConfigRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(bannerAuditConfig));
    }

    /**
     * DELETE  /banner-audit-configs/:id : delete the "id" bannerAuditConfig.
     *
     * @param id the id of the bannerAuditConfig to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/banner-audit-configs/{id}")
    @Timed
    public ResponseEntity<Void> deleteBannerAuditConfig(@PathVariable Long id) {
        log.debug("REST request to delete BannerAuditConfig : {}", id);
        bannerAuditConfigRepository.delete(id);
        bannerAuditConfigSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/banner-audit-configs?query=:query : search for the bannerAuditConfig corresponding
     * to the query.
     *
     * @param query the query of the bannerAuditConfig search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/banner-audit-configs")
    @Timed
    public ResponseEntity<List<BannerAuditConfig>> searchBannerAuditConfigs(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of BannerAuditConfigs for query {}", query);
        Page<BannerAuditConfig> page = bannerAuditConfigSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/banner-audit-configs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}

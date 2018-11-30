package com.cus.jastip.banner.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cus.jastip.banner.domain.BannerAudit;

import com.cus.jastip.banner.repository.BannerAuditRepository;
import com.cus.jastip.banner.repository.search.BannerAuditSearchRepository;
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
 * REST controller for managing BannerAudit.
 */
@RestController
@RequestMapping("/api")
public class BannerAuditResource {

    private final Logger log = LoggerFactory.getLogger(BannerAuditResource.class);

    private static final String ENTITY_NAME = "bannerAudit";

    private final BannerAuditRepository bannerAuditRepository;

    private final BannerAuditSearchRepository bannerAuditSearchRepository;

    public BannerAuditResource(BannerAuditRepository bannerAuditRepository, BannerAuditSearchRepository bannerAuditSearchRepository) {
        this.bannerAuditRepository = bannerAuditRepository;
        this.bannerAuditSearchRepository = bannerAuditSearchRepository;
    }

    /**
     * POST  /banner-audits : Create a new bannerAudit.
     *
     * @param bannerAudit the bannerAudit to create
     * @return the ResponseEntity with status 201 (Created) and with body the new bannerAudit, or with status 400 (Bad Request) if the bannerAudit has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/banner-audits")
    @Timed
    public ResponseEntity<BannerAudit> createBannerAudit(@Valid @RequestBody BannerAudit bannerAudit) throws URISyntaxException {
        log.debug("REST request to save BannerAudit : {}", bannerAudit);
        if (bannerAudit.getId() != null) {
            throw new BadRequestAlertException("A new bannerAudit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BannerAudit result = bannerAuditRepository.save(bannerAudit);
        bannerAuditSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/banner-audits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /banner-audits : Updates an existing bannerAudit.
     *
     * @param bannerAudit the bannerAudit to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated bannerAudit,
     * or with status 400 (Bad Request) if the bannerAudit is not valid,
     * or with status 500 (Internal Server Error) if the bannerAudit couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/banner-audits")
    @Timed
    public ResponseEntity<BannerAudit> updateBannerAudit(@Valid @RequestBody BannerAudit bannerAudit) throws URISyntaxException {
        log.debug("REST request to update BannerAudit : {}", bannerAudit);
        if (bannerAudit.getId() == null) {
            return createBannerAudit(bannerAudit);
        }
        BannerAudit result = bannerAuditRepository.save(bannerAudit);
        bannerAuditSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, bannerAudit.getId().toString()))
            .body(result);
    }

    /**
     * GET  /banner-audits : get all the bannerAudits.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of bannerAudits in body
     */
    @GetMapping("/banner-audits")
    @Timed
    public ResponseEntity<List<BannerAudit>> getAllBannerAudits(Pageable pageable) {
        log.debug("REST request to get a page of BannerAudits");
        Page<BannerAudit> page = bannerAuditRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/banner-audits");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /banner-audits/:id : get the "id" bannerAudit.
     *
     * @param id the id of the bannerAudit to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the bannerAudit, or with status 404 (Not Found)
     */
    @GetMapping("/banner-audits/{id}")
    @Timed
    public ResponseEntity<BannerAudit> getBannerAudit(@PathVariable Long id) {
        log.debug("REST request to get BannerAudit : {}", id);
        BannerAudit bannerAudit = bannerAuditRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(bannerAudit));
    }

    /**
     * DELETE  /banner-audits/:id : delete the "id" bannerAudit.
     *
     * @param id the id of the bannerAudit to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/banner-audits/{id}")
    @Timed
    public ResponseEntity<Void> deleteBannerAudit(@PathVariable Long id) {
        log.debug("REST request to delete BannerAudit : {}", id);
        bannerAuditRepository.delete(id);
        bannerAuditSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/banner-audits?query=:query : search for the bannerAudit corresponding
     * to the query.
     *
     * @param query the query of the bannerAudit search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/banner-audits")
    @Timed
    public ResponseEntity<List<BannerAudit>> searchBannerAudits(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of BannerAudits for query {}", query);
        Page<BannerAudit> page = bannerAuditSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/banner-audits");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}

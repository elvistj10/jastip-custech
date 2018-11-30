package com.cus.jastip.master.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cus.jastip.master.domain.MasterAudit;

import com.cus.jastip.master.repository.MasterAuditRepository;
import com.cus.jastip.master.repository.search.MasterAuditSearchRepository;
import com.cus.jastip.master.web.rest.errors.BadRequestAlertException;
import com.cus.jastip.master.web.rest.util.HeaderUtil;
import com.cus.jastip.master.web.rest.util.PaginationUtil;
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
 * REST controller for managing MasterAudit.
 */
@RestController
@RequestMapping("/api")
public class MasterAuditResource {

    private final Logger log = LoggerFactory.getLogger(MasterAuditResource.class);

    private static final String ENTITY_NAME = "masterAudit";

    private final MasterAuditRepository masterAuditRepository;

    private final MasterAuditSearchRepository masterAuditSearchRepository;

    public MasterAuditResource(MasterAuditRepository masterAuditRepository, MasterAuditSearchRepository masterAuditSearchRepository) {
        this.masterAuditRepository = masterAuditRepository;
        this.masterAuditSearchRepository = masterAuditSearchRepository;
    }

    /**
     * POST  /master-audits : Create a new masterAudit.
     *
     * @param masterAudit the masterAudit to create
     * @return the ResponseEntity with status 201 (Created) and with body the new masterAudit, or with status 400 (Bad Request) if the masterAudit has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/master-audits")
    @Timed
    public ResponseEntity<MasterAudit> createMasterAudit(@Valid @RequestBody MasterAudit masterAudit) throws URISyntaxException {
        log.debug("REST request to save MasterAudit : {}", masterAudit);
        if (masterAudit.getId() != null) {
            throw new BadRequestAlertException("A new masterAudit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MasterAudit result = masterAuditRepository.save(masterAudit);
        masterAuditSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/master-audits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /master-audits : Updates an existing masterAudit.
     *
     * @param masterAudit the masterAudit to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated masterAudit,
     * or with status 400 (Bad Request) if the masterAudit is not valid,
     * or with status 500 (Internal Server Error) if the masterAudit couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/master-audits")
    @Timed
    public ResponseEntity<MasterAudit> updateMasterAudit(@Valid @RequestBody MasterAudit masterAudit) throws URISyntaxException {
        log.debug("REST request to update MasterAudit : {}", masterAudit);
        if (masterAudit.getId() == null) {
            return createMasterAudit(masterAudit);
        }
        MasterAudit result = masterAuditRepository.save(masterAudit);
        masterAuditSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, masterAudit.getId().toString()))
            .body(result);
    }

    /**
     * GET  /master-audits : get all the masterAudits.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of masterAudits in body
     */
    @GetMapping("/master-audits")
    @Timed
    public ResponseEntity<List<MasterAudit>> getAllMasterAudits(Pageable pageable) {
        log.debug("REST request to get a page of MasterAudits");
        Page<MasterAudit> page = masterAuditRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/master-audits");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /master-audits/:id : get the "id" masterAudit.
     *
     * @param id the id of the masterAudit to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the masterAudit, or with status 404 (Not Found)
     */
    @GetMapping("/master-audits/{id}")
    @Timed
    public ResponseEntity<MasterAudit> getMasterAudit(@PathVariable Long id) {
        log.debug("REST request to get MasterAudit : {}", id);
        MasterAudit masterAudit = masterAuditRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(masterAudit));
    }

    /**
     * DELETE  /master-audits/:id : delete the "id" masterAudit.
     *
     * @param id the id of the masterAudit to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/master-audits/{id}")
    @Timed
    public ResponseEntity<Void> deleteMasterAudit(@PathVariable Long id) {
        log.debug("REST request to delete MasterAudit : {}", id);
        masterAuditRepository.delete(id);
        masterAuditSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/master-audits?query=:query : search for the masterAudit corresponding
     * to the query.
     *
     * @param query the query of the masterAudit search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/master-audits")
    @Timed
    public ResponseEntity<List<MasterAudit>> searchMasterAudits(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of MasterAudits for query {}", query);
        Page<MasterAudit> page = masterAuditSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/master-audits");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}

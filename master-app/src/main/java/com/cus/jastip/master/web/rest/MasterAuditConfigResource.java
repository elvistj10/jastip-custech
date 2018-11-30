package com.cus.jastip.master.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cus.jastip.master.domain.MasterAuditConfig;

import com.cus.jastip.master.repository.MasterAuditConfigRepository;
import com.cus.jastip.master.repository.search.MasterAuditConfigSearchRepository;
import com.cus.jastip.master.web.rest.errors.BadRequestAlertException;
import com.cus.jastip.master.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing MasterAuditConfig.
 */
@RestController
@RequestMapping("/api")
public class MasterAuditConfigResource {

    private final Logger log = LoggerFactory.getLogger(MasterAuditConfigResource.class);

    private static final String ENTITY_NAME = "masterAuditConfig";

    private final MasterAuditConfigRepository masterAuditConfigRepository;

    private final MasterAuditConfigSearchRepository masterAuditConfigSearchRepository;

    public MasterAuditConfigResource(MasterAuditConfigRepository masterAuditConfigRepository, MasterAuditConfigSearchRepository masterAuditConfigSearchRepository) {
        this.masterAuditConfigRepository = masterAuditConfigRepository;
        this.masterAuditConfigSearchRepository = masterAuditConfigSearchRepository;
    }

    /**
     * POST  /master-audit-configs : Create a new masterAuditConfig.
     *
     * @param masterAuditConfig the masterAuditConfig to create
     * @return the ResponseEntity with status 201 (Created) and with body the new masterAuditConfig, or with status 400 (Bad Request) if the masterAuditConfig has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/master-audit-configs")
    @Timed
    public ResponseEntity<MasterAuditConfig> createMasterAuditConfig(@Valid @RequestBody MasterAuditConfig masterAuditConfig) throws URISyntaxException {
        log.debug("REST request to save MasterAuditConfig : {}", masterAuditConfig);
        if (masterAuditConfig.getId() != null) {
            throw new BadRequestAlertException("A new masterAuditConfig cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MasterAuditConfig result = masterAuditConfigRepository.save(masterAuditConfig);
        masterAuditConfigSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/master-audit-configs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /master-audit-configs : Updates an existing masterAuditConfig.
     *
     * @param masterAuditConfig the masterAuditConfig to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated masterAuditConfig,
     * or with status 400 (Bad Request) if the masterAuditConfig is not valid,
     * or with status 500 (Internal Server Error) if the masterAuditConfig couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/master-audit-configs")
    @Timed
    public ResponseEntity<MasterAuditConfig> updateMasterAuditConfig(@Valid @RequestBody MasterAuditConfig masterAuditConfig) throws URISyntaxException {
        log.debug("REST request to update MasterAuditConfig : {}", masterAuditConfig);
        if (masterAuditConfig.getId() == null) {
            return createMasterAuditConfig(masterAuditConfig);
        }
        MasterAuditConfig result = masterAuditConfigRepository.save(masterAuditConfig);
        masterAuditConfigSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, masterAuditConfig.getId().toString()))
            .body(result);
    }

    /**
     * GET  /master-audit-configs : get all the masterAuditConfigs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of masterAuditConfigs in body
     */
    @GetMapping("/master-audit-configs")
    @Timed
    public List<MasterAuditConfig> getAllMasterAuditConfigs() {
        log.debug("REST request to get all MasterAuditConfigs");
        return masterAuditConfigRepository.findAll();
        }

    /**
     * GET  /master-audit-configs/:id : get the "id" masterAuditConfig.
     *
     * @param id the id of the masterAuditConfig to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the masterAuditConfig, or with status 404 (Not Found)
     */
    @GetMapping("/master-audit-configs/{id}")
    @Timed
    public ResponseEntity<MasterAuditConfig> getMasterAuditConfig(@PathVariable Long id) {
        log.debug("REST request to get MasterAuditConfig : {}", id);
        MasterAuditConfig masterAuditConfig = masterAuditConfigRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(masterAuditConfig));
    }

    /**
     * DELETE  /master-audit-configs/:id : delete the "id" masterAuditConfig.
     *
     * @param id the id of the masterAuditConfig to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/master-audit-configs/{id}")
    @Timed
    public ResponseEntity<Void> deleteMasterAuditConfig(@PathVariable Long id) {
        log.debug("REST request to delete MasterAuditConfig : {}", id);
        masterAuditConfigRepository.delete(id);
        masterAuditConfigSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/master-audit-configs?query=:query : search for the masterAuditConfig corresponding
     * to the query.
     *
     * @param query the query of the masterAuditConfig search
     * @return the result of the search
     */
    @GetMapping("/_search/master-audit-configs")
    @Timed
    public List<MasterAuditConfig> searchMasterAuditConfigs(@RequestParam String query) {
        log.debug("REST request to search MasterAuditConfigs for query {}", query);
        return StreamSupport
            .stream(masterAuditConfigSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}

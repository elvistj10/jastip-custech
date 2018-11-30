package com.cus.jastip.wallet.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cus.jastip.wallet.domain.WalletAuditConfig;

import com.cus.jastip.wallet.repository.WalletAuditConfigRepository;
import com.cus.jastip.wallet.repository.search.WalletAuditConfigSearchRepository;
import com.cus.jastip.wallet.web.rest.errors.BadRequestAlertException;
import com.cus.jastip.wallet.web.rest.util.HeaderUtil;
import com.cus.jastip.wallet.web.rest.util.PaginationUtil;
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
 * REST controller for managing WalletAuditConfig.
 */
@RestController
@RequestMapping("/api")
public class WalletAuditConfigResource {

    private final Logger log = LoggerFactory.getLogger(WalletAuditConfigResource.class);

    private static final String ENTITY_NAME = "walletAuditConfig";

    private final WalletAuditConfigRepository walletAuditConfigRepository;

    private final WalletAuditConfigSearchRepository walletAuditConfigSearchRepository;

    public WalletAuditConfigResource(WalletAuditConfigRepository walletAuditConfigRepository, WalletAuditConfigSearchRepository walletAuditConfigSearchRepository) {
        this.walletAuditConfigRepository = walletAuditConfigRepository;
        this.walletAuditConfigSearchRepository = walletAuditConfigSearchRepository;
    }

    /**
     * POST  /wallet-audit-configs : Create a new walletAuditConfig.
     *
     * @param walletAuditConfig the walletAuditConfig to create
     * @return the ResponseEntity with status 201 (Created) and with body the new walletAuditConfig, or with status 400 (Bad Request) if the walletAuditConfig has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/wallet-audit-configs")
    @Timed
    public ResponseEntity<WalletAuditConfig> createWalletAuditConfig(@Valid @RequestBody WalletAuditConfig walletAuditConfig) throws URISyntaxException {
        log.debug("REST request to save WalletAuditConfig : {}", walletAuditConfig);
        if (walletAuditConfig.getId() != null) {
            throw new BadRequestAlertException("A new walletAuditConfig cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WalletAuditConfig result = walletAuditConfigRepository.save(walletAuditConfig);
        walletAuditConfigSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/wallet-audit-configs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /wallet-audit-configs : Updates an existing walletAuditConfig.
     *
     * @param walletAuditConfig the walletAuditConfig to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated walletAuditConfig,
     * or with status 400 (Bad Request) if the walletAuditConfig is not valid,
     * or with status 500 (Internal Server Error) if the walletAuditConfig couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/wallet-audit-configs")
    @Timed
    public ResponseEntity<WalletAuditConfig> updateWalletAuditConfig(@Valid @RequestBody WalletAuditConfig walletAuditConfig) throws URISyntaxException {
        log.debug("REST request to update WalletAuditConfig : {}", walletAuditConfig);
        if (walletAuditConfig.getId() == null) {
            return createWalletAuditConfig(walletAuditConfig);
        }
        WalletAuditConfig result = walletAuditConfigRepository.save(walletAuditConfig);
        walletAuditConfigSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, walletAuditConfig.getId().toString()))
            .body(result);
    }

    /**
     * GET  /wallet-audit-configs : get all the walletAuditConfigs.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of walletAuditConfigs in body
     */
    @GetMapping("/wallet-audit-configs")
    @Timed
    public ResponseEntity<List<WalletAuditConfig>> getAllWalletAuditConfigs(Pageable pageable) {
        log.debug("REST request to get a page of WalletAuditConfigs");
        Page<WalletAuditConfig> page = walletAuditConfigRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/wallet-audit-configs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /wallet-audit-configs/:id : get the "id" walletAuditConfig.
     *
     * @param id the id of the walletAuditConfig to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the walletAuditConfig, or with status 404 (Not Found)
     */
    @GetMapping("/wallet-audit-configs/{id}")
    @Timed
    public ResponseEntity<WalletAuditConfig> getWalletAuditConfig(@PathVariable Long id) {
        log.debug("REST request to get WalletAuditConfig : {}", id);
        WalletAuditConfig walletAuditConfig = walletAuditConfigRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(walletAuditConfig));
    }

    /**
     * DELETE  /wallet-audit-configs/:id : delete the "id" walletAuditConfig.
     *
     * @param id the id of the walletAuditConfig to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/wallet-audit-configs/{id}")
    @Timed
    public ResponseEntity<Void> deleteWalletAuditConfig(@PathVariable Long id) {
        log.debug("REST request to delete WalletAuditConfig : {}", id);
        walletAuditConfigRepository.delete(id);
        walletAuditConfigSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/wallet-audit-configs?query=:query : search for the walletAuditConfig corresponding
     * to the query.
     *
     * @param query the query of the walletAuditConfig search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/wallet-audit-configs")
    @Timed
    public ResponseEntity<List<WalletAuditConfig>> searchWalletAuditConfigs(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of WalletAuditConfigs for query {}", query);
        Page<WalletAuditConfig> page = walletAuditConfigSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/wallet-audit-configs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}

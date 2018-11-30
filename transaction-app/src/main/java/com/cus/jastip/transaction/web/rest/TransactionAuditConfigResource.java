package com.cus.jastip.transaction.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cus.jastip.transaction.domain.TransactionAuditConfig;

import com.cus.jastip.transaction.repository.TransactionAuditConfigRepository;
import com.cus.jastip.transaction.repository.search.TransactionAuditConfigSearchRepository;
import com.cus.jastip.transaction.web.rest.errors.BadRequestAlertException;
import com.cus.jastip.transaction.web.rest.util.HeaderUtil;
import com.cus.jastip.transaction.web.rest.util.PaginationUtil;
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
 * REST controller for managing TransactionAuditConfig.
 */
@RestController
@RequestMapping("/api")
public class TransactionAuditConfigResource {

    private final Logger log = LoggerFactory.getLogger(TransactionAuditConfigResource.class);

    private static final String ENTITY_NAME = "transactionAuditConfig";

    private final TransactionAuditConfigRepository transactionAuditConfigRepository;

    private final TransactionAuditConfigSearchRepository transactionAuditConfigSearchRepository;

    public TransactionAuditConfigResource(TransactionAuditConfigRepository transactionAuditConfigRepository, TransactionAuditConfigSearchRepository transactionAuditConfigSearchRepository) {
        this.transactionAuditConfigRepository = transactionAuditConfigRepository;
        this.transactionAuditConfigSearchRepository = transactionAuditConfigSearchRepository;
    }

    /**
     * POST  /transaction-audit-configs : Create a new transactionAuditConfig.
     *
     * @param transactionAuditConfig the transactionAuditConfig to create
     * @return the ResponseEntity with status 201 (Created) and with body the new transactionAuditConfig, or with status 400 (Bad Request) if the transactionAuditConfig has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/transaction-audit-configs")
    @Timed
    public ResponseEntity<TransactionAuditConfig> createTransactionAuditConfig(@Valid @RequestBody TransactionAuditConfig transactionAuditConfig) throws URISyntaxException {
        log.debug("REST request to save TransactionAuditConfig : {}", transactionAuditConfig);
        if (transactionAuditConfig.getId() != null) {
            throw new BadRequestAlertException("A new transactionAuditConfig cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TransactionAuditConfig result = transactionAuditConfigRepository.save(transactionAuditConfig);
        transactionAuditConfigSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/transaction-audit-configs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /transaction-audit-configs : Updates an existing transactionAuditConfig.
     *
     * @param transactionAuditConfig the transactionAuditConfig to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated transactionAuditConfig,
     * or with status 400 (Bad Request) if the transactionAuditConfig is not valid,
     * or with status 500 (Internal Server Error) if the transactionAuditConfig couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/transaction-audit-configs")
    @Timed
    public ResponseEntity<TransactionAuditConfig> updateTransactionAuditConfig(@Valid @RequestBody TransactionAuditConfig transactionAuditConfig) throws URISyntaxException {
        log.debug("REST request to update TransactionAuditConfig : {}", transactionAuditConfig);
        if (transactionAuditConfig.getId() == null) {
            return createTransactionAuditConfig(transactionAuditConfig);
        }
        TransactionAuditConfig result = transactionAuditConfigRepository.save(transactionAuditConfig);
        transactionAuditConfigSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, transactionAuditConfig.getId().toString()))
            .body(result);
    }

    /**
     * GET  /transaction-audit-configs : get all the transactionAuditConfigs.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of transactionAuditConfigs in body
     */
    @GetMapping("/transaction-audit-configs")
    @Timed
    public ResponseEntity<List<TransactionAuditConfig>> getAllTransactionAuditConfigs(Pageable pageable) {
        log.debug("REST request to get a page of TransactionAuditConfigs");
        Page<TransactionAuditConfig> page = transactionAuditConfigRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/transaction-audit-configs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /transaction-audit-configs/:id : get the "id" transactionAuditConfig.
     *
     * @param id the id of the transactionAuditConfig to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the transactionAuditConfig, or with status 404 (Not Found)
     */
    @GetMapping("/transaction-audit-configs/{id}")
    @Timed
    public ResponseEntity<TransactionAuditConfig> getTransactionAuditConfig(@PathVariable Long id) {
        log.debug("REST request to get TransactionAuditConfig : {}", id);
        TransactionAuditConfig transactionAuditConfig = transactionAuditConfigRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(transactionAuditConfig));
    }

    /**
     * DELETE  /transaction-audit-configs/:id : delete the "id" transactionAuditConfig.
     *
     * @param id the id of the transactionAuditConfig to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/transaction-audit-configs/{id}")
    @Timed
    public ResponseEntity<Void> deleteTransactionAuditConfig(@PathVariable Long id) {
        log.debug("REST request to delete TransactionAuditConfig : {}", id);
        transactionAuditConfigRepository.delete(id);
        transactionAuditConfigSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/transaction-audit-configs?query=:query : search for the transactionAuditConfig corresponding
     * to the query.
     *
     * @param query the query of the transactionAuditConfig search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/transaction-audit-configs")
    @Timed
    public ResponseEntity<List<TransactionAuditConfig>> searchTransactionAuditConfigs(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of TransactionAuditConfigs for query {}", query);
        Page<TransactionAuditConfig> page = transactionAuditConfigSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/transaction-audit-configs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}

package com.cus.jastip.transaction.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cus.jastip.transaction.domain.TransactionAudit;

import com.cus.jastip.transaction.repository.TransactionAuditRepository;
import com.cus.jastip.transaction.repository.search.TransactionAuditSearchRepository;
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
 * REST controller for managing TransactionAudit.
 */
@RestController
@RequestMapping("/api")
public class TransactionAuditResource {

    private final Logger log = LoggerFactory.getLogger(TransactionAuditResource.class);

    private static final String ENTITY_NAME = "transactionAudit";

    private final TransactionAuditRepository transactionAuditRepository;

    private final TransactionAuditSearchRepository transactionAuditSearchRepository;

    public TransactionAuditResource(TransactionAuditRepository transactionAuditRepository, TransactionAuditSearchRepository transactionAuditSearchRepository) {
        this.transactionAuditRepository = transactionAuditRepository;
        this.transactionAuditSearchRepository = transactionAuditSearchRepository;
    }

    /**
     * POST  /transaction-audits : Create a new transactionAudit.
     *
     * @param transactionAudit the transactionAudit to create
     * @return the ResponseEntity with status 201 (Created) and with body the new transactionAudit, or with status 400 (Bad Request) if the transactionAudit has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/transaction-audits")
    @Timed
    public ResponseEntity<TransactionAudit> createTransactionAudit(@Valid @RequestBody TransactionAudit transactionAudit) throws URISyntaxException {
        log.debug("REST request to save TransactionAudit : {}", transactionAudit);
        if (transactionAudit.getId() != null) {
            throw new BadRequestAlertException("A new transactionAudit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TransactionAudit result = transactionAuditRepository.save(transactionAudit);
        transactionAuditSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/transaction-audits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /transaction-audits : Updates an existing transactionAudit.
     *
     * @param transactionAudit the transactionAudit to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated transactionAudit,
     * or with status 400 (Bad Request) if the transactionAudit is not valid,
     * or with status 500 (Internal Server Error) if the transactionAudit couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/transaction-audits")
    @Timed
    public ResponseEntity<TransactionAudit> updateTransactionAudit(@Valid @RequestBody TransactionAudit transactionAudit) throws URISyntaxException {
        log.debug("REST request to update TransactionAudit : {}", transactionAudit);
        if (transactionAudit.getId() == null) {
            return createTransactionAudit(transactionAudit);
        }
        TransactionAudit result = transactionAuditRepository.save(transactionAudit);
        transactionAuditSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, transactionAudit.getId().toString()))
            .body(result);
    }

    /**
     * GET  /transaction-audits : get all the transactionAudits.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of transactionAudits in body
     */
    @GetMapping("/transaction-audits")
    @Timed
    public ResponseEntity<List<TransactionAudit>> getAllTransactionAudits(Pageable pageable) {
        log.debug("REST request to get a page of TransactionAudits");
        Page<TransactionAudit> page = transactionAuditRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/transaction-audits");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /transaction-audits/:id : get the "id" transactionAudit.
     *
     * @param id the id of the transactionAudit to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the transactionAudit, or with status 404 (Not Found)
     */
    @GetMapping("/transaction-audits/{id}")
    @Timed
    public ResponseEntity<TransactionAudit> getTransactionAudit(@PathVariable Long id) {
        log.debug("REST request to get TransactionAudit : {}", id);
        TransactionAudit transactionAudit = transactionAuditRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(transactionAudit));
    }

    /**
     * DELETE  /transaction-audits/:id : delete the "id" transactionAudit.
     *
     * @param id the id of the transactionAudit to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/transaction-audits/{id}")
    @Timed
    public ResponseEntity<Void> deleteTransactionAudit(@PathVariable Long id) {
        log.debug("REST request to delete TransactionAudit : {}", id);
        transactionAuditRepository.delete(id);
        transactionAuditSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/transaction-audits?query=:query : search for the transactionAudit corresponding
     * to the query.
     *
     * @param query the query of the transactionAudit search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/transaction-audits")
    @Timed
    public ResponseEntity<List<TransactionAudit>> searchTransactionAudits(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of TransactionAudits for query {}", query);
        Page<TransactionAudit> page = transactionAuditSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/transaction-audits");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}

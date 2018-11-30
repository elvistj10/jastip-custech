package com.cus.jastip.wallet.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cus.jastip.wallet.domain.WithdrawalTransferHistory;
import com.cus.jastip.wallet.domain.enumeration.UpdateType;
import com.cus.jastip.wallet.repository.WithdrawalTransferHistoryRepository;
import com.cus.jastip.wallet.repository.search.WithdrawalTransferHistorySearchRepository;
import com.cus.jastip.wallet.service.WalletAuditService;
import com.cus.jastip.wallet.web.rest.errors.BadRequestAlertException;
import com.cus.jastip.wallet.web.rest.util.HeaderUtil;
import com.cus.jastip.wallet.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing WithdrawalTransferHistory.
 */
@RestController
@RequestMapping("/api")
public class WithdrawalTransferHistoryResource {

    private final Logger log = LoggerFactory.getLogger(WithdrawalTransferHistoryResource.class);

    private static final String ENTITY_NAME = "withdrawalTransferHistory";

    private final WithdrawalTransferHistoryRepository withdrawalTransferHistoryRepository;

    private final WithdrawalTransferHistorySearchRepository withdrawalTransferHistorySearchRepository;
    
    @Autowired
    private WalletAuditService walletAuditService;

    public WithdrawalTransferHistoryResource(WithdrawalTransferHistoryRepository withdrawalTransferHistoryRepository, WithdrawalTransferHistorySearchRepository withdrawalTransferHistorySearchRepository) {
        this.withdrawalTransferHistoryRepository = withdrawalTransferHistoryRepository;
        this.withdrawalTransferHistorySearchRepository = withdrawalTransferHistorySearchRepository;
    }

    /**
     * POST  /withdrawal-transfer-histories : Create a new withdrawalTransferHistory.
     *
     * @param withdrawalTransferHistory the withdrawalTransferHistory to create
     * @return the ResponseEntity with status 201 (Created) and with body the new withdrawalTransferHistory, or with status 400 (Bad Request) if the withdrawalTransferHistory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/withdrawal-transfer-histories")
    @Timed
    public ResponseEntity<WithdrawalTransferHistory> createWithdrawalTransferHistory(@RequestBody WithdrawalTransferHistory withdrawalTransferHistory) throws URISyntaxException {
        log.debug("REST request to save WithdrawalTransferHistory : {}", withdrawalTransferHistory);
        if (withdrawalTransferHistory.getId() != null) {
            throw new BadRequestAlertException("A new withdrawalTransferHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WithdrawalTransferHistory result = withdrawalTransferHistoryRepository.save(withdrawalTransferHistory);
        withdrawalTransferHistorySearchRepository.save(result);
        walletAuditService.addWithdrawalTransferHistory(result, ENTITY_NAME, UpdateType.CREATE);
        return ResponseEntity.created(new URI("/api/withdrawal-transfer-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /withdrawal-transfer-histories : Updates an existing withdrawalTransferHistory.
     *
     * @param withdrawalTransferHistory the withdrawalTransferHistory to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated withdrawalTransferHistory,
     * or with status 400 (Bad Request) if the withdrawalTransferHistory is not valid,
     * or with status 500 (Internal Server Error) if the withdrawalTransferHistory couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/withdrawal-transfer-histories")
    @Timed
    public ResponseEntity<WithdrawalTransferHistory> updateWithdrawalTransferHistory(@RequestBody WithdrawalTransferHistory withdrawalTransferHistory) throws URISyntaxException {
        log.debug("REST request to update WithdrawalTransferHistory : {}", withdrawalTransferHistory);
        if (withdrawalTransferHistory.getId() == null) {
            return createWithdrawalTransferHistory(withdrawalTransferHistory);
        }
        WithdrawalTransferHistory result = withdrawalTransferHistoryRepository.save(withdrawalTransferHistory);
        withdrawalTransferHistorySearchRepository.save(result);
        walletAuditService.addWithdrawalTransferHistory(result, ENTITY_NAME, UpdateType.UPDATE);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, withdrawalTransferHistory.getId().toString()))
            .body(result);
    }

    /**
     * GET  /withdrawal-transfer-histories : get all the withdrawalTransferHistories.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of withdrawalTransferHistories in body
     */
    @GetMapping("/withdrawal-transfer-histories")
    @Timed
    public ResponseEntity<List<WithdrawalTransferHistory>> getAllWithdrawalTransferHistories(Pageable pageable) {
        log.debug("REST request to get a page of WithdrawalTransferHistories");
        Page<WithdrawalTransferHistory> page = withdrawalTransferHistoryRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/withdrawal-transfer-histories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /withdrawal-transfer-histories/:id : get the "id" withdrawalTransferHistory.
     *
     * @param id the id of the withdrawalTransferHistory to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the withdrawalTransferHistory, or with status 404 (Not Found)
     */
    @GetMapping("/withdrawal-transfer-histories/{id}")
    @Timed
    public ResponseEntity<WithdrawalTransferHistory> getWithdrawalTransferHistory(@PathVariable Long id) {
        log.debug("REST request to get WithdrawalTransferHistory : {}", id);
        WithdrawalTransferHistory withdrawalTransferHistory = withdrawalTransferHistoryRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(withdrawalTransferHistory));
    }

    /**
     * DELETE  /withdrawal-transfer-histories/:id : delete the "id" withdrawalTransferHistory.
     *
     * @param id the id of the withdrawalTransferHistory to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/withdrawal-transfer-histories/{id}")
    @Timed
    public ResponseEntity<Void> deleteWithdrawalTransferHistory(@PathVariable Long id) {
        log.debug("REST request to delete WithdrawalTransferHistory : {}", id);
        WithdrawalTransferHistory result = withdrawalTransferHistoryRepository.findOne(id);
        withdrawalTransferHistoryRepository.delete(id);
        withdrawalTransferHistorySearchRepository.delete(id);
		walletAuditService.addWithdrawalTransferHistory(result, ENTITY_NAME, UpdateType.DELETE);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/withdrawal-transfer-histories?query=:query : search for the withdrawalTransferHistory corresponding
     * to the query.
     *
     * @param query the query of the withdrawalTransferHistory search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/withdrawal-transfer-histories")
    @Timed
    public ResponseEntity<List<WithdrawalTransferHistory>> searchWithdrawalTransferHistories(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of WithdrawalTransferHistories for query {}", query);
        Page<WithdrawalTransferHistory> page = withdrawalTransferHistorySearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/withdrawal-transfer-histories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}

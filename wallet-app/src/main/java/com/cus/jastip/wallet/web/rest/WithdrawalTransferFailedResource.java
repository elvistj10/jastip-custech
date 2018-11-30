package com.cus.jastip.wallet.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cus.jastip.wallet.domain.WithdrawalTransferFailed;
import com.cus.jastip.wallet.domain.enumeration.UpdateType;
import com.cus.jastip.wallet.repository.WithdrawalTransferFailedRepository;
import com.cus.jastip.wallet.repository.search.WithdrawalTransferFailedSearchRepository;
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
 * REST controller for managing WithdrawalTransferFailed.
 */
@RestController
@RequestMapping("/api")
public class WithdrawalTransferFailedResource {

    private final Logger log = LoggerFactory.getLogger(WithdrawalTransferFailedResource.class);

    private static final String ENTITY_NAME = "withdrawalTransferFailed";

    private final WithdrawalTransferFailedRepository withdrawalTransferFailedRepository;

    private final WithdrawalTransferFailedSearchRepository withdrawalTransferFailedSearchRepository;
    
    @Autowired
    private WalletAuditService walletAuditService;

    public WithdrawalTransferFailedResource(WithdrawalTransferFailedRepository withdrawalTransferFailedRepository, WithdrawalTransferFailedSearchRepository withdrawalTransferFailedSearchRepository) {
        this.withdrawalTransferFailedRepository = withdrawalTransferFailedRepository;
        this.withdrawalTransferFailedSearchRepository = withdrawalTransferFailedSearchRepository;
    }

    /**
     * POST  /withdrawal-transfer-faileds : Create a new withdrawalTransferFailed.
     *
     * @param withdrawalTransferFailed the withdrawalTransferFailed to create
     * @return the ResponseEntity with status 201 (Created) and with body the new withdrawalTransferFailed, or with status 400 (Bad Request) if the withdrawalTransferFailed has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/withdrawal-transfer-faileds")
    @Timed
    public ResponseEntity<WithdrawalTransferFailed> createWithdrawalTransferFailed(@RequestBody WithdrawalTransferFailed withdrawalTransferFailed) throws URISyntaxException {
        log.debug("REST request to save WithdrawalTransferFailed : {}", withdrawalTransferFailed);
        if (withdrawalTransferFailed.getId() != null) {
            throw new BadRequestAlertException("A new withdrawalTransferFailed cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WithdrawalTransferFailed result = withdrawalTransferFailedRepository.save(withdrawalTransferFailed);
        withdrawalTransferFailedSearchRepository.save(result);
        walletAuditService.addWithdrawalTransferFailed(result, ENTITY_NAME, UpdateType.CREATE);
        return ResponseEntity.created(new URI("/api/withdrawal-transfer-faileds/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /withdrawal-transfer-faileds : Updates an existing withdrawalTransferFailed.
     *
     * @param withdrawalTransferFailed the withdrawalTransferFailed to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated withdrawalTransferFailed,
     * or with status 400 (Bad Request) if the withdrawalTransferFailed is not valid,
     * or with status 500 (Internal Server Error) if the withdrawalTransferFailed couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/withdrawal-transfer-faileds")
    @Timed
    public ResponseEntity<WithdrawalTransferFailed> updateWithdrawalTransferFailed(@RequestBody WithdrawalTransferFailed withdrawalTransferFailed) throws URISyntaxException {
        log.debug("REST request to update WithdrawalTransferFailed : {}", withdrawalTransferFailed);
        if (withdrawalTransferFailed.getId() == null) {
            return createWithdrawalTransferFailed(withdrawalTransferFailed);
        }
        WithdrawalTransferFailed result = withdrawalTransferFailedRepository.save(withdrawalTransferFailed);
        withdrawalTransferFailedSearchRepository.save(result);
        walletAuditService.addWithdrawalTransferFailed(result, ENTITY_NAME, UpdateType.UPDATE);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, withdrawalTransferFailed.getId().toString()))
            .body(result);
    }

    /**
     * GET  /withdrawal-transfer-faileds : get all the withdrawalTransferFaileds.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of withdrawalTransferFaileds in body
     */
    @GetMapping("/withdrawal-transfer-faileds")
    @Timed
    public ResponseEntity<List<WithdrawalTransferFailed>> getAllWithdrawalTransferFaileds(Pageable pageable) {
        log.debug("REST request to get a page of WithdrawalTransferFaileds");
        Page<WithdrawalTransferFailed> page = withdrawalTransferFailedRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/withdrawal-transfer-faileds");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /withdrawal-transfer-faileds/:id : get the "id" withdrawalTransferFailed.
     *
     * @param id the id of the withdrawalTransferFailed to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the withdrawalTransferFailed, or with status 404 (Not Found)
     */
    @GetMapping("/withdrawal-transfer-faileds/{id}")
    @Timed
    public ResponseEntity<WithdrawalTransferFailed> getWithdrawalTransferFailed(@PathVariable Long id) {
        log.debug("REST request to get WithdrawalTransferFailed : {}", id);
        WithdrawalTransferFailed withdrawalTransferFailed = withdrawalTransferFailedRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(withdrawalTransferFailed));
    }

    /**
     * DELETE  /withdrawal-transfer-faileds/:id : delete the "id" withdrawalTransferFailed.
     *
     * @param id the id of the withdrawalTransferFailed to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/withdrawal-transfer-faileds/{id}")
    @Timed
    public ResponseEntity<Void> deleteWithdrawalTransferFailed(@PathVariable Long id) {
        log.debug("REST request to delete WithdrawalTransferFailed : {}", id);
        WithdrawalTransferFailed result = withdrawalTransferFailedRepository.findOne(id);
        withdrawalTransferFailedRepository.delete(id);
        withdrawalTransferFailedSearchRepository.delete(id);
		walletAuditService.addWithdrawalTransferFailed(result, ENTITY_NAME, UpdateType.DELETE);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/withdrawal-transfer-faileds?query=:query : search for the withdrawalTransferFailed corresponding
     * to the query.
     *
     * @param query the query of the withdrawalTransferFailed search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/withdrawal-transfer-faileds")
    @Timed
    public ResponseEntity<List<WithdrawalTransferFailed>> searchWithdrawalTransferFaileds(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of WithdrawalTransferFaileds for query {}", query);
        Page<WithdrawalTransferFailed> page = withdrawalTransferFailedSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/withdrawal-transfer-faileds");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}

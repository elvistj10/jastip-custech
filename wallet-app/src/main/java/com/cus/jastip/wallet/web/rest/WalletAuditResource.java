package com.cus.jastip.wallet.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cus.jastip.wallet.domain.WalletAudit;

import com.cus.jastip.wallet.repository.WalletAuditRepository;
import com.cus.jastip.wallet.repository.search.WalletAuditSearchRepository;
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
 * REST controller for managing WalletAudit.
 */
@RestController
@RequestMapping("/api")
public class WalletAuditResource {

    private final Logger log = LoggerFactory.getLogger(WalletAuditResource.class);

    private static final String ENTITY_NAME = "walletAudit";

    private final WalletAuditRepository walletAuditRepository;

    private final WalletAuditSearchRepository walletAuditSearchRepository;

    public WalletAuditResource(WalletAuditRepository walletAuditRepository, WalletAuditSearchRepository walletAuditSearchRepository) {
        this.walletAuditRepository = walletAuditRepository;
        this.walletAuditSearchRepository = walletAuditSearchRepository;
    }

    /**
     * POST  /wallet-audits : Create a new walletAudit.
     *
     * @param walletAudit the walletAudit to create
     * @return the ResponseEntity with status 201 (Created) and with body the new walletAudit, or with status 400 (Bad Request) if the walletAudit has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/wallet-audits")
    @Timed
    public ResponseEntity<WalletAudit> createWalletAudit(@Valid @RequestBody WalletAudit walletAudit) throws URISyntaxException {
        log.debug("REST request to save WalletAudit : {}", walletAudit);
        if (walletAudit.getId() != null) {
            throw new BadRequestAlertException("A new walletAudit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WalletAudit result = walletAuditRepository.save(walletAudit);
        walletAuditSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/wallet-audits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /wallet-audits : Updates an existing walletAudit.
     *
     * @param walletAudit the walletAudit to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated walletAudit,
     * or with status 400 (Bad Request) if the walletAudit is not valid,
     * or with status 500 (Internal Server Error) if the walletAudit couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/wallet-audits")
    @Timed
    public ResponseEntity<WalletAudit> updateWalletAudit(@Valid @RequestBody WalletAudit walletAudit) throws URISyntaxException {
        log.debug("REST request to update WalletAudit : {}", walletAudit);
        if (walletAudit.getId() == null) {
            return createWalletAudit(walletAudit);
        }
        WalletAudit result = walletAuditRepository.save(walletAudit);
        walletAuditSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, walletAudit.getId().toString()))
            .body(result);
    }

    /**
     * GET  /wallet-audits : get all the walletAudits.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of walletAudits in body
     */
    @GetMapping("/wallet-audits")
    @Timed
    public ResponseEntity<List<WalletAudit>> getAllWalletAudits(Pageable pageable) {
        log.debug("REST request to get a page of WalletAudits");
        Page<WalletAudit> page = walletAuditRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/wallet-audits");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /wallet-audits/:id : get the "id" walletAudit.
     *
     * @param id the id of the walletAudit to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the walletAudit, or with status 404 (Not Found)
     */
    @GetMapping("/wallet-audits/{id}")
    @Timed
    public ResponseEntity<WalletAudit> getWalletAudit(@PathVariable Long id) {
        log.debug("REST request to get WalletAudit : {}", id);
        WalletAudit walletAudit = walletAuditRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(walletAudit));
    }

    /**
     * DELETE  /wallet-audits/:id : delete the "id" walletAudit.
     *
     * @param id the id of the walletAudit to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/wallet-audits/{id}")
    @Timed
    public ResponseEntity<Void> deleteWalletAudit(@PathVariable Long id) {
        log.debug("REST request to delete WalletAudit : {}", id);
        walletAuditRepository.delete(id);
        walletAuditSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/wallet-audits?query=:query : search for the walletAudit corresponding
     * to the query.
     *
     * @param query the query of the walletAudit search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/wallet-audits")
    @Timed
    public ResponseEntity<List<WalletAudit>> searchWalletAudits(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of WalletAudits for query {}", query);
        Page<WalletAudit> page = walletAuditSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/wallet-audits");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}

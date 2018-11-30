package com.cus.jastip.wallet.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cus.jastip.wallet.domain.WalletTransaction;
import com.cus.jastip.wallet.domain.enumeration.UpdateType;
import com.cus.jastip.wallet.repository.WalletTransactionRepository;
import com.cus.jastip.wallet.repository.search.WalletTransactionSearchRepository;
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
 * REST controller for managing WalletTransaction.
 */
@RestController
@RequestMapping("/api")
public class WalletTransactionResource {

	private final Logger log = LoggerFactory.getLogger(WalletTransactionResource.class);

	private static final String ENTITY_NAME = "walletTransaction";

	private final WalletTransactionRepository walletTransactionRepository;

	private final WalletTransactionSearchRepository walletTransactionSearchRepository;

	@Autowired
	private WalletAuditService walletAuditService;

	public WalletTransactionResource(WalletTransactionRepository walletTransactionRepository,
			WalletTransactionSearchRepository walletTransactionSearchRepository) {
		this.walletTransactionRepository = walletTransactionRepository;
		this.walletTransactionSearchRepository = walletTransactionSearchRepository;
	}

	/**
	 * POST /wallet-transactions : Create a new walletTransaction.
	 *
	 * @param walletTransaction
	 *            the walletTransaction to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         walletTransaction, or with status 400 (Bad Request) if the
	 *         walletTransaction has already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/wallet-transactions")
	@Timed
	public ResponseEntity<WalletTransaction> createWalletTransaction(@RequestBody WalletTransaction walletTransaction)
			throws URISyntaxException {
		log.debug("REST request to save WalletTransaction : {}", walletTransaction);
		if (walletTransaction.getId() != null) {
			throw new BadRequestAlertException("A new walletTransaction cannot already have an ID", ENTITY_NAME,
					"idexists");
		}
		WalletTransaction result = walletTransactionRepository.save(walletTransaction);
		walletTransactionSearchRepository.save(result);
		walletAuditService.addWalletTransaction(result, ENTITY_NAME, UpdateType.CREATE);
		return ResponseEntity.created(new URI("/api/wallet-transactions/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /wallet-transactions : Updates an existing walletTransaction.
	 *
	 * @param walletTransaction
	 *            the walletTransaction to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         walletTransaction, or with status 400 (Bad Request) if the
	 *         walletTransaction is not valid, or with status 500 (Internal Server
	 *         Error) if the walletTransaction couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/wallet-transactions")
	@Timed
	public ResponseEntity<WalletTransaction> updateWalletTransaction(@RequestBody WalletTransaction walletTransaction)
			throws URISyntaxException {
		log.debug("REST request to update WalletTransaction : {}", walletTransaction);
		if (walletTransaction.getId() == null) {
			return createWalletTransaction(walletTransaction);
		}
		WalletTransaction result = walletTransactionRepository.save(walletTransaction);
		walletTransactionSearchRepository.save(result);
		walletAuditService.addWalletTransaction(result, ENTITY_NAME, UpdateType.UPDATE);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, walletTransaction.getId().toString()))
				.body(result);
	}

	/**
	 * GET /wallet-transactions : get all the walletTransactions.
	 *
	 * @param pageable
	 *            the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         walletTransactions in body
	 */
	@GetMapping("/wallet-transactions")
	@Timed
	public ResponseEntity<List<WalletTransaction>> getAllWalletTransactions(Pageable pageable) {
		log.debug("REST request to get a page of WalletTransactions");
		Page<WalletTransaction> page = walletTransactionRepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/wallet-transactions");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /wallet-transactions/:id : get the "id" walletTransaction.
	 *
	 * @param id
	 *            the id of the walletTransaction to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         walletTransaction, or with status 404 (Not Found)
	 */
	@GetMapping("/wallet-transactions/{id}")
	@Timed
	public ResponseEntity<WalletTransaction> getWalletTransaction(@PathVariable Long id) {
		log.debug("REST request to get WalletTransaction : {}", id);
		WalletTransaction walletTransaction = walletTransactionRepository.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(walletTransaction));
	}

	/**
	 * DELETE /wallet-transactions/:id : delete the "id" walletTransaction.
	 *
	 * @param id
	 *            the id of the walletTransaction to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/wallet-transactions/{id}")
	@Timed
	public ResponseEntity<Void> deleteWalletTransaction(@PathVariable Long id) {
		log.debug("REST request to delete WalletTransaction : {}", id);
		WalletTransaction result = walletTransactionRepository.findOne(id);
		walletTransactionRepository.delete(id);
		walletTransactionSearchRepository.delete(id);
		walletAuditService.addWalletTransaction(result, ENTITY_NAME, UpdateType.DELETE);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * SEARCH /_search/wallet-transactions?query=:query : search for the
	 * walletTransaction corresponding to the query.
	 *
	 * @param query
	 *            the query of the walletTransaction search
	 * @param pageable
	 *            the pagination information
	 * @return the result of the search
	 */
	@GetMapping("/_search/wallet-transactions")
	@Timed
	public ResponseEntity<List<WalletTransaction>> searchWalletTransactions(@RequestParam String query,
			Pageable pageable) {
		log.debug("REST request to search for a page of WalletTransactions for query {}", query);
		Page<WalletTransaction> page = walletTransactionSearchRepository.search(queryStringQuery(query), pageable);
		HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page,
				"/api/_search/wallet-transactions");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

}

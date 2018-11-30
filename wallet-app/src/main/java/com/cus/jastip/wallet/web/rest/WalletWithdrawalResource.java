package com.cus.jastip.wallet.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cus.jastip.wallet.domain.WalletWithdrawal;
import com.cus.jastip.wallet.domain.WithdrawalTransferList;
import com.cus.jastip.wallet.domain.enumeration.UpdateType;
import com.cus.jastip.wallet.repository.WalletWithdrawalRepository;
import com.cus.jastip.wallet.repository.search.WalletWithdrawalSearchRepository;
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

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing WalletWithdrawal.
 */
@RestController
@RequestMapping("/api")
public class WalletWithdrawalResource {

	private final Logger log = LoggerFactory.getLogger(WalletWithdrawalResource.class);

	private static final String ENTITY_NAME = "walletWithdrawal";

	private final WalletWithdrawalRepository walletWithdrawalRepository;

	private final WalletWithdrawalSearchRepository walletWithdrawalSearchRepository;

	@Autowired
	private WalletAuditService walletAuditService;

	@Autowired
	private WithdrawalTransferListResource withdrawalTransferListResource;

	public WalletWithdrawalResource(WalletWithdrawalRepository walletWithdrawalRepository,
			WalletWithdrawalSearchRepository walletWithdrawalSearchRepository) {
		this.walletWithdrawalRepository = walletWithdrawalRepository;
		this.walletWithdrawalSearchRepository = walletWithdrawalSearchRepository;
	}

	/**
	 * POST /wallet-withdrawals : Create a new walletWithdrawal.
	 *
	 * @param walletWithdrawal
	 *            the walletWithdrawal to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         walletWithdrawal, or with status 400 (Bad Request) if the
	 *         walletWithdrawal has already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/wallet-withdrawals")
	@Timed
	public ResponseEntity<WalletWithdrawal> createWalletWithdrawal(
			@Valid @RequestBody WalletWithdrawal walletWithdrawal) throws URISyntaxException {
		log.debug("REST request to save WalletWithdrawal : {}", walletWithdrawal);
		if (walletWithdrawal.getId() != null) {
			throw new BadRequestAlertException("A new walletWithdrawal cannot already have an ID", ENTITY_NAME,
					"idexists");
		}
		WalletWithdrawal result = walletWithdrawalRepository.save(walletWithdrawal);
		walletWithdrawalSearchRepository.save(result);
		walletAuditService.addWalletWithdrawal(result, ENTITY_NAME, UpdateType.CREATE);
		WithdrawalTransferList wList = new WithdrawalTransferList();
		wList.setDestBankAccount(walletWithdrawal.getDestBankAccount());
		wList.setDestBankName(walletWithdrawal.getDestBankName());
		wList.setNominal(walletWithdrawal.getNominal());
		wList.setWithdrawalId(result.getId());
		withdrawalTransferListResource.createWithdrawalTransferList(wList);
		return ResponseEntity.created(new URI("/api/wallet-withdrawals/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /wallet-withdrawals : Updates an existing walletWithdrawal.
	 *
	 * @param walletWithdrawal
	 *            the walletWithdrawal to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         walletWithdrawal, or with status 400 (Bad Request) if the
	 *         walletWithdrawal is not valid, or with status 500 (Internal Server
	 *         Error) if the walletWithdrawal couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/wallet-withdrawals")
	@Timed
	public ResponseEntity<WalletWithdrawal> updateWalletWithdrawal(
			@Valid @RequestBody WalletWithdrawal walletWithdrawal) throws URISyntaxException {
		log.debug("REST request to update WalletWithdrawal : {}", walletWithdrawal);
		if (walletWithdrawal.getId() == null) {
			return createWalletWithdrawal(walletWithdrawal);
		}
		WalletWithdrawal result = walletWithdrawalRepository.save(walletWithdrawal);
		walletWithdrawalSearchRepository.save(result);
		walletAuditService.addWalletWithdrawal(result, ENTITY_NAME, UpdateType.UPDATE);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, walletWithdrawal.getId().toString()))
				.body(result);
	}

	/**
	 * GET /wallet-withdrawals : get all the walletWithdrawals.
	 *
	 * @param pageable
	 *            the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         walletWithdrawals in body
	 */
	@GetMapping("/wallet-withdrawals")
	@Timed
	public ResponseEntity<List<WalletWithdrawal>> getAllWalletWithdrawals(Pageable pageable) {
		log.debug("REST request to get a page of WalletWithdrawals");
		Page<WalletWithdrawal> page = walletWithdrawalRepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/wallet-withdrawals");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /wallet-withdrawals/:id : get the "id" walletWithdrawal.
	 *
	 * @param id
	 *            the id of the walletWithdrawal to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         walletWithdrawal, or with status 404 (Not Found)
	 */
	@GetMapping("/wallet-withdrawals/{id}")
	@Timed
	public ResponseEntity<WalletWithdrawal> getWalletWithdrawal(@PathVariable Long id) {
		log.debug("REST request to get WalletWithdrawal : {}", id);
		WalletWithdrawal walletWithdrawal = walletWithdrawalRepository.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(walletWithdrawal));
	}

	/**
	 * DELETE /wallet-withdrawals/:id : delete the "id" walletWithdrawal.
	 *
	 * @param id
	 *            the id of the walletWithdrawal to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/wallet-withdrawals/{id}")
	@Timed
	public ResponseEntity<Void> deleteWalletWithdrawal(@PathVariable Long id) {
		log.debug("REST request to delete WalletWithdrawal : {}", id);
		WalletWithdrawal result = walletWithdrawalRepository.findOne(id);
		walletWithdrawalRepository.delete(id);
		walletWithdrawalSearchRepository.delete(id);
		walletAuditService.addWalletWithdrawal(result, ENTITY_NAME, UpdateType.DELETE);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * SEARCH /_search/wallet-withdrawals?query=:query : search for the
	 * walletWithdrawal corresponding to the query.
	 *
	 * @param query
	 *            the query of the walletWithdrawal search
	 * @param pageable
	 *            the pagination information
	 * @return the result of the search
	 */
	@GetMapping("/_search/wallet-withdrawals")
	@Timed
	public ResponseEntity<List<WalletWithdrawal>> searchWalletWithdrawals(@RequestParam String query,
			Pageable pageable) {
		log.debug("REST request to search for a page of WalletWithdrawals for query {}", query);
		Page<WalletWithdrawal> page = walletWithdrawalSearchRepository.search(queryStringQuery(query), pageable);
		HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page,
				"/api/_search/wallet-withdrawals");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

}

package com.cus.jastip.wallet.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cus.jastip.wallet.domain.WithdrawalTransferList;
import com.cus.jastip.wallet.domain.enumeration.UpdateType;
import com.cus.jastip.wallet.repository.WithdrawalTransferListRepository;
import com.cus.jastip.wallet.repository.search.WithdrawalTransferListSearchRepository;
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
 * REST controller for managing WithdrawalTransferList.
 */
@RestController
@RequestMapping("/api")
public class WithdrawalTransferListResource {

	private final Logger log = LoggerFactory.getLogger(WithdrawalTransferListResource.class);

	private static final String ENTITY_NAME = "withdrawalTransferList";

	private final WithdrawalTransferListRepository withdrawalTransferListRepository;

	private final WithdrawalTransferListSearchRepository withdrawalTransferListSearchRepository;

	@Autowired
	private WalletAuditService walletAuditService;

	public WithdrawalTransferListResource(WithdrawalTransferListRepository withdrawalTransferListRepository,
			WithdrawalTransferListSearchRepository withdrawalTransferListSearchRepository) {
		this.withdrawalTransferListRepository = withdrawalTransferListRepository;
		this.withdrawalTransferListSearchRepository = withdrawalTransferListSearchRepository;
	}

	/**
	 * POST /withdrawal-transfer-lists : Create a new withdrawalTransferList.
	 *
	 * @param withdrawalTransferList
	 *            the withdrawalTransferList to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         withdrawalTransferList, or with status 400 (Bad Request) if the
	 *         withdrawalTransferList has already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/withdrawal-transfer-lists")
	@Timed
	public ResponseEntity<WithdrawalTransferList> createWithdrawalTransferList(
			@RequestBody WithdrawalTransferList withdrawalTransferList) throws URISyntaxException {
		log.debug("REST request to save WithdrawalTransferList : {}", withdrawalTransferList);
		if (withdrawalTransferList.getId() != null) {
			throw new BadRequestAlertException("A new withdrawalTransferList cannot already have an ID", ENTITY_NAME,
					"idexists");
		}
		WithdrawalTransferList result = withdrawalTransferListRepository.save(withdrawalTransferList);
		withdrawalTransferListSearchRepository.save(result);
		walletAuditService.addWithdrawalTransferList(result, ENTITY_NAME, UpdateType.CREATE);
		return ResponseEntity.created(new URI("/api/withdrawal-transfer-lists/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /withdrawal-transfer-lists : Updates an existing withdrawalTransferList.
	 *
	 * @param withdrawalTransferList
	 *            the withdrawalTransferList to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         withdrawalTransferList, or with status 400 (Bad Request) if the
	 *         withdrawalTransferList is not valid, or with status 500 (Internal
	 *         Server Error) if the withdrawalTransferList couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/withdrawal-transfer-lists")
	@Timed
	public ResponseEntity<WithdrawalTransferList> updateWithdrawalTransferList(
			@RequestBody WithdrawalTransferList withdrawalTransferList) throws URISyntaxException {
		log.debug("REST request to update WithdrawalTransferList : {}", withdrawalTransferList);
		if (withdrawalTransferList.getId() == null) {
			return createWithdrawalTransferList(withdrawalTransferList);
		}
		WithdrawalTransferList result = withdrawalTransferListRepository.save(withdrawalTransferList);
		withdrawalTransferListSearchRepository.save(result);
		walletAuditService.addWithdrawalTransferList(result, ENTITY_NAME, UpdateType.UPDATE);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, withdrawalTransferList.getId().toString()))
				.body(result);
	}

	/**
	 * GET /withdrawal-transfer-lists : get all the withdrawalTransferLists.
	 *
	 * @param pageable
	 *            the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         withdrawalTransferLists in body
	 */
	@GetMapping("/withdrawal-transfer-lists")
	@Timed
	public ResponseEntity<List<WithdrawalTransferList>> getAllWithdrawalTransferLists(Pageable pageable) {
		log.debug("REST request to get a page of WithdrawalTransferLists");
		Page<WithdrawalTransferList> page = withdrawalTransferListRepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/withdrawal-transfer-lists");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /withdrawal-transfer-lists/:id : get the "id" withdrawalTransferList.
	 *
	 * @param id
	 *            the id of the withdrawalTransferList to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         withdrawalTransferList, or with status 404 (Not Found)
	 */
	@GetMapping("/withdrawal-transfer-lists/{id}")
	@Timed
	public ResponseEntity<WithdrawalTransferList> getWithdrawalTransferList(@PathVariable Long id) {
		log.debug("REST request to get WithdrawalTransferList : {}", id);
		WithdrawalTransferList withdrawalTransferList = withdrawalTransferListRepository.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(withdrawalTransferList));
	}

	/**
	 * DELETE /withdrawal-transfer-lists/:id : delete the "id"
	 * withdrawalTransferList.
	 *
	 * @param id
	 *            the id of the withdrawalTransferList to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/withdrawal-transfer-lists/{id}")
	@Timed
	public ResponseEntity<Void> deleteWithdrawalTransferList(@PathVariable Long id) {
		log.debug("REST request to delete WithdrawalTransferList : {}", id);
		WithdrawalTransferList result = withdrawalTransferListRepository.findOne(id);
		withdrawalTransferListRepository.delete(id);
		withdrawalTransferListSearchRepository.delete(id);
		walletAuditService.addWithdrawalTransferList(result, ENTITY_NAME, UpdateType.DELETE);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * SEARCH /_search/withdrawal-transfer-lists?query=:query : search for the
	 * withdrawalTransferList corresponding to the query.
	 *
	 * @param query
	 *            the query of the withdrawalTransferList search
	 * @param pageable
	 *            the pagination information
	 * @return the result of the search
	 */
	@GetMapping("/_search/withdrawal-transfer-lists")
	@Timed
	public ResponseEntity<List<WithdrawalTransferList>> searchWithdrawalTransferLists(@RequestParam String query,
			Pageable pageable) {
		log.debug("REST request to search for a page of WithdrawalTransferLists for query {}", query);
		Page<WithdrawalTransferList> page = withdrawalTransferListSearchRepository.search(queryStringQuery(query),
				pageable);
		HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page,
				"/api/_search/withdrawal-transfer-lists");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

}

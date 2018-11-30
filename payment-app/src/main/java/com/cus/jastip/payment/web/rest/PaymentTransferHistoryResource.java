package com.cus.jastip.payment.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cus.jastip.payment.domain.Payment;
import com.cus.jastip.payment.domain.PaymentTransferHistory;
import com.cus.jastip.payment.domain.enumeration.UpdateType;
import com.cus.jastip.payment.repository.PaymentTransferHistoryRepository;
import com.cus.jastip.payment.repository.search.PaymentTransferHistorySearchRepository;
import com.cus.jastip.payment.service.PaymentAuditService;
import com.cus.jastip.payment.web.rest.errors.BadRequestAlertException;
import com.cus.jastip.payment.web.rest.util.HeaderUtil;
import com.cus.jastip.payment.web.rest.util.PaginationUtil;

import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
 * REST controller for managing PaymentTransferHistory.
 */
@RestController
@RequestMapping("/api")
public class PaymentTransferHistoryResource {
	
	@Autowired
	private PaymentAuditService paymentAuditService;

	private final Logger log = LoggerFactory.getLogger(PaymentTransferHistoryResource.class);

	private static final String ENTITY_NAME = "paymentTransferHistory";

	private final PaymentTransferHistoryRepository paymentTransferHistoryRepository;

	private final PaymentTransferHistorySearchRepository paymentTransferHistorySearchRepository;

	public PaymentTransferHistoryResource(PaymentTransferHistoryRepository paymentTransferHistoryRepository,
			PaymentTransferHistorySearchRepository paymentTransferHistorySearchRepository) {
		this.paymentTransferHistoryRepository = paymentTransferHistoryRepository;
		this.paymentTransferHistorySearchRepository = paymentTransferHistorySearchRepository;
	}

	/**
	 * POST /payment-transfer-histories : Create a new paymentTransferHistory.
	 *
	 * @param paymentTransferHistory
	 *            the paymentTransferHistory to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         paymentTransferHistory, or with status 400 (Bad Request) if the
	 *         paymentTransferHistory has already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/payment-transfer-histories")
	@Timed
	public ResponseEntity<PaymentTransferHistory> createPaymentTransferHistory(
			@Valid @RequestBody PaymentTransferHistory paymentTransferHistory) throws URISyntaxException {
		log.debug("REST request to save PaymentTransferHistory : {}", paymentTransferHistory);
		if (paymentTransferHistory.getId() != null) {
			throw new BadRequestAlertException("A new paymentTransferHistory cannot already have an ID", ENTITY_NAME,
					"idexists");
		}
		PaymentTransferHistory result = paymentTransferHistoryRepository.save(paymentTransferHistory);
		paymentTransferHistorySearchRepository.save(result);
		paymentAuditService.addPaymentTransferHistory(result, ENTITY_NAME, UpdateType.CREATE);
		return ResponseEntity.created(new URI("/api/payment-transfer-histories/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /payment-transfer-histories : Updates an existing paymentTransferHistory.
	 *
	 * @param paymentTransferHistory
	 *            the paymentTransferHistory to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         paymentTransferHistory, or with status 400 (Bad Request) if the
	 *         paymentTransferHistory is not valid, or with status 500 (Internal
	 *         Server Error) if the paymentTransferHistory couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/payment-transfer-histories")
	@Timed
	public ResponseEntity<PaymentTransferHistory> updatePaymentTransferHistory(
			@Valid @RequestBody PaymentTransferHistory paymentTransferHistory) throws URISyntaxException {
		log.debug("REST request to update PaymentTransferHistory : {}", paymentTransferHistory);
		if (paymentTransferHistory.getId() == null) {
			return createPaymentTransferHistory(paymentTransferHistory);
		}
		PaymentTransferHistory result = paymentTransferHistoryRepository.save(paymentTransferHistory);
		paymentTransferHistorySearchRepository.save(result);
		paymentAuditService.addPaymentTransferHistory(result, ENTITY_NAME, UpdateType.UPDATE);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, paymentTransferHistory.getId().toString()))
				.body(result);
	}

	/**
	 * GET /payment-transfer-histories : get all the paymentTransferHistories.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         paymentTransferHistories in body
	 */
	@GetMapping("/payment-transfer-histories")
	@Timed
	public ResponseEntity<List<PaymentTransferHistory>> getAllPaymentTransferHistories(Pageable pageable) {
		log.debug("REST request to get all PaymentTransferHistories");
		Page<PaymentTransferHistory> page = paymentTransferHistoryRepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/payment-transfer-histories");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
		
	}

	/**
	 * GET /payment-transfer-histories/:id : get the "id" paymentTransferHistory.
	 *
	 * @param id
	 *            the id of the paymentTransferHistory to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         paymentTransferHistory, or with status 404 (Not Found)
	 */
	@GetMapping("/payment-transfer-histories/{id}")
	@Timed
	public ResponseEntity<PaymentTransferHistory> getPaymentTransferHistory(@PathVariable Long id) {
		log.debug("REST request to get PaymentTransferHistory : {}", id);
		PaymentTransferHistory paymentTransferHistory = paymentTransferHistoryRepository.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(paymentTransferHistory));
	}

	/**
	 * DELETE /payment-transfer-histories/:id : delete the "id"
	 * paymentTransferHistory.
	 *
	 * @param id
	 *            the id of the paymentTransferHistory to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/payment-transfer-histories/{id}")
	@Timed
	public ResponseEntity<Void> deletePaymentTransferHistory(@PathVariable Long id) {
		log.debug("REST request to delete PaymentTransferHistory : {}", id);
		PaymentTransferHistory result = paymentTransferHistoryRepository.findOne(id);
		paymentTransferHistoryRepository.delete(id);
		paymentTransferHistorySearchRepository.delete(id);
		paymentAuditService.addPaymentTransferHistory(result, ENTITY_NAME, UpdateType.DELETE);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * SEARCH /_search/payment-transfer-histories?query=:query : search for the
	 * paymentTransferHistory corresponding to the query.
	 *
	 * @param query
	 *            the query of the paymentTransferHistory search
	 * @return the result of the search
	 */
	@GetMapping("/_search/payment-transfer-histories")
	@Timed
	public ResponseEntity<List<PaymentTransferHistory>> searchPaymentTransferHistories(@RequestParam String query,
			Pageable pageable) {
		log.debug("REST request to search PaymentTransferHistories for query {}", query);
		List<PaymentTransferHistory> payment = StreamSupport
				.stream(paymentTransferHistorySearchRepository.search(queryStringQuery(query), pageable).spliterator(),
						false)
				.collect(Collectors.toList());
		Page<PaymentTransferHistory> page = new PageImpl<>(payment);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page,
				"/api/_search/payment-transfer-histories");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

}

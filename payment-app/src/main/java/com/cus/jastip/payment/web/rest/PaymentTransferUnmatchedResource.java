package com.cus.jastip.payment.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cus.jastip.payment.domain.Payment;
import com.cus.jastip.payment.domain.PaymentTransferUnmatched;
import com.cus.jastip.payment.domain.enumeration.UpdateType;
import com.cus.jastip.payment.repository.PaymentTransferUnmatchedRepository;
import com.cus.jastip.payment.repository.search.PaymentTransferUnmatchedSearchRepository;
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
 * REST controller for managing PaymentTransferUnmatched.
 */
@RestController
@RequestMapping("/api")
public class PaymentTransferUnmatchedResource {

	@Autowired
	private PaymentAuditService paymentAuditService;

	private final Logger log = LoggerFactory.getLogger(PaymentTransferUnmatchedResource.class);

	private static final String ENTITY_NAME = "paymentTransferUnmatched";

	private final PaymentTransferUnmatchedRepository paymentTransferUnmatchedRepository;

	private final PaymentTransferUnmatchedSearchRepository paymentTransferUnmatchedSearchRepository;

	public PaymentTransferUnmatchedResource(PaymentTransferUnmatchedRepository paymentTransferUnmatchedRepository,
			PaymentTransferUnmatchedSearchRepository paymentTransferUnmatchedSearchRepository) {
		this.paymentTransferUnmatchedRepository = paymentTransferUnmatchedRepository;
		this.paymentTransferUnmatchedSearchRepository = paymentTransferUnmatchedSearchRepository;
	}

	/**
	 * POST /payment-transfer-unmatcheds : Create a new paymentTransferUnmatched.
	 *
	 * @param paymentTransferUnmatched
	 *            the paymentTransferUnmatched to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         paymentTransferUnmatched, or with status 400 (Bad Request) if the
	 *         paymentTransferUnmatched has already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/payment-transfer-unmatcheds")
	@Timed
	public ResponseEntity<PaymentTransferUnmatched> createPaymentTransferUnmatched(
			@Valid @RequestBody PaymentTransferUnmatched paymentTransferUnmatched) throws URISyntaxException {
		log.debug("REST request to save PaymentTransferUnmatched : {}", paymentTransferUnmatched);
		if (paymentTransferUnmatched.getId() != null) {
			throw new BadRequestAlertException("A new paymentTransferUnmatched cannot already have an ID", ENTITY_NAME,
					"idexists");
		}
		PaymentTransferUnmatched result = paymentTransferUnmatchedRepository.save(paymentTransferUnmatched);
		paymentTransferUnmatchedSearchRepository.save(result);
		paymentAuditService.addPaymentTransferUnmatched(result, ENTITY_NAME, UpdateType.CREATE);
		return ResponseEntity.created(new URI("/api/payment-transfer-unmatcheds/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /payment-transfer-unmatcheds : Updates an existing
	 * paymentTransferUnmatched.
	 *
	 * @param paymentTransferUnmatched
	 *            the paymentTransferUnmatched to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         paymentTransferUnmatched, or with status 400 (Bad Request) if the
	 *         paymentTransferUnmatched is not valid, or with status 500 (Internal
	 *         Server Error) if the paymentTransferUnmatched couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/payment-transfer-unmatcheds")
	@Timed
	public ResponseEntity<PaymentTransferUnmatched> updatePaymentTransferUnmatched(
			@Valid @RequestBody PaymentTransferUnmatched paymentTransferUnmatched) throws URISyntaxException {
		log.debug("REST request to update PaymentTransferUnmatched : {}", paymentTransferUnmatched);
		if (paymentTransferUnmatched.getId() == null) {
			return createPaymentTransferUnmatched(paymentTransferUnmatched);
		}
		PaymentTransferUnmatched result = paymentTransferUnmatchedRepository.save(paymentTransferUnmatched);
		paymentTransferUnmatchedSearchRepository.save(result);
		paymentAuditService.addPaymentTransferUnmatched(result, ENTITY_NAME, UpdateType.UPDATE);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, paymentTransferUnmatched.getId().toString()))
				.body(result);
	}

	/**
	 * GET /payment-transfer-unmatcheds : get all the paymentTransferUnmatcheds.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         paymentTransferUnmatcheds in body
	 */
	@GetMapping("/payment-transfer-unmatcheds")
	@Timed
	public ResponseEntity<List<PaymentTransferUnmatched>> getAllPaymentTransferUnmatcheds(Pageable pageable) {
		log.debug("REST request to get all PaymentTransferUnmatcheds");
		Page<PaymentTransferUnmatched> page = paymentTransferUnmatchedRepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/payment-transfer-unmatcheds");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /payment-transfer-unmatcheds/:id : get the "id" paymentTransferUnmatched.
	 *
	 * @param id
	 *            the id of the paymentTransferUnmatched to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         paymentTransferUnmatched, or with status 404 (Not Found)
	 */
	@GetMapping("/payment-transfer-unmatcheds/{id}")
	@Timed
	public ResponseEntity<PaymentTransferUnmatched> getPaymentTransferUnmatched(@PathVariable Long id) {
		log.debug("REST request to get PaymentTransferUnmatched : {}", id);
		PaymentTransferUnmatched paymentTransferUnmatched = paymentTransferUnmatchedRepository.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(paymentTransferUnmatched));
	}

	/**
	 * DELETE /payment-transfer-unmatcheds/:id : delete the "id"
	 * paymentTransferUnmatched.
	 *
	 * @param id
	 *            the id of the paymentTransferUnmatched to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/payment-transfer-unmatcheds/{id}")
	@Timed
	public ResponseEntity<Void> deletePaymentTransferUnmatched(@PathVariable Long id) {
		log.debug("REST request to delete PaymentTransferUnmatched : {}", id);
		PaymentTransferUnmatched result = paymentTransferUnmatchedRepository.findOne(id);
		paymentTransferUnmatchedRepository.delete(id);
		paymentTransferUnmatchedSearchRepository.delete(id);
		paymentAuditService.addPaymentTransferUnmatched(result, ENTITY_NAME, UpdateType.DELETE);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * SEARCH /_search/payment-transfer-unmatcheds?query=:query : search for the
	 * paymentTransferUnmatched corresponding to the query.
	 *
	 * @param query
	 *            the query of the paymentTransferUnmatched search
	 * @return the result of the search
	 */
	@GetMapping("/_search/payment-transfer-unmatcheds")
	@Timed
	public ResponseEntity<List<PaymentTransferUnmatched>> searchPaymentTransferUnmatcheds(@RequestParam String query,
			Pageable pageable) {
		log.debug("REST request to search PaymentTransferUnmatcheds for query {}", query);
		List<PaymentTransferUnmatched> payment = StreamSupport.stream(
				paymentTransferUnmatchedSearchRepository.search(queryStringQuery(query), pageable).spliterator(), false)
				.collect(Collectors.toList());
		Page<PaymentTransferUnmatched> page = new PageImpl<>(payment);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page,
				"/api/_search/payment-transfer-unmatcheds");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

}

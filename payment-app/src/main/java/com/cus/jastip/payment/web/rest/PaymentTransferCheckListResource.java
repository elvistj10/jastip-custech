package com.cus.jastip.payment.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cus.jastip.payment.domain.Payment;
import com.cus.jastip.payment.domain.PaymentTransferCheckList;
import com.cus.jastip.payment.domain.enumeration.UpdateType;
import com.cus.jastip.payment.repository.PaymentTransferCheckListRepository;
import com.cus.jastip.payment.repository.search.PaymentTransferCheckListSearchRepository;
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
 * REST controller for managing PaymentTransferCheckList.
 */
@RestController
@RequestMapping("/api")
public class PaymentTransferCheckListResource {
	
	@Autowired
	private PaymentAuditService paymentAuditService;

	private final Logger log = LoggerFactory.getLogger(PaymentTransferCheckListResource.class);

	private static final String ENTITY_NAME = "paymentTransferCheckList";

	private final PaymentTransferCheckListRepository paymentTransferCheckListRepository;

	private final PaymentTransferCheckListSearchRepository paymentTransferCheckListSearchRepository;

	public PaymentTransferCheckListResource(PaymentTransferCheckListRepository paymentTransferCheckListRepository,
			PaymentTransferCheckListSearchRepository paymentTransferCheckListSearchRepository) {
		this.paymentTransferCheckListRepository = paymentTransferCheckListRepository;
		this.paymentTransferCheckListSearchRepository = paymentTransferCheckListSearchRepository;
	}

	/**
	 * POST /payment-transfer-check-lists : Create a new paymentTransferCheckList.
	 *
	 * @param paymentTransferCheckList
	 *            the paymentTransferCheckList to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         paymentTransferCheckList, or with status 400 (Bad Request) if the
	 *         paymentTransferCheckList has already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/payment-transfer-check-lists")
	@Timed
	public ResponseEntity<PaymentTransferCheckList> createPaymentTransferCheckList(
			@Valid @RequestBody PaymentTransferCheckList paymentTransferCheckList) throws URISyntaxException {
		log.debug("REST request to save PaymentTransferCheckList : {}", paymentTransferCheckList);
		if (paymentTransferCheckList.getId() != null) {
			throw new BadRequestAlertException("A new paymentTransferCheckList cannot already have an ID", ENTITY_NAME,
					"idexists");
		}
		PaymentTransferCheckList result = paymentTransferCheckListRepository.save(paymentTransferCheckList);
		paymentTransferCheckListSearchRepository.save(result);
		paymentAuditService.addPaymentTransferCheckList(result, ENTITY_NAME, UpdateType.CREATE);
		return ResponseEntity.created(new URI("/api/payment-transfer-check-lists/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /payment-transfer-check-lists : Updates an existing
	 * paymentTransferCheckList.
	 *
	 * @param paymentTransferCheckList
	 *            the paymentTransferCheckList to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         paymentTransferCheckList, or with status 400 (Bad Request) if the
	 *         paymentTransferCheckList is not valid, or with status 500 (Internal
	 *         Server Error) if the paymentTransferCheckList couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/payment-transfer-check-lists")
	@Timed
	public ResponseEntity<PaymentTransferCheckList> updatePaymentTransferCheckList(
			@Valid @RequestBody PaymentTransferCheckList paymentTransferCheckList) throws URISyntaxException {
		log.debug("REST request to update PaymentTransferCheckList : {}", paymentTransferCheckList);
		if (paymentTransferCheckList.getId() == null) {
			return createPaymentTransferCheckList(paymentTransferCheckList);
		}
		PaymentTransferCheckList result = paymentTransferCheckListRepository.save(paymentTransferCheckList);
		paymentTransferCheckListSearchRepository.save(result);
		paymentAuditService.addPaymentTransferCheckList(result, ENTITY_NAME, UpdateType.UPDATE);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, paymentTransferCheckList.getId().toString()))
				.body(result);
	}

	/**
	 * GET /payment-transfer-check-lists : get all the paymentTransferCheckLists.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         paymentTransferCheckLists in body
	 */
	@GetMapping("/payment-transfer-check-lists")
	@Timed
	public ResponseEntity<List<PaymentTransferCheckList>> getAllPaymentTransferCheckLists(Pageable pageable) {
		log.debug("REST request to get all PaymentTransferCheckLists");
		Page<PaymentTransferCheckList> page = paymentTransferCheckListRepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/payment-transfer-check-lists");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /payment-transfer-check-lists/:id : get the "id"
	 * paymentTransferCheckList.
	 *
	 * @param id
	 *            the id of the paymentTransferCheckList to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         paymentTransferCheckList, or with status 404 (Not Found)
	 */
	@GetMapping("/payment-transfer-check-lists/{id}")
	@Timed
	public ResponseEntity<PaymentTransferCheckList> getPaymentTransferCheckList(@PathVariable Long id) {
		log.debug("REST request to get PaymentTransferCheckList : {}", id);
		PaymentTransferCheckList paymentTransferCheckList = paymentTransferCheckListRepository.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(paymentTransferCheckList));
	}

	/**
	 * DELETE /payment-transfer-check-lists/:id : delete the "id"
	 * paymentTransferCheckList.
	 *
	 * @param id
	 *            the id of the paymentTransferCheckList to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/payment-transfer-check-lists/{id}")
	@Timed
	public ResponseEntity<Void> deletePaymentTransferCheckList(@PathVariable Long id) {
		log.debug("REST request to delete PaymentTransferCheckList : {}", id);
		PaymentTransferCheckList result = paymentTransferCheckListRepository.findOne(id);
		paymentTransferCheckListRepository.delete(id);
		paymentTransferCheckListSearchRepository.delete(id);
		paymentAuditService.addPaymentTransferCheckList(result, ENTITY_NAME, UpdateType.DELETE);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * SEARCH /_search/payment-transfer-check-lists?query=:query : search for the
	 * paymentTransferCheckList corresponding to the query.
	 *
	 * @param query
	 *            the query of the paymentTransferCheckList search
	 * @return the result of the search
	 */
	@GetMapping("/_search/payment-transfer-check-lists")
	@Timed
	public ResponseEntity<List<PaymentTransferCheckList>> searchPaymentTransferCheckLists(@RequestParam String query,
			Pageable pageable) {
		log.debug("REST request to search PaymentTransferCheckLists for query {}", query);
		List<PaymentTransferCheckList> payment = StreamSupport.stream(
				paymentTransferCheckListSearchRepository.search(queryStringQuery(query), pageable).spliterator(), false)
				.collect(Collectors.toList());
		Page<PaymentTransferCheckList> page = new PageImpl<>(payment);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page,
				"/api/_search/payment-transfer-check-lists");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

}

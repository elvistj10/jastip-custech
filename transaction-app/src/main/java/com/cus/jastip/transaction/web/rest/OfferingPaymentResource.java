package com.cus.jastip.transaction.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cus.jastip.transaction.domain.OfferingDeliveryInfo;
import com.cus.jastip.transaction.domain.OfferingPayment;
import com.cus.jastip.transaction.domain.enumeration.UpdateType;
import com.cus.jastip.transaction.repository.OfferingPaymentRepository;
import com.cus.jastip.transaction.repository.search.OfferingPaymentSearchRepository;
import com.cus.jastip.transaction.service.TransactionAuditService;
import com.cus.jastip.transaction.web.rest.errors.BadRequestAlertException;
import com.cus.jastip.transaction.web.rest.util.HeaderUtil;
import com.cus.jastip.transaction.web.rest.util.PaginationUtil;

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
 * REST controller for managing OfferingPayment.
 */
@RestController
@RequestMapping("/api")
public class OfferingPaymentResource {

	private final Logger log = LoggerFactory.getLogger(OfferingPaymentResource.class);

	@Autowired
	private TransactionAuditService transactionAuditService;

	private static final String ENTITY_NAME = "offeringPayment";

	private final OfferingPaymentRepository offeringPaymentRepository;

	private final OfferingPaymentSearchRepository offeringPaymentSearchRepository;

	public OfferingPaymentResource(OfferingPaymentRepository offeringPaymentRepository,
			OfferingPaymentSearchRepository offeringPaymentSearchRepository) {
		this.offeringPaymentRepository = offeringPaymentRepository;
		this.offeringPaymentSearchRepository = offeringPaymentSearchRepository;
	}

	/**
	 * POST /offering-payments : Create a new offeringPayment.
	 *
	 * @param offeringPayment
	 *            the offeringPayment to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         offeringPayment, or with status 400 (Bad Request) if the
	 *         offeringPayment has already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/offering-payments")
	@Timed
	public ResponseEntity<OfferingPayment> createOfferingPayment(@Valid @RequestBody OfferingPayment offeringPayment)
			throws URISyntaxException {
		log.debug("REST request to save OfferingPayment : {}", offeringPayment);
		if (offeringPayment.getId() != null) {
			throw new BadRequestAlertException("A new offeringPayment cannot already have an ID", ENTITY_NAME,
					"idexists");
		}
		OfferingPayment result = offeringPaymentRepository.save(offeringPayment);
		offeringPaymentSearchRepository.save(result);
		transactionAuditService.addOfferingPayment(result, ENTITY_NAME, UpdateType.CREATE);
		return ResponseEntity.created(new URI("/api/offering-payments/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /offering-payments : Updates an existing offeringPayment.
	 *
	 * @param offeringPayment
	 *            the offeringPayment to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         offeringPayment, or with status 400 (Bad Request) if the
	 *         offeringPayment is not valid, or with status 500 (Internal Server
	 *         Error) if the offeringPayment couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/offering-payments")
	@Timed
	public ResponseEntity<OfferingPayment> updateOfferingPayment(@Valid @RequestBody OfferingPayment offeringPayment)
			throws URISyntaxException {
		log.debug("REST request to update OfferingPayment : {}", offeringPayment);
		if (offeringPayment.getId() == null) {
			return createOfferingPayment(offeringPayment);
		}
		OfferingPayment result = offeringPaymentRepository.save(offeringPayment);
		offeringPaymentSearchRepository.save(result);
		transactionAuditService.addOfferingPayment(result, ENTITY_NAME, UpdateType.UPDATE);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, offeringPayment.getId().toString()))
				.body(result);
	}

	/**
	 * GET /offering-payments : get all the offeringPayments.
	 *
	 * @param filter
	 *            the filter of the request
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         offeringPayments in body
	 */
	@GetMapping("/offering-payments")
	@Timed
	public ResponseEntity<List<OfferingPayment>> getAllOfferingPayments(@RequestParam(required = false) String filter,
			Pageable pageable) {
		if ("offering-is-null".equals(filter)) {
			log.debug("REST request to get all OfferingPayments where offering is null");
			List<OfferingPayment> list = StreamSupport.stream(offeringPaymentRepository.findAll().spliterator(), false)
					.filter(offeringPayment -> offeringPayment.getOffering() == null).collect(Collectors.toList());
			Page<OfferingPayment> page = new PageImpl<>(list);
			HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/offering-payments");
			return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
		}
		log.debug("REST request to get all OfferingPayments");
		Page<OfferingPayment> page = offeringPaymentRepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/offering-payments");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /offering-payments/:id : get the "id" offeringPayment.
	 *
	 * @param id
	 *            the id of the offeringPayment to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         offeringPayment, or with status 404 (Not Found)
	 */
	@GetMapping("/offering-payments/{id}")
	@Timed
	public ResponseEntity<OfferingPayment> getOfferingPayment(@PathVariable Long id) {
		log.debug("REST request to get OfferingPayment : {}", id);
		OfferingPayment offeringPayment = offeringPaymentRepository.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(offeringPayment));
	}

	/**
	 * DELETE /offering-payments/:id : delete the "id" offeringPayment.
	 *
	 * @param id
	 *            the id of the offeringPayment to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/offering-payments/{id}")
	@Timed
	public ResponseEntity<Void> deleteOfferingPayment(@PathVariable Long id) {
		log.debug("REST request to delete OfferingPayment : {}", id);
		OfferingPayment result = offeringPaymentRepository.findOne(id);
		offeringPaymentRepository.delete(id);
		offeringPaymentSearchRepository.delete(id);
		transactionAuditService.addOfferingPayment(result, ENTITY_NAME, UpdateType.DELETE);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * SEARCH /_search/offering-payments?query=:query : search for the
	 * offeringPayment corresponding to the query.
	 *
	 * @param query
	 *            the query of the offeringPayment search
	 * @return the result of the search
	 */
	@GetMapping("/_search/offering-payments")
	@Timed
	public ResponseEntity<List<OfferingPayment>> searchOfferingPayments(@RequestParam String query, Pageable pageable) {
		log.debug("REST request to search OfferingPayments for query {}", query);
		List<OfferingPayment> list = StreamSupport
				.stream(offeringPaymentSearchRepository.search(queryStringQuery(query), pageable).spliterator(), false)
				.collect(Collectors.toList());
		Page<OfferingPayment> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/_search/offering-payments");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

}

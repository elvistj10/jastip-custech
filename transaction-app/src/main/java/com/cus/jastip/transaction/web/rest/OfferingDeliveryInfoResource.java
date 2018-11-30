package com.cus.jastip.transaction.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cus.jastip.transaction.domain.OfferingDeliveryInfo;
import com.cus.jastip.transaction.domain.Trip;
import com.cus.jastip.transaction.domain.enumeration.UpdateType;
import com.cus.jastip.transaction.repository.OfferingDeliveryInfoRepository;
import com.cus.jastip.transaction.repository.search.OfferingDeliveryInfoSearchRepository;
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
 * REST controller for managing OfferingDeliveryInfo.
 */
@RestController
@RequestMapping("/api")
public class OfferingDeliveryInfoResource {

	@Autowired
	private TransactionAuditService transactionAuditService;

	private final Logger log = LoggerFactory.getLogger(OfferingDeliveryInfoResource.class);

	private static final String ENTITY_NAME = "offeringDeliveryInfo";

	private final OfferingDeliveryInfoRepository offeringDeliveryInfoRepository;

	private final OfferingDeliveryInfoSearchRepository offeringDeliveryInfoSearchRepository;

	public OfferingDeliveryInfoResource(OfferingDeliveryInfoRepository offeringDeliveryInfoRepository,
			OfferingDeliveryInfoSearchRepository offeringDeliveryInfoSearchRepository) {
		this.offeringDeliveryInfoRepository = offeringDeliveryInfoRepository;
		this.offeringDeliveryInfoSearchRepository = offeringDeliveryInfoSearchRepository;
	}

	/**
	 * POST /offering-delivery-infos : Create a new offeringDeliveryInfo.
	 *
	 * @param offeringDeliveryInfo
	 *            the offeringDeliveryInfo to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         offeringDeliveryInfo, or with status 400 (Bad Request) if the
	 *         offeringDeliveryInfo has already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/offering-delivery-infos")
	@Timed
	public ResponseEntity<OfferingDeliveryInfo> createOfferingDeliveryInfo(
			@Valid @RequestBody OfferingDeliveryInfo offeringDeliveryInfo) throws URISyntaxException {
		log.debug("REST request to save OfferingDeliveryInfo : {}", offeringDeliveryInfo);
		if (offeringDeliveryInfo.getId() != null) {
			throw new BadRequestAlertException("A new offeringDeliveryInfo cannot already have an ID", ENTITY_NAME,
					"idexists");
		}
		OfferingDeliveryInfo result = offeringDeliveryInfoRepository.save(offeringDeliveryInfo);
		offeringDeliveryInfoSearchRepository.save(result);
		transactionAuditService.addOfferingDeliveryInfo(result, ENTITY_NAME, UpdateType.CREATE);
		return ResponseEntity.created(new URI("/api/offering-delivery-infos/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /offering-delivery-infos : Updates an existing offeringDeliveryInfo.
	 *
	 * @param offeringDeliveryInfo
	 *            the offeringDeliveryInfo to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         offeringDeliveryInfo, or with status 400 (Bad Request) if the
	 *         offeringDeliveryInfo is not valid, or with status 500 (Internal
	 *         Server Error) if the offeringDeliveryInfo couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/offering-delivery-infos")
	@Timed
	public ResponseEntity<OfferingDeliveryInfo> updateOfferingDeliveryInfo(
			@Valid @RequestBody OfferingDeliveryInfo offeringDeliveryInfo) throws URISyntaxException {
		log.debug("REST request to update OfferingDeliveryInfo : {}", offeringDeliveryInfo);
		if (offeringDeliveryInfo.getId() == null) {
			return createOfferingDeliveryInfo(offeringDeliveryInfo);
		}
		OfferingDeliveryInfo result = offeringDeliveryInfoRepository.save(offeringDeliveryInfo);
		offeringDeliveryInfoSearchRepository.save(result);
		transactionAuditService.addOfferingDeliveryInfo(result, ENTITY_NAME, UpdateType.UPDATE);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, offeringDeliveryInfo.getId().toString()))
				.body(result);
	}

	/**
	 * GET /offering-delivery-infos : get all the offeringDeliveryInfos.
	 *
	 * @param filter
	 *            the filter of the request
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         offeringDeliveryInfos in body
	 */
	@GetMapping("/offering-delivery-infos")
	@Timed
	public ResponseEntity<List<OfferingDeliveryInfo>> getAllOfferingDeliveryInfos(
			@RequestParam(required = false) String filter, Pageable pageable) {
		if ("offering-is-null".equals(filter)) {
			log.debug("REST request to get all OfferingDeliveryInfos where offering is null");
			List<OfferingDeliveryInfo> list = StreamSupport
					.stream(offeringDeliveryInfoRepository.findAll(pageable).spliterator(), false)
					.filter(offeringDeliveryInfo -> offeringDeliveryInfo.getOffering() == null)
					.collect(Collectors.toList());
			Page<OfferingDeliveryInfo> page = new PageImpl<>(list);
			HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/offering-delivery-infos");
			return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
		}
		log.debug("REST request to get all OfferingDeliveryInfos");
		Page<OfferingDeliveryInfo> page = offeringDeliveryInfoRepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/offering-delivery-infos");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /offering-delivery-infos/:id : get the "id" offeringDeliveryInfo.
	 *
	 * @param id
	 *            the id of the offeringDeliveryInfo to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         offeringDeliveryInfo, or with status 404 (Not Found)
	 */
	@GetMapping("/offering-delivery-infos/{id}")
	@Timed
	public ResponseEntity<OfferingDeliveryInfo> getOfferingDeliveryInfo(@PathVariable Long id) {
		log.debug("REST request to get OfferingDeliveryInfo : {}", id);
		OfferingDeliveryInfo offeringDeliveryInfo = offeringDeliveryInfoRepository.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(offeringDeliveryInfo));
	}

	/**
	 * DELETE /offering-delivery-infos/:id : delete the "id" offeringDeliveryInfo.
	 *
	 * @param id
	 *            the id of the offeringDeliveryInfo to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/offering-delivery-infos/{id}")
	@Timed
	public ResponseEntity<Void> deleteOfferingDeliveryInfo(@PathVariable Long id) {
		log.debug("REST request to delete OfferingDeliveryInfo : {}", id);
		OfferingDeliveryInfo result = offeringDeliveryInfoRepository.findOne(id);
		offeringDeliveryInfoRepository.delete(id);
		offeringDeliveryInfoSearchRepository.delete(id);
		transactionAuditService.addOfferingDeliveryInfo(result, ENTITY_NAME, UpdateType.DELETE);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * SEARCH /_search/offering-delivery-infos?query=:query : search for the
	 * offeringDeliveryInfo corresponding to the query.
	 *
	 * @param query
	 *            the query of the offeringDeliveryInfo search
	 * @return the result of the search
	 */
	@GetMapping("/_search/offering-delivery-infos")
	@Timed
	public ResponseEntity<List<OfferingDeliveryInfo>> searchOfferingDeliveryInfos(@RequestParam String query,
			Pageable pageable) {
		log.debug("REST request to search OfferingDeliveryInfos for query {}", query);
		List<OfferingDeliveryInfo> list = StreamSupport
				.stream(offeringDeliveryInfoSearchRepository.search(queryStringQuery(query), pageable).spliterator(),
						false)
				.collect(Collectors.toList());
		Page<OfferingDeliveryInfo> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page,
				"/api/_search/offering-delivery-infos");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

}

package com.cus.jastip.transaction.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cus.jastip.transaction.domain.OfferingCourier;
import com.cus.jastip.transaction.domain.OfferingPuchase;
import com.cus.jastip.transaction.domain.Trip;
import com.cus.jastip.transaction.domain.enumeration.UpdateType;
import com.cus.jastip.transaction.repository.OfferingCourierRepository;
import com.cus.jastip.transaction.repository.search.OfferingCourierSearchRepository;
import com.cus.jastip.transaction.service.ImageProcessService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing OfferingCourier.
 */
@RestController
@RequestMapping("/api")
public class OfferingCourierResource {

	@Autowired
	private ImageProcessService imageProcessService;
	
	@Autowired
	private TransactionAuditService transactionAuditService;

	private final Logger log = LoggerFactory.getLogger(OfferingCourierResource.class);

	private static final String ENTITY_NAME = "offeringCourier";

	private final OfferingCourierRepository offeringCourierRepository;

	private final OfferingCourierSearchRepository offeringCourierSearchRepository;

	public OfferingCourierResource(OfferingCourierRepository offeringCourierRepository,
			OfferingCourierSearchRepository offeringCourierSearchRepository) {
		this.offeringCourierRepository = offeringCourierRepository;
		this.offeringCourierSearchRepository = offeringCourierSearchRepository;
	}

	/**
	 * POST /offering-couriers : Create a new offeringCourier.
	 *
	 * @param offeringCourier
	 *            the offeringCourier to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         offeringCourier, or with status 400 (Bad Request) if the
	 *         offeringCourier has already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/offering-couriers")
	@Timed
	public ResponseEntity<OfferingCourier> createOfferingCourier(@Valid @RequestBody OfferingCourier offeringCourier)
			throws URISyntaxException {
		log.debug("REST request to save OfferingCourier : {}", offeringCourier);
		if (offeringCourier.getId() != null) {
			throw new BadRequestAlertException("A new offeringCourier cannot already have an ID", ENTITY_NAME,
					"idexists");
		}
		if (offeringCourier.getCourierReceiptImg() != null) {
			try {
				String url = imageProcessService.onSubmit(offeringCourier.getCourierReceiptImg());
				offeringCourier.setCourierReceiptImgUrl(imageProcessService.urlImage(url));
				offeringCourier.setCourierReceiptImgThumbUrl(imageProcessService.urlImageThumb(url));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		OfferingCourier result = offeringCourierRepository.save(offeringCourier);
		offeringCourierSearchRepository.save(result);
		transactionAuditService.addOfferingCourier(result, ENTITY_NAME, UpdateType.CREATE);
		return ResponseEntity.created(new URI("/api/offering-couriers/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	@GetMapping("/offering-couriers/mobile")
	@Timed
	public ResponseEntity<List<OfferingCourier>> getAllOfferingCourierMobile(Pageable pageable) {
		List<OfferingCourier> list = new ArrayList<>();
		for (OfferingCourier offering : offeringCourierRepository.findAll(pageable)) {
			offering.setCourierReceiptImgContentType(null);
			list.add(offering);
		}
		Page<OfferingCourier> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/offering-couriers/mobile");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@GetMapping("/offering-couriers/mobile/{id}")
	@Timed
	public ResponseEntity<OfferingCourier> getOfferingCourierMobileById(@PathVariable Long id) {
		OfferingCourier offering = offeringCourierRepository.findOne(id);
		offering.setCourierReceiptImgContentType(null);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(offering));
	}

	/**
	 * PUT /offering-couriers : Updates an existing offeringCourier.
	 *
	 * @param offeringCourier
	 *            the offeringCourier to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         offeringCourier, or with status 400 (Bad Request) if the
	 *         offeringCourier is not valid, or with status 500 (Internal Server
	 *         Error) if the offeringCourier couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/offering-couriers")
	@Timed
	public ResponseEntity<OfferingCourier> updateOfferingCourier(@Valid @RequestBody OfferingCourier offeringCourier)
			throws URISyntaxException {
		log.debug("REST request to update OfferingCourier : {}", offeringCourier);
		if (offeringCourier.getId() == null) {
			return createOfferingCourier(offeringCourier);
		}
		if (offeringCourier.getCourierReceiptImg() == null) {
			OfferingCourier offering = offeringCourierRepository.findOne(offeringCourier.getId());
			offeringCourier.setCourierReceiptImgUrl(offering.getCourierReceiptImgUrl());
			offeringCourier.setCourierReceiptImgThumbUrl(offering.getCourierReceiptImgThumbUrl());
			offeringCourier.setCourierReceiptImgContentType(offering.getCourierReceiptImgContentType());
		} else {
			try {
				String url = imageProcessService.onSubmit(offeringCourier.getCourierReceiptImg());
				offeringCourier.setCourierReceiptImgUrl(imageProcessService.urlImage(url));
				offeringCourier.setCourierReceiptImgThumbUrl(imageProcessService.urlImageThumb(url));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		OfferingCourier result = offeringCourierRepository.save(offeringCourier);
		offeringCourierSearchRepository.save(result);
		transactionAuditService.addOfferingCourier(result, ENTITY_NAME, UpdateType.UPDATE);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, offeringCourier.getId().toString()))
				.body(result);
	}

	/**
	 * GET /offering-couriers : get all the offeringCouriers.
	 *
	 * @param filter
	 *            the filter of the request
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         offeringCouriers in body
	 */
	@GetMapping("/offering-couriers")
	@Timed
	public ResponseEntity<List<OfferingCourier>> getAllOfferingCouriers(@RequestParam(required = false) String filter,
			Pageable pageable) {
		if ("offering-is-null".equals(filter)) {
			log.debug("REST request to get all OfferingCouriers where offering is null");
			List<OfferingCourier> list = StreamSupport
					.stream(offeringCourierRepository.findAll(pageable).spliterator(), false)
					.filter(offeringCourier -> offeringCourier.getOffering() == null).collect(Collectors.toList());
			Page<OfferingCourier> page = new PageImpl<>(list);
			HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/offering-couriers/mobile");
			return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
		}
		log.debug("REST request to get all OfferingCouriers");
		Page<OfferingCourier> page = offeringCourierRepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/offering-couriers/mobile");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /offering-couriers/:id : get the "id" offeringCourier.
	 *
	 * @param id
	 *            the id of the offeringCourier to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         offeringCourier, or with status 404 (Not Found)
	 */
	@GetMapping("/offering-couriers/{id}")
	@Timed
	public ResponseEntity<OfferingCourier> getOfferingCourier(@PathVariable Long id) {
		log.debug("REST request to get OfferingCourier : {}", id);
		OfferingCourier offeringCourier = offeringCourierRepository.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(offeringCourier));
	}

	/**
	 * DELETE /offering-couriers/:id : delete the "id" offeringCourier.
	 *
	 * @param id
	 *            the id of the offeringCourier to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/offering-couriers/{id}")
	@Timed
	public ResponseEntity<Void> deleteOfferingCourier(@PathVariable Long id) {
		log.debug("REST request to delete OfferingCourier : {}", id);
		OfferingCourier result = offeringCourierRepository.findOne(id);
		offeringCourierRepository.delete(id);
		offeringCourierSearchRepository.delete(id);
		transactionAuditService.addOfferingCourier(result, ENTITY_NAME, UpdateType.DELETE);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * SEARCH /_search/offering-couriers?query=:query : search for the
	 * offeringCourier corresponding to the query.
	 *
	 * @param query
	 *            the query of the offeringCourier search
	 * @return the result of the search
	 */
	@GetMapping("/_search/offering-couriers")
	@Timed
	public ResponseEntity<List<OfferingCourier>> searchOfferingCouriers(@RequestParam String query, Pageable pageable) {
		log.debug("REST request to search OfferingCouriers for query {}", query);
		List<OfferingCourier> list = StreamSupport
				.stream(offeringCourierSearchRepository.search(queryStringQuery(query)).spliterator(), false)
				.collect(Collectors.toList());
		Page<OfferingCourier> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/_search/offering-couriers");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}
	
	@GetMapping("/_search/offering-couriers/mobile")
	@Timed
	public ResponseEntity<List<OfferingCourier>> searchOfferingCouriersMobile(@RequestParam String query, Pageable pageable) {
		log.debug("REST request to search OfferingCouriers for query {}", query);
		List<OfferingCourier> list = new ArrayList<>();
		List<OfferingCourier> offList = StreamSupport
				.stream(offeringCourierSearchRepository.search(queryStringQuery(query)).spliterator(), false)
				.collect(Collectors.toList());
		for (OfferingCourier offeringCourier : offList) {
			offeringCourier.setCourierReceiptImg(null);
			list.add(offeringCourier);
		}
		Page<OfferingCourier> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/_search/offering-couriers/mobile");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}
	
	

}

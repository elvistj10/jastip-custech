package com.cus.jastip.transaction.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cus.jastip.transaction.domain.OfferingCourier;
import com.cus.jastip.transaction.domain.OfferingPuchase;
import com.cus.jastip.transaction.domain.Posting;
import com.cus.jastip.transaction.domain.enumeration.UpdateType;
import com.cus.jastip.transaction.repository.OfferingPuchaseRepository;
import com.cus.jastip.transaction.repository.search.OfferingPuchaseSearchRepository;
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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing OfferingPuchase.
 */
@RestController
@RequestMapping("/api")
public class OfferingPuchaseResource {

	@Autowired
	private ImageProcessService imageProcessService;
	
	@Autowired
	private TransactionAuditService transactionAuditService;

	private final Logger log = LoggerFactory.getLogger(OfferingPuchaseResource.class);

	private static final String ENTITY_NAME = "offeringPuchase";

	private final OfferingPuchaseRepository offeringPuchaseRepository;

	private final OfferingPuchaseSearchRepository offeringPuchaseSearchRepository;

	public OfferingPuchaseResource(OfferingPuchaseRepository offeringPuchaseRepository,
			OfferingPuchaseSearchRepository offeringPuchaseSearchRepository) {
		this.offeringPuchaseRepository = offeringPuchaseRepository;
		this.offeringPuchaseSearchRepository = offeringPuchaseSearchRepository;
	}

	/**
	 * POST /offering-puchases : Create a new offeringPuchase.
	 *
	 * @param offeringPuchase
	 *            the offeringPuchase to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         offeringPuchase, or with status 400 (Bad Request) if the
	 *         offeringPuchase has already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/offering-puchases")
	@Timed
	public ResponseEntity<OfferingPuchase> createOfferingPuchase(@RequestBody OfferingPuchase offeringPuchase)
			throws URISyntaxException {
		log.debug("REST request to save OfferingPuchase : {}", offeringPuchase);
		if (offeringPuchase.getId() != null) {
			throw new BadRequestAlertException("A new offeringPuchase cannot already have an ID", ENTITY_NAME,
					"idexists");
		}
		if (offeringPuchase.getReceiptImg() != null) {
			try {
				String url = imageProcessService.onSubmit(offeringPuchase.getReceiptImg());
				offeringPuchase.setReceiptImgUrl(imageProcessService.urlImage(url));
				offeringPuchase.setReceiptImgThumbUrl(imageProcessService.urlImageThumb(url));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		OfferingPuchase result = offeringPuchaseRepository.save(offeringPuchase);
		offeringPuchaseSearchRepository.save(result);
		transactionAuditService.addOfferingPuchase(result, ENTITY_NAME, UpdateType.CREATE);
		return ResponseEntity.created(new URI("/api/offering-puchases/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	@GetMapping("/offering-purchases/mobile")
	@Timed
	public ResponseEntity<List<OfferingPuchase>> getAllOfferingPuchaseMobile(Pageable pageable) {
		List<OfferingPuchase> list = new ArrayList<>();
		for (OfferingPuchase offering : offeringPuchaseRepository.findAll(pageable)) {
			offering.setReceiptImg(null);
			list.add(offering);
		}
		Page<OfferingPuchase> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/offering-purchases/mobile");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@GetMapping("/offering-purchases/mobile/{id}")
	@Timed
	public ResponseEntity<OfferingPuchase> getOfferingPuchaseMobileById(@PathVariable Long id) {
		OfferingPuchase offering = offeringPuchaseRepository.findOne(id);
		offering.setReceiptImg(null);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(offering));
	}

	/**
	 * PUT /offering-puchases : Updates an existing offeringPuchase.
	 *
	 * @param offeringPuchase
	 *            the offeringPuchase to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         offeringPuchase, or with status 400 (Bad Request) if the
	 *         offeringPuchase is not valid, or with status 500 (Internal Server
	 *         Error) if the offeringPuchase couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/offering-puchases")
	@Timed
	public ResponseEntity<OfferingPuchase> updateOfferingPuchase(@RequestBody OfferingPuchase offeringPuchase)
			throws URISyntaxException {
		log.debug("REST request to update OfferingPuchase : {}", offeringPuchase);
		if (offeringPuchase.getId() == null) {
			return createOfferingPuchase(offeringPuchase);
		}
		if (offeringPuchase.getReceiptImg() == null) {
			OfferingPuchase offering = offeringPuchaseRepository.findOne(offeringPuchase.getId());
			offeringPuchase.setReceiptImgUrl(offering.getReceiptImgUrl());
			offeringPuchase.setReceiptImgThumbUrl(offering.getReceiptImgThumbUrl());
			offeringPuchase.setReceiptImgContentType(offering.getReceiptImgContentType());
		} else {
			try {
				String url = imageProcessService.onSubmit(offeringPuchase.getReceiptImg());
				offeringPuchase.setReceiptImgUrl(imageProcessService.urlImage(url));
				offeringPuchase.setReceiptImgThumbUrl(imageProcessService.urlImageThumb(url));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		OfferingPuchase result = offeringPuchaseRepository.save(offeringPuchase);
		offeringPuchaseSearchRepository.save(result);
		transactionAuditService.addOfferingPuchase(result, ENTITY_NAME, UpdateType.UPDATE);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, offeringPuchase.getId().toString()))
				.body(result);
	}

	/**
	 * GET /offering-puchases : get all the offeringPuchases.
	 *
	 * @param filter
	 *            the filter of the request
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         offeringPuchases in body
	 */
	@GetMapping("/offering-puchases")
	@Timed
	public ResponseEntity<List<OfferingPuchase>> getAllOfferingPuchases(@RequestParam(required = false) String filter,
			Pageable pageable) {
		if ("offering-is-null".equals(filter)) {
			log.debug("REST request to get all OfferingPuchases where offering is null");
			List<OfferingPuchase> list = StreamSupport
					.stream(offeringPuchaseRepository.findAll(pageable).spliterator(), false)
					.filter(offeringPuchase -> offeringPuchase.getOffering() == null).collect(Collectors.toList());
			Page<OfferingPuchase> page = new PageImpl<>(list);
			HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/offering-puchases");
			return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
		}
		log.debug("REST request to get all OfferingPuchases");
		Page<OfferingPuchase> page = offeringPuchaseRepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/offering-puchases");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@GetMapping("/offering-puchases/mobile")
	@Timed
	public ResponseEntity<List<OfferingPuchase>> getAllOfferingPuchasesMobile(
			@RequestParam(required = false) String filter, Pageable pageable) {
		List<OfferingPuchase> list = new ArrayList<>();
		if ("offering-is-null".equals(filter)) {
			log.debug("REST request to get all OfferingPuchases where offering is null");
			List<OfferingPuchase> listOff = StreamSupport
					.stream(offeringPuchaseRepository.findAll(pageable).spliterator(), false)
					.filter(offeringPuchase -> offeringPuchase.getOffering() == null).collect(Collectors.toList());
			for (OfferingPuchase off : listOff) {
				off.setReceiptImg(null);
				list.add(off);
			}
			Page<OfferingPuchase> page = new PageImpl<>(list);
			HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/offering-puchases/mobile");
			return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
		}
		log.debug("REST request to get all OfferingPuchases");
		Page<OfferingPuchase> pages = offeringPuchaseRepository.findAll(pageable);
		for (OfferingPuchase offeringPuchase2 : pages.getContent()) {
			offeringPuchase2.setReceiptImg(null);
			list.add(offeringPuchase2);
		}
		Page<OfferingPuchase> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/offering-puchases/mobile");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /offering-puchases/:id : get the "id" offeringPuchase.
	 *
	 * @param id
	 *            the id of the offeringPuchase to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         offeringPuchase, or with status 404 (Not Found)
	 */
	@GetMapping("/offering-puchases/{id}")
	@Timed
	public ResponseEntity<OfferingPuchase> getOfferingPuchase(@PathVariable Long id) {
		log.debug("REST request to get OfferingPuchase : {}", id);
		OfferingPuchase offeringPuchase = offeringPuchaseRepository.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(offeringPuchase));
	}

	/**
	 * DELETE /offering-puchases/:id : delete the "id" offeringPuchase.
	 *
	 * @param id
	 *            the id of the offeringPuchase to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/offering-puchases/{id}")
	@Timed
	public ResponseEntity<Void> deleteOfferingPuchase(@PathVariable Long id) {
		log.debug("REST request to delete OfferingPuchase : {}", id);
		OfferingPuchase result = offeringPuchaseRepository.findOne(id);
		offeringPuchaseRepository.delete(id);
		offeringPuchaseSearchRepository.delete(id);
		transactionAuditService.addOfferingPuchase(result, ENTITY_NAME, UpdateType.DELETE);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * SEARCH /_search/offering-puchases?query=:query : search for the
	 * offeringPuchase corresponding to the query.
	 *
	 * @param query
	 *            the query of the offeringPuchase search
	 * @return the result of the search
	 */
	@GetMapping("/_search/offering-puchases")
	@Timed
	public ResponseEntity<List<OfferingPuchase>> searchOfferingPuchases(@RequestParam String query, Pageable pageable) {
		log.debug("REST request to search OfferingPuchases for query {}", query);
		List<OfferingPuchase> list = StreamSupport
				.stream(offeringPuchaseSearchRepository.search(queryStringQuery(query), pageable).spliterator(), false)
				.collect(Collectors.toList());
		Page<OfferingPuchase> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/_search/offering-puchases");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@GetMapping("/_search/offering-puchases/mobile")
	@Timed
	public ResponseEntity<List<OfferingPuchase>> searchOfferingPuchasesMobile(@RequestParam String query,
			Pageable pageable) {
		log.debug("REST request to search OfferingPuchases for query {}", query);
		List<OfferingPuchase> list = new ArrayList<>();
		List<OfferingPuchase> listOff = StreamSupport
				.stream(offeringPuchaseSearchRepository.search(queryStringQuery(query), pageable).spliterator(), false)
				.collect(Collectors.toList());
		for (OfferingPuchase offeringPuchase : listOff) {
			offeringPuchase.setReceiptImg(null);
			list.add(offeringPuchase);
		}
		Page<OfferingPuchase> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/_search/offering-puchases/mobile");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

}

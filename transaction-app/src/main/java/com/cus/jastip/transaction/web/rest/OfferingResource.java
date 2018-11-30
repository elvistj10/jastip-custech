package com.cus.jastip.transaction.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cus.jastip.transaction.domain.Offering;
import com.cus.jastip.transaction.domain.Posting;
import com.cus.jastip.transaction.domain.Trip;
import com.cus.jastip.transaction.domain.enumeration.ActorType;
import com.cus.jastip.transaction.domain.enumeration.OfferingStatus;
import com.cus.jastip.transaction.domain.enumeration.PostingStatus;
import com.cus.jastip.transaction.domain.enumeration.PostingType;
import com.cus.jastip.transaction.domain.enumeration.UpdateType;
import com.cus.jastip.transaction.repository.OfferingRepository;
import com.cus.jastip.transaction.repository.PostingRepository;
import com.cus.jastip.transaction.repository.search.OfferingSearchRepository;
import com.cus.jastip.transaction.service.KafkaService;
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
 * REST controller for managing Offering.
 */
@RestController
@RequestMapping("/api")
public class OfferingResource {

	@Autowired
	private PostingRepository postingRepository;
	
	@Autowired
	private KafkaService kafkaService;
	
	@Autowired
	private TransactionAuditService transactionAuditService;

	private final Logger log = LoggerFactory.getLogger(OfferingResource.class);

	private static final String ENTITY_NAME = "offering";

	private final OfferingRepository offeringRepository;

	private final OfferingSearchRepository offeringSearchRepository;

	public OfferingResource(OfferingRepository offeringRepository, OfferingSearchRepository offeringSearchRepository) {
		this.offeringRepository = offeringRepository;
		this.offeringSearchRepository = offeringSearchRepository;
	}

	@GetMapping("/offerings/posting/{id}")
	@Timed
	public ResponseEntity<List<Offering>> getOfferingsByPosting(@PathVariable Long id, Pageable pageable) {
		log.debug("REST request to get all Offering");
		Posting posting = postingRepository.findOne(id);
		List<Offering> list = offeringRepository.findByPosting(posting, pageable);
		Page<Offering> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/offerings/posting/" + id);
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@GetMapping("/offerings/posting/mobile/{id}")
	@Timed
	public ResponseEntity<List<Offering>> getOfferingsByPostingMobile(@PathVariable Long id, Pageable pageable) {
		log.debug("REST request to get all Offering");
		Posting posting = postingRepository.findOne(id);
		List<Offering> list = new ArrayList<>();
		for (Offering offering : offeringRepository.findByPosting(posting, pageable)) {
			if (offering.getPosting() != null) {
				offering.getPosting().setPostingItemImg(null);
			}
			if (offering.getOfferingCourier() != null) {
				offering.getOfferingCourier().setCourierReceiptImg(null);
			}
			if (offering.getOfferingPurchase() != null) {
				offering.getOfferingPurchase().setReceiptImg(null);
			}
			list.add(offering);
		}
		Page<Offering> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/offerings/posting/mobile/" + id);
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@GetMapping("/offerings/actor-id/{id}/actor-type/{type}")
	@Timed
	public ResponseEntity<List<Offering>> getOfferingsByActor(@PathVariable Long id, @PathVariable ActorType type,
			Pageable pageable) {
		List<Offering> list = offeringRepository.findByActorIdInAndActorTypeIn(id, type, pageable);
		Page<Offering> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page,
				"/api/offerings/actor-id/" + id + "/actor-type/" + type);
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@GetMapping("/offerings/mobile/actor-id/{id}/actor-type/{type}")
	@Timed
	public ResponseEntity<List<Offering>> getOfferingsByActorMobile(@PathVariable Long id, @PathVariable ActorType type,
			Pageable pageable) {
		List<Offering> list = new ArrayList<>();
		for (Offering offering : offeringRepository.findByActorIdInAndActorTypeIn(id, type, pageable)) {
			if (offering.getPosting() != null) {
				offering.getPosting().setPostingItemImg(null);
			}
			if (offering.getOfferingCourier() != null) {
				offering.getOfferingCourier().setCourierReceiptImg(null);
			}
			if (offering.getOfferingPurchase() != null) {
				offering.getOfferingPurchase().setReceiptImg(null);
			}
			list.add(offering);
		}
		Page<Offering> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page,
				"/api/offerings/mobile/actor-id/" + id + "/actor-type/" + type);
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@GetMapping("/offerings/status/{status}")
	@Timed
	public ResponseEntity<List<Offering>> getOfferingsByStatus(@PathVariable OfferingStatus status, Pageable pageable) {
		List<Offering> list = offeringRepository.findByStatus(status, pageable);
		Page<Offering> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/offerings/status/" + status);
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@GetMapping("/offerings/mobile/status/{status}")
	@Timed
	public ResponseEntity<List<Offering>> getOfferingsByStatusMobile(@PathVariable OfferingStatus status,
			Pageable pageable) {
		List<Offering> list = new ArrayList<>();
		for (Offering offering : offeringRepository.findByStatus(status, pageable)) {
			if (offering.getPosting() != null) {
				offering.getPosting().setPostingItemImg(null);
			}
			if (offering.getOfferingCourier() != null) {
				offering.getOfferingCourier().setCourierReceiptImg(null);
			}
			if (offering.getOfferingPurchase() != null) {
				offering.getOfferingPurchase().setReceiptImg(null);
			}
			list.add(offering);
		}
		Page<Offering> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page,
				"/api/offerings/mobile/status/" + status);
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * POST /offerings : Create a new offering.
	 *
	 * @param offering
	 *            the offering to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         offering, or with status 400 (Bad Request) if the offering has
	 *         already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/offerings")
	@Timed
	public ResponseEntity<Offering> createOffering(@Valid @RequestBody Offering offering) throws URISyntaxException {
		log.debug("REST request to save Offering : {}", offering);
		if (offering.getId() != null) {
			throw new BadRequestAlertException("A new offering cannot already have an ID", ENTITY_NAME, "idexists");
		}
		Offering result = offeringRepository.save(offering);
		offeringSearchRepository.save(result);
		transactionAuditService.addOffering(result, ENTITY_NAME, UpdateType.CREATE);
		kafkaService.offeringCountProcess(result);
		return ResponseEntity.created(new URI("/api/offerings/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /offerings : Updates an existing offering.
	 *
	 * @param offering
	 *            the offering to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         offering, or with status 400 (Bad Request) if the offering is not
	 *         valid, or with status 500 (Internal Server Error) if the offering
	 *         couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/offerings")
	@Timed
	public ResponseEntity<Offering> updateOffering(@Valid @RequestBody Offering offering) throws URISyntaxException {
		log.debug("REST request to update Offering : {}", offering);
		if (offering.getId() == null) {
			return createOffering(offering);
		}
		Offering result = offeringRepository.save(offering);
		offeringSearchRepository.save(result);
		transactionAuditService.addOffering(result, ENTITY_NAME, UpdateType.UPDATE);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, offering.getId().toString()))
				.body(result);
	}

	/**
	 * GET /offerings : get all the offerings.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of offerings in
	 *         body
	 */
	@GetMapping("/offerings")
	@Timed
	public ResponseEntity<List<Offering>> getAllOfferings(Pageable pageable) {
		log.debug("REST request to get all Offerings");
		Page<Offering> page = offeringRepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/offerings");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@GetMapping("/offerings/mobile")
	@Timed
	public ResponseEntity<List<Offering>> getAllOfferingsMobile(Pageable pageable) {
		log.debug("REST request to get all Offerings");
		List<Offering> list = new ArrayList<>();
		Page<Offering> pages = offeringRepository.findAll(pageable);
		for (Offering offering : pages.getContent()) {
			if (offering.getPosting() != null) {
				offering.getPosting().setPostingItemImg(null);
			}
			if (offering.getOfferingCourier() != null) {
				offering.getOfferingCourier().setCourierReceiptImg(null);
			}
			if (offering.getOfferingPurchase() != null) {
				offering.getOfferingPurchase().setReceiptImg(null);
			}
			list.add(offering);
		}
		Page<Offering> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/offerings/mobile");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /offerings/:id : get the "id" offering.
	 *
	 * @param id
	 *            the id of the offering to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the offering,
	 *         or with status 404 (Not Found)
	 */
	@GetMapping("/offerings/{id}")
	@Timed
	public ResponseEntity<Offering> getOffering(@PathVariable Long id) {
		log.debug("REST request to get Offering : {}", id);
		Offering offering = offeringRepository.findOne(id);
		if (offering.getPosting() != null) {
			offering.getPosting().setPostingItemImg(null);
		}
		if (offering.getOfferingCourier() != null) {
			offering.getOfferingCourier().setCourierReceiptImg(null);
		}
		if (offering.getOfferingPurchase() != null) {
			offering.getOfferingPurchase().setReceiptImg(null);
		}
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(offering));
	}

	@GetMapping("/offerings/mobile/{id}")
	@Timed
	public ResponseEntity<Offering> getOfferingMobile(@PathVariable Long id) {
		log.debug("REST request to get Offering : {}", id);
		Offering offering = offeringRepository.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(offering));
	}

	/**
	 * DELETE /offerings/:id : delete the "id" offering.
	 *
	 * @param id
	 *            the id of the offering to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/offerings/{id}")
	@Timed
	public ResponseEntity<Void> deleteOffering(@PathVariable Long id) {
		log.debug("REST request to delete Offering : {}", id);
		Offering result = offeringRepository.findOne(id);
		offeringRepository.delete(id);
		offeringSearchRepository.delete(id);
		transactionAuditService.addOffering(result, ENTITY_NAME, UpdateType.DELETE);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * SEARCH /_search/offerings?query=:query : search for the offering
	 * corresponding to the query.
	 *
	 * @param query
	 *            the query of the offering search
	 * @return the result of the search
	 */
	@GetMapping("/_search/offerings")
	@Timed
	public ResponseEntity<List<Offering>> searchOfferings(@RequestParam String query, Pageable pageable) {
		log.debug("REST request to search Offerings for query {}", query);
		List<Offering> list = StreamSupport
				.stream(offeringSearchRepository.search(queryStringQuery(query), pageable).spliterator(), false)
				.collect(Collectors.toList());
		Page<Offering> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/_search/offerings");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@GetMapping("/_search/offerings/mobile")
	@Timed
	public ResponseEntity<List<Offering>> searchOfferingMobile(@RequestParam String query, Pageable pageable) {
		log.debug("REST request to search Offerings for query {}", query);
		List<Offering> list = new ArrayList<>();
		List<Offering> listOff = StreamSupport
				.stream(offeringSearchRepository.search(queryStringQuery(query), pageable).spliterator(), false)
				.collect(Collectors.toList());
		for (Offering offering : listOff) {
			if (offering.getPosting() != null) {
				offering.getPosting().setPostingItemImg(null);
			}
			if (offering.getOfferingCourier() != null) {
				offering.getOfferingCourier().setCourierReceiptImg(null);
			}
			if (offering.getOfferingPurchase() != null) {
				offering.getOfferingPurchase().setReceiptImg(null);
			}
			list.add(offering);
		}
		Page<Offering> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/_search/offerings/mobile");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

}

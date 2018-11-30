package com.cus.jastip.profile.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cus.jastip.profile.domain.BlockList;
import com.cus.jastip.profile.domain.CreditCard;
import com.cus.jastip.profile.domain.Profile;
import com.cus.jastip.profile.domain.enumeration.UpdateType;
import com.cus.jastip.profile.repository.CreditCardRepository;
import com.cus.jastip.profile.repository.ProfileRepository;
import com.cus.jastip.profile.repository.search.CreditCardSearchRepository;
import com.cus.jastip.profile.service.ProfilesAuditService;
import com.cus.jastip.profile.web.rest.errors.BadRequestAlertException;
import com.cus.jastip.profile.web.rest.util.HeaderUtil;
import com.cus.jastip.profile.web.rest.util.PaginationUtil;

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
 * REST controller for managing CreditCard.
 */
@RestController
@RequestMapping("/api")
public class CreditCardResource {

	@Autowired
	private ProfileRepository profileRepository;
	
	@Autowired
	private ProfilesAuditService profilesAuditService;

	private final Logger log = LoggerFactory.getLogger(CreditCardResource.class);

	private static final String ENTITY_NAME = "creditCard";

	private final CreditCardRepository creditCardRepository;

	private final CreditCardSearchRepository creditCardSearchRepository;

	public CreditCardResource(CreditCardRepository creditCardRepository,
			CreditCardSearchRepository creditCardSearchRepository) {
		this.creditCardRepository = creditCardRepository;
		this.creditCardSearchRepository = creditCardSearchRepository;
	}

	/**
	 * POST /credit-cards : Create a new creditCard.
	 *
	 * @param creditCard
	 *            the creditCard to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         creditCard, or with status 400 (Bad Request) if the creditCard has
	 *         already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/credit-cards")
	@Timed
	public ResponseEntity<CreditCard> createCreditCard(@Valid @RequestBody CreditCard creditCard)
			throws URISyntaxException {
		log.debug("REST request to save CreditCard : {}", creditCard);
		if (creditCard.getId() != null) {
			throw new BadRequestAlertException("A new creditCard cannot already have an ID", ENTITY_NAME, "idexists");
		}
		CreditCard result = creditCardRepository.save(creditCard);
		creditCardSearchRepository.save(result);
		profilesAuditService.addCreditCard(result, ENTITY_NAME, UpdateType.CREATE);
		return ResponseEntity.created(new URI("/api/credit-cards/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /credit-cards : Updates an existing creditCard.
	 *
	 * @param creditCard
	 *            the creditCard to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         creditCard, or with status 400 (Bad Request) if the creditCard is not
	 *         valid, or with status 500 (Internal Server Error) if the creditCard
	 *         couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/credit-cards")
	@Timed
	public ResponseEntity<CreditCard> updateCreditCard(@Valid @RequestBody CreditCard creditCard)
			throws URISyntaxException {
		log.debug("REST request to update CreditCard : {}", creditCard);
		if (creditCard.getId() == null) {
			return createCreditCard(creditCard);
		}
		CreditCard result = creditCardRepository.save(creditCard);
		creditCardSearchRepository.save(result);
		profilesAuditService.addCreditCard(result, ENTITY_NAME, UpdateType.UPDATE);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, creditCard.getId().toString())).body(result);
	}

	/**
	 * GET /credit-cards : get all the creditCards.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of creditCards
	 *         in body
	 */
	@GetMapping("/credit-cards")
	@Timed
	public ResponseEntity<List<CreditCard>> getAllCreditCards(Pageable pageable) {
		log.debug("REST request to get all CreditCards");
		Page<CreditCard> page = creditCardRepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/credit-cards");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@GetMapping("/credit-cards/mobile")
	@Timed
	public ResponseEntity<List<CreditCard>> getAllCreditCardsMobile(Pageable pageable) {
		log.debug("REST request to get all CreditCards");
		List<CreditCard> list = new ArrayList<>();
		Page<CreditCard> pages = creditCardRepository.findAll(pageable);
		for (CreditCard creditCard : pages.getContent()) {
			creditCard.getProfile().setImage(null);
			list.add(creditCard);
		}
		Page<CreditCard> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/credit-cards/mobile");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /credit-cards/:id : get the "id" creditCard.
	 *
	 * @param id
	 *            the id of the creditCard to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the creditCard,
	 *         or with status 404 (Not Found)
	 */
	@GetMapping("/credit-cards/{id}")
	@Timed
	public ResponseEntity<CreditCard> getCreditCard(@PathVariable Long id) {
		log.debug("REST request to get CreditCard : {}", id);
		CreditCard creditCard = creditCardRepository.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(creditCard));
	}

	@GetMapping("/credit-cards/mobile/{id}")
	@Timed
	public ResponseEntity<CreditCard> getCreditCardMobile(@PathVariable Long id) {
		log.debug("REST request to get CreditCard : {}", id);
		CreditCard creditCard = creditCardRepository.findOne(id);
		creditCard.getProfile().setImage(null);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(creditCard));
	}

	@GetMapping("/credit-cards/profiles/{id}")
	@Timed
	public ResponseEntity<List<CreditCard>> getCreditCardByProfile(@PathVariable Long id, Pageable pageable) {
		log.debug("REST request to get all CreditCard");
		Profile profile = profileRepository.findOne(id);
		List<CreditCard> list = creditCardRepository.findByProfile(profile, pageable);
		Page<CreditCard> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/credit-cards/profiles/" + id);
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@GetMapping("/credit-cards/profiles/mobile/{id}")
	@Timed
	public ResponseEntity<List<CreditCard>> getCreditCardByProfileMobile(@PathVariable Long id, Pageable pageable) {
		log.debug("REST request to get all CreditCard");
		List<CreditCard> list = new ArrayList<>();
		Profile profile = profileRepository.findOne(id);
		List<CreditCard> cc = creditCardRepository.findByProfile(profile, pageable);
		for (CreditCard creditCard : cc) {
			creditCard.getProfile().setImage(null);
			list.add(creditCard);
		}
		Page<CreditCard> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page,
				"/api/credit-cards/profiles/mobile/" + id);
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * DELETE /credit-cards/:id : delete the "id" creditCard.
	 *
	 * @param id
	 *            the id of the creditCard to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/credit-cards/{id}")
	@Timed
	public ResponseEntity<Void> deleteCreditCard(@PathVariable Long id) {
		log.debug("REST request to delete CreditCard : {}", id);
		Profile result = profileRepository.findOne(id);
		creditCardRepository.delete(id);
		creditCardSearchRepository.delete(id);
		profilesAuditService.addCreditCard(result, ENTITY_NAME, UpdateType.DELETE);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * SEARCH /_search/credit-cards?query=:query : search for the creditCard
	 * corresponding to the query.
	 *
	 * @param query
	 *            the query of the creditCard search
	 * @return the result of the search
	 */
	@GetMapping("/_search/credit-cards")
	@Timed
	public ResponseEntity<List<CreditCard>> searchCreditCards(@RequestParam String query, Pageable pageable) {
		log.debug("REST request to search CreditCards for query {}", query);
		List<CreditCard> list = StreamSupport
				.stream(creditCardSearchRepository.search(queryStringQuery(query), pageable).spliterator(), false)
				.collect(Collectors.toList());
		Page<CreditCard> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/_search/credit-cards");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@GetMapping("/_search/credit-cards/mobile")
	@Timed
	public ResponseEntity<List<CreditCard>> searchCreditCardsMobile(@RequestParam String query, Pageable pageable) {
		log.debug("REST request to search CreditCards for query {}", query);
		List<CreditCard> list = new ArrayList<>();
		List<CreditCard> cc = StreamSupport
				.stream(creditCardSearchRepository.search(queryStringQuery(query), pageable).spliterator(), false)
				.collect(Collectors.toList());
		for (CreditCard creditCard : cc) {
			creditCard.getProfile().setImage(null);
			list.add(creditCard);
		}
		Page<CreditCard> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/_search/credit-cards/mobile");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

}

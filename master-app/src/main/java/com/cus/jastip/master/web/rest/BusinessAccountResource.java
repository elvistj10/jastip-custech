package com.cus.jastip.master.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cus.jastip.master.domain.Bank;
import com.cus.jastip.master.domain.BusinessAccount;
import com.cus.jastip.master.domain.enumeration.UpdateType;
import com.cus.jastip.master.repository.BusinessAccountRepository;
import com.cus.jastip.master.repository.search.BusinessAccountSearchRepository;
import com.cus.jastip.master.service.MasterAuditService;
import com.cus.jastip.master.web.rest.errors.BadRequestAlertException;
import com.cus.jastip.master.web.rest.util.HeaderUtil;
import com.cus.jastip.master.web.rest.util.PaginationUtil;

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
 * REST controller for managing BusinessAccount.
 */
@RestController
@RequestMapping("/api")
public class BusinessAccountResource {

	private final Logger log = LoggerFactory.getLogger(BusinessAccountResource.class);

	private static final String ENTITY_NAME = "businessAccount";


	@Autowired
	private MasterAuditService masterAuditService;

	private final BusinessAccountRepository businessAccountRepository;

	private final BusinessAccountSearchRepository businessAccountSearchRepository;

	public BusinessAccountResource(BusinessAccountRepository businessAccountRepository,
			BusinessAccountSearchRepository businessAccountSearchRepository) {
		this.businessAccountRepository = businessAccountRepository;
		this.businessAccountSearchRepository = businessAccountSearchRepository;
	}

	/**
	 * POST /business-accounts : Create a new businessAccount.
	 *
	 * @param businessAccount
	 *            the businessAccount to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         businessAccount, or with status 400 (Bad Request) if the
	 *         businessAccount has already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/business-accounts")
	@Timed
	public ResponseEntity<BusinessAccount> createBusinessAccount(@Valid @RequestBody BusinessAccount businessAccount)
			throws URISyntaxException {
		log.debug("REST request to save BusinessAccount : {}", businessAccount);
		if (businessAccount.getId() != null) {
			throw new BadRequestAlertException("A new businessAccount cannot already have an ID", ENTITY_NAME,
					"idexists");
		}
		BusinessAccount result = businessAccountRepository.save(businessAccount);
		businessAccountSearchRepository.save(result);
		masterAuditService.addBusinessAccount(result, ENTITY_NAME, UpdateType.CREATE);
		return ResponseEntity.created(new URI("/api/business-accounts/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /business-accounts : Updates an existing businessAccount.
	 *
	 * @param businessAccount
	 *            the businessAccount to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         businessAccount, or with status 400 (Bad Request) if the
	 *         businessAccount is not valid, or with status 500 (Internal Server
	 *         Error) if the businessAccount couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/business-accounts")
	@Timed
	public ResponseEntity<BusinessAccount> updateBusinessAccount(@Valid @RequestBody BusinessAccount businessAccount)
			throws URISyntaxException {
		log.debug("REST request to update BusinessAccount : {}", businessAccount);
		if (businessAccount.getId() == null) {
			return createBusinessAccount(businessAccount);
		}
		BusinessAccount result = businessAccountRepository.save(businessAccount);
		businessAccountSearchRepository.save(result);
		masterAuditService.addBusinessAccount(result, ENTITY_NAME, UpdateType.UPDATE);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, businessAccount.getId().toString()))
				.body(result);
	}

	/**
	 * GET /business-accounts : get all the businessAccounts.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         businessAccounts in body
	 */
	@GetMapping("/business-accounts")
	@Timed
	public ResponseEntity<List<BusinessAccount>> getAllBusinessAccounts(Pageable pageable) {
		log.debug("REST request to get all BusinessAccounts");
		Page<BusinessAccount> page = businessAccountRepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/business-accounts");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /business-accounts/:id : get the "id" businessAccount.
	 *
	 * @param id
	 *            the id of the businessAccount to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         businessAccount, or with status 404 (Not Found)
	 */
	@GetMapping("/business-accounts/{id}")
	@Timed
	public ResponseEntity<BusinessAccount> getBusinessAccount(@PathVariable Long id) {
		log.debug("REST request to get BusinessAccount : {}", id);
		BusinessAccount businessAccount = businessAccountRepository.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(businessAccount));
	}

	/**
	 * DELETE /business-accounts/:id : delete the "id" businessAccount.
	 *
	 * @param id
	 *            the id of the businessAccount to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/business-accounts/{id}")
	@Timed
	public ResponseEntity<Void> deleteBusinessAccount(@PathVariable Long id) {
		log.debug("REST request to delete BusinessAccount : {}", id);
		BusinessAccount result = businessAccountRepository.findOne(id);
		businessAccountRepository.delete(id);
		businessAccountSearchRepository.delete(id);
		masterAuditService.addBusinessAccount(result, ENTITY_NAME, UpdateType.DELETE);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * SEARCH /_search/business-accounts?query=:query : search for the
	 * businessAccount corresponding to the query.
	 *
	 * @param query
	 *            the query of the businessAccount search
	 * @return the result of the search
	 */
	@GetMapping("/_search/business-accounts")
	@Timed
	public ResponseEntity<List<BusinessAccount>> searchBusinessAccounts(@RequestParam String query, Pageable pageable) {
		log.debug("REST request to search BusinessAccounts for query {}", query);
		List<BusinessAccount> businessAccount = StreamSupport
				.stream(businessAccountSearchRepository.search(queryStringQuery(query), pageable).spliterator(), false)
				.collect(Collectors.toList());
		Page<BusinessAccount> page = new PageImpl<>(businessAccount);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/_search/business-accounts");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

}

package com.cus.jastip.master.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cus.jastip.master.domain.Bank;
import com.cus.jastip.master.domain.Country;
import com.cus.jastip.master.domain.enumeration.UpdateType;
import com.cus.jastip.master.repository.BankRepository;
import com.cus.jastip.master.repository.search.BankSearchRepository;
import com.cus.jastip.master.service.MasterAuditService;
import com.cus.jastip.master.web.rest.errors.BadRequestAlertException;
import com.cus.jastip.master.web.rest.util.HeaderUtil;
import com.cus.jastip.master.web.rest.util.PaginationUtil;
import com.fasterxml.jackson.core.JsonProcessingException;

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
 * REST controller for managing Bank.
 */
@RestController
@RequestMapping("/api")
public class BankResource {

	private final Logger log = LoggerFactory.getLogger(BankResource.class);

	private static final String ENTITY_NAME = "bank";
	private static final String EVENT_TYPE_POST = "POST";
	private static final String EVENT_TYPE_PUT = "PUT";
	private static final String EVENT_TYPE_DELETE = "DELETE";

	@Autowired
	private MasterAuditService masterAuditService;

	private final BankRepository bankRepository;

	private final BankSearchRepository bankSearchRepository;

	public BankResource(BankRepository bankRepository, BankSearchRepository bankSearchRepository) {
		this.bankRepository = bankRepository;
		this.bankSearchRepository = bankSearchRepository;
	}

	/**
	 * POST /banks : Create a new bank.
	 *
	 * @param bank
	 *            the bank to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         bank, or with status 400 (Bad Request) if the bank has already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	@PostMapping("/banks")
	@Timed
	public ResponseEntity<Bank> createBank(@Valid @RequestBody Bank bank)
			throws URISyntaxException, IllegalArgumentException, IllegalAccessException {
		log.debug("REST request to save Bank : {}", bank);
		if (bank.getId() != null) {
			throw new BadRequestAlertException("A new bank cannot already have an ID", ENTITY_NAME, "idexists");
		}
		if (bank.isActiveStatus() == null) {
			bank.setActiveStatus(false);
		}
		Bank result = bankRepository.save(bank);
		bankSearchRepository.save(result);
		masterAuditService.addBank(result, ENTITY_NAME, UpdateType.CREATE);
		return ResponseEntity.created(new URI("/api/banks/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /banks : Updates an existing bank.
	 *
	 * @param bank
	 *            the bank to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         bank, or with status 400 (Bad Request) if the bank is not valid, or
	 *         with status 500 (Internal Server Error) if the bank couldn't be
	 *         updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	@PutMapping("/banks")
	@Timed
	public ResponseEntity<Bank> updateBank(@Valid @RequestBody Bank bank)
			throws URISyntaxException, IllegalArgumentException, IllegalAccessException {
		log.debug("REST request to update Bank : {}", bank);
		if (bank.getId() == null) {
			return createBank(bank);
		}
		Bank result = bankRepository.save(bank);
		bankSearchRepository.save(result);
		masterAuditService.addBank(result, ENTITY_NAME, UpdateType.UPDATE);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, bank.getId().toString()))
				.body(result);
	}

	/**
	 * GET /banks : get all the banks.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of banks in body
	 */
	@GetMapping("/banks")
	@Timed
	public ResponseEntity<List<Bank>> getAllBanks(Pageable pegeable) {
		log.debug("REST request to get all Banks");
		Page<Bank> page = bankSearchRepository.findAll(pegeable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/banks");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /banks/:id : get the "id" bank.
	 *
	 * @param id
	 *            the id of the bank to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the bank, or
	 *         with status 404 (Not Found)
	 */
	@GetMapping("/banks/{id}")
	@Timed
	public ResponseEntity<Bank> getBank(@PathVariable Long id) {
		log.debug("REST request to get Bank : {}", id);
		Bank bank = bankRepository.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(bank));
	}

	/**
	 * DELETE /banks/:id : delete the "id" bank.
	 *
	 * @param id
	 *            the id of the bank to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/banks/{id}")
	@Timed
	public ResponseEntity<Void> deleteBank(@PathVariable Long id) {
		log.debug("REST request to delete Bank : {}", id);
		Bank result = bankRepository.findOne(id);
		bankRepository.delete(id);
		bankSearchRepository.delete(id);
		masterAuditService.addBank(result, ENTITY_NAME, UpdateType.DELETE);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * SEARCH /_search/banks?query=:query : search for the bank corresponding to the
	 * query.
	 *
	 * @param query
	 *            the query of the bank search
	 * @return the result of the search
	 */
	@GetMapping("/_search/banks")
	@Timed
	public ResponseEntity<List<Bank>> searchBanks(@RequestParam String query, Pageable pageable) {
		log.debug("REST request to search Banks for query {}", query);
		List<Bank> bank = StreamSupport
				.stream(bankSearchRepository.search(queryStringQuery(query), pageable).spliterator(), false)
				.collect(Collectors.toList());
		Page<Bank> page = new PageImpl<>(bank);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/_search/banks");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

}

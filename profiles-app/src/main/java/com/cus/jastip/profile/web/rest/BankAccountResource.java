package com.cus.jastip.profile.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cus.jastip.profile.domain.Address;
import com.cus.jastip.profile.domain.BankAccount;
import com.cus.jastip.profile.domain.Profile;
import com.cus.jastip.profile.domain.enumeration.UpdateType;
import com.cus.jastip.profile.repository.BankAccountRepository;
import com.cus.jastip.profile.repository.ProfileRepository;
import com.cus.jastip.profile.repository.search.BankAccountSearchRepository;
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
 * REST controller for managing BankAccount.
 */
@RestController
@RequestMapping("/api")
public class BankAccountResource {

	@Autowired
	private ProfileRepository profileRepository;
	
	@Autowired
	private ProfilesAuditService profilesAuditService;

	private final Logger log = LoggerFactory.getLogger(BankAccountResource.class);

	private static final String ENTITY_NAME = "bankAccount";

	private final BankAccountRepository bankAccountRepository;

	private final BankAccountSearchRepository bankAccountSearchRepository;

	public BankAccountResource(BankAccountRepository bankAccountRepository,
			BankAccountSearchRepository bankAccountSearchRepository) {
		this.bankAccountRepository = bankAccountRepository;
		this.bankAccountSearchRepository = bankAccountSearchRepository;
	}

	@GetMapping("/bank-accounts/profiles/{id}")
	@Timed
	public ResponseEntity<List<BankAccount>> getBankAccountByProfile(@PathVariable Long id, Pageable pageable) {
		log.debug("REST request to get all BankAccount");
		Profile profile = profileRepository.findOne(id);
		List<BankAccount> list = bankAccountRepository.findByProfile(profile, pageable);
		Page<BankAccount> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/bank-accounts/profiles/" + id);
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@GetMapping("/bank-accounts/profiles/mobile/{id}")
	@Timed
	public ResponseEntity<List<BankAccount>> getBankAccountByProfileMobile(@PathVariable Long id, Pageable pageable) {
		log.debug("REST request to get all BankAccount");
		List<BankAccount> list = new ArrayList<>();
		Profile profile = profileRepository.findOne(id);
		List<BankAccount> bank = bankAccountRepository.findByProfile(profile, pageable);
		for (BankAccount bankAccount : bank) {
			bankAccount.getProfile().setImage(null);
			list.add(bankAccount);
		}
		Page<BankAccount> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page,
				"/api/bank-accounts/profiles/mobile/" + id);
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * POST /bank-accounts : Create a new bankAccount.
	 *
	 * @param bankAccount
	 *            the bankAccount to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         bankAccount, or with status 400 (Bad Request) if the bankAccount has
	 *         already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/bank-accounts")
	@Timed
	public ResponseEntity<BankAccount> createBankAccount(@Valid @RequestBody BankAccount bankAccount)
			throws URISyntaxException {
		log.debug("REST request to save BankAccount : {}", bankAccount);
		if (bankAccount.getId() != null) {
			throw new BadRequestAlertException("A new bankAccount cannot already have an ID", ENTITY_NAME, "idexists");
		}
		BankAccount result = bankAccountRepository.save(bankAccount);
		bankAccountSearchRepository.save(result);
		profilesAuditService.addBankAccount(result, ENTITY_NAME, UpdateType.CREATE);
		return ResponseEntity.created(new URI("/api/bank-accounts/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /bank-accounts : Updates an existing bankAccount.
	 *
	 * @param bankAccount
	 *            the bankAccount to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         bankAccount, or with status 400 (Bad Request) if the bankAccount is
	 *         not valid, or with status 500 (Internal Server Error) if the
	 *         bankAccount couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/bank-accounts")
	@Timed
	public ResponseEntity<BankAccount> updateBankAccount(@Valid @RequestBody BankAccount bankAccount)
			throws URISyntaxException {
		log.debug("REST request to update BankAccount : {}", bankAccount);
		if (bankAccount.getId() == null) {
			return createBankAccount(bankAccount);
		}
		BankAccount result = bankAccountRepository.save(bankAccount);
		bankAccountSearchRepository.save(result);
		profilesAuditService.addBankAccount(result, ENTITY_NAME, UpdateType.UPDATE);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, bankAccount.getId().toString())).body(result);
	}

	/**
	 * GET /bank-accounts : get all the bankAccounts.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of bankAccounts
	 *         in body
	 */
	@GetMapping("/bank-accounts")
	@Timed
	public ResponseEntity<List<BankAccount>> getAllBankAccounts(Pageable pageable) {
		log.debug("REST request to get all BankAccounts");
		List<BankAccount> list = new ArrayList<>();
		Page<BankAccount> page = bankAccountRepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/bank-accounts");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@GetMapping("/bank-accounts/mobile")
	@Timed
	public ResponseEntity<List<BankAccount>> getAllBankAccountsMobile(Pageable pageable) {
		log.debug("REST request to get all BankAccounts");
		List<BankAccount> list = new ArrayList<>();
		Page<BankAccount> pages = bankAccountRepository.findAll(pageable);
		for (BankAccount bankAccount : pages.getContent()) {
			bankAccount.getProfile().setImage(null);
			list.add(bankAccount);
		}
		Page<BankAccount> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/bank-accounts/mobile");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /bank-accounts/:id : get the "id" bankAccount.
	 *
	 * @param id
	 *            the id of the bankAccount to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         bankAccount, or with status 404 (Not Found)
	 */
	@GetMapping("/bank-accounts/{id}")
	@Timed
	public ResponseEntity<BankAccount> getBankAccount(@PathVariable Long id) {
		log.debug("REST request to get BankAccount : {}", id);
		BankAccount bankAccount = bankAccountRepository.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(bankAccount));
	}

	@GetMapping("/bank-accounts/mobile/{id}")
	@Timed
	public ResponseEntity<BankAccount> getBankAccountMobile(@PathVariable Long id) {
		log.debug("REST request to get BankAccount : {}", id);
		BankAccount bankAccount = bankAccountRepository.findOne(id);
		bankAccount.getProfile().setImage(null);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(bankAccount));
	}

	/**
	 * DELETE /bank-accounts/:id : delete the "id" bankAccount.
	 *
	 * @param id
	 *            the id of the bankAccount to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/bank-accounts/{id}")
	@Timed
	public ResponseEntity<Void> deleteBankAccount(@PathVariable Long id) {
		log.debug("REST request to delete BankAccount : {}", id);
		BankAccount result = bankAccountRepository.findOne(id);
		bankAccountRepository.delete(id);
		bankAccountSearchRepository.delete(id);
		profilesAuditService.addBankAccount(result, ENTITY_NAME, UpdateType.DELETE);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * SEARCH /_search/bank-accounts?query=:query : search for the bankAccount
	 * corresponding to the query.
	 *
	 * @param query
	 *            the query of the bankAccount search
	 * @return the result of the search
	 */
	@GetMapping("/_search/bank-accounts")
	@Timed
	public ResponseEntity<List<BankAccount>> searchBankAccounts(@RequestParam String query, Pageable pageable) {
		log.debug("REST request to search BankAccounts for query {}", query);
		List<BankAccount> list = StreamSupport
				.stream(bankAccountSearchRepository.search(queryStringQuery(query), pageable).spliterator(), false)
				.collect(Collectors.toList());
		Page<BankAccount> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/_search/addresses");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@GetMapping("/_search/bank-accounts/mobile")
	@Timed
	public ResponseEntity<List<BankAccount>> searchBankAccountsMobile(@RequestParam String query, Pageable pageable) {
		log.debug("REST request to search BankAccounts for query {}", query);
		List<BankAccount> list = new ArrayList<>();
		List<BankAccount> bankAcc = StreamSupport
				.stream(bankAccountSearchRepository.search(queryStringQuery(query), pageable).spliterator(), false)
				.collect(Collectors.toList());
		for (BankAccount bankAccount : bankAcc) {
			bankAccount.getProfile().setImage(null);
			list.add(bankAccount);
		}
		Page<BankAccount> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/_search/addresses/mobile");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

}

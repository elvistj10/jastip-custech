package com.cus.jastip.master.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cus.jastip.master.domain.BusinessAccount;
import com.cus.jastip.master.domain.PackageSize;
import com.cus.jastip.master.domain.PostalCode;
import com.cus.jastip.master.domain.enumeration.UpdateType;
import com.cus.jastip.master.repository.PostalCodeRepository;
import com.cus.jastip.master.repository.search.PostalCodeSearchRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing PostalCode.
 */
@RestController
@RequestMapping("/api")
public class PostalCodeResource {

	private final Logger log = LoggerFactory.getLogger(PostalCodeResource.class);

	private static final String ENTITY_NAME = "postalCode";

	@Autowired
	private MasterAuditService masterAuditService;

	private final PostalCodeRepository postalCodeRepository;

	private final PostalCodeSearchRepository postalCodeSearchRepository;

	public PostalCodeResource(PostalCodeRepository postalCodeRepository,
			PostalCodeSearchRepository postalCodeSearchRepository) {
		this.postalCodeRepository = postalCodeRepository;
		this.postalCodeSearchRepository = postalCodeSearchRepository;
	}

	/**
	 * POST /postal-codes : Create a new postalCode.
	 *
	 * @param postalCode
	 *            the postalCode to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         postalCode, or with status 400 (Bad Request) if the postalCode has
	 *         already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/postal-codes")
	@Timed
	public ResponseEntity<PostalCode> createPostalCode(@Valid @RequestBody PostalCode postalCode)
			throws URISyntaxException {
		log.debug("REST request to save PostalCode : {}", postalCode);
		if (postalCode.getId() != null) {
			throw new BadRequestAlertException("A new postalCode cannot already have an ID", ENTITY_NAME, "idexists");
		}
		PostalCode result = postalCodeRepository.save(postalCode);
		postalCodeSearchRepository.save(result);
		masterAuditService.addPostalCode(result, ENTITY_NAME, UpdateType.CREATE);
		return ResponseEntity.created(new URI("/api/postal-codes/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /postal-codes : Updates an existing postalCode.
	 *
	 * @param postalCode
	 *            the postalCode to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         postalCode, or with status 400 (Bad Request) if the postalCode is not
	 *         valid, or with status 500 (Internal Server Error) if the postalCode
	 *         couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/postal-codes")
	@Timed
	public ResponseEntity<PostalCode> updatePostalCode(@Valid @RequestBody PostalCode postalCode)
			throws URISyntaxException {
		log.debug("REST request to update PostalCode : {}", postalCode);
		if (postalCode.getId() == null) {
			return createPostalCode(postalCode);
		}
		PostalCode result = postalCodeRepository.save(postalCode);
		postalCodeSearchRepository.save(result);
		masterAuditService.addPostalCode(result, ENTITY_NAME, UpdateType.UPDATE);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, postalCode.getId().toString())).body(result);
	}

	/**
	 * GET /postal-codes : get all the postalCodes.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of postalCodes
	 *         in body
	 */
	@GetMapping("/postal-codes")
	@Timed
	public ResponseEntity<List<PostalCode>> getAllPostalCodes(Pageable pageable) {
		log.debug("REST request to get all PostalCodes");
		Page<PostalCode> page = postalCodeRepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/postal-codes");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@GetMapping("/postal-codes/mobile")
	@Timed
	public ResponseEntity<List<PostalCode>> getAllPostalCodesMobile(Pageable pageable) {
		log.debug("REST request to get all PostalCodes");
		List<PostalCode> list = new ArrayList<>();
		Page<PostalCode> pages = postalCodeRepository.findAll(pageable);
		for (PostalCode postalCode : pages.getContent()) {
			postalCode.getCity().getProvince().getCountry().setImage(null);
			postalCode.getCity().getProvince().getCountry().setImageFlag(null);
			list.add(postalCode);
		}
		Page<PostalCode> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/postal-codes/mobile");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /postal-codes/:id : get the "id" postalCode.
	 *
	 * @param id
	 *            the id of the postalCode to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the postalCode,
	 *         or with status 404 (Not Found)
	 */
	@GetMapping("/postal-codes/{id}")
	@Timed
	public ResponseEntity<PostalCode> getPostalCode(@PathVariable Long id) {
		log.debug("REST request to get PostalCode : {}", id);
		PostalCode postalCode = postalCodeRepository.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(postalCode));
	}

	@GetMapping("/postal-codes/mobile/{id}")
	@Timed
	public ResponseEntity<PostalCode> getPostalCodeMobile(@PathVariable Long id) {
		log.debug("REST request to get PostalCode : {}", id);
		PostalCode postalCode = postalCodeRepository.findOne(id);
		postalCode.getCity().getProvince().getCountry().setImage(null);
		postalCode.getCity().getProvince().getCountry().setImageFlag(null);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(postalCode));
	}

	/**
	 * DELETE /postal-codes/:id : delete the "id" postalCode.
	 *
	 * @param id
	 *            the id of the postalCode to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/postal-codes/{id}")
	@Timed
	public ResponseEntity<Void> deletePostalCode(@PathVariable Long id) {
		log.debug("REST request to delete PostalCode : {}", id);
		PostalCode result = postalCodeRepository.findOne(id);
		postalCodeRepository.delete(id);
		postalCodeSearchRepository.delete(id);
		masterAuditService.addPostalCode(result, ENTITY_NAME, UpdateType.DELETE);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * SEARCH /_search/postal-codes?query=:query : search for the postalCode
	 * corresponding to the query.
	 *
	 * @param query
	 *            the query of the postalCode search
	 * @return the result of the search
	 */
	@GetMapping("/_search/postal-codes")
	@Timed
	public ResponseEntity<List<PostalCode>> searchPostalCodes(@RequestParam String query, Pageable pageable) {
		log.debug("REST request to search PostalCodes for query {}", query);
		List<PostalCode> postalCode = StreamSupport
				.stream(postalCodeSearchRepository.search(queryStringQuery(query), pageable).spliterator(), false)
				.collect(Collectors.toList());
		Page<PostalCode> page = new PageImpl<>(postalCode);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/_search/postal-codes");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@GetMapping("/_search/postal-codes/mobile")
	@Timed
	public ResponseEntity<List<PostalCode>> searchPostalCodesMobile(@RequestParam String query, Pageable pageable) {
		log.debug("REST request to search PostalCodes for query {}", query);
		List<PostalCode> list = new ArrayList<>();
		List<PostalCode> postalCode = StreamSupport
				.stream(postalCodeSearchRepository.search(queryStringQuery(query), pageable).spliterator(), false)
				.collect(Collectors.toList());
		for (PostalCode pCode : postalCode) {
			pCode.getCity().getProvince().getCountry().setImage(null);
			pCode.getCity().getProvince().getCountry().setImageFlag(null);
			list.add(pCode);
		}
		Page<PostalCode> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/_search/postal-codes/mobile");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

}

package com.cus.jastip.profile.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cus.jastip.profile.domain.Address;
import com.cus.jastip.profile.domain.BlockedByList;
import com.cus.jastip.profile.domain.Profile;
import com.cus.jastip.profile.domain.enumeration.UpdateType;
import com.cus.jastip.profile.repository.BlockedByListRepository;
import com.cus.jastip.profile.repository.ProfileRepository;
import com.cus.jastip.profile.repository.search.BlockedByListSearchRepository;
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
 * REST controller for managing BlockedByList.
 */
@RestController
@RequestMapping("/api")
public class BlockedByListResource {

	@Autowired
	private ProfileRepository profileRepository;
	
	@Autowired
	private ProfilesAuditService profilesAuditService;

	private final Logger log = LoggerFactory.getLogger(BlockedByListResource.class);

	private static final String ENTITY_NAME = "blockedByList";

	private final BlockedByListRepository blockedByListRepository;

	private final BlockedByListSearchRepository blockedByListSearchRepository;

	public BlockedByListResource(BlockedByListRepository blockedByListRepository,
			BlockedByListSearchRepository blockedByListSearchRepository) {
		this.blockedByListRepository = blockedByListRepository;
		this.blockedByListSearchRepository = blockedByListSearchRepository;
	}

	/**
	 * POST /blocked-by-lists : Create a new blockedByList.
	 *
	 * @param blockedByList
	 *            the blockedByList to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         blockedByList, or with status 400 (Bad Request) if the blockedByList
	 *         has already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/blocked-by-lists")
	@Timed
	public ResponseEntity<BlockedByList> createBlockedByList(@Valid @RequestBody BlockedByList blockedByList)
			throws URISyntaxException {
		log.debug("REST request to save BlockedByList : {}", blockedByList);
		if (blockedByList.getId() != null) {
			throw new BadRequestAlertException("A new blockedByList cannot already have an ID", ENTITY_NAME,
					"idexists");
		}
		BlockedByList result = blockedByListRepository.save(blockedByList);
		blockedByListSearchRepository.save(result);
		profilesAuditService.addBlockedByList(result, ENTITY_NAME, UpdateType.CREATE);
		return ResponseEntity.created(new URI("/api/blocked-by-lists/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	@GetMapping("/blocked-by-lists/profiles/{id}")
	@Timed
	public ResponseEntity<List<BlockedByList>> getBlockedByListByProfile(@PathVariable Long id, Pageable pageable) {
		log.debug("REST request to get all BlockedByList");
		Profile profile = profileRepository.findOne(id);
		List<BlockedByList> list = blockedByListRepository.findByProfile(profile, pageable);
		Page<BlockedByList> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page,
				"/api/blocked-by-lists/profiles/" + id);
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@GetMapping("/blocked-by-lists/profiles/mobile/{id}")
	@Timed
	public ResponseEntity<List<BlockedByList>> getBlockedByListByProfileMobile(@PathVariable Long id,
			Pageable pageable) {
		log.debug("REST request to get all BlockedByList");
		List<BlockedByList> list = new ArrayList<>();
		Profile profile = profileRepository.findOne(id);
		List<BlockedByList> block = blockedByListRepository.findByProfile(profile, pageable);
		for (BlockedByList blockedByList : block) {
			blockedByList.getProfile().setImage(null);
			list.add(blockedByList);
		}
		Page<BlockedByList> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page,
				"/api/blocked-by-lists/profiles/mobile/" + id);
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * PUT /blocked-by-lists : Updates an existing blockedByList.
	 *
	 * @param blockedByList
	 *            the blockedByList to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         blockedByList, or with status 400 (Bad Request) if the blockedByList
	 *         is not valid, or with status 500 (Internal Server Error) if the
	 *         blockedByList couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/blocked-by-lists")
	@Timed
	public ResponseEntity<BlockedByList> updateBlockedByList(@Valid @RequestBody BlockedByList blockedByList)
			throws URISyntaxException {
		log.debug("REST request to update BlockedByList : {}", blockedByList);
		if (blockedByList.getId() == null) {
			return createBlockedByList(blockedByList);
		}
		BlockedByList result = blockedByListRepository.save(blockedByList);
		blockedByListSearchRepository.save(result);
		profilesAuditService.addBlockedByList(result, ENTITY_NAME, UpdateType.UPDATE);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, blockedByList.getId().toString()))
				.body(result);
	}

	/**
	 * GET /blocked-by-lists : get all the blockedByLists.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         blockedByLists in body
	 */
	@GetMapping("/blocked-by-lists")
	@Timed
	public ResponseEntity<List<BlockedByList>> getAllBlockedByLists(Pageable pageable) {
		log.debug("REST request to get all BlockedByLists");
		Page<BlockedByList> page = blockedByListRepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/blocked-by-lists");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@GetMapping("/blocked-by-lists/mobile")
	@Timed
	public ResponseEntity<List<BlockedByList>> getAllBlockedByListsMobile(Pageable pageable) {
		log.debug("REST request to get all BlockedByLists");
		List<BlockedByList> list = new ArrayList<>();
		Page<BlockedByList> pages = blockedByListRepository.findAll(pageable);
		for (BlockedByList blockedByList : pages.getContent()) {
			blockedByList.getProfile().setImage(null);
			list.add(blockedByList);
		}
		Page<BlockedByList> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/blocked-by-lists/mobile");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /blocked-by-lists/:id : get the "id" blockedByList.
	 *
	 * @param id
	 *            the id of the blockedByList to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         blockedByList, or with status 404 (Not Found)
	 */
	@GetMapping("/blocked-by-lists/{id}")
	@Timed
	public ResponseEntity<BlockedByList> getBlockedByList(@PathVariable Long id) {
		log.debug("REST request to get BlockedByList : {}", id);
		BlockedByList blockedByList = blockedByListRepository.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(blockedByList));
	}

	@GetMapping("/blocked-by-lists/mobile/{id}")
	@Timed
	public ResponseEntity<BlockedByList> getBlockedByListMobile(@PathVariable Long id) {
		log.debug("REST request to get BlockedByList : {}", id);
		BlockedByList blockedByList = blockedByListRepository.findOne(id);
		blockedByList.getProfile().setImage(null);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(blockedByList));
	}

	/**
	 * DELETE /blocked-by-lists/:id : delete the "id" blockedByList.
	 *
	 * @param id
	 *            the id of the blockedByList to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/blocked-by-lists/{id}")
	@Timed
	public ResponseEntity<Void> deleteBlockedByList(@PathVariable Long id) {
		log.debug("REST request to delete BlockedByList : {}", id);
		BlockedByList result = blockedByListRepository.findOne(id);
		blockedByListRepository.delete(id);
		blockedByListSearchRepository.delete(id);
		profilesAuditService.addBlockedByList(result, ENTITY_NAME, UpdateType.DELETE);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * SEARCH /_search/blocked-by-lists?query=:query : search for the blockedByList
	 * corresponding to the query.
	 *
	 * @param query
	 *            the query of the blockedByList search
	 * @return the result of the search
	 */
	@GetMapping("/_search/blocked-by-lists")
	@Timed
	public ResponseEntity<List<BlockedByList>> searchBlockedByLists(@RequestParam String query, Pageable pageable) {
		log.debug("REST request to search BlockedByLists for query {}", query);
		List<BlockedByList> list = StreamSupport
				.stream(blockedByListSearchRepository.search(queryStringQuery(query), pageable).spliterator(), false)
				.collect(Collectors.toList());
		Page<BlockedByList> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/_search/blocked-by-lists");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@GetMapping("/_search/blocked-by-lists/mobile")
	@Timed
	public ResponseEntity<List<BlockedByList>> searchBlockedByListsMobile(@RequestParam String query,
			Pageable pageable) {
		log.debug("REST request to search BlockedByLists for query {}", query);
		List<BlockedByList> list = new ArrayList<>();
		List<BlockedByList> block = StreamSupport
				.stream(blockedByListSearchRepository.search(queryStringQuery(query), pageable).spliterator(), false)
				.collect(Collectors.toList());
		for (BlockedByList blockedByList : block) {
			blockedByList.getProfile().setImage(null);
			list.add(blockedByList);
		}
		Page<BlockedByList> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page,
				"/api/_search/blocked-by-lists/mobile");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

}

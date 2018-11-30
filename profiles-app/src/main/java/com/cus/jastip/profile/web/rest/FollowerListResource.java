package com.cus.jastip.profile.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cus.jastip.profile.domain.Feedback;
import com.cus.jastip.profile.domain.FollowerList;
import com.cus.jastip.profile.domain.FollowingList;
import com.cus.jastip.profile.domain.Profile;
import com.cus.jastip.profile.domain.enumeration.UpdateType;
import com.cus.jastip.profile.repository.FollowerListRepository;
import com.cus.jastip.profile.repository.ProfileRepository;
import com.cus.jastip.profile.repository.search.FollowerListSearchRepository;
import com.cus.jastip.profile.service.FollowService;
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
 * REST controller for managing FollowerList.
 */
@RestController
@RequestMapping("/api")
public class FollowerListResource {

	@Autowired
	private ProfileRepository profileRepository;

	@Autowired
	private FollowService followService;

	@Autowired
	private ProfilesAuditService profilesAuditService;

	private final Logger log = LoggerFactory.getLogger(FollowerListResource.class);

	private static final String ENTITY_NAME = "followerList";

	private final FollowerListRepository followerListRepository;

	private final FollowerListSearchRepository followerListSearchRepository;

	public FollowerListResource(FollowerListRepository followerListRepository,
			FollowerListSearchRepository followerListSearchRepository) {
		this.followerListRepository = followerListRepository;
		this.followerListSearchRepository = followerListSearchRepository;
	}

	@GetMapping("/follower-lists/profiles/{id}")
	@Timed
	public ResponseEntity<List<FollowerList>> getFollowerListByProfile(@PathVariable Long id, Pageable pageable) {
		log.debug("REST request to get all FollowerList");
		Profile profile = profileRepository.findOne(id);
		List<FollowerList> list = followerListRepository.findByProfile(profile, pageable);
		Page<FollowerList> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/follower-list/profiles/" + id);
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@GetMapping("/follower-lists/profiles/mobile/{id}")
	@Timed
	public ResponseEntity<List<FollowerList>> getFollowerListByProfileMobile(@PathVariable Long id, Pageable pageable) {
		log.debug("REST request to get all FollowerList");
		Profile profile = profileRepository.findOne(id);
		List<FollowerList> list = new ArrayList<>();
		List<FollowerList> fl = followerListRepository.findByProfile(profile, pageable);
		for (FollowerList followerList : fl) {
			followerList.getProfile().setImage(null);
			list.add(followerList);
		}
		Page<FollowerList> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page,
				"/api/follower-list/profiles/mobile/" + id);
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * POST /follower-lists : Create a new followerList.
	 *
	 * @param followerList
	 *            the followerList to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         followerList, or with status 400 (Bad Request) if the followerList
	 *         has already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/follower-lists")
	@Timed
	public ResponseEntity<FollowerList> createFollowerList(@Valid @RequestBody FollowerList followerList)
			throws URISyntaxException {
		log.debug("REST request to save FollowerList : {}", followerList);
		if (followerList.getId() != null) {
			throw new BadRequestAlertException("A new followerList cannot already have an ID", ENTITY_NAME, "idexists");
		}
		FollowerList result = followerListRepository.save(followerList);
		followerListSearchRepository.save(result);
		profilesAuditService.addFollowerList(result, ENTITY_NAME, UpdateType.CREATE);
		return ResponseEntity.created(new URI("/api/follower-lists/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /follower-lists : Updates an existing followerList.
	 *
	 * @param followerList
	 *            the followerList to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         followerList, or with status 400 (Bad Request) if the followerList is
	 *         not valid, or with status 500 (Internal Server Error) if the
	 *         followerList couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/follower-lists")
	@Timed
	public ResponseEntity<FollowerList> updateFollowerList(@Valid @RequestBody FollowerList followerList)
			throws URISyntaxException {
		log.debug("REST request to update FollowerList : {}", followerList);
		if (followerList.getId() == null) {
			return createFollowerList(followerList);
		}
		FollowerList result = followerListRepository.save(followerList);
		followerListSearchRepository.save(result);
		profilesAuditService.addFollowerList(result, ENTITY_NAME, UpdateType.UPDATE);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, followerList.getId().toString())).body(result);
	}

	/**
	 * GET /follower-lists : get all the followerLists.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of followerLists
	 *         in body
	 */
	@GetMapping("/follower-lists")
	@Timed
	public ResponseEntity<List<FollowerList>> getAllFollowerLists(Pageable pageable) {
		log.debug("REST request to get all FollowerLists");
		Page<FollowerList> page = followerListRepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/follower-lists");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@GetMapping("/follower-lists/mobile")
	@Timed
	public ResponseEntity<List<FollowerList>> getAllFollowerListsMobile(Pageable pageable) {
		log.debug("REST request to get all FollowerLists");
		List<FollowerList> list = new ArrayList<>();
		Page<FollowerList> pages = followerListRepository.findAll(pageable);
		for (FollowerList followerList : pages.getContent()) {
			followerList.getProfile().setImage(null);
			list.add(followerList);
		}
		Page<FollowerList> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/follower-lists/mobile");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /follower-lists/:id : get the "id" followerList.
	 *
	 * @param id
	 *            the id of the followerList to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         followerList, or with status 404 (Not Found)
	 */
	@GetMapping("/follower-lists/{id}")
	@Timed
	public ResponseEntity<FollowerList> getFollowerList(@PathVariable Long id) {
		log.debug("REST request to get FollowerList : {}", id);
		FollowerList followerList = followerListRepository.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(followerList));
	}

	@GetMapping("/follower-lists/mobile/{id}")
	@Timed
	public ResponseEntity<FollowerList> getFollowerListMobile(@PathVariable Long id) {
		log.debug("REST request to get FollowerList : {}", id);
		FollowerList followerList = followerListRepository.findOne(id);
		followerList.getProfile().setImage(null);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(followerList));
	}

	/**
	 * DELETE /follower-lists/:id : delete the "id" followerList.
	 *
	 * @param id
	 *            the id of the followerList to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/follower-lists/{id}")
	@Timed
	public ResponseEntity<Void> deleteFollowerList(@PathVariable Long id) {
		log.debug("REST request to delete FollowerList : {}", id);
		FollowerList result = followerListRepository.findOne(id);
		followerListRepository.delete(id);
		followerListSearchRepository.delete(id);
		profilesAuditService.addFollowerList(result, ENTITY_NAME, UpdateType.DELETE);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * SEARCH /_search/follower-lists?query=:query : search for the followerList
	 * corresponding to the query.
	 *
	 * @param query
	 *            the query of the followerList search
	 * @return the result of the search
	 */
	@GetMapping("/_search/follower-lists")
	@Timed
	public ResponseEntity<List<FollowerList>> searchFollowerLists(@RequestParam String query, Pageable pageable) {
		log.debug("REST request to search FollowerLists for query {}", query);
		List<FollowerList> list = StreamSupport
				.stream(followerListSearchRepository.search(queryStringQuery(query), pageable).spliterator(), false)
				.collect(Collectors.toList());
		Page<FollowerList> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/_search/feedbacks");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@GetMapping("/_search/follower-lists/mobile")
	@Timed
	public ResponseEntity<List<FollowerList>> searchFollowerListsMobile(@RequestParam String query, Pageable pageable) {
		log.debug("REST request to search FollowerLists for query {}", query);
		List<FollowerList> list = new ArrayList<>();
		List<FollowerList> fl = StreamSupport
				.stream(followerListSearchRepository.search(queryStringQuery(query), pageable).spliterator(), false)
				.collect(Collectors.toList());
		for (FollowerList followerList : fl) {
			followerList.getProfile().setImage(null);
			list.add(followerList);
		}
		Page<FollowerList> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/_search/feedbacks/mobile");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

}

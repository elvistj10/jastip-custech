package com.cus.jastip.profile.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cus.jastip.profile.domain.CreditCard;
import com.cus.jastip.profile.domain.Feedback;
import com.cus.jastip.profile.domain.FollowingList;
import com.cus.jastip.profile.domain.Profile;
import com.cus.jastip.profile.repository.FollowingListRepository;
import com.cus.jastip.profile.repository.ProfileRepository;
import com.cus.jastip.profile.repository.search.FollowingListSearchRepository;
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
 * REST controller for managing FollowingList.
 */
@RestController
@RequestMapping("/api")
public class FollowingListResource {

	@Autowired
	private FollowService followService;

	@Autowired
	private ProfileRepository profileRepository;
	
	@Autowired
	private ProfilesAuditService profilesAuditService;

	private final Logger log = LoggerFactory.getLogger(FollowingListResource.class);

	private static final String ENTITY_NAME = "followingList";

	private final FollowingListRepository followingListRepository;

	private final FollowingListSearchRepository followingListSearchRepository;

	public FollowingListResource(FollowingListRepository followingListRepository,
			FollowingListSearchRepository followingListSearchRepository) {
		this.followingListRepository = followingListRepository;
		this.followingListSearchRepository = followingListSearchRepository;
	}

	@GetMapping("/following-lists/profiles/{id}")
	@Timed
	public ResponseEntity<List<FollowingList>> getFollowingListByprofile(@PathVariable Long id, Pageable pageable) {
		log.debug("REST request to get all FollowingList");
		Profile profile = profileRepository.findOne(id);
		List<FollowingList> list = followingListRepository.findByProfile(profile, pageable);
		Page<FollowingList> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/following-lists/profiles/" + id);
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@GetMapping("/following-lists/profiles/mobile/{id}")
	@Timed
	public ResponseEntity<List<FollowingList>> getFollowingListByprofileMobile(@PathVariable Long id,
			Pageable pageable) {
		log.debug("REST request to get all FollowingList");
		List<FollowingList> list = new ArrayList<>();
		Profile profile = profileRepository.findOne(id);
		List<FollowingList> fl = followingListRepository.findByProfile(profile, pageable);
		for (FollowingList followingList : fl) {
			followingList.getProfile().setImage(null);
			list.add(followingList);
		}
		Page<FollowingList> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page,
				"/api/following-lists/profiles/mobile/" + id);
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * POST /following-lists : Create a new followingList.
	 *
	 * @param followingList
	 *            the followingList to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         followingList, or with status 400 (Bad Request) if the followingList
	 *         has already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/following-lists")
	@Timed
	public void createFollowingList(@Valid @RequestBody FollowingList followingList) throws URISyntaxException {
		log.debug("REST request to save FollowingList : {}", followingList);
		followService.updateFollowing(followingList);

	}

	/**
	 * PUT /following-lists : Updates an existing followingList.
	 *
	 * @param followingList
	 *            the followingList to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         followingList, or with status 400 (Bad Request) if the followingList
	 *         is not valid, or with status 500 (Internal Server Error) if the
	 *         followingList couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/following-lists")
	@Timed
	public void updateFollowingList(@Valid @RequestBody FollowingList followingList) throws URISyntaxException {
		log.debug("REST request to update FollowingList : {}", followingList);
		createFollowingList(followingList);
	}

	/**
	 * GET /following-lists : get all the followingLists.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         followingLists in body
	 */
	@GetMapping("/following-lists")
	@Timed
	public ResponseEntity<List<FollowingList>> getAllFollowingLists(Pageable pageable) {
		log.debug("REST request to get all FollowingLists");
		Page<FollowingList> page = followingListRepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/following-lists");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@GetMapping("/following-lists/mobile")
	@Timed
	public ResponseEntity<List<FollowingList>> getAllFollowingListsMobile(Pageable pageable) {
		log.debug("REST request to get all FollowingLists");
		List<FollowingList> list = new ArrayList<>();
		Page<FollowingList> pages = followingListRepository.findAll(pageable);
		for (FollowingList followingList : pages.getContent()) {
			followingList.getProfile().setImage(null);
			list.add(followingList);
		}
		Page<FollowingList> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/following-lists/mobile");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /following-lists/:id : get the "id" followingList.
	 *
	 * @param id
	 *            the id of the followingList to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         followingList, or with status 404 (Not Found)
	 */
	@GetMapping("/following-lists/{id}")
	@Timed
	public ResponseEntity<FollowingList> getFollowingList(@PathVariable Long id) {
		log.debug("REST request to get FollowingList : {}", id);
		FollowingList followingList = followingListRepository.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(followingList));
	}

	@GetMapping("/following-lists/mobile/{id}")
	@Timed
	public ResponseEntity<FollowingList> getFollowingListMobile(@PathVariable Long id) {
		log.debug("REST request to get FollowingList : {}", id);
		FollowingList followingList = followingListRepository.findOne(id);
		followingList.getProfile().setImage(null);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(followingList));
	}

	/**
	 * DELETE /following-lists/:id : delete the "id" followingList.
	 *
	 * @param id
	 *            the id of the followingList to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/following-lists/{id}")
	@Timed
	public ResponseEntity<Void> deleteFollowingList(@PathVariable Long id) {
		log.debug("REST request to delete FollowingList : {}", id);
		followingListRepository.delete(id);
		followingListSearchRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * SEARCH /_search/following-lists?query=:query : search for the followingList
	 * corresponding to the query.
	 *
	 * @param query
	 *            the query of the followingList search
	 * @return the result of the search
	 */
	@GetMapping("/_search/following-lists")
	@Timed
	public ResponseEntity<List<FollowingList>> searchFollowingLists(@RequestParam String query, Pageable pageable) {
		log.debug("REST request to search FollowingLists for query {}", query);
		List<FollowingList> list = StreamSupport
				.stream(followingListSearchRepository.search(queryStringQuery(query), pageable).spliterator(), false)
				.collect(Collectors.toList());
		Page<FollowingList> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/_search/following-lists");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@GetMapping("/_search/following-lists/mobile")
	@Timed
	public ResponseEntity<List<FollowingList>> searchFollowingListsMobile(@RequestParam String query,
			Pageable pageable) {
		log.debug("REST request to search FollowingLists for query {}", query);
		List<FollowingList> list = new ArrayList<>();
		List<FollowingList> fl = StreamSupport
				.stream(followingListSearchRepository.search(queryStringQuery(query), pageable).spliterator(), false)
				.collect(Collectors.toList());
		for (FollowingList followingList : fl) {
			followingList.getProfile().setImage(null);
			list.add(followingList);
		}
		Page<FollowingList> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/_search/following-lists/mobile");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

}

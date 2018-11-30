package com.cus.jastip.profile.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cus.jastip.profile.domain.BlockList;
import com.cus.jastip.profile.domain.BlockedByList;
import com.cus.jastip.profile.domain.Profile;
import com.cus.jastip.profile.domain.enumeration.UpdateType;
import com.cus.jastip.profile.repository.BlockListRepository;
import com.cus.jastip.profile.repository.ProfileRepository;
import com.cus.jastip.profile.repository.search.BlockListSearchRepository;
import com.cus.jastip.profile.service.BlockService;
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
 * REST controller for managing BlockList.
 */
@RestController
@RequestMapping("/api")
public class BlockListResource {

	@Autowired
	private ProfileRepository profileRepository;

	@Autowired
	private ProfilesAuditService profilesAuditService;

	@Autowired
	private BlockService blockService;

	private final Logger log = LoggerFactory.getLogger(BlockListResource.class);

	private static final String ENTITY_NAME = "blockList";

	private final BlockListRepository blockListRepository;

	private final BlockListSearchRepository blockListSearchRepository;

	public BlockListResource(BlockListRepository blockListRepository,
			BlockListSearchRepository blockListSearchRepository) {
		this.blockListRepository = blockListRepository;
		this.blockListSearchRepository = blockListSearchRepository;
	}

	/**
	 * POST /block-lists : Create a new blockList.
	 *
	 * @param blockList
	 *            the blockList to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         blockList, or with status 400 (Bad Request) if the blockList has
	 *         already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/block-lists")
	@Timed
	public void createBlockList(@Valid @RequestBody BlockList blockList) throws URISyntaxException {
		log.debug("REST request to save BlockList : {}", blockList);
		blockService.updateBlockList(blockList);
	}

	@GetMapping("/block-lists/profiles/{id}")
	@Timed
	public ResponseEntity<List<BlockList>> getBlockListByProfile(@PathVariable Long id, Pageable pageable) {
		log.debug("REST request to get all BlockList");
		Profile profile = profileRepository.findOne(id);
		List<BlockList> list = blockListRepository.findByProfile(profile, pageable);
		Page<BlockList> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/block-lists/profiles/" + id);
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@GetMapping("/block-lists/profiles/mobile/{id}")
	@Timed
	public ResponseEntity<List<BlockList>> getBlockListByProfileMobile(@PathVariable Long id, Pageable pageable) {
		log.debug("REST request to get all BlockList");
		List<BlockList> list = new ArrayList<>();
		Profile profile = profileRepository.findOne(id);
		List<BlockList> block = blockListRepository.findByProfile(profile, pageable);
		for (BlockList blockList : block) {
			blockList.getProfile().setImage(null);
			list.add(blockList);
		}
		Page<BlockList> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page,
				"/api/block-lists/profiles/mobile/" + id);
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * PUT /block-lists : Updates an existing blockList.
	 *
	 * @param blockList
	 *            the blockList to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         blockList, or with status 400 (Bad Request) if the blockList is not
	 *         valid, or with status 500 (Internal Server Error) if the blockList
	 *         couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/block-lists")
	@Timed
	public void updateBlockList(@Valid @RequestBody BlockList blockList) throws URISyntaxException {
		log.debug("REST request to update BlockList : {}", blockList);
		createBlockList(blockList);
	}

	/**
	 * GET /block-lists : get all the blockLists.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of blockLists in
	 *         body
	 */
	@GetMapping("/block-lists")
	@Timed
	public ResponseEntity<List<BlockList>> getAllBlockLists(Pageable pageable) {
		log.debug("REST request to get all BlockLists");
		Page<BlockList> page = blockListRepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/block-lists");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@GetMapping("/block-lists/mobile")
	@Timed
	public ResponseEntity<List<BlockList>> getAllBlockListsMobile(Pageable pageable) {
		log.debug("REST request to get all BlockLists");
		List<BlockList> list = new ArrayList<>();
		Page<BlockList> pages = blockListRepository.findAll(pageable);
		for (BlockList blockList : pages.getContent()) {
			blockList.getProfile().setImage(null);
			list.add(blockList);
		}
		Page<BlockList> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/block-lists/mobile");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /block-lists/:id : get the "id" blockList.
	 *
	 * @param id
	 *            the id of the blockList to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the blockList,
	 *         or with status 404 (Not Found)
	 */
	@GetMapping("/block-lists/{id}")
	@Timed
	public ResponseEntity<BlockList> getBlockList(@PathVariable Long id) {
		log.debug("REST request to get BlockList : {}", id);
		BlockList blockList = blockListRepository.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(blockList));
	}

	@GetMapping("/block-lists/mobile/{id}")
	@Timed
	public ResponseEntity<BlockList> getBlockListMobile(@PathVariable Long id) {
		log.debug("REST request to get BlockList : {}", id);
		BlockList blockList = blockListRepository.findOne(id);
		blockList.getProfile().setImage(null);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(blockList));
	}

	/**
	 * DELETE /block-lists/:id : delete the "id" blockList.
	 *
	 * @param id
	 *            the id of the blockList to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/block-lists/{id}")
	@Timed
	public ResponseEntity<Void> deleteBlockList(@PathVariable Long id) {
		log.debug("REST request to delete BlockList : {}", id);
		BlockList result = blockListRepository.findOne(id);
		blockListRepository.delete(id);
		blockListSearchRepository.delete(id);
		profilesAuditService.addBlockList(result, ENTITY_NAME, UpdateType.DELETE);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * SEARCH /_search/block-lists?query=:query : search for the blockList
	 * corresponding to the query.
	 *
	 * @param query
	 *            the query of the blockList search
	 * @return the result of the search
	 */
	@GetMapping("/_search/block-lists")
	@Timed
	public ResponseEntity<List<BlockList>> searchBlockLists(@RequestParam String query, Pageable pageable) {
		log.debug("REST request to search BlockLists for query {}", query);
		List<BlockList> list = StreamSupport
				.stream(blockListSearchRepository.search(queryStringQuery(query), pageable).spliterator(), false)
				.collect(Collectors.toList());
		Page<BlockList> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/_search/block-lists");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@GetMapping("/_search/block-lists/mobile")
	@Timed
	public ResponseEntity<List<BlockList>> searchBlockListsMobile(@RequestParam String query, Pageable pageable) {
		log.debug("REST request to search BlockLists for query {}", query);
		List<BlockList> list = new ArrayList<>();
		List<BlockList> block = StreamSupport
				.stream(blockListSearchRepository.search(queryStringQuery(query), pageable).spliterator(), false)
				.collect(Collectors.toList());
		for (BlockList blockList : block) {
			blockList.getProfile().setImage(null);
			list.add(blockList);
		}
		Page<BlockList> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/_search/block-lists/mobile");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

}

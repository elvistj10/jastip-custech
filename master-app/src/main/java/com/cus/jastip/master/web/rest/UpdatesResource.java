package com.cus.jastip.master.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cus.jastip.master.domain.BusinessAccount;
import com.cus.jastip.master.domain.ServiceFee;
import com.cus.jastip.master.domain.Updates;

import com.cus.jastip.master.repository.UpdatesRepository;
import com.cus.jastip.master.repository.search.UpdatesSearchRepository;
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
 * REST controller for managing Updates.
 */
@RestController
@RequestMapping("/api")
public class UpdatesResource {

	private final Logger log = LoggerFactory.getLogger(UpdatesResource.class);

	private static final String ENTITY_NAME = "updates";
	@Autowired
	private MasterAuditService masterAuditService;

	private final UpdatesRepository updatesRepository;

	private final UpdatesSearchRepository updatesSearchRepository;

	public UpdatesResource(UpdatesRepository updatesRepository, UpdatesSearchRepository updatesSearchRepository) {
		this.updatesRepository = updatesRepository;
		this.updatesSearchRepository = updatesSearchRepository;
	}

	/**
	 * POST /updates : Create a new updates.
	 *
	 * @param updates
	 *            the updates to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         updates, or with status 400 (Bad Request) if the updates has already
	 *         an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/updates")
	@Timed
	public ResponseEntity<Updates> createUpdates(@Valid @RequestBody Updates updates) throws URISyntaxException {
		log.debug("REST request to save Updates : {}", updates);
		if (updates.getId() != null) {
			throw new BadRequestAlertException("A new updates cannot already have an ID", ENTITY_NAME, "idexists");
		}
		Updates result = updatesRepository.save(updates);
		updatesSearchRepository.save(result);
		return ResponseEntity.created(new URI("/api/updates/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /updates : Updates an existing updates.
	 *
	 * @param updates
	 *            the updates to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         updates, or with status 400 (Bad Request) if the updates is not
	 *         valid, or with status 500 (Internal Server Error) if the updates
	 *         couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/updates")
	@Timed
	public ResponseEntity<Updates> updateUpdates(@Valid @RequestBody Updates updates) throws URISyntaxException {
		log.debug("REST request to update Updates : {}", updates);
		if (updates.getId() == null) {
			return createUpdates(updates);
		}
		Updates result = updatesRepository.save(updates);
		updatesSearchRepository.save(result);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, updates.getId().toString()))
				.body(result);
	}

	/**
	 * GET /updates : get all the updates.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of updates in
	 *         body
	 */
	@GetMapping("/updates")
	@Timed
	public ResponseEntity<List<Updates>> getAllUpdates(Pageable pageable) {
		log.debug("REST request to get all Updates");
		Page<Updates> page = updatesRepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/updates");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /updates/:id : get the "id" updates.
	 *
	 * @param id
	 *            the id of the updates to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the updates, or
	 *         with status 404 (Not Found)
	 */
	@GetMapping("/updates/{id}")
	@Timed
	public ResponseEntity<Updates> getUpdates(@PathVariable Long id) {
		log.debug("REST request to get Updates : {}", id);
		Updates updates = updatesRepository.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(updates));
	}

	/**
	 * DELETE /updates/:id : delete the "id" updates.
	 *
	 * @param id
	 *            the id of the updates to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/updates/{id}")
	@Timed
	public ResponseEntity<Void> deleteUpdates(@PathVariable Long id) {
		log.debug("REST request to delete Updates : {}", id);
		Updates result = updatesRepository.findOne(id);
		updatesRepository.delete(id);
		updatesSearchRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * SEARCH /_search/updates?query=:query : search for the updates corresponding
	 * to the query.
	 *
	 * @param query
	 *            the query of the updates search
	 * @return the result of the search
	 */
	@GetMapping("/_search/updates")
	@Timed
	public ResponseEntity<List<Updates>> searchUpdates(@RequestParam String query, Pageable pageable) {
		log.debug("REST request to search Updates for query {}", query);
		List<Updates> update = StreamSupport
				.stream(updatesSearchRepository.search(queryStringQuery(query), pageable).spliterator(), false)
				.collect(Collectors.toList());
		Page<Updates> page = new PageImpl<>(update);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/_search/updates");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

}

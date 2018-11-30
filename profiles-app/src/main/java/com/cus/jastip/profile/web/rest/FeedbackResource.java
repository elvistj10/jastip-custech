package com.cus.jastip.profile.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cus.jastip.profile.domain.BlockList;
import com.cus.jastip.profile.domain.CreditCard;
import com.cus.jastip.profile.domain.Feedback;
import com.cus.jastip.profile.domain.FollowerList;
import com.cus.jastip.profile.domain.Profile;
import com.cus.jastip.profile.domain.enumeration.UpdateType;
import com.cus.jastip.profile.repository.FeedbackRepository;
import com.cus.jastip.profile.repository.ProfileRepository;
import com.cus.jastip.profile.repository.search.FeedbackSearchRepository;
import com.cus.jastip.profile.service.FeedbackService;
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
 * REST controller for managing Feedback.
 */
@RestController
@RequestMapping("/api")
public class FeedbackResource {

	@Autowired
	private FeedbackService feedbackService;

	@Autowired
	private ProfileRepository profileRepository;
	
	@Autowired
	private ProfilesAuditService profilesAuditService;

	private final Logger log = LoggerFactory.getLogger(FeedbackResource.class);

	private static final String ENTITY_NAME = "feedback";

	private final FeedbackRepository feedbackRepository;

	private final FeedbackSearchRepository feedbackSearchRepository;

	public FeedbackResource(FeedbackRepository feedbackRepository, FeedbackSearchRepository feedbackSearchRepository) {
		this.feedbackRepository = feedbackRepository;
		this.feedbackSearchRepository = feedbackSearchRepository;
	}

	@GetMapping("/feedbacks/profiles/{id}")
	@Timed
	public ResponseEntity<List<Feedback>> getFeedbackByProfile(@PathVariable Long id, Pageable pageable) {
		log.debug("REST request to get all Feedback");
		Profile profile = profileRepository.findOne(id);
		List<Feedback> list = feedbackRepository.findByProfile(profile, pageable);
		Page<Feedback> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/feedbacks/profiles/" + id);
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@GetMapping("/feedbacks/profiles/mobile/{id}")
	@Timed
	public ResponseEntity<List<Feedback>> getFeedbackByProfileMobile(@PathVariable Long id, Pageable pageable) {
		log.debug("REST request to get all Feedback");
		Profile profile = profileRepository.findOne(id);
		List<Feedback> list = new ArrayList<>();
		List<Feedback> fb = feedbackRepository.findByProfile(profile, pageable);
		for (Feedback feedback : fb) {
			feedback.getProfile().setImage(null);
			list.add(feedback);
		}
		Page<Feedback> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page,
				"/api/feedbacks/profiles/mobile/" + id);
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * POST /feedbacks : Create a new feedback.
	 *
	 * @param feedback
	 *            the feedback to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         feedback, or with status 400 (Bad Request) if the feedback has
	 *         already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/feedbacks")
	@Timed
	public ResponseEntity<Feedback> createFeedback(@Valid @RequestBody Feedback feedback) throws URISyntaxException {
		log.debug("REST request to save Feedback : {}", feedback);
		if (feedback.getId() != null) {
			throw new BadRequestAlertException("A new feedback cannot already have an ID", ENTITY_NAME, "idexists");
		}
		Feedback result = feedbackRepository.save(feedback);
		feedbackSearchRepository.save(result);
		feedbackService.addStarCount(feedback.getProfile().getId(), feedback.getRating());
		profilesAuditService.addFeedback(result, ENTITY_NAME, UpdateType.CREATE);
		return ResponseEntity.created(new URI("/api/feedbacks/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	// author : adit
	// date : 16-10-18
	// Desckripsi :tambah rating

	/**
	 * PUT /feedbacks : Updates an existing feedback.
	 *
	 * @param feedback
	 *            the feedback to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         feedback, or with status 400 (Bad Request) if the feedback is not
	 *         valid, or with status 500 (Internal Server Error) if the feedback
	 *         couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/feedbacks")
	@Timed
	public ResponseEntity<Feedback> updateFeedback(@Valid @RequestBody Feedback feedback) throws URISyntaxException {
		log.debug("REST request to update Feedback : {}", feedback);
		if (feedback.getId() == null) {
			return createFeedback(feedback);
		}
		Feedback result = feedbackRepository.save(feedback);
		feedbackSearchRepository.save(result);
		profilesAuditService.addFeedback(result, ENTITY_NAME, UpdateType.UPDATE);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, feedback.getId().toString()))
				.body(result);
	}

	/**
	 * GET /feedbacks : get all the feedbacks.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of feedbacks in
	 *         body
	 */
	@GetMapping("/feedbacks")
	@Timed
	public ResponseEntity<List<Feedback>> getAllFeedbacks(Pageable pageable) {
		log.debug("REST request to get all Feedbacks");
		Page<Feedback> page = feedbackRepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/feedbacks");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@GetMapping("/feedbacks/mobile")
	@Timed
	public ResponseEntity<List<Feedback>> getAllFeedbacksMobile(Pageable pageable) {
		log.debug("REST request to get all Feedbacks");
		List<Feedback> list = new ArrayList<>();
		Page<Feedback> page = feedbackRepository.findAll(pageable);
		for (Feedback feedback : page.getContent()) {
			feedback.getProfile().setImage(null);
			list.add(feedback);
		}
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/feedbacks/mobile");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /feedbacks/:id : get the "id" feedback.
	 *
	 * @param id
	 *            the id of the feedback to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the feedback,
	 *         or with status 404 (Not Found)
	 */
	@GetMapping("/feedbacks/{id}")
	@Timed
	public ResponseEntity<Feedback> getFeedback(@PathVariable Long id) {
		log.debug("REST request to get Feedback : {}", id);
		Feedback feedback = feedbackRepository.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(feedback));
	}

	@GetMapping("/feedbacks/mobile/{id}")
	@Timed
	public ResponseEntity<Feedback> getFeedbackMobile(@PathVariable Long id) {
		log.debug("REST request to get Feedback : {}", id);
		Feedback feedback = feedbackRepository.findOne(id);
		feedback.getProfile().setImage(null);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(feedback));
	}

	/**
	 * DELETE /feedbacks/:id : delete the "id" feedback.
	 *
	 * @param id
	 *            the id of the feedback to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/feedbacks/{id}")
	@Timed
	public ResponseEntity<Void> deleteFeedback(@PathVariable Long id) {
		log.debug("REST request to delete Feedback : {}", id);
		Feedback result = feedbackRepository.findOne(id);
		feedbackRepository.delete(id);
		feedbackSearchRepository.delete(id);
		profilesAuditService.addFeedback(result, ENTITY_NAME, UpdateType.DELETE);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * SEARCH /_search/feedbacks?query=:query : search for the feedback
	 * corresponding to the query.
	 *
	 * @param query
	 *            the query of the feedback search
	 * @return the result of the search
	 */
	@GetMapping("/_search/feedbacks")
	@Timed
	public ResponseEntity<List<Feedback>> searchFeedbacks(@RequestParam String query, Pageable pageable) {
		log.debug("REST request to search Feedbacks for query {}", query);
		List<Feedback> list = StreamSupport
				.stream(feedbackSearchRepository.search(queryStringQuery(query), pageable).spliterator(), false)
				.collect(Collectors.toList());
		Page<Feedback> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/_search/feedbacks");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@GetMapping("/_search/feedbacks/mobile")
	@Timed
	public ResponseEntity<List<Feedback>> searchFeedbacksMobile(@RequestParam String query, Pageable pageable) {
		log.debug("REST request to search Feedbacks for query {}", query);
		List<Feedback> list = new ArrayList<>();
		List<Feedback> fb = StreamSupport
				.stream(feedbackSearchRepository.search(queryStringQuery(query), pageable).spliterator(), false)
				.collect(Collectors.toList());
		for (Feedback feedback : fb) {
			feedback.getProfile().setImage(null);
			list.add(feedback);
		}
		Page<Feedback> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/_search/feedbacks/mobile");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

}

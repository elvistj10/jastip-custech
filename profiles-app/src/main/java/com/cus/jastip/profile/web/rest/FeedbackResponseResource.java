package com.cus.jastip.profile.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cus.jastip.profile.domain.FeedbackResponse;
import com.cus.jastip.profile.domain.enumeration.UpdateType;
import com.cus.jastip.profile.repository.FeedbackResponseRepository;
import com.cus.jastip.profile.repository.search.FeedbackResponseSearchRepository;
import com.cus.jastip.profile.service.ProfilesAuditService;
import com.cus.jastip.profile.web.rest.errors.BadRequestAlertException;
import com.cus.jastip.profile.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
 * REST controller for managing FeedbackResponse.
 */
@RestController
@RequestMapping("/api")
public class FeedbackResponseResource {
	
	@Autowired
	private ProfilesAuditService profilesAuditService;

    private final Logger log = LoggerFactory.getLogger(FeedbackResponseResource.class);

    private static final String ENTITY_NAME = "feedbackResponse";

    private final FeedbackResponseRepository feedbackResponseRepository;

    private final FeedbackResponseSearchRepository feedbackResponseSearchRepository;

    public FeedbackResponseResource(FeedbackResponseRepository feedbackResponseRepository, FeedbackResponseSearchRepository feedbackResponseSearchRepository) {
        this.feedbackResponseRepository = feedbackResponseRepository;
        this.feedbackResponseSearchRepository = feedbackResponseSearchRepository;
    }

    /**
     * POST  /feedback-responses : Create a new feedbackResponse.
     *
     * @param feedbackResponse the feedbackResponse to create
     * @return the ResponseEntity with status 201 (Created) and with body the new feedbackResponse, or with status 400 (Bad Request) if the feedbackResponse has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/feedback-responses")
    @Timed
    public ResponseEntity<FeedbackResponse> createFeedbackResponse(@Valid @RequestBody FeedbackResponse feedbackResponse) throws URISyntaxException {
        log.debug("REST request to save FeedbackResponse : {}", feedbackResponse);
        if (feedbackResponse.getId() != null) {
            throw new BadRequestAlertException("A new feedbackResponse cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FeedbackResponse result = feedbackResponseRepository.save(feedbackResponse);
        feedbackResponseSearchRepository.save(result);
        profilesAuditService.addFeedbackResponse(result, ENTITY_NAME, UpdateType.CREATE);
        return ResponseEntity.created(new URI("/api/feedback-responses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /feedback-responses : Updates an existing feedbackResponse.
     *
     * @param feedbackResponse the feedbackResponse to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated feedbackResponse,
     * or with status 400 (Bad Request) if the feedbackResponse is not valid,
     * or with status 500 (Internal Server Error) if the feedbackResponse couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/feedback-responses")
    @Timed
    public ResponseEntity<FeedbackResponse> updateFeedbackResponse(@Valid @RequestBody FeedbackResponse feedbackResponse) throws URISyntaxException {
        log.debug("REST request to update FeedbackResponse : {}", feedbackResponse);
        if (feedbackResponse.getId() == null) {
            return createFeedbackResponse(feedbackResponse);
        }
        FeedbackResponse result = feedbackResponseRepository.save(feedbackResponse);
        feedbackResponseSearchRepository.save(result);
        profilesAuditService.addFeedbackResponse(result, ENTITY_NAME, UpdateType.UPDATE);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, feedbackResponse.getId().toString()))
            .body(result);
    }

    /**
     * GET  /feedback-responses : get all the feedbackResponses.
     *
     * @param filter the filter of the request
     * @return the ResponseEntity with status 200 (OK) and the list of feedbackResponses in body
     */
    @GetMapping("/feedback-responses")
    @Timed
    public List<FeedbackResponse> getAllFeedbackResponses(@RequestParam(required = false) String filter) {
        if ("feedback-is-null".equals(filter)) {
            log.debug("REST request to get all FeedbackResponses where feedback is null");
            return StreamSupport
                .stream(feedbackResponseRepository.findAll().spliterator(), false)
                .filter(feedbackResponse -> feedbackResponse.getFeedback() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all FeedbackResponses");
        return feedbackResponseRepository.findAll();
        }

    /**
     * GET  /feedback-responses/:id : get the "id" feedbackResponse.
     *
     * @param id the id of the feedbackResponse to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the feedbackResponse, or with status 404 (Not Found)
     */
    @GetMapping("/feedback-responses/{id}")
    @Timed
    public ResponseEntity<FeedbackResponse> getFeedbackResponse(@PathVariable Long id) {
        log.debug("REST request to get FeedbackResponse : {}", id);
        FeedbackResponse feedbackResponse = feedbackResponseRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(feedbackResponse));
    }

    /**
     * DELETE  /feedback-responses/:id : delete the "id" feedbackResponse.
     *
     * @param id the id of the feedbackResponse to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/feedback-responses/{id}")
    @Timed
    public ResponseEntity<Void> deleteFeedbackResponse(@PathVariable Long id) {
        log.debug("REST request to delete FeedbackResponse : {}", id);
        FeedbackResponse result = feedbackResponseRepository.findOne(id);
        feedbackResponseRepository.delete(id);
        feedbackResponseSearchRepository.delete(id);
        profilesAuditService.addFeedbackResponse(result, ENTITY_NAME, UpdateType.DELETE);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/feedback-responses?query=:query : search for the feedbackResponse corresponding
     * to the query.
     *
     * @param query the query of the feedbackResponse search
     * @return the result of the search
     */
    @GetMapping("/_search/feedback-responses")
    @Timed
    public List<FeedbackResponse> searchFeedbackResponses(@RequestParam String query) {
        log.debug("REST request to search FeedbackResponses for query {}", query);
        return StreamSupport
            .stream(feedbackResponseSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}

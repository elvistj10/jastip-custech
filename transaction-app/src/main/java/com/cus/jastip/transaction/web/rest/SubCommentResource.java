package com.cus.jastip.transaction.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cus.jastip.transaction.domain.Comment;
import com.cus.jastip.transaction.domain.Posting;
import com.cus.jastip.transaction.domain.SubComment;
import com.cus.jastip.transaction.domain.enumeration.UpdateType;
import com.cus.jastip.transaction.repository.CommentRepository;
import com.cus.jastip.transaction.repository.SubCommentRepository;
import com.cus.jastip.transaction.repository.search.SubCommentSearchRepository;
import com.cus.jastip.transaction.service.TransactionAuditService;
import com.cus.jastip.transaction.web.rest.errors.BadRequestAlertException;
import com.cus.jastip.transaction.web.rest.util.HeaderUtil;
import com.cus.jastip.transaction.web.rest.util.PaginationUtil;

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
 * REST controller for managing SubComment.
 */
@RestController
@RequestMapping("/api")
public class SubCommentResource {

	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private TransactionAuditService transactionAuditService;

	private final Logger log = LoggerFactory.getLogger(SubCommentResource.class);

	private static final String ENTITY_NAME = "subComment";

	private final SubCommentRepository subCommentRepository;

	private final SubCommentSearchRepository subCommentSearchRepository;

	public SubCommentResource(SubCommentRepository subCommentRepository,
			SubCommentSearchRepository subCommentSearchRepository) {
		this.subCommentRepository = subCommentRepository;
		this.subCommentSearchRepository = subCommentSearchRepository;
	}

	@GetMapping("/sub-comments/comment/{id}")
	@Timed
	public ResponseEntity<List<SubComment>> getSubCommentsByComment(@PathVariable Long id, Pageable pageable) {
		Comment comment = commentRepository.findOne(id);
		List<SubComment> list = subCommentRepository.findByComment(comment, pageable);
		Page<SubComment> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sub-comments/comment/" + id);
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * POST /sub-comments : Create a new subComment.
	 *
	 * @param subComment
	 *            the subComment to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         subComment, or with status 400 (Bad Request) if the subComment has
	 *         already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/sub-comments")
	@Timed
	public ResponseEntity<SubComment> createSubComment(@Valid @RequestBody SubComment subComment)
			throws URISyntaxException {
		log.debug("REST request to save SubComment : {}", subComment);
		if (subComment.getId() != null) {
			throw new BadRequestAlertException("A new subComment cannot already have an ID", ENTITY_NAME, "idexists");
		}
		SubComment result = subCommentRepository.save(subComment);
		subCommentSearchRepository.save(result);
		transactionAuditService.addSubComment(result, ENTITY_NAME, UpdateType.CREATE);
		return ResponseEntity.created(new URI("/api/sub-comments/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /sub-comments : Updates an existing subComment.
	 *
	 * @param subComment
	 *            the subComment to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         subComment, or with status 400 (Bad Request) if the subComment is not
	 *         valid, or with status 500 (Internal Server Error) if the subComment
	 *         couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/sub-comments")
	@Timed
	public ResponseEntity<SubComment> updateSubComment(@Valid @RequestBody SubComment subComment)
			throws URISyntaxException {
		log.debug("REST request to update SubComment : {}", subComment);
		if (subComment.getId() == null) {
			return createSubComment(subComment);
		}
		SubComment result = subCommentRepository.save(subComment);
		subCommentSearchRepository.save(result);
		transactionAuditService.addSubComment(result, ENTITY_NAME, UpdateType.UPDATE);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, subComment.getId().toString())).body(result);
	}

	/**
	 * GET /sub-comments : get all the subComments.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of subComments
	 *         in body
	 */
	@GetMapping("/sub-comments")
	@Timed
	public ResponseEntity<List<SubComment>> getAllSubComments(Pageable pageable) {
		log.debug("REST request to get all SubComments");
		Page<SubComment> page = subCommentRepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sub-comments");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /sub-comments/:id : get the "id" subComment.
	 *
	 * @param id
	 *            the id of the subComment to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the subComment,
	 *         or with status 404 (Not Found)
	 */
	@GetMapping("/sub-comments/{id}")
	@Timed
	public ResponseEntity<SubComment> getSubComment(@PathVariable Long id) {
		log.debug("REST request to get SubComment : {}", id);
		SubComment subComment = subCommentRepository.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(subComment));
	}

	/**
	 * DELETE /sub-comments/:id : delete the "id" subComment.
	 *
	 * @param id
	 *            the id of the subComment to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/sub-comments/{id}")
	@Timed
	public ResponseEntity<Void> deleteSubComment(@PathVariable Long id) {
		log.debug("REST request to delete SubComment : {}", id);
		SubComment result = subCommentRepository.findOne(id);
		subCommentRepository.delete(id);
		subCommentSearchRepository.delete(id);
		transactionAuditService.addSubComment(result, ENTITY_NAME, UpdateType.DELETE);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * SEARCH /_search/sub-comments?query=:query : search for the subComment
	 * corresponding to the query.
	 *
	 * @param query
	 *            the query of the subComment search
	 * @return the result of the search
	 */
	@GetMapping("/_search/sub-comments")
	@Timed
	public ResponseEntity<List<SubComment>> searchSubComments(@RequestParam String query, Pageable pageable) {
		log.debug("REST request to search SubComments for query {}", query);
		List<SubComment> list = StreamSupport
				.stream(subCommentSearchRepository.search(queryStringQuery(query), pageable).spliterator(), false)
				.collect(Collectors.toList());
		Page<SubComment> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/_search/sub-comments");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

}

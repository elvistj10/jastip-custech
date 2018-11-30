package com.cus.jastip.transaction.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cus.jastip.transaction.domain.Posting;
import com.cus.jastip.transaction.domain.enumeration.PostingStatus;
import com.cus.jastip.transaction.domain.enumeration.PostingType;
import com.cus.jastip.transaction.domain.enumeration.UpdateType;
import com.cus.jastip.transaction.messaging.TransactionCountSender;
import com.cus.jastip.transaction.messaging.model.TransactionCountModel;
import com.cus.jastip.transaction.repository.PostingRepository;
import com.cus.jastip.transaction.repository.search.PostingSearchRepository;
import com.cus.jastip.transaction.service.ImageProcessService;
import com.cus.jastip.transaction.service.KafkaService;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Posting.
 */
@RestController
@RequestMapping("/api")
public class PostingResource {

	@Autowired
	private ImageProcessService imageProcessService;
	
	@Autowired
	private KafkaService kafkaService;
	
	@Autowired
	private TransactionAuditService transactionAuditService;

	private final Logger log = LoggerFactory.getLogger(PostingResource.class);

	private static final String ENTITY_NAME = "posting";

	private final PostingRepository postingRepository;

	private final PostingSearchRepository postingSearchRepository;

	Calendar cal = Calendar.getInstance();

	public PostingResource(PostingRepository postingRepository, PostingSearchRepository postingSearchRepository) {
		this.postingRepository = postingRepository;
		this.postingSearchRepository = postingSearchRepository;
	}

	@GetMapping("/postings/owner/{ownerId}/posting-type/{type}")
	@Timed
	public ResponseEntity<List<Posting>> getPostingsByOwner(@PathVariable Long ownerId, @PathVariable PostingType type,
			Pageable pageable) {
		log.debug("REST request to get all Posting");
		List<Posting> list = postingRepository.findByOwnerIdInAndTypeIn(ownerId, type, pageable);
		Page<Posting> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page,
				"/api/postings/owner/" + ownerId + "/posting-type/" + type);
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@GetMapping("/postings/owner/mobile/{ownerId}/posting-type/{type}")
	@Timed
	public ResponseEntity<List<Posting>> getPostingsByOwnerMobile(@PathVariable Long ownerId,
			@PathVariable PostingType type, Pageable pageable) {
		log.debug("REST request to get all Posting");
		List<Posting> list = new ArrayList<>();
		List<Posting> listPost = postingRepository.findByOwnerIdInAndTypeIn(ownerId, type, pageable);
		for (Posting posting : listPost) {
			posting.setPostingItemImg(null);
			list.add(posting);
		}
		Page<Posting> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page,
				"/api/postings/owner/mobile/" + ownerId + "/posting-type/" + type);
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@GetMapping("/postings/posting-status/{status}/posting-type/{type}")
	@Timed
	public ResponseEntity<List<Posting>> getPostingsByStatus(@PathVariable PostingStatus status,
			@PathVariable PostingType type, Pageable pageable) {
		log.debug("REST request to get all Posting");
		List<Posting> list = postingRepository.findByStatusInAndTypeIn(status, type, pageable);
		Page<Posting> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page,
				"/api/postings/posting-statu/" + status + "/posting-type/" + type);
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@GetMapping("/postings/posting-status/mobile/{status}/posting-type/{type}")
	@Timed
	public ResponseEntity<List<Posting>> getPostingsByStatusMobile(@PathVariable PostingStatus status,
			@PathVariable PostingType type, Pageable pageable) {
		log.debug("REST request to get all Posting");
		List<Posting> list = new ArrayList<>();
		List<Posting> listPost = postingRepository.findByStatusInAndTypeIn(status, type, pageable);
		for (Posting posting : listPost) {
			posting.setPostingItemImg(null);
			list.add(posting);
		}
		Page<Posting> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page,
				"/api/postings/posting-statu/mobile/" + status + "/posting-type/" + type);
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@PostMapping("/postings")
	@Timed
	public ResponseEntity<Posting> createPosting(@Valid @RequestBody Posting posting) throws URISyntaxException {
		log.debug("REST request to save Posting : {}", posting);
		if (posting.getId() != null) {
			throw new BadRequestAlertException("A new posting cannot already have an ID", ENTITY_NAME, "idexists");
		}
		if (posting.getPostingItemImg() != null) {
			try {
				String url = imageProcessService.onSubmit(posting.getPostingItemImg());
				posting.setPostingItemImgUrl(imageProcessService.urlImage(url));
				posting.setPostingItemImgThumbUrl(imageProcessService.urlImageThumb(url));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		cal.add(Calendar.MONTH, 1);
		posting.setExpiredDate(cal.toInstant());
		Posting result = postingRepository.save(posting);
		postingSearchRepository.save(result);
		transactionAuditService.addPosting(result, ENTITY_NAME, UpdateType.CREATE);
		kafkaService.postingCountProcess(result);
		return ResponseEntity.created(new URI("/api/postings/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	@PutMapping("/postings")
	@Timed
	public ResponseEntity<Posting> updatePosting(@Valid @RequestBody Posting posting) throws URISyntaxException {
		log.debug("REST request to update Posting : {}", posting);
		if (posting.getId() == null) {
			return createPosting(posting);
		}
		if (posting.getPostingItemImg() == null) {
			Posting pos = postingRepository.findOne(posting.getId());
			posting.setPostingItemImg(pos.getPostingItemImg());
			posting.setPostingItemImgUrl(pos.getPostingItemImgUrl());
			posting.setPostingItemImgThumbUrl(pos.getPostingItemImgThumbUrl());
			posting.setPostingItemImgContentType(pos.getPostingItemImgContentType());
		} else {
			try {
				String url = imageProcessService.onSubmit(posting.getPostingItemImg());
				posting.setPostingItemImgUrl(imageProcessService.urlImage(url));
				posting.setPostingItemImgThumbUrl(imageProcessService.urlImageThumb(url));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Posting result = postingRepository.save(posting);
		postingSearchRepository.save(result);
		transactionAuditService.addPosting(result, ENTITY_NAME, UpdateType.UPDATE);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, posting.getId().toString()))
				.body(result);
	}

	@GetMapping("/postings")
	@Timed
	public ResponseEntity<List<Posting>> getAllPostings(Pageable pageable) {
		log.debug("REST request to get all Postings");
		Page<Posting> page = postingRepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/postings");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@GetMapping("/postings/{id}")
	@Timed
	public ResponseEntity<Posting> getPosting(@PathVariable Long id) {
		log.debug("REST request to get Posting : {}", id);
		Posting posting = postingRepository.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(posting));
	}

	@GetMapping("/postings/mobile")
	@Timed
	public ResponseEntity<List<Posting>> getAllPostingsMobile(Pageable pageable) {
		List<Posting> list = new ArrayList<>();
		for (Posting posting : postingRepository.findAll(pageable)) {
			posting.setPostingItemImg(null);
			list.add(posting);
		}
		Page<Posting> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/postings/mobile");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@GetMapping("/postings/mobile/{id}")
	@Timed
	public ResponseEntity<Posting> getPostingMobileById(@PathVariable Long id) {
		Posting posting = postingRepository.findOne(id);
		posting.setPostingItemImg(null);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(posting));
	}

	@DeleteMapping("/postings/{id}")
	@Timed
	public ResponseEntity<Void> deletePosting(@PathVariable Long id) {
		log.debug("REST request to delete Posting : {}", id);
		Posting result = postingRepository.findOne(id);
		postingRepository.delete(id);
		postingSearchRepository.delete(id);
		transactionAuditService.addPosting(result, ENTITY_NAME, UpdateType.DELETE);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	@GetMapping("/_search/postings")
	@Timed
	public ResponseEntity<List<Posting>> searchPostings(@RequestParam String query, Pageable pageable) {
		log.debug("REST request to search Postings for query {}", query);
		List<Posting> list = StreamSupport
				.stream(postingSearchRepository.search(queryStringQuery(query), pageable).spliterator(), false)
				.collect(Collectors.toList());
		Page<Posting> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/_search/postings");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@GetMapping("/_search/postings/mobile")
	@Timed
	public ResponseEntity<List<Posting>> searchPostingsMobile(@RequestParam String query, Pageable pageable) {
		log.debug("REST request to search Postings for query {}", query);
		List<Posting> list = new ArrayList<>();
		List<Posting> listPost = StreamSupport
				.stream(postingSearchRepository.search(queryStringQuery(query), pageable).spliterator(), false)
				.collect(Collectors.toList());
		for (Posting posting : listPost) {
			posting.setPostingItemImg(null);
			list.add(posting);
		}
		Page<Posting> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/_search/postings/mobile");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

}

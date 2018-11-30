package com.cus.jastip.master.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cus.jastip.master.domain.BusinessAccount;
import com.cus.jastip.master.domain.Province;
import com.cus.jastip.master.domain.ServiceFee;
import com.cus.jastip.master.domain.enumeration.UpdateType;
import com.cus.jastip.master.repository.ServiceFeeRepository;
import com.cus.jastip.master.repository.search.ServiceFeeSearchRepository;
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
 * REST controller for managing ServiceFee.
 */
@RestController
@RequestMapping("/api")
public class ServiceFeeResource {

	private final Logger log = LoggerFactory.getLogger(ServiceFeeResource.class);

	private static final String ENTITY_NAME = "serviceFee";

	@Autowired
	private MasterAuditService masterAuditService;

	private final ServiceFeeRepository serviceFeeRepository;

	private final ServiceFeeSearchRepository serviceFeeSearchRepository;

	public ServiceFeeResource(ServiceFeeRepository serviceFeeRepository,
			ServiceFeeSearchRepository serviceFeeSearchRepository) {
		this.serviceFeeRepository = serviceFeeRepository;
		this.serviceFeeSearchRepository = serviceFeeSearchRepository;
	}

	/**
	 * POST /service-fees : Create a new serviceFee.
	 *
	 * @param serviceFee
	 *            the serviceFee to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         serviceFee, or with status 400 (Bad Request) if the serviceFee has
	 *         already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/service-fees")
	@Timed
	public ResponseEntity<ServiceFee> createServiceFee(@Valid @RequestBody ServiceFee serviceFee)
			throws URISyntaxException {
		log.debug("REST request to save ServiceFee : {}", serviceFee);
		if (serviceFee.getId() != null) {
			throw new BadRequestAlertException("A new serviceFee cannot already have an ID", ENTITY_NAME, "idexists");
		}
		ServiceFee result = serviceFeeRepository.save(serviceFee);
		serviceFeeSearchRepository.save(result);
		masterAuditService.addServiceFee(result, ENTITY_NAME, UpdateType.CREATE);
		return ResponseEntity.created(new URI("/api/service-fees/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /service-fees : Updates an existing serviceFee.
	 *
	 * @param serviceFee
	 *            the serviceFee to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         serviceFee, or with status 400 (Bad Request) if the serviceFee is not
	 *         valid, or with status 500 (Internal Server Error) if the serviceFee
	 *         couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/service-fees")
	@Timed
	public ResponseEntity<ServiceFee> updateServiceFee(@Valid @RequestBody ServiceFee serviceFee)
			throws URISyntaxException {
		log.debug("REST request to update ServiceFee : {}", serviceFee);
		if (serviceFee.getId() == null) {
			return createServiceFee(serviceFee);
		}
		ServiceFee result = serviceFeeRepository.save(serviceFee);
		serviceFeeSearchRepository.save(result);
		masterAuditService.addServiceFee(result, ENTITY_NAME, UpdateType.UPDATE);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, serviceFee.getId().toString())).body(result);
	}

	/**
	 * GET /service-fees : get all the serviceFees.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of serviceFees
	 *         in body
	 */
	@GetMapping("/service-fees")
	@Timed
	public ResponseEntity<List<ServiceFee>> getAllServiceFees(Pageable pageable) {
		log.debug("REST request to get all ServiceFees");
		Page<ServiceFee> page = serviceFeeRepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/service-fees");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /service-fees/:id : get the "id" serviceFee.
	 *
	 * @param id
	 *            the id of the serviceFee to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the serviceFee,
	 *         or with status 404 (Not Found)
	 */
	@GetMapping("/service-fees/{id}")
	@Timed
	public ResponseEntity<ServiceFee> getServiceFee(@PathVariable Long id) {
		log.debug("REST request to get ServiceFee : {}", id);
		ServiceFee serviceFee = serviceFeeRepository.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(serviceFee));
	}

	/**
	 * DELETE /service-fees/:id : delete the "id" serviceFee.
	 *
	 * @param id
	 *            the id of the serviceFee to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/service-fees/{id}")
	@Timed
	public ResponseEntity<Void> deleteServiceFee(@PathVariable Long id) {
		log.debug("REST request to delete ServiceFee : {}", id);
		ServiceFee result = serviceFeeRepository.findOne(id);
		serviceFeeRepository.delete(id);
		serviceFeeSearchRepository.delete(id);
		masterAuditService.addServiceFee(result, ENTITY_NAME, UpdateType.DELETE);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * SEARCH /_search/service-fees?query=:query : search for the serviceFee
	 * corresponding to the query.
	 *
	 * @param query
	 *            the query of the serviceFee search
	 * @return the result of the search
	 */
	@GetMapping("/_search/service-fees")
	@Timed
	public ResponseEntity<List<ServiceFee>> searchServiceFees(@RequestParam String query, Pageable pageable) {
		log.debug("REST request to search ServiceFees for query {}", query);
		List<ServiceFee> serviceFee = StreamSupport
				.stream(serviceFeeSearchRepository.search(queryStringQuery(query), pageable).spliterator(), false)
				.collect(Collectors.toList());
		Page<ServiceFee> page = new PageImpl<>(serviceFee);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/_search/service-fees");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

}

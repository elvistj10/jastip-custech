package com.cus.jastip.transaction.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cus.jastip.transaction.domain.OfferingDeliveryInfo;
import com.cus.jastip.transaction.domain.OfferingDeliveryService;
import com.cus.jastip.transaction.domain.enumeration.UpdateType;
import com.cus.jastip.transaction.repository.OfferingDeliveryServiceRepository;
import com.cus.jastip.transaction.repository.search.OfferingDeliveryServiceSearchRepository;
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
 * REST controller for managing OfferingDeliveryService.
 */
@RestController
@RequestMapping("/api")
public class OfferingDeliveryServiceResource {
	
	@Autowired
	private TransactionAuditService transactionAuditService;

    private final Logger log = LoggerFactory.getLogger(OfferingDeliveryServiceResource.class);

    private static final String ENTITY_NAME = "offeringDeliveryService";

    private final OfferingDeliveryServiceRepository offeringDeliveryServiceRepository;

    private final OfferingDeliveryServiceSearchRepository offeringDeliveryServiceSearchRepository;

    public OfferingDeliveryServiceResource(OfferingDeliveryServiceRepository offeringDeliveryServiceRepository, OfferingDeliveryServiceSearchRepository offeringDeliveryServiceSearchRepository) {
        this.offeringDeliveryServiceRepository = offeringDeliveryServiceRepository;
        this.offeringDeliveryServiceSearchRepository = offeringDeliveryServiceSearchRepository;
    }

    /**
     * POST  /offering-delivery-services : Create a new offeringDeliveryService.
     *
     * @param offeringDeliveryService the offeringDeliveryService to create
     * @return the ResponseEntity with status 201 (Created) and with body the new offeringDeliveryService, or with status 400 (Bad Request) if the offeringDeliveryService has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/offering-delivery-services")
    @Timed
    public ResponseEntity<OfferingDeliveryService> createOfferingDeliveryService(@Valid @RequestBody OfferingDeliveryService offeringDeliveryService) throws URISyntaxException {
        log.debug("REST request to save OfferingDeliveryService : {}", offeringDeliveryService);
        if (offeringDeliveryService.getId() != null) {
            throw new BadRequestAlertException("A new offeringDeliveryService cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OfferingDeliveryService result = offeringDeliveryServiceRepository.save(offeringDeliveryService);
        offeringDeliveryServiceSearchRepository.save(result);
        transactionAuditService.addOfferingDeliveryService(result, ENTITY_NAME, UpdateType.CREATE);
        return ResponseEntity.created(new URI("/api/offering-delivery-services/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /offering-delivery-services : Updates an existing offeringDeliveryService.
     *
     * @param offeringDeliveryService the offeringDeliveryService to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated offeringDeliveryService,
     * or with status 400 (Bad Request) if the offeringDeliveryService is not valid,
     * or with status 500 (Internal Server Error) if the offeringDeliveryService couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/offering-delivery-services")
    @Timed
    public ResponseEntity<OfferingDeliveryService> updateOfferingDeliveryService(@Valid @RequestBody OfferingDeliveryService offeringDeliveryService) throws URISyntaxException {
        log.debug("REST request to update OfferingDeliveryService : {}", offeringDeliveryService);
        if (offeringDeliveryService.getId() == null) {
            return createOfferingDeliveryService(offeringDeliveryService);
        }
        OfferingDeliveryService result = offeringDeliveryServiceRepository.save(offeringDeliveryService);
        offeringDeliveryServiceSearchRepository.save(result);
        transactionAuditService.addOfferingDeliveryService(result, ENTITY_NAME, UpdateType.UPDATE);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, offeringDeliveryService.getId().toString()))
            .body(result);
    }

    /**
     * GET  /offering-delivery-services : get all the offeringDeliveryServices.
     *
     * @param filter the filter of the request
     * @return the ResponseEntity with status 200 (OK) and the list of offeringDeliveryServices in body
     */
    @GetMapping("/offering-delivery-services")
    @Timed
    public ResponseEntity<List<OfferingDeliveryService>> getAllOfferingDeliveryServices(@RequestParam(required = false) String filter, Pageable pageable) {
        if ("offering-is-null".equals(filter)) {
            log.debug("REST request to get all OfferingDeliveryServices where offering is null");
            List<OfferingDeliveryService> list =  StreamSupport
                .stream(offeringDeliveryServiceRepository.findAll().spliterator(), false)
                .filter(offeringDeliveryService -> offeringDeliveryService.getOffering() == null)
                .collect(Collectors.toList());
            Page<OfferingDeliveryService> page = new PageImpl<>(list);
			HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/offering-delivery-services");
			return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
        }
        log.debug("REST request to get all OfferingDeliveryServices");
        Page<OfferingDeliveryService> page = offeringDeliveryServiceRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/offering-delivery-services");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
        }

    /**
     * GET  /offering-delivery-services/:id : get the "id" offeringDeliveryService.
     *
     * @param id the id of the offeringDeliveryService to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the offeringDeliveryService, or with status 404 (Not Found)
     */
    @GetMapping("/offering-delivery-services/{id}")
    @Timed
    public ResponseEntity<OfferingDeliveryService> getOfferingDeliveryService(@PathVariable Long id) {
        log.debug("REST request to get OfferingDeliveryService : {}", id);
        OfferingDeliveryService offeringDeliveryService = offeringDeliveryServiceRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(offeringDeliveryService));
    }

    /**
     * DELETE  /offering-delivery-services/:id : delete the "id" offeringDeliveryService.
     *
     * @param id the id of the offeringDeliveryService to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/offering-delivery-services/{id}")
    @Timed
    public ResponseEntity<Void> deleteOfferingDeliveryService(@PathVariable Long id) {
        log.debug("REST request to delete OfferingDeliveryService : {}", id);
        OfferingDeliveryService result = offeringDeliveryServiceRepository.findOne(id);
        offeringDeliveryServiceRepository.delete(id);
        offeringDeliveryServiceSearchRepository.delete(id);
        transactionAuditService.addOfferingDeliveryService(result, ENTITY_NAME, UpdateType.DELETE);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/offering-delivery-services?query=:query : search for the offeringDeliveryService corresponding
     * to the query.
     *
     * @param query the query of the offeringDeliveryService search
     * @return the result of the search
     */
    @GetMapping("/_search/offering-delivery-services")
    @Timed
    public ResponseEntity<List<OfferingDeliveryService>> searchOfferingDeliveryServices(@RequestParam String query,Pageable pageable) {
        log.debug("REST request to search OfferingDeliveryServices for query {}", query);
        List<OfferingDeliveryService> list = StreamSupport
            .stream(offeringDeliveryServiceSearchRepository.search(queryStringQuery(query),pageable).spliterator(), false)
            .collect(Collectors.toList());
        Page<OfferingDeliveryService> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/_search/offering-delivery-services");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}

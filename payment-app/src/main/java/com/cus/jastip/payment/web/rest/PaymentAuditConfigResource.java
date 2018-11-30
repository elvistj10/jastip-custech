package com.cus.jastip.payment.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cus.jastip.payment.domain.PaymentAuditConfig;

import com.cus.jastip.payment.repository.PaymentAuditConfigRepository;
import com.cus.jastip.payment.repository.search.PaymentAuditConfigSearchRepository;
import com.cus.jastip.payment.web.rest.errors.BadRequestAlertException;
import com.cus.jastip.payment.web.rest.util.HeaderUtil;
import com.cus.jastip.payment.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
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
 * REST controller for managing PaymentAuditConfig.
 */
@RestController
@RequestMapping("/api")
public class PaymentAuditConfigResource {

    private final Logger log = LoggerFactory.getLogger(PaymentAuditConfigResource.class);

    private static final String ENTITY_NAME = "paymentAuditConfig";

    private final PaymentAuditConfigRepository paymentAuditConfigRepository;

    private final PaymentAuditConfigSearchRepository paymentAuditConfigSearchRepository;

    public PaymentAuditConfigResource(PaymentAuditConfigRepository paymentAuditConfigRepository, PaymentAuditConfigSearchRepository paymentAuditConfigSearchRepository) {
        this.paymentAuditConfigRepository = paymentAuditConfigRepository;
        this.paymentAuditConfigSearchRepository = paymentAuditConfigSearchRepository;
    }

    /**
     * POST  /payment-audit-configs : Create a new paymentAuditConfig.
     *
     * @param paymentAuditConfig the paymentAuditConfig to create
     * @return the ResponseEntity with status 201 (Created) and with body the new paymentAuditConfig, or with status 400 (Bad Request) if the paymentAuditConfig has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/payment-audit-configs")
    @Timed
    public ResponseEntity<PaymentAuditConfig> createPaymentAuditConfig(@Valid @RequestBody PaymentAuditConfig paymentAuditConfig) throws URISyntaxException {
        log.debug("REST request to save PaymentAuditConfig : {}", paymentAuditConfig);
        if (paymentAuditConfig.getId() != null) {
            throw new BadRequestAlertException("A new paymentAuditConfig cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PaymentAuditConfig result = paymentAuditConfigRepository.save(paymentAuditConfig);
        paymentAuditConfigSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/payment-audit-configs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /payment-audit-configs : Updates an existing paymentAuditConfig.
     *
     * @param paymentAuditConfig the paymentAuditConfig to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated paymentAuditConfig,
     * or with status 400 (Bad Request) if the paymentAuditConfig is not valid,
     * or with status 500 (Internal Server Error) if the paymentAuditConfig couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/payment-audit-configs")
    @Timed
    public ResponseEntity<PaymentAuditConfig> updatePaymentAuditConfig(@Valid @RequestBody PaymentAuditConfig paymentAuditConfig) throws URISyntaxException {
        log.debug("REST request to update PaymentAuditConfig : {}", paymentAuditConfig);
        if (paymentAuditConfig.getId() == null) {
            return createPaymentAuditConfig(paymentAuditConfig);
        }
        PaymentAuditConfig result = paymentAuditConfigRepository.save(paymentAuditConfig);
        paymentAuditConfigSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, paymentAuditConfig.getId().toString()))
            .body(result);
    }

    /**
     * GET  /payment-audit-configs : get all the paymentAuditConfigs.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of paymentAuditConfigs in body
     */
    @GetMapping("/payment-audit-configs")
    @Timed
    public ResponseEntity<List<PaymentAuditConfig>> getAllPaymentAuditConfigs(Pageable pageable) {
        log.debug("REST request to get a page of PaymentAuditConfigs");
        Page<PaymentAuditConfig> page = paymentAuditConfigRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/payment-audit-configs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /payment-audit-configs/:id : get the "id" paymentAuditConfig.
     *
     * @param id the id of the paymentAuditConfig to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the paymentAuditConfig, or with status 404 (Not Found)
     */
    @GetMapping("/payment-audit-configs/{id}")
    @Timed
    public ResponseEntity<PaymentAuditConfig> getPaymentAuditConfig(@PathVariable Long id) {
        log.debug("REST request to get PaymentAuditConfig : {}", id);
        PaymentAuditConfig paymentAuditConfig = paymentAuditConfigRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(paymentAuditConfig));
    }

    /**
     * DELETE  /payment-audit-configs/:id : delete the "id" paymentAuditConfig.
     *
     * @param id the id of the paymentAuditConfig to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/payment-audit-configs/{id}")
    @Timed
    public ResponseEntity<Void> deletePaymentAuditConfig(@PathVariable Long id) {
        log.debug("REST request to delete PaymentAuditConfig : {}", id);
        paymentAuditConfigRepository.delete(id);
        paymentAuditConfigSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/payment-audit-configs?query=:query : search for the paymentAuditConfig corresponding
     * to the query.
     *
     * @param query the query of the paymentAuditConfig search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/payment-audit-configs")
    @Timed
    public ResponseEntity<List<PaymentAuditConfig>> searchPaymentAuditConfigs(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of PaymentAuditConfigs for query {}", query);
        Page<PaymentAuditConfig> page = paymentAuditConfigSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/payment-audit-configs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}

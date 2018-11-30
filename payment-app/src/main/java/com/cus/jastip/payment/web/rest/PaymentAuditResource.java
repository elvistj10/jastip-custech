package com.cus.jastip.payment.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cus.jastip.payment.domain.PaymentAudit;

import com.cus.jastip.payment.repository.PaymentAuditRepository;
import com.cus.jastip.payment.repository.search.PaymentAuditSearchRepository;
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
 * REST controller for managing PaymentAudit.
 */
@RestController
@RequestMapping("/api")
public class PaymentAuditResource {

    private final Logger log = LoggerFactory.getLogger(PaymentAuditResource.class);

    private static final String ENTITY_NAME = "paymentAudit";

    private final PaymentAuditRepository paymentAuditRepository;

    private final PaymentAuditSearchRepository paymentAuditSearchRepository;

    public PaymentAuditResource(PaymentAuditRepository paymentAuditRepository, PaymentAuditSearchRepository paymentAuditSearchRepository) {
        this.paymentAuditRepository = paymentAuditRepository;
        this.paymentAuditSearchRepository = paymentAuditSearchRepository;
    }

    /**
     * POST  /payment-audits : Create a new paymentAudit.
     *
     * @param paymentAudit the paymentAudit to create
     * @return the ResponseEntity with status 201 (Created) and with body the new paymentAudit, or with status 400 (Bad Request) if the paymentAudit has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/payment-audits")
    @Timed
    public ResponseEntity<PaymentAudit> createPaymentAudit(@Valid @RequestBody PaymentAudit paymentAudit) throws URISyntaxException {
        log.debug("REST request to save PaymentAudit : {}", paymentAudit);
        if (paymentAudit.getId() != null) {
            throw new BadRequestAlertException("A new paymentAudit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PaymentAudit result = paymentAuditRepository.save(paymentAudit);
        paymentAuditSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/payment-audits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /payment-audits : Updates an existing paymentAudit.
     *
     * @param paymentAudit the paymentAudit to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated paymentAudit,
     * or with status 400 (Bad Request) if the paymentAudit is not valid,
     * or with status 500 (Internal Server Error) if the paymentAudit couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/payment-audits")
    @Timed
    public ResponseEntity<PaymentAudit> updatePaymentAudit(@Valid @RequestBody PaymentAudit paymentAudit) throws URISyntaxException {
        log.debug("REST request to update PaymentAudit : {}", paymentAudit);
        if (paymentAudit.getId() == null) {
            return createPaymentAudit(paymentAudit);
        }
        PaymentAudit result = paymentAuditRepository.save(paymentAudit);
        paymentAuditSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, paymentAudit.getId().toString()))
            .body(result);
    }

    /**
     * GET  /payment-audits : get all the paymentAudits.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of paymentAudits in body
     */
    @GetMapping("/payment-audits")
    @Timed
    public ResponseEntity<List<PaymentAudit>> getAllPaymentAudits(Pageable pageable) {
        log.debug("REST request to get a page of PaymentAudits");
        Page<PaymentAudit> page = paymentAuditRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/payment-audits");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /payment-audits/:id : get the "id" paymentAudit.
     *
     * @param id the id of the paymentAudit to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the paymentAudit, or with status 404 (Not Found)
     */
    @GetMapping("/payment-audits/{id}")
    @Timed
    public ResponseEntity<PaymentAudit> getPaymentAudit(@PathVariable Long id) {
        log.debug("REST request to get PaymentAudit : {}", id);
        PaymentAudit paymentAudit = paymentAuditRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(paymentAudit));
    }

    /**
     * DELETE  /payment-audits/:id : delete the "id" paymentAudit.
     *
     * @param id the id of the paymentAudit to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/payment-audits/{id}")
    @Timed
    public ResponseEntity<Void> deletePaymentAudit(@PathVariable Long id) {
        log.debug("REST request to delete PaymentAudit : {}", id);
        paymentAuditRepository.delete(id);
        paymentAuditSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/payment-audits?query=:query : search for the paymentAudit corresponding
     * to the query.
     *
     * @param query the query of the paymentAudit search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/payment-audits")
    @Timed
    public ResponseEntity<List<PaymentAudit>> searchPaymentAudits(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of PaymentAudits for query {}", query);
        Page<PaymentAudit> page = paymentAuditSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/payment-audits");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}

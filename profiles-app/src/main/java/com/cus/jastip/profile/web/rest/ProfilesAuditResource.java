package com.cus.jastip.profile.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cus.jastip.profile.domain.ProfilesAudit;

import com.cus.jastip.profile.repository.ProfilesAuditRepository;
import com.cus.jastip.profile.repository.search.ProfilesAuditSearchRepository;
import com.cus.jastip.profile.web.rest.errors.BadRequestAlertException;
import com.cus.jastip.profile.web.rest.util.HeaderUtil;
import com.cus.jastip.profile.web.rest.util.PaginationUtil;
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
 * REST controller for managing ProfilesAudit.
 */
@RestController
@RequestMapping("/api")
public class ProfilesAuditResource {

    private final Logger log = LoggerFactory.getLogger(ProfilesAuditResource.class);

    private static final String ENTITY_NAME = "profilesAudit";

    private final ProfilesAuditRepository profilesAuditRepository;

    private final ProfilesAuditSearchRepository profilesAuditSearchRepository;

    public ProfilesAuditResource(ProfilesAuditRepository profilesAuditRepository, ProfilesAuditSearchRepository profilesAuditSearchRepository) {
        this.profilesAuditRepository = profilesAuditRepository;
        this.profilesAuditSearchRepository = profilesAuditSearchRepository;
    }

    /**
     * POST  /profiles-audits : Create a new profilesAudit.
     *
     * @param profilesAudit the profilesAudit to create
     * @return the ResponseEntity with status 201 (Created) and with body the new profilesAudit, or with status 400 (Bad Request) if the profilesAudit has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/profiles-audits")
    @Timed
    public ResponseEntity<ProfilesAudit> createProfilesAudit(@Valid @RequestBody ProfilesAudit profilesAudit) throws URISyntaxException {
        log.debug("REST request to save ProfilesAudit : {}", profilesAudit);
        if (profilesAudit.getId() != null) {
            throw new BadRequestAlertException("A new profilesAudit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProfilesAudit result = profilesAuditRepository.save(profilesAudit);
        profilesAuditSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/profiles-audits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /profiles-audits : Updates an existing profilesAudit.
     *
     * @param profilesAudit the profilesAudit to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated profilesAudit,
     * or with status 400 (Bad Request) if the profilesAudit is not valid,
     * or with status 500 (Internal Server Error) if the profilesAudit couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/profiles-audits")
    @Timed
    public ResponseEntity<ProfilesAudit> updateProfilesAudit(@Valid @RequestBody ProfilesAudit profilesAudit) throws URISyntaxException {
        log.debug("REST request to update ProfilesAudit : {}", profilesAudit);
        if (profilesAudit.getId() == null) {
            return createProfilesAudit(profilesAudit);
        }
        ProfilesAudit result = profilesAuditRepository.save(profilesAudit);
        profilesAuditSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, profilesAudit.getId().toString()))
            .body(result);
    }

    /**
     * GET  /profiles-audits : get all the profilesAudits.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of profilesAudits in body
     */
    @GetMapping("/profiles-audits")
    @Timed
    public ResponseEntity<List<ProfilesAudit>> getAllProfilesAudits(Pageable pageable) {
        log.debug("REST request to get a page of ProfilesAudits");
        Page<ProfilesAudit> page = profilesAuditRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/profiles-audits");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /profiles-audits/:id : get the "id" profilesAudit.
     *
     * @param id the id of the profilesAudit to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the profilesAudit, or with status 404 (Not Found)
     */
    @GetMapping("/profiles-audits/{id}")
    @Timed
    public ResponseEntity<ProfilesAudit> getProfilesAudit(@PathVariable Long id) {
        log.debug("REST request to get ProfilesAudit : {}", id);
        ProfilesAudit profilesAudit = profilesAuditRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(profilesAudit));
    }

    /**
     * DELETE  /profiles-audits/:id : delete the "id" profilesAudit.
     *
     * @param id the id of the profilesAudit to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/profiles-audits/{id}")
    @Timed
    public ResponseEntity<Void> deleteProfilesAudit(@PathVariable Long id) {
        log.debug("REST request to delete ProfilesAudit : {}", id);
        profilesAuditRepository.delete(id);
        profilesAuditSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/profiles-audits?query=:query : search for the profilesAudit corresponding
     * to the query.
     *
     * @param query the query of the profilesAudit search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/profiles-audits")
    @Timed
    public ResponseEntity<List<ProfilesAudit>> searchProfilesAudits(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ProfilesAudits for query {}", query);
        Page<ProfilesAudit> page = profilesAuditSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/profiles-audits");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}

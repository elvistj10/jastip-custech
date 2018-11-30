package com.cus.jastip.profile.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cus.jastip.profile.domain.ProfilesAuditConfig;

import com.cus.jastip.profile.repository.ProfilesAuditConfigRepository;
import com.cus.jastip.profile.repository.search.ProfilesAuditConfigSearchRepository;
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
 * REST controller for managing ProfilesAuditConfig.
 */
@RestController
@RequestMapping("/api")
public class ProfilesAuditConfigResource {

    private final Logger log = LoggerFactory.getLogger(ProfilesAuditConfigResource.class);

    private static final String ENTITY_NAME = "profilesAuditConfig";

    private final ProfilesAuditConfigRepository profilesAuditConfigRepository;

    private final ProfilesAuditConfigSearchRepository profilesAuditConfigSearchRepository;

    public ProfilesAuditConfigResource(ProfilesAuditConfigRepository profilesAuditConfigRepository, ProfilesAuditConfigSearchRepository profilesAuditConfigSearchRepository) {
        this.profilesAuditConfigRepository = profilesAuditConfigRepository;
        this.profilesAuditConfigSearchRepository = profilesAuditConfigSearchRepository;
    }

    /**
     * POST  /profiles-audit-configs : Create a new profilesAuditConfig.
     *
     * @param profilesAuditConfig the profilesAuditConfig to create
     * @return the ResponseEntity with status 201 (Created) and with body the new profilesAuditConfig, or with status 400 (Bad Request) if the profilesAuditConfig has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/profiles-audit-configs")
    @Timed
    public ResponseEntity<ProfilesAuditConfig> createProfilesAuditConfig(@Valid @RequestBody ProfilesAuditConfig profilesAuditConfig) throws URISyntaxException {
        log.debug("REST request to save ProfilesAuditConfig : {}", profilesAuditConfig);
        if (profilesAuditConfig.getId() != null) {
            throw new BadRequestAlertException("A new profilesAuditConfig cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProfilesAuditConfig result = profilesAuditConfigRepository.save(profilesAuditConfig);
        profilesAuditConfigSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/profiles-audit-configs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /profiles-audit-configs : Updates an existing profilesAuditConfig.
     *
     * @param profilesAuditConfig the profilesAuditConfig to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated profilesAuditConfig,
     * or with status 400 (Bad Request) if the profilesAuditConfig is not valid,
     * or with status 500 (Internal Server Error) if the profilesAuditConfig couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/profiles-audit-configs")
    @Timed
    public ResponseEntity<ProfilesAuditConfig> updateProfilesAuditConfig(@Valid @RequestBody ProfilesAuditConfig profilesAuditConfig) throws URISyntaxException {
        log.debug("REST request to update ProfilesAuditConfig : {}", profilesAuditConfig);
        if (profilesAuditConfig.getId() == null) {
            return createProfilesAuditConfig(profilesAuditConfig);
        }
        ProfilesAuditConfig result = profilesAuditConfigRepository.save(profilesAuditConfig);
        profilesAuditConfigSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, profilesAuditConfig.getId().toString()))
            .body(result);
    }

    /**
     * GET  /profiles-audit-configs : get all the profilesAuditConfigs.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of profilesAuditConfigs in body
     */
    @GetMapping("/profiles-audit-configs")
    @Timed
    public ResponseEntity<List<ProfilesAuditConfig>> getAllProfilesAuditConfigs(Pageable pageable) {
        log.debug("REST request to get a page of ProfilesAuditConfigs");
        Page<ProfilesAuditConfig> page = profilesAuditConfigRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/profiles-audit-configs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /profiles-audit-configs/:id : get the "id" profilesAuditConfig.
     *
     * @param id the id of the profilesAuditConfig to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the profilesAuditConfig, or with status 404 (Not Found)
     */
    @GetMapping("/profiles-audit-configs/{id}")
    @Timed
    public ResponseEntity<ProfilesAuditConfig> getProfilesAuditConfig(@PathVariable Long id) {
        log.debug("REST request to get ProfilesAuditConfig : {}", id);
        ProfilesAuditConfig profilesAuditConfig = profilesAuditConfigRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(profilesAuditConfig));
    }

    /**
     * DELETE  /profiles-audit-configs/:id : delete the "id" profilesAuditConfig.
     *
     * @param id the id of the profilesAuditConfig to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/profiles-audit-configs/{id}")
    @Timed
    public ResponseEntity<Void> deleteProfilesAuditConfig(@PathVariable Long id) {
        log.debug("REST request to delete ProfilesAuditConfig : {}", id);
        profilesAuditConfigRepository.delete(id);
        profilesAuditConfigSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/profiles-audit-configs?query=:query : search for the profilesAuditConfig corresponding
     * to the query.
     *
     * @param query the query of the profilesAuditConfig search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/profiles-audit-configs")
    @Timed
    public ResponseEntity<List<ProfilesAuditConfig>> searchProfilesAuditConfigs(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ProfilesAuditConfigs for query {}", query);
        Page<ProfilesAuditConfig> page = profilesAuditConfigSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/profiles-audit-configs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}

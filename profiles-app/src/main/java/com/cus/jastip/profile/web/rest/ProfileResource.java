package com.cus.jastip.profile.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cus.jastip.profile.domain.Profile;
import com.cus.jastip.profile.domain.enumeration.ProfileStatus;
import com.cus.jastip.profile.domain.enumeration.UpdateType;
import com.cus.jastip.profile.repository.ProfileRepository;
import com.cus.jastip.profile.repository.search.ProfileSearchRepository;
import com.cus.jastip.profile.service.ImageProcessService;
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
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Profile.
 */
@RestController
@RequestMapping("/api")
public class ProfileResource {

	@Autowired
	private ImageProcessService imageProcessService;
	
	@Autowired
	private ProfilesAuditService profilesAuditService;

	private final Logger log = LoggerFactory.getLogger(ProfileResource.class);

	private static final String ENTITY_NAME = "profile";

	private final ProfileRepository profileRepository;

	private final ProfileSearchRepository profileSearchRepository;

	public ProfileResource(ProfileRepository profileRepository, ProfileSearchRepository profileSearchRepository) {
		this.profileRepository = profileRepository;
		this.profileSearchRepository = profileSearchRepository;
	}

	/**
	 * POST /profiles : Create a new profile.
	 *
	 * @param profile
	 *            the profile to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         profile, or with status 400 (Bad Request) if the profile has already
	 *         an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/profiles")
	@Timed
	public ResponseEntity<Profile> createProfile(@Valid @RequestBody Profile profile) throws URISyntaxException {
		log.debug("REST request to save Profile : {}", profile);
		if (profile.getId() != null) {
			throw new BadRequestAlertException("A new profile cannot already have an ID", ENTITY_NAME, "idexists");
		}
		if (profile.getImage() != null) {
			try {
				String url = imageProcessService.onSubmit(profile.getImage());
				profile.setUrlImage(imageProcessService.urlImage(url));
				profile.setUrlImageThumb(imageProcessService.urlImageThumb(url));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Profile result = profileRepository.save(profile);
		profileSearchRepository.save(result);
		profilesAuditService.addProfiles(result, ENTITY_NAME, UpdateType.CREATE);
		return ResponseEntity.created(new URI("/api/profiles/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /profiles : Updates an existing profile.
	 *
	 * @param profile
	 *            the profile to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         profile, or with status 400 (Bad Request) if the profile is not
	 *         valid, or with status 500 (Internal Server Error) if the profile
	 *         couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/profiles")
	@Timed
	public ResponseEntity<Profile> updateProfile(@Valid @RequestBody Profile profile) throws URISyntaxException {
		log.debug("REST request to update Profile : {}", profile);
		if (profile.getId() == null) {
			return createProfile(profile);
		}
		if (profile.getImage() == null) {
			Profile prof = profileRepository.findOne(profile.getId());
			profile.setImage(prof.getImage());
			profile.setUrlImage(prof.getUrlImage());
			profile.setUrlImageThumb(prof.getUrlImageThumb());
			profile.setImageContentType(prof.getImageContentType());
		} else {
			try {
				String url = imageProcessService.onSubmit(profile.getImage());
				profile.setUrlImage(imageProcessService.urlImage(url));
				profile.setUrlImageThumb(imageProcessService.urlImageThumb(url));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		Profile result = profileRepository.save(profile);
		profileSearchRepository.save(result);
		profilesAuditService.addProfiles(result, ENTITY_NAME, UpdateType.UPDATE);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, profile.getId().toString()))
				.body(result);
	}

	/**
	 * GET /profiles : get all the profiles.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of profiles in
	 *         body
	 */
	@GetMapping("/profiles")
	@Timed
	public ResponseEntity<List<Profile>> getAllProfiles(Pageable pageable) {
		log.debug("REST request to get all Profiles");
		Page<Profile> page = profileRepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/profiles");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@GetMapping("/profiles/status/{status}")
	public ResponseEntity<List<Profile>> getAllProfilesByStatus(@PathVariable ProfileStatus status, Pageable pageable) {
		log.debug("REST request to get all Profiles");
		List<Profile> list = profileRepository.findByStatus(status, pageable);
		Page<Profile> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/profiles/status/" + status);
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@GetMapping("/profiles/mobile/status/{status}")
	public ResponseEntity<List<Profile>> getAllProfilesByStatusMobile(@PathVariable ProfileStatus status,
			Pageable pageable) {
		log.debug("REST request to get all Profiles");
		List<Profile> list = new ArrayList<>();
		List<Profile> profile = profileRepository.findByStatus(status, pageable);
		for (Profile prof : profile) {
			prof.setImage(null);
			list.add(prof);
		}
		Page<Profile> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/profiles/status/mobile/" + status);
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@GetMapping("/profiles/mobile")
	@Timed
	public ResponseEntity<List<Profile>> getAllProfilesMobile(Pageable pageable) {
		log.debug("REST request to get all Profiles");
		List<Profile> list = new ArrayList<>();
		for (Profile profile : profileRepository.findAll(pageable)) {
			profile.setImage(null);
			list.add(profile);
		}
		Page<Profile> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/profiles/mobile");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@GetMapping("/profiles/mobile/{id}")
	@Timed
	public ResponseEntity<Profile> getProfilesMobileById(@PathVariable Long id) {
		log.debug("REST request to get all Profiles");
		Profile profile = profileRepository.findOne(id);
		profile.setImage(null);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(profile));
	}

	/**
	 * GET /profiles/:id : get the "id" profile.
	 *
	 * @param id
	 *            the id of the profile to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the profile, or
	 *         with status 404 (Not Found)
	 */
	@GetMapping("/profiles/{id}")
	@Timed
	public ResponseEntity<Profile> getProfile(@PathVariable Long id) {
		log.debug("REST request to get Profile : {}", id);
		Profile profile = profileRepository.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(profile));
	}

	@GetMapping("/profiles/username/{username}")
	@Timed
	public ResponseEntity<Profile> getProfileByUsername(@PathVariable String username) {
		log.debug("REST request to get Profile : {}", username);
		Profile profile = profileRepository.findByUsername(username);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(profile));
	}

	@GetMapping("/profiles/username/mobile/{username}")
	@Timed
	public ResponseEntity<Profile> getProfileByUsernameMobile(@PathVariable String username) {
		log.debug("REST request to get Profile : {}", username);
		Profile profile = profileRepository.findByUsername(username);
		profile.setImage(null);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(profile));
	}

	/**
	 * DELETE /profiles/:id : delete the "id" profile.
	 *
	 * @param id
	 *            the id of the profile to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/profiles/{id}")
	@Timed
	public ResponseEntity<Void> deleteProfile(@PathVariable Long id) {
		log.debug("REST request to delete Profile : {}", id);
		Profile result = profileRepository.findOne(id);
		profileRepository.delete(id);
		profileSearchRepository.delete(id);
		profilesAuditService.addProfiles(result, ENTITY_NAME, UpdateType.DELETE);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * SEARCH /_search/profiles?query=:query : search for the profile corresponding
	 * to the query.
	 *
	 * @param query
	 *            the query of the profile search
	 * @return the result of the search
	 */
	@GetMapping("/_search/profiles")
	@Timed
	public ResponseEntity<List<Profile>> searchProfiles(@RequestParam String query, Pageable pageable) {
		log.debug("REST request to search Profiles for query {}", query);
		List<Profile> list = StreamSupport
				.stream(profileSearchRepository.search(queryStringQuery(query), pageable).spliterator(), false)
				.collect(Collectors.toList());
		Page<Profile> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/_search/profiles");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@GetMapping("/_search/profiles/mobile")
	@Timed
	public ResponseEntity<List<Profile>> searchProfilesMobile(@RequestParam String query, Pageable pageable) {
		log.debug("REST request to search Profiles for query {}", query);
		List<Profile> list = new ArrayList<>();
		List<Profile> profile = StreamSupport
				.stream(profileSearchRepository.search(queryStringQuery(query), pageable).spliterator(), false)
				.collect(Collectors.toList());
		for (Profile profile2 : profile) {
			profile2.setImage(null);
			list.add(profile2);
		}
		Page<Profile> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/_search/profiles/mobile");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

}

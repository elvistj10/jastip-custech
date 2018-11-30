package com.cus.jastip.profile.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cus.jastip.profile.domain.Address;
import com.cus.jastip.profile.domain.Profile;
import com.cus.jastip.profile.domain.enumeration.UpdateType;
import com.cus.jastip.profile.repository.AddressRepository;
import com.cus.jastip.profile.repository.ProfileRepository;
import com.cus.jastip.profile.repository.search.AddressSearchRepository;
import com.cus.jastip.profile.service.DefaultAddressService;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Address.
 */
@RestController
@RequestMapping("/api")
public class AddressResource {
	
	@Autowired
	private ProfilesAuditService profilesAuditService;

	@Autowired
	private ProfileRepository profileRepository;
	@Autowired
	private DefaultAddressService defualtAddressService;

	private final Logger log = LoggerFactory.getLogger(AddressResource.class);

	private static final String ENTITY_NAME = "address";

	private final AddressRepository addressRepository;

	private final AddressSearchRepository addressSearchRepository;

	public AddressResource(AddressRepository addressRepository, AddressSearchRepository addressSearchRepository) {
		this.addressRepository = addressRepository;
		this.addressSearchRepository = addressSearchRepository;
	}

	@GetMapping("/changeDefaultAddress/{id}")
	public void changeDefaultAddress(@PathVariable Long id) {
		Address address = addressRepository.findOne(id);
		if (address.isDefaultAddress() == false) {
			defualtAddressService.addressChange(address.getProfile().getId());
			address.setDefaultAddress(true);
			Address result = addressRepository.save(address);
			addressSearchRepository.save(result);
		}

	}

	@GetMapping("/addresses/profiles/{id}")
	@Timed
	public ResponseEntity<List<Address>> getAllAddressesByProfile(@PathVariable Long id, Pageable pageable) {
		log.debug("REST request to get all Addresses");
		Profile profile = profileRepository.findOne(id);
		List<Address> list = addressRepository.findByProfile(profile, pageable);
		Page<Address> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/addresses/profiles/" + id);
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@GetMapping("/addresses/profiles/mobile/{id}")
	@Timed
	public ResponseEntity<List<Address>> getAllAddressesByProfileMobile(@PathVariable Long id, Pageable pageable) {
		log.debug("REST request to get all Addresses");
		Profile profile = profileRepository.findOne(id);
		List<Address> list = new ArrayList<>();
		List<Address> add = addressRepository.findByProfile(profile, pageable);
		for (Address address : add) {
			address.getProfile().setImage(null);
			list.add(address);
		}
		Page<Address> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/addresses/profiles/mobile" + id);
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * POST /addresses : Create a new address.
	 *
	 * @param address
	 *            the address to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         address, or with status 400 (Bad Request) if the address has already
	 *         an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/addresses")
	@Timed
	public ResponseEntity<Address> createAddress(@Valid @RequestBody Address address) throws URISyntaxException {
		log.debug("REST request to save Address : {}", address);
		if (address.getId() != null) {
			throw new BadRequestAlertException("A new address cannot already have an ID", ENTITY_NAME, "idexists");
		}
		Pageable pageable = null;
		List<Address> list = addressRepository.findByProfile(address.getProfile(), pageable);
		if (address.isDefaultAddress() == null) {
			address.setDefaultAddress(false);
		}
		if (list.size() == 0) {
			address.setDefaultAddress(true);
		} else if (address.isDefaultAddress() == true) {
			defualtAddressService.addressChange(address.getProfile().getId());
		}

		Address result = addressRepository.save(address);
		addressSearchRepository.save(result);
		profilesAuditService.addAddress(result, ENTITY_NAME, UpdateType.CREATE);
		return ResponseEntity.created(new URI("/api/addresses/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /addresses : Updates an existing address.
	 *
	 * @param address
	 *            the address to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         address, or with status 400 (Bad Request) if the address is not
	 *         valid, or with status 500 (Internal Server Error) if the address
	 *         couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/addresses")
	@Timed
	public ResponseEntity<Address> updateAddress(@Valid @RequestBody Address address) throws URISyntaxException {
		log.debug("REST request to update Address : {}", address);
		if (address.getId() == null) {
			return createAddress(address);
		}
		Pageable pageable = null;
		List<Address> list = addressRepository.findByProfile(address.getProfile(), pageable);
		if (address.isDefaultAddress() == null) {
			address.setDefaultAddress(false);
		}
		if (list.size() == 0) {
			address.setDefaultAddress(true);
		} else if (address.isDefaultAddress() == true) {
			defualtAddressService.addressChange(address.getProfile().getId());
		}
		Address result = addressRepository.save(address);
		addressSearchRepository.save(result);
		profilesAuditService.addAddress(result, ENTITY_NAME, UpdateType.UPDATE);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, address.getId().toString()))
				.body(result);
	}

	/**
	 * GET /addresses : get all the addresses.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of addresses in
	 *         body
	 */
	@GetMapping("/addresses")
	@Timed
	public ResponseEntity<List<Address>> getAllAddresses(Pageable pageable) {
		log.debug("REST request to get all Addresses");
		Page<Address> page = addressRepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/addresses");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@GetMapping("/addresses/mobile")
	@Timed
	public ResponseEntity<List<Address>> getAllAddressesMobile(Pageable pageable) {
		log.debug("REST request to get all Addresses");
		List<Address> list = new ArrayList<>();
		Page<Address> pages = addressRepository.findAll(pageable);
		for (Address address : pages.getContent()) {
			address.getProfile().setImage(null);
			list.add(address);
		}
		Page<Address> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/addresses/mobile");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /addresses/:id : get the "id" address.
	 *
	 * @param id
	 *            the id of the address to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the address, or
	 *         with status 404 (Not Found)
	 */
	@GetMapping("/addresses/{id}")
	@Timed
	public ResponseEntity<Address> getAddress(@PathVariable Long id) {
		log.debug("REST request to get Address : {}", id);
		Address address = addressRepository.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(address));
	}

	@GetMapping("/addresses/mobile/{id}")
	@Timed
	public ResponseEntity<Address> getAddressMobile(@PathVariable Long id) {
		log.debug("REST request to get Address : {}", id);
		Address address = addressRepository.findOne(id);
		address.getProfile().setImage(null);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(address));
	}

	/**
	 * DELETE /addresses/:id : delete the "id" address.
	 *
	 * @param id
	 *            the id of the address to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/addresses/{id}")
	@Timed
	public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
		log.debug("REST request to delete Address : {}", id);
		Address result = addressRepository.findOne(id);
		addressRepository.delete(id);
		addressSearchRepository.delete(id);
		profilesAuditService.addAddress(result, ENTITY_NAME, UpdateType.DELETE);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * SEARCH /_search/addresses?query=:query : search for the address corresponding
	 * to the query.
	 *
	 * @param query
	 *            the query of the address search
	 * @return the result of the search
	 */
	@GetMapping("/_search/addresses")
	@Timed
	public ResponseEntity<List<Address>> searchAddresses(@RequestParam String query, Pageable pageable) {
		log.debug("REST request to search Addresses for query {}", query);
		List<Address> list = StreamSupport
				.stream(addressSearchRepository.search(queryStringQuery(query), pageable).spliterator(), false)
				.collect(Collectors.toList());
		Page<Address> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/_search/addresses");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@GetMapping("/_search/addresses/mobile")
	@Timed
	public ResponseEntity<List<Address>> searchAddressesMobile(@RequestParam String query, Pageable pageable) {
		log.debug("REST request to search Addresses for query {}", query);
		List<Address> list = new ArrayList<>();
		List<Address> addr = StreamSupport
				.stream(addressSearchRepository.search(queryStringQuery(query), pageable).spliterator(), false)
				.collect(Collectors.toList());
		for (Address address : addr) {
			address.getProfile().setImage(null);
			list.add(address);
		}
		Page<Address> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/_search/addresses/mobile");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

}

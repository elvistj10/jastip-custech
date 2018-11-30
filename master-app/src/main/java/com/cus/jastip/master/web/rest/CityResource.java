package com.cus.jastip.master.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cus.jastip.master.domain.BusinessAccount;
import com.cus.jastip.master.domain.City;
import com.cus.jastip.master.domain.enumeration.UpdateType;
import com.cus.jastip.master.repository.CityRepository;
import com.cus.jastip.master.repository.search.CitySearchRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing City.
 */
@RestController
@RequestMapping("/api")
public class CityResource {

	private final Logger log = LoggerFactory.getLogger(CityResource.class);

	private static final String ENTITY_NAME = "city";
	

	@Autowired
	private MasterAuditService masterAuditService;

	private final CityRepository cityRepository;

	private final CitySearchRepository citySearchRepository;

	public CityResource(CityRepository cityRepository, CitySearchRepository citySearchRepository) {
		this.cityRepository = cityRepository;
		this.citySearchRepository = citySearchRepository;
	}

	/**
	 * POST /cities : Create a new city.
	 *
	 * @param city
	 *            the city to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         city, or with status 400 (Bad Request) if the city has already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/cities")
	@Timed
	public ResponseEntity<City> createCity(@Valid @RequestBody City city) throws URISyntaxException {
		log.debug("REST request to save City : {}", city);
		if (city.getId() != null) {
			throw new BadRequestAlertException("A new city cannot already have an ID", ENTITY_NAME, "idexists");
		}
		City result = cityRepository.save(city);
		citySearchRepository.save(result);
		masterAuditService.addCity(result, ENTITY_NAME, UpdateType.CREATE);
		return ResponseEntity.created(new URI("/api/cities/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /cities : Updates an existing city.
	 *
	 * @param city
	 *            the city to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         city, or with status 400 (Bad Request) if the city is not valid, or
	 *         with status 500 (Internal Server Error) if the city couldn't be
	 *         updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/cities")
	@Timed
	public ResponseEntity<City> updateCity(@Valid @RequestBody City city) throws URISyntaxException {
		log.debug("REST request to update City : {}", city);
		if (city.getId() == null) {
			return createCity(city);
		}
		City result = cityRepository.save(city);
		citySearchRepository.save(result);
		masterAuditService.addCity(result, ENTITY_NAME, UpdateType.UPDATE);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, city.getId().toString()))
				.body(result);
	}

	/**
	 * GET /cities : get all the cities.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of cities in
	 *         body
	 */
	@GetMapping("/cities")
	@Timed
	public ResponseEntity<List<City>> getAllCities(Pageable pageable) {
		log.debug("REST request to get all Cities");
		Page<City> page = cityRepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cities");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@GetMapping("/cities/mobile")
	@Timed
	public ResponseEntity<List<City>> getAllCitiesMobile(Pageable pageable) {
		log.debug("REST request to get all Cities");
		List<City> list = new ArrayList<>();
		Page<City> pages = cityRepository.findAll(pageable);
		for (City city : pages.getContent()) {
			city.getProvince().getCountry().setImage(null);
			city.getProvince().getCountry().setImageFlag(null);
			list.add(city);
		}
		Page<City> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cities/mobile");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /cities/:id : get the "id" city.
	 *
	 * @param id
	 *            the id of the city to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the city, or
	 *         with status 404 (Not Found)
	 */
	@GetMapping("/cities/{id}")
	@Timed
	public ResponseEntity<City> getCity(@PathVariable Long id) {
		log.debug("REST request to get City : {}", id);
		City city = cityRepository.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(city));
	}

	@GetMapping("/cities/mobile/{id}")
	@Timed
	public ResponseEntity<City> getCityMobile(@PathVariable Long id) {
		log.debug("REST request to get City : {}", id);
		City city = cityRepository.findOne(id);
		city.getProvince().getCountry().setImage(null);
		city.getProvince().getCountry().setImageFlag(null);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(city));
	}

	/**
	 * DELETE /cities/:id : delete the "id" city.
	 *
	 * @param id
	 *            the id of the city to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/cities/{id}")
	@Timed
	public ResponseEntity<Void> deleteCity(@PathVariable Long id) {
		log.debug("REST request to delete City : {}", id);
		City result = cityRepository.findOne(id);
		cityRepository.delete(id);
		citySearchRepository.delete(id);
		masterAuditService.addCity(result, ENTITY_NAME, UpdateType.DELETE);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * SEARCH /_search/cities?query=:query : search for the city corresponding to
	 * the query.
	 *
	 * @param query
	 *            the query of the city search
	 * @return the result of the search
	 */
	@GetMapping("/_search/cities")
	@Timed
	public ResponseEntity<List<City>> searchCities(@RequestParam String query, Pageable pageable) {
		log.debug("REST request to search Cities for query {}", query);
		List<City> city = StreamSupport
				.stream(citySearchRepository.search(queryStringQuery(query), pageable).spliterator(), false)
				.collect(Collectors.toList());
		Page<City> page = new PageImpl<>(city);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/_search/cities");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@GetMapping("/_search/cities/mobile")
	@Timed
	public ResponseEntity<List<City>> searchCitiesMobile(@RequestParam String query, Pageable pageable) {
		log.debug("REST request to search Cities for query {}", query);
		List<City> list = new ArrayList<>();
		List<City> city = StreamSupport
				.stream(citySearchRepository.search(queryStringQuery(query), pageable).spliterator(), false)
				.collect(Collectors.toList());
		for (City cty : city) {
			cty.getProvince().getCountry().setImage(null);
			cty.getProvince().getCountry().setImageFlag(null);
			list.add(cty);
		}
		Page<City> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/_search/cities/mobile");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

}

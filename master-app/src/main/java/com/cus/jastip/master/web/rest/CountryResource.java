package com.cus.jastip.master.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cus.jastip.master.domain.Country;
import com.cus.jastip.master.domain.enumeration.UpdateType;
import com.cus.jastip.master.repository.CountryRepository;
import com.cus.jastip.master.repository.search.CountrySearchRepository;
import com.cus.jastip.master.service.ImageProcessService;
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
 * REST controller for managing Country.
 */
@RestController
@RequestMapping("/api")
public class CountryResource {

	@Autowired
	private ImageProcessService imageProcessService;

	private final Logger log = LoggerFactory.getLogger(CountryResource.class);

	private static final String ENTITY_NAME = "country";


	@Autowired
	private MasterAuditService masterAuditService;

	private final CountryRepository countryRepository;

	private final CountrySearchRepository countrySearchRepository;

	public CountryResource(CountryRepository countryRepository, CountrySearchRepository countrySearchRepository) {
		this.countryRepository = countryRepository;
		this.countrySearchRepository = countrySearchRepository;
	}

	/**
	 * POST /countries : Create a new country.
	 *
	 * @param country
	 *            the country to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         country, or with status 400 (Bad Request) if the country has already
	 *         an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/countries")
	@Timed
	public ResponseEntity<Country> createCountry(@Valid @RequestBody Country country) throws URISyntaxException {
		log.debug("REST request to save Country : {}", country);
		if (country.getId() != null) {
			throw new BadRequestAlertException("A new country cannot already have an ID", ENTITY_NAME, "idexists");
		}
		if (country.getImage() != null) {
			try {
				String url = imageProcessService.onSubmit(country.getImage());
				country.setImageUrl(imageProcessService.urlImage(url));
				country.setImageThumbUrl(imageProcessService.urlImageThumb(url));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (country.getImageFlag() != null) {
			try {
				String urlflag = imageProcessService.onSubmit(country.getImageFlag());
				country.setImageFlagUrl(imageProcessService.urlImage(urlflag));
				country.setImageFlagThumbUrl(imageProcessService.urlImageThumb(urlflag));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Country result = countryRepository.save(country);
		countrySearchRepository.save(result);
		masterAuditService.addCountry(result, ENTITY_NAME, UpdateType.CREATE);
		return ResponseEntity.created(new URI("/api/countries/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /countries : Updates an existing country.
	 *
	 * @param country
	 *            the country to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         country, or with status 400 (Bad Request) if the country is not
	 *         valid, or with status 500 (Internal Server Error) if the country
	 *         couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/countries")
	@Timed
	public ResponseEntity<Country> updateCountry(@Valid @RequestBody Country country) throws URISyntaxException {
		log.debug("REST request to update Country : {}", country);
		if (country.getId() == null) {
			return createCountry(country);
		}

		Country result = countryRepository.save(country);
		countrySearchRepository.save(result);
		masterAuditService.addCountry(result, ENTITY_NAME, UpdateType.UPDATE);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, country.getId().toString()))
				.body(result);
	}

	/**
	 * GET /countries : get all the countries.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of countries in
	 *         body
	 */
	@GetMapping("/countries")
	@Timed
	public ResponseEntity<List<Country>> getAllCountries(Pageable pageable) {
		log.debug("REST request to get all Countries");
		Page<Country> page = countryRepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/countries");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /countries/:id : get the "id" country.
	 *
	 * @param id
	 *            the id of the country to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the country, or
	 *         with status 404 (Not Found)
	 */
	@GetMapping("/countries/{id}")
	@Timed
	public ResponseEntity<Country> getCountry(@PathVariable Long id) {
		log.debug("REST request to get Country : {}", id);
		Country country = countryRepository.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(country));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GetMapping("/countries/mobile")
	@Timed
	public ResponseEntity<List<Country>> getAllCountriesMobile() {
		log.debug("REST request to get all Countries");
		List<Country> list = new ArrayList();
		for (Country country : countryRepository.findAll()) {
			country.setImage(null);
			country.setImageFlag(null);
			list.add(country);
		}
		Page<Country> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/countries/mobile");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@GetMapping("/countries/mobile/{id}")
	@Timed
	public ResponseEntity<Country> getCountryMobile(@PathVariable Long id) {
		log.debug("REST request to get Country : {}", id);
		Country country = countryRepository.findOne(id);
		country.setImage(null);
		country.setImageFlag(null);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(country));
	}

	/**
	 * DELETE /countries/:id : delete the "id" country.
	 *
	 * @param id
	 *            the id of the country to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/countries/{id}")
	@Timed
	public ResponseEntity<Void> deleteCountry(@PathVariable Long id) {
		log.debug("REST request to delete Country : {}", id);
		Country result = countryRepository.findOne(id);
		countryRepository.delete(id);
		countrySearchRepository.delete(id);
		masterAuditService.addCountry(result, ENTITY_NAME, UpdateType.DELETE);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * SEARCH /_search/countries?query=:query : search for the country corresponding
	 * to the query.
	 *
	 * @param query
	 *            the query of the country search
	 * @return the result of the search
	 */
	@GetMapping("/_search/countries")
	@Timed
	public ResponseEntity<List<Country>> searchCountries(@RequestParam String query, Pageable pageable) {
		log.debug("REST request to search Countries for query {}", query);
		List<Country> country = StreamSupport
				.stream(countrySearchRepository.search(queryStringQuery(query), pageable).spliterator(), false)
				.collect(Collectors.toList());
		Page<Country> page = new PageImpl<>(country);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/_search/countries");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);

	}

	@SuppressWarnings("unchecked")
	@GetMapping("/_search/countries/mobile")
	@Timed
	public ResponseEntity<List<Country>> searchCountriesMobile(@RequestParam String query, Pageable pageable) {
		log.debug("REST request to search Countries for query {}", query);
		List<Country> country = StreamSupport
				.stream(countrySearchRepository.search(queryStringQuery(query), pageable).spliterator(), false)
				.collect(Collectors.toList());
		@SuppressWarnings("rawtypes")
		List<Country> list = new ArrayList();
		for (Country cnty : country) {
			cnty.setImage(null);
			cnty.setImageFlag(null);
			list.add(cnty);
		}
		Page<Country> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/_search/countries/mobile");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);

	}

}

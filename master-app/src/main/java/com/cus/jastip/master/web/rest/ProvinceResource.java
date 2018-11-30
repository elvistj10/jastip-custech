package com.cus.jastip.master.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cus.jastip.master.domain.BusinessAccount;
import com.cus.jastip.master.domain.PostalCode;
import com.cus.jastip.master.domain.Province;
import com.cus.jastip.master.domain.enumeration.UpdateType;
import com.cus.jastip.master.repository.ProvinceRepository;
import com.cus.jastip.master.repository.search.ProvinceSearchRepository;
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
 * REST controller for managing Province.
 */
@RestController
@RequestMapping("/api")
public class ProvinceResource {

	private final Logger log = LoggerFactory.getLogger(ProvinceResource.class);

	private static final String ENTITY_NAME = "province";

	@Autowired
	private MasterAuditService masterAuditService;

	private final ProvinceRepository provinceRepository;

	private final ProvinceSearchRepository provinceSearchRepository;

	public ProvinceResource(ProvinceRepository provinceRepository, ProvinceSearchRepository provinceSearchRepository) {
		this.provinceRepository = provinceRepository;
		this.provinceSearchRepository = provinceSearchRepository;
	}

	/**
	 * POST /provinces : Create a new province.
	 *
	 * @param province
	 *            the province to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         province, or with status 400 (Bad Request) if the province has
	 *         already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/provinces")
	@Timed
	public ResponseEntity<Province> createProvince(@Valid @RequestBody Province province) throws URISyntaxException {
		log.debug("REST request to save Province : {}", province);
		if (province.getId() != null) {
			throw new BadRequestAlertException("A new province cannot already have an ID", ENTITY_NAME, "idexists");
		}
		Province result = provinceRepository.save(province);
		provinceSearchRepository.save(result);
		masterAuditService.addProvince(result, ENTITY_NAME, UpdateType.CREATE);
		return ResponseEntity.created(new URI("/api/provinces/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /provinces : Updates an existing province.
	 *
	 * @param province
	 *            the province to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         province, or with status 400 (Bad Request) if the province is not
	 *         valid, or with status 500 (Internal Server Error) if the province
	 *         couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/provinces")
	@Timed
	public ResponseEntity<Province> updateProvince(@Valid @RequestBody Province province) throws URISyntaxException {
		log.debug("REST request to update Province : {}", province);
		if (province.getId() == null) {
			return createProvince(province);
		}
		Province result = provinceRepository.save(province);
		provinceSearchRepository.save(result);
		masterAuditService.addProvince(result, ENTITY_NAME, UpdateType.UPDATE);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, province.getId().toString()))
				.body(result);
	}

	/**
	 * GET /provinces : get all the provinces.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of provinces in
	 *         body
	 */
	@GetMapping("/provinces")
	@Timed
	public ResponseEntity<List<Province>> getAllProvinces(Pageable pageable) {
		log.debug("REST request to get all Provinces");
		Page<Province> page = provinceRepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/provinces");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@GetMapping("/provinces/mobile")
	@Timed
	public ResponseEntity<List<Province>> getAllProvincesMobile(Pageable pageable) {
		log.debug("REST request to get all Provinces");
		Page<Province> pages = provinceRepository.findAll(pageable);
		List<Province> list = new ArrayList<>();
		for (Province province : pages.getContent()) {
			province.getCountry().setImage(null);
			province.getCountry().setImageFlag(null);
			list.add(province);
		}
		Page<Province> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/provinces/mobile");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /provinces/:id : get the "id" province.
	 *
	 * @param id
	 *            the id of the province to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the province,
	 *         or with status 404 (Not Found)
	 */
	@GetMapping("/provinces/{id}")
	@Timed
	public ResponseEntity<Province> getProvince(@PathVariable Long id) {
		log.debug("REST request to get Province : {}", id);
		Province province = provinceRepository.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(province));
	}

	@GetMapping("/provinces/mobile/{id}")
	@Timed
	public ResponseEntity<Province> getProvinceMobile(@PathVariable Long id) {
		log.debug("REST request to get Province : {}", id);
		Province province = provinceRepository.findOne(id);
		province.getCountry().setImage(null);
		province.getCountry().setImageFlag(null);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(province));
	}

	/**
	 * DELETE /provinces/:id : delete the "id" province.
	 *
	 * @param id
	 *            the id of the province to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/provinces/{id}")
	@Timed
	public ResponseEntity<Void> deleteProvince(@PathVariable Long id) {
		log.debug("REST request to delete Province : {}", id);
		Province result = provinceRepository.findOne(id);
		provinceRepository.delete(id);
		provinceSearchRepository.delete(id);
		masterAuditService.addProvince(result, ENTITY_NAME, UpdateType.DELETE);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * SEARCH /_search/provinces?query=:query : search for the province
	 * corresponding to the query.
	 *
	 * @param query
	 *            the query of the province search
	 * @return the result of the search
	 */
	@GetMapping("/_search/provinces")
	@Timed
	public ResponseEntity<List<Province>> searchProvinces(@RequestParam String query, Pageable pageable) {
		log.debug("REST request to search Provinces for query {}", query);
		List<Province> province = StreamSupport
				.stream(provinceSearchRepository.search(queryStringQuery(query), pageable).spliterator(), false)
				.collect(Collectors.toList());
		Page<Province> page = new PageImpl<>(province);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/_search/provinces");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@GetMapping("/_search/provinces/mobile")
	@Timed
	public ResponseEntity<List<Province>> searchProvincesMobile(@RequestParam String query, Pageable pageable) {
		log.debug("REST request to search Provinces for query {}", query);
		List<Province> list = new ArrayList<>();
		List<Province> province = StreamSupport
				.stream(provinceSearchRepository.search(queryStringQuery(query), pageable).spliterator(), false)
				.collect(Collectors.toList());
		for (Province prov : province) {
			prov.getCountry().setImage(null);
			prov.getCountry().setImageFlag(null);
			list.add(prov);
		}
		Page<Province> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/_search/provinces/mobile");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

}

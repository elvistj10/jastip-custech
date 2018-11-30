package com.cus.jastip.master.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cus.jastip.master.domain.BusinessAccount;
import com.cus.jastip.master.domain.ItemSubCategory;
import com.cus.jastip.master.domain.PackageSize;
import com.cus.jastip.master.domain.enumeration.UpdateType;
import com.cus.jastip.master.repository.PackageSizeRepository;
import com.cus.jastip.master.repository.search.PackageSizeSearchRepository;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing PackageSize.
 */
@RestController
@RequestMapping("/api")
public class PackageSizeResource {

	private final Logger log = LoggerFactory.getLogger(PackageSizeResource.class);

	private static final String ENTITY_NAME = "packageSize";
	
	@Autowired
	private MasterAuditService masterAuditService;

	private final PackageSizeRepository packageSizeRepository;

	private final PackageSizeSearchRepository packageSizeSearchRepository;

	public PackageSizeResource(PackageSizeRepository packageSizeRepository,
			PackageSizeSearchRepository packageSizeSearchRepository) {
		this.packageSizeRepository = packageSizeRepository;
		this.packageSizeSearchRepository = packageSizeSearchRepository;
	}

	/**
	 * POST /package-sizes : Create a new packageSize.
	 *
	 * @param packageSize
	 *            the packageSize to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         packageSize, or with status 400 (Bad Request) if the packageSize has
	 *         already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/package-sizes")
	@Timed
	public ResponseEntity<PackageSize> createPackageSize(@Valid @RequestBody PackageSize packageSize)
			throws URISyntaxException {
		log.debug("REST request to save PackageSize : {}", packageSize);
		if (packageSize.getId() != null) {
			throw new BadRequestAlertException("A new packageSize cannot already have an ID", ENTITY_NAME, "idexists");
		}
		PackageSize result = packageSizeRepository.save(packageSize);
		packageSizeSearchRepository.save(result);
		masterAuditService.addPackageSize(result, ENTITY_NAME, UpdateType.CREATE);
		return ResponseEntity.created(new URI("/api/package-sizes/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /package-sizes : Updates an existing packageSize.
	 *
	 * @param packageSize
	 *            the packageSize to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         packageSize, or with status 400 (Bad Request) if the packageSize is
	 *         not valid, or with status 500 (Internal Server Error) if the
	 *         packageSize couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/package-sizes")
	@Timed
	public ResponseEntity<PackageSize> updatePackageSize(@Valid @RequestBody PackageSize packageSize)
			throws URISyntaxException {
		log.debug("REST request to update PackageSize : {}", packageSize);
		if (packageSize.getId() == null) {
			return createPackageSize(packageSize);
		}
		PackageSize result = packageSizeRepository.save(packageSize);
		packageSizeSearchRepository.save(result);
		masterAuditService.addPackageSize(result, ENTITY_NAME, UpdateType.UPDATE);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, packageSize.getId().toString())).body(result);
	}

	/**
	 * GET /package-sizes : get all the packageSizes.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of packageSizes
	 *         in body
	 */
	@GetMapping("/package-sizes")
	@Timed
	public ResponseEntity<List<PackageSize>> getAllPackageSizes(Pageable pageable) {
		log.debug("REST request to get all PackageSizes");
		Page<PackageSize> page = packageSizeRepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/package-sizes");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /package-sizes/:id : get the "id" packageSize.
	 *
	 * @param id
	 *            the id of the packageSize to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         packageSize, or with status 404 (Not Found)
	 */
	@GetMapping("/package-sizes/{id}")
	@Timed
	public ResponseEntity<PackageSize> getPackageSize(@PathVariable Long id) {
		log.debug("REST request to get PackageSize : {}", id);
		PackageSize packageSize = packageSizeRepository.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(packageSize));
	}

	/**
	 * DELETE /package-sizes/:id : delete the "id" packageSize.
	 *
	 * @param id
	 *            the id of the packageSize to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/package-sizes/{id}")
	@Timed
	public ResponseEntity<Void> deletePackageSize(@PathVariable Long id) {
		log.debug("REST request to delete PackageSize : {}", id);
		PackageSize result = packageSizeRepository.findOne(id);
		packageSizeRepository.delete(id);
		packageSizeSearchRepository.delete(id);
		masterAuditService.addPackageSize(result, ENTITY_NAME, UpdateType.DELETE);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * SEARCH /_search/package-sizes?query=:query : search for the packageSize
	 * corresponding to the query.
	 *
	 * @param query
	 *            the query of the packageSize search
	 * @return the result of the search
	 */
	@GetMapping("/_search/package-sizes")
	@Timed
	public ResponseEntity<List<PackageSize>> searchPackageSizes(@RequestParam String query, Pageable pageable) {
		log.debug("REST request to search PackageSizes for query {}", query);
		List<PackageSize> packageSize = StreamSupport
				.stream(packageSizeSearchRepository.search(queryStringQuery(query), pageable).spliterator(), false)
				.collect(Collectors.toList());
		Page<PackageSize> page = new PageImpl<>(packageSize);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/_search/package-sizes");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

}

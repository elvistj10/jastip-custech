package com.cus.jastip.banner.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cus.jastip.banner.domain.Banner;
import com.cus.jastip.banner.domain.enumeration.UpdateType;
import com.cus.jastip.banner.repository.BannerRepository;
import com.cus.jastip.banner.repository.search.BannerSearchRepository;
import com.cus.jastip.banner.service.BannerAuditService;
import com.cus.jastip.banner.web.rest.errors.BadRequestAlertException;
import com.cus.jastip.banner.web.rest.util.HeaderUtil;
import com.cus.jastip.banner.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Banner.
 */
@RestController
@RequestMapping("/api")
public class BannerResource {

	private final Logger log = LoggerFactory.getLogger(BannerResource.class);

	private static final String ENTITY_NAME = "banner";

	private final BannerRepository bannerRepository;

	private final BannerSearchRepository bannerSearchRepository;

	@Autowired
	private BannerAuditService bannerAuditSevice;

	public BannerResource(BannerRepository bannerRepository, BannerSearchRepository bannerSearchRepository) {
		this.bannerRepository = bannerRepository;
		this.bannerSearchRepository = bannerSearchRepository;
	}

	/**
	 * POST /banners : Create a new banner.
	 *
	 * @param banner
	 *            the banner to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         banner, or with status 400 (Bad Request) if the banner has already an
	 *         ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/banners")
	@Timed
	public ResponseEntity<Banner> createBanner(@RequestBody Banner banner) throws URISyntaxException {
		log.debug("REST request to save Banner : {}", banner);
		if (banner.getId() != null) {
			throw new BadRequestAlertException("A new banner cannot already have an ID", ENTITY_NAME, "idexists");
		}
		Banner result = bannerRepository.save(banner);
		bannerSearchRepository.save(result);
		bannerAuditSevice.addBanner(result, ENTITY_NAME, UpdateType.CREATE);
		return ResponseEntity.created(new URI("/api/banners/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /banners : Updates an existing banner.
	 *
	 * @param banner
	 *            the banner to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         banner, or with status 400 (Bad Request) if the banner is not valid,
	 *         or with status 500 (Internal Server Error) if the banner couldn't be
	 *         updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/banners")
	@Timed
	public ResponseEntity<Banner> updateBanner(@RequestBody Banner banner) throws URISyntaxException {
		log.debug("REST request to update Banner : {}", banner);
		if (banner.getId() == null) {
			return createBanner(banner);
		}
		Banner result = bannerRepository.save(banner);
		bannerSearchRepository.save(result);
		bannerAuditSevice.addBanner(result, ENTITY_NAME, UpdateType.UPDATE);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, banner.getId().toString()))
				.body(result);
	}

	/**
	 * GET /banners : get all the banners.
	 *
	 * @param pageable
	 *            the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of banners in
	 *         body
	 */
	@GetMapping("/banners")
	@Timed
	public ResponseEntity<List<Banner>> getAllBanners(Pageable pageable) {
		log.debug("REST request to get a page of Banners");
		Page<Banner> page = bannerRepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/banners");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /banners/:id : get the "id" banner.
	 *
	 * @param id
	 *            the id of the banner to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the banner, or
	 *         with status 404 (Not Found)
	 */
	@GetMapping("/banners/{id}")
	@Timed
	public ResponseEntity<Banner> getBanner(@PathVariable Long id) {
		log.debug("REST request to get Banner : {}", id);
		Banner banner = bannerRepository.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(banner));
	}

	/**
	 * DELETE /banners/:id : delete the "id" banner.
	 *
	 * @param id
	 *            the id of the banner to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/banners/{id}")
	@Timed
	public ResponseEntity<Void> deleteBanner(@PathVariable Long id) {
		log.debug("REST request to delete Banner : {}", id);
		Banner result = bannerRepository.findOne(id);
		bannerRepository.delete(id);
		bannerSearchRepository.delete(id);
		bannerAuditSevice.addBanner(result, ENTITY_NAME, UpdateType.DELETE);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * SEARCH /_search/banners?query=:query : search for the banner corresponding to
	 * the query.
	 *
	 * @param query
	 *            the query of the banner search
	 * @param pageable
	 *            the pagination information
	 * @return the result of the search
	 */
	@GetMapping("/_search/banners")
	@Timed
	public ResponseEntity<List<Banner>> searchBanners(@RequestParam String query, Pageable pageable) {
		log.debug("REST request to search for a page of Banners for query {}", query);
		Page<Banner> page = bannerSearchRepository.search(queryStringQuery(query), pageable);
		HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/banners");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

}

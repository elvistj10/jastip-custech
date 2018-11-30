package com.cus.jastip.master.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cus.jastip.master.domain.BusinessAccount;
import com.cus.jastip.master.domain.ItemCategory;
import com.cus.jastip.master.domain.ItemSubCategory;
import com.cus.jastip.master.domain.enumeration.UpdateType;
import com.cus.jastip.master.repository.ItemSubCategoryRepository;
import com.cus.jastip.master.repository.search.ItemSubCategorySearchRepository;
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
 * REST controller for managing ItemSubCategory.
 */
@RestController
@RequestMapping("/api")
public class ItemSubCategoryResource {

	@Autowired
	private ImageProcessService imageProcessService;

	private final Logger log = LoggerFactory.getLogger(ItemSubCategoryResource.class);

	private static final String ENTITY_NAME = "itemSubCategory";

	@Autowired
	private MasterAuditService masterAuditService;

	private final ItemSubCategoryRepository itemSubCategoryRepository;

	private final ItemSubCategorySearchRepository itemSubCategorySearchRepository;

	public ItemSubCategoryResource(ItemSubCategoryRepository itemSubCategoryRepository,
			ItemSubCategorySearchRepository itemSubCategorySearchRepository) {
		this.itemSubCategoryRepository = itemSubCategoryRepository;
		this.itemSubCategorySearchRepository = itemSubCategorySearchRepository;
	}

	/**
	 * POST /item-sub-categories : Create a new itemSubCategory.
	 *
	 * @param itemSubCategory
	 *            the itemSubCategory to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         itemSubCategory, or with status 400 (Bad Request) if the
	 *         itemSubCategory has already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/item-sub-categories")
	@Timed
	public ResponseEntity<ItemSubCategory> createItemSubCategory(@Valid @RequestBody ItemSubCategory itemSubCategory)
			throws URISyntaxException {
		log.debug("REST request to save ItemSubCategory : {}", itemSubCategory);
		if (itemSubCategory.getId() != null) {
			throw new BadRequestAlertException("A new itemSubCategory cannot already have an ID", ENTITY_NAME,
					"idexists");
		}
		if (itemSubCategory.getItemSubCategoryIcon() != null) {
			try {
				String url = imageProcessService.onSubmit(itemSubCategory.getItemSubCategoryIcon());
				itemSubCategory.setItemSubCategoryIconUrl(imageProcessService.urlImageThumb(url));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		ItemSubCategory result = itemSubCategoryRepository.save(itemSubCategory);
		itemSubCategorySearchRepository.save(result);
		masterAuditService.addItemSubCategory(result, ENTITY_NAME, UpdateType.CREATE);
		return ResponseEntity.created(new URI("/api/item-sub-categories/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /item-sub-categories : Updates an existing itemSubCategory.
	 *
	 * @param itemSubCategory
	 *            the itemSubCategory to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         itemSubCategory, or with status 400 (Bad Request) if the
	 *         itemSubCategory is not valid, or with status 500 (Internal Server
	 *         Error) if the itemSubCategory couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/item-sub-categories")
	@Timed
	public ResponseEntity<ItemSubCategory> updateItemSubCategory(@Valid @RequestBody ItemSubCategory itemSubCategory)
			throws URISyntaxException {
		log.debug("REST request to update ItemSubCategory : {}", itemSubCategory);
		if (itemSubCategory.getId() == null) {
			return createItemSubCategory(itemSubCategory);
		}
		ItemSubCategory result = itemSubCategoryRepository.save(itemSubCategory);
		itemSubCategorySearchRepository.save(result);
		masterAuditService.addItemSubCategory(result, ENTITY_NAME, UpdateType.UPDATE);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, itemSubCategory.getId().toString()))
				.body(result);
	}

	/**
	 * GET /item-sub-categories : get all the itemSubCategories.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         itemSubCategories in body
	 */
	@GetMapping("/item-sub-categories")
	@Timed
	public ResponseEntity<List<ItemSubCategory>> getAllItemSubCategories(Pageable pageable) {
		log.debug("REST request to get all ItemSubCategories");
		Page<ItemSubCategory> page = itemSubCategoryRepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/item-sub-categories");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@GetMapping("/item-sub-categories/mobile")
	@Timed
	public ResponseEntity<List<ItemSubCategory>> getAllItemSubCategoriesMobile(Pageable pageable) {
		log.debug("REST request to get all ItemSubCategories");
		List<ItemSubCategory> list = new ArrayList<>();
		Page<ItemSubCategory> pages = itemSubCategoryRepository.findAll(pageable);
		for (ItemSubCategory itemSubCategory : pages.getContent()) {
			itemSubCategory.getItemCategory().setItemCategoryIcon(null);
			list.add(itemSubCategory);
		}
		Page<ItemSubCategory> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/item-sub-categories/mobile");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /item-sub-categories/:id : get the "id" itemSubCategory.
	 *
	 * @param id
	 *            the id of the itemSubCategory to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         itemSubCategory, or with status 404 (Not Found)
	 */
	@GetMapping("/item-sub-categories/{id}")
	@Timed
	public ResponseEntity<ItemSubCategory> getItemSubCategory(@PathVariable Long id) {
		log.debug("REST request to get ItemSubCategory : {}", id);
		ItemSubCategory itemSubCategory = itemSubCategoryRepository.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(itemSubCategory));
	}

	@GetMapping("/item-sub-categories/mobile/{id}")
	@Timed
	public ResponseEntity<ItemSubCategory> getItemSubCategoryMobile(@PathVariable Long id) {
		log.debug("REST request to get ItemSubCategory : {}", id);
		ItemSubCategory itemSubCategory = itemSubCategoryRepository.findOne(id);
		itemSubCategory.getItemCategory().setItemCategoryIcon(null);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(itemSubCategory));
	}

	/**
	 * DELETE /item-sub-categories/:id : delete the "id" itemSubCategory.
	 *
	 * @param id
	 *            the id of the itemSubCategory to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/item-sub-categories/{id}")
	@Timed
	public ResponseEntity<Void> deleteItemSubCategory(@PathVariable Long id) {
		log.debug("REST request to delete ItemSubCategory : {}", id);
		ItemSubCategory result = itemSubCategoryRepository.findOne(id);
		itemSubCategoryRepository.delete(id);
		itemSubCategorySearchRepository.delete(id);
		masterAuditService.addItemSubCategory(result, ENTITY_NAME, UpdateType.DELETE);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * SEARCH /_search/item-sub-categories?query=:query : search for the
	 * itemSubCategory corresponding to the query.
	 *
	 * @param query
	 *            the query of the itemSubCategory search
	 * @return the result of the search
	 */
	@GetMapping("/_search/item-sub-categories")
	@Timed
	public ResponseEntity<List<ItemSubCategory>> searchItemSubCategories(@RequestParam String query,
			Pageable pageable) {
		log.debug("REST request to search ItemSubCategories for query {}", query);
		List<ItemSubCategory> itemSubCategory = StreamSupport
				.stream(itemSubCategorySearchRepository.search(queryStringQuery(query), pageable).spliterator(), false)
				.collect(Collectors.toList());
		Page<ItemSubCategory> page = new PageImpl<>(itemSubCategory);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/_search/item-sub-categories");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@GetMapping("/_search/item-sub-categories/mobile")
	@Timed
	public ResponseEntity<List<ItemSubCategory>> searchItemSubCategoriesMobile(@RequestParam String query,
			Pageable pageable) {
		List<ItemSubCategory> list = new ArrayList<>();
		log.debug("REST request to search ItemSubCategories for query {}", query);
		List<ItemSubCategory> itemSubCategory = StreamSupport
				.stream(itemSubCategorySearchRepository.search(queryStringQuery(query), pageable).spliterator(), false)
				.collect(Collectors.toList());
		for (ItemSubCategory itemSubCategory2 : itemSubCategory) {
			itemSubCategory2.setItemSubCategoryIcon(null);
			list.add(itemSubCategory2);
		}
		Page<ItemSubCategory> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page,
				"/api/_search/item-sub-categories/mobile");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

}

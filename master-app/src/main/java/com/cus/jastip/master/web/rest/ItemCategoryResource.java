package com.cus.jastip.master.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cus.jastip.master.domain.BusinessAccount;
import com.cus.jastip.master.domain.City;
import com.cus.jastip.master.domain.Country;
import com.cus.jastip.master.domain.ItemCategory;
import com.cus.jastip.master.domain.ItemSubCategory;
import com.cus.jastip.master.domain.enumeration.UpdateType;
import com.cus.jastip.master.repository.ItemCategoryRepository;
import com.cus.jastip.master.repository.search.ItemCategorySearchRepository;
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
 * REST controller for managing ItemCategory.
 */
@RestController
@RequestMapping("/api")
public class ItemCategoryResource {

	@Autowired
	private ImageProcessService imageProcessService;

	private final Logger log = LoggerFactory.getLogger(ItemCategoryResource.class);

	private static final String ENTITY_NAME = "itemCategory";


	@Autowired
	private MasterAuditService masterAuditService;

	private final ItemCategoryRepository itemCategoryRepository;

	private final ItemCategorySearchRepository itemCategorySearchRepository;

	public ItemCategoryResource(ItemCategoryRepository itemCategoryRepository,
			ItemCategorySearchRepository itemCategorySearchRepository) {
		this.itemCategoryRepository = itemCategoryRepository;
		this.itemCategorySearchRepository = itemCategorySearchRepository;
	}

	/**
	 * POST /item-categories : Create a new itemCategory.
	 *
	 * @param itemCategory
	 *            the itemCategory to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         itemCategory, or with status 400 (Bad Request) if the itemCategory
	 *         has already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PostMapping("/item-categories")
	@Timed
	public ResponseEntity<ItemCategory> createItemCategory(@Valid @RequestBody ItemCategory itemCategory)
			throws URISyntaxException {
		log.debug("REST request to save ItemCategory : {}", itemCategory);
		if (itemCategory.getId() != null) {
			throw new BadRequestAlertException("A new itemCategory cannot already have an ID", ENTITY_NAME, "idexists");
		}
		if (itemCategory.getItemCategoryIcon() != null) {
			try {
				String url = imageProcessService.onSubmit(itemCategory.getItemCategoryIcon());
				itemCategory.setItemCategoryIconUrl(imageProcessService.urlImageThumb(url));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		ItemCategory result = itemCategoryRepository.save(itemCategory);
		itemCategorySearchRepository.save(result);
		masterAuditService.addItemCategory(result, ENTITY_NAME, UpdateType.CREATE);
		return ResponseEntity.created(new URI("/api/item-categories/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /item-categories : Updates an existing itemCategory.
	 *
	 * @param itemCategory
	 *            the itemCategory to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         itemCategory, or with status 400 (Bad Request) if the itemCategory is
	 *         not valid, or with status 500 (Internal Server Error) if the
	 *         itemCategory couldn't be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
	 */
	@PutMapping("/item-categories")
	@Timed
	public ResponseEntity<ItemCategory> updateItemCategory(@Valid @RequestBody ItemCategory itemCategory)
			throws URISyntaxException {
		log.debug("REST request to update ItemCategory : {}", itemCategory);
		if (itemCategory.getId() == null) {
			return createItemCategory(itemCategory);
		}
		ItemCategory result = itemCategoryRepository.save(itemCategory);
		itemCategorySearchRepository.save(result);
		masterAuditService.addItemCategory(result, ENTITY_NAME, UpdateType.UPDATE);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, itemCategory.getId().toString())).body(result);
	}

	/**
	 * GET /item-categories : get all the itemCategories.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of
	 *         itemCategories in body
	 */
	@GetMapping("/item-categories")
	@Timed
	public ResponseEntity<List<ItemCategory>> getAllItemCategories(Pageable pageable) {
		log.debug("REST request to get all ItemCategories");
		Page<ItemCategory> page = itemCategoryRepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/item-categories");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /item-categories/:id : get the "id" itemCategory.
	 *
	 * @param id
	 *            the id of the itemCategory to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         itemCategory, or with status 404 (Not Found)
	 */
	@GetMapping("/item-categories/{id}")
	@Timed
	public ResponseEntity<ItemCategory> getItemCategory(@PathVariable Long id) {
		log.debug("REST request to get ItemCategory : {}", id);
		ItemCategory itemCategory = itemCategoryRepository.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(itemCategory));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GetMapping("/item-categories/mobile")
	@Timed
	public List<ItemCategory> getAllItemCategoriesMobile() {
		List<ItemCategory> list = new ArrayList();
		for (ItemCategory item : itemCategoryRepository.findAll()) {
			item.setItemCategoryIcon(null);
			list.add(item);
		}
		return list;
	}

	@GetMapping("/item-categories/mobile/{id}")
	@Timed
	public ResponseEntity<ItemCategory> getItemCategoryMobile(@PathVariable Long id) {
		log.debug("REST request to get Country : {}", id);
		ItemCategory item = itemCategoryRepository.findOne(id);
		item.setItemCategoryIcon(null);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(item));
	}

	/**
	 * DELETE /item-categories/:id : delete the "id" itemCategory.
	 *
	 * @param id
	 *            the id of the itemCategory to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/item-categories/{id}")
	@Timed
	public ResponseEntity<Void> deleteItemCategory(@PathVariable Long id) {
		log.debug("REST request to delete ItemCategory : {}", id);
		ItemCategory result = itemCategoryRepository.findOne(id);
		itemCategoryRepository.delete(id);
		itemCategorySearchRepository.delete(id);
		masterAuditService.addItemCategory(result, ENTITY_NAME, UpdateType.DELETE);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * SEARCH /_search/item-categories?query=:query : search for the itemCategory
	 * corresponding to the query.
	 *
	 * @param query
	 *            the query of the itemCategory search
	 * @return the result of the search
	 */
	@GetMapping("/_search/item-categories")
	@Timed
	public ResponseEntity<List<ItemCategory>> searchItemCategories(@RequestParam String query, Pageable pageable) {
		log.debug("REST request to search ItemCategories for query {}", query);
		List<ItemCategory> itemCategory = StreamSupport
				.stream(itemCategorySearchRepository.search(queryStringQuery(query), pageable).spliterator(), false)
				.collect(Collectors.toList());
		Page<ItemCategory> page = new PageImpl<>(itemCategory);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/_search/item-categories");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@GetMapping("/_search/item-categories/mobile")
	@Timed
	public ResponseEntity<List<ItemCategory>> searchItemCategoriesMobile(@RequestParam String query,
			Pageable pageable) {
		log.debug("REST request to search ItemCategories for query {}", query);
		List<ItemCategory> list = new ArrayList<>();
		List<ItemCategory> itemCategory = StreamSupport
				.stream(itemCategorySearchRepository.search(queryStringQuery(query), pageable).spliterator(), false)
				.collect(Collectors.toList());
		for (ItemCategory itemCategory2 : itemCategory) {
			itemCategory2.setItemCategoryIcon(null);
			list.add(itemCategory2);
		}
		Page<ItemCategory> page = new PageImpl<>(list);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/_search/item-categories/mobile");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

}

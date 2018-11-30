package com.cus.jastip.master.repository.search;

import com.cus.jastip.master.domain.ItemSubCategory;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ItemSubCategory entity.
 */
public interface ItemSubCategorySearchRepository extends ElasticsearchRepository<ItemSubCategory, Long> {
}

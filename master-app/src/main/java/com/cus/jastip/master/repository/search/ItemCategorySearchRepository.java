package com.cus.jastip.master.repository.search;

import com.cus.jastip.master.domain.ItemCategory;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ItemCategory entity.
 */
public interface ItemCategorySearchRepository extends ElasticsearchRepository<ItemCategory, Long> {
}

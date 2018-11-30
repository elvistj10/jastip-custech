package com.cus.jastip.master.repository.search;

import com.cus.jastip.master.domain.Updates;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Updates entity.
 */
public interface UpdatesSearchRepository extends ElasticsearchRepository<Updates, Long> {
}

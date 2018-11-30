package com.cus.jastip.master.repository.search;

import com.cus.jastip.master.domain.PostalCode;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the PostalCode entity.
 */
public interface PostalCodeSearchRepository extends ElasticsearchRepository<PostalCode, Long> {
}

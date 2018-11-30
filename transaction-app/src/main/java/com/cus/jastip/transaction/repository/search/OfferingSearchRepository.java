package com.cus.jastip.transaction.repository.search;

import com.cus.jastip.transaction.domain.Offering;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Offering entity.
 */
public interface OfferingSearchRepository extends ElasticsearchRepository<Offering, Long> {
}

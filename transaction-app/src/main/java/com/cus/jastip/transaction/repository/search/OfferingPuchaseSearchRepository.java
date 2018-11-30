package com.cus.jastip.transaction.repository.search;

import com.cus.jastip.transaction.domain.OfferingPuchase;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the OfferingPuchase entity.
 */
public interface OfferingPuchaseSearchRepository extends ElasticsearchRepository<OfferingPuchase, Long> {
}

package com.cus.jastip.transaction.repository.search;

import com.cus.jastip.transaction.domain.OfferingDeliveryService;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the OfferingDeliveryService entity.
 */
public interface OfferingDeliveryServiceSearchRepository extends ElasticsearchRepository<OfferingDeliveryService, Long> {
}

package com.cus.jastip.transaction.repository.search;

import com.cus.jastip.transaction.domain.OfferingDeliveryInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the OfferingDeliveryInfo entity.
 */
public interface OfferingDeliveryInfoSearchRepository extends ElasticsearchRepository<OfferingDeliveryInfo, Long> {
}

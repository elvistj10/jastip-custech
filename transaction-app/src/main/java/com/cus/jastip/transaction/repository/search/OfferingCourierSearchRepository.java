package com.cus.jastip.transaction.repository.search;

import com.cus.jastip.transaction.domain.OfferingCourier;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the OfferingCourier entity.
 */
public interface OfferingCourierSearchRepository extends ElasticsearchRepository<OfferingCourier, Long> {
}

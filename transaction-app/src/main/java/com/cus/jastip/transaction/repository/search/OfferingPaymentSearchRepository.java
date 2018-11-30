package com.cus.jastip.transaction.repository.search;

import com.cus.jastip.transaction.domain.OfferingPayment;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the OfferingPayment entity.
 */
public interface OfferingPaymentSearchRepository extends ElasticsearchRepository<OfferingPayment, Long> {
}

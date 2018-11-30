package com.cus.jastip.payment.repository.search;

import com.cus.jastip.payment.domain.PaymentTransferUnmatched;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the PaymentTransferUnmatched entity.
 */
public interface PaymentTransferUnmatchedSearchRepository extends ElasticsearchRepository<PaymentTransferUnmatched, Long> {
}

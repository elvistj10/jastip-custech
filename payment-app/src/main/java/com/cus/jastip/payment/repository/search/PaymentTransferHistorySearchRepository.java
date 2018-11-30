package com.cus.jastip.payment.repository.search;

import com.cus.jastip.payment.domain.PaymentTransferHistory;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the PaymentTransferHistory entity.
 */
public interface PaymentTransferHistorySearchRepository extends ElasticsearchRepository<PaymentTransferHistory, Long> {
}

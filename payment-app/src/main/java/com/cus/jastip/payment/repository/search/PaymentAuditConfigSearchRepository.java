package com.cus.jastip.payment.repository.search;

import com.cus.jastip.payment.domain.PaymentAuditConfig;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the PaymentAuditConfig entity.
 */
public interface PaymentAuditConfigSearchRepository extends ElasticsearchRepository<PaymentAuditConfig, Long> {
}

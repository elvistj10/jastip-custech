package com.cus.jastip.payment.repository.search;

import com.cus.jastip.payment.domain.PaymentAudit;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the PaymentAudit entity.
 */
public interface PaymentAuditSearchRepository extends ElasticsearchRepository<PaymentAudit, Long> {
}

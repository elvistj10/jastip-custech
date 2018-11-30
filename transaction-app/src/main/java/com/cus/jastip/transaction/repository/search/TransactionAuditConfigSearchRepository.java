package com.cus.jastip.transaction.repository.search;

import com.cus.jastip.transaction.domain.TransactionAuditConfig;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the TransactionAuditConfig entity.
 */
public interface TransactionAuditConfigSearchRepository extends ElasticsearchRepository<TransactionAuditConfig, Long> {
}

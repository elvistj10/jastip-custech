package com.cus.jastip.transaction.repository.search;

import com.cus.jastip.transaction.domain.TransactionAudit;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the TransactionAudit entity.
 */
public interface TransactionAuditSearchRepository extends ElasticsearchRepository<TransactionAudit, Long> {
}

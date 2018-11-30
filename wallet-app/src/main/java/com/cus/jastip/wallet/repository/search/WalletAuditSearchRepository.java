package com.cus.jastip.wallet.repository.search;

import com.cus.jastip.wallet.domain.WalletAudit;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the WalletAudit entity.
 */
public interface WalletAuditSearchRepository extends ElasticsearchRepository<WalletAudit, Long> {
}

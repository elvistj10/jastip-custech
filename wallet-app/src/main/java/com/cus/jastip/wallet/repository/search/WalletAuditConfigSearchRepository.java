package com.cus.jastip.wallet.repository.search;

import com.cus.jastip.wallet.domain.WalletAuditConfig;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the WalletAuditConfig entity.
 */
public interface WalletAuditConfigSearchRepository extends ElasticsearchRepository<WalletAuditConfig, Long> {
}

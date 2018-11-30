package com.cus.jastip.wallet.repository.search;

import com.cus.jastip.wallet.domain.WalletTransaction;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the WalletTransaction entity.
 */
public interface WalletTransactionSearchRepository extends ElasticsearchRepository<WalletTransaction, Long> {
}

package com.cus.jastip.wallet.repository.search;

import com.cus.jastip.wallet.domain.WalletWithdrawal;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the WalletWithdrawal entity.
 */
public interface WalletWithdrawalSearchRepository extends ElasticsearchRepository<WalletWithdrawal, Long> {
}

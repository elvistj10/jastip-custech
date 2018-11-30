package com.cus.jastip.wallet.repository.search;

import com.cus.jastip.wallet.domain.Wallet;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Wallet entity.
 */
public interface WalletSearchRepository extends ElasticsearchRepository<Wallet, Long> {
}

package com.cus.jastip.wallet.repository.search;

import com.cus.jastip.wallet.domain.WithdrawalTransferHistory;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the WithdrawalTransferHistory entity.
 */
public interface WithdrawalTransferHistorySearchRepository extends ElasticsearchRepository<WithdrawalTransferHistory, Long> {
}

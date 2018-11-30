package com.cus.jastip.wallet.repository.search;

import com.cus.jastip.wallet.domain.WithdrawalTransferFailed;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the WithdrawalTransferFailed entity.
 */
public interface WithdrawalTransferFailedSearchRepository extends ElasticsearchRepository<WithdrawalTransferFailed, Long> {
}

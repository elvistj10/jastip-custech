package com.cus.jastip.wallet.repository.search;

import com.cus.jastip.wallet.domain.WithdrawalTransferList;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the WithdrawalTransferList entity.
 */
public interface WithdrawalTransferListSearchRepository extends ElasticsearchRepository<WithdrawalTransferList, Long> {
}

package com.cus.jastip.payment.repository.search;

import com.cus.jastip.payment.domain.PaymentTransferCheckList;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the PaymentTransferCheckList entity.
 */
public interface PaymentTransferCheckListSearchRepository extends ElasticsearchRepository<PaymentTransferCheckList, Long> {
}

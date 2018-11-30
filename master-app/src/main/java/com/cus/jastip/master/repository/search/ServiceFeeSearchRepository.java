package com.cus.jastip.master.repository.search;

import com.cus.jastip.master.domain.ServiceFee;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ServiceFee entity.
 */
public interface ServiceFeeSearchRepository extends ElasticsearchRepository<ServiceFee, Long> {
}

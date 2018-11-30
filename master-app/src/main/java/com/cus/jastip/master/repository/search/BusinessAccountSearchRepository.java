package com.cus.jastip.master.repository.search;

import com.cus.jastip.master.domain.BusinessAccount;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the BusinessAccount entity.
 */
public interface BusinessAccountSearchRepository extends ElasticsearchRepository<BusinessAccount, Long> {
}

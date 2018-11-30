package com.cus.jastip.master.repository.search;

import com.cus.jastip.master.domain.Bank;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Bank entity.
 */
public interface BankSearchRepository extends ElasticsearchRepository<Bank, Long> {
}

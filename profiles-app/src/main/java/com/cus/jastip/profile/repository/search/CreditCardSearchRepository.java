package com.cus.jastip.profile.repository.search;

import com.cus.jastip.profile.domain.CreditCard;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the CreditCard entity.
 */
public interface CreditCardSearchRepository extends ElasticsearchRepository<CreditCard, Long> {
}

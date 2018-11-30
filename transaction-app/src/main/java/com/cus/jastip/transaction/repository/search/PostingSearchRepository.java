package com.cus.jastip.transaction.repository.search;

import com.cus.jastip.transaction.domain.Posting;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Posting entity.
 */
public interface PostingSearchRepository extends ElasticsearchRepository<Posting, Long> {
}

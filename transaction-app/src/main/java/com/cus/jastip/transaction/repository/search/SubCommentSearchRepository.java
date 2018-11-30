package com.cus.jastip.transaction.repository.search;

import com.cus.jastip.transaction.domain.SubComment;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the SubComment entity.
 */
public interface SubCommentSearchRepository extends ElasticsearchRepository<SubComment, Long> {
}

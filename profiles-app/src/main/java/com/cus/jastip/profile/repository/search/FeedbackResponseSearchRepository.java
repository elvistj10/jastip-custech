package com.cus.jastip.profile.repository.search;

import com.cus.jastip.profile.domain.FeedbackResponse;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the FeedbackResponse entity.
 */
public interface FeedbackResponseSearchRepository extends ElasticsearchRepository<FeedbackResponse, Long> {
}

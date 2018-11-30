package com.cus.jastip.profile.repository.search;

import com.cus.jastip.profile.domain.FollowerList;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the FollowerList entity.
 */
public interface FollowerListSearchRepository extends ElasticsearchRepository<FollowerList, Long> {
}

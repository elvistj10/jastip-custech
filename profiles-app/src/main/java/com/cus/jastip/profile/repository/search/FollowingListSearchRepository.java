package com.cus.jastip.profile.repository.search;

import com.cus.jastip.profile.domain.FollowingList;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the FollowingList entity.
 */
public interface FollowingListSearchRepository extends ElasticsearchRepository<FollowingList, Long> {
}

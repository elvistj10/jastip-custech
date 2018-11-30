package com.cus.jastip.profile.repository.search;

import com.cus.jastip.profile.domain.BlockedByList;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the BlockedByList entity.
 */
public interface BlockedByListSearchRepository extends ElasticsearchRepository<BlockedByList, Long> {
}

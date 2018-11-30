package com.cus.jastip.profile.repository.search;

import com.cus.jastip.profile.domain.BlockList;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the BlockList entity.
 */
public interface BlockListSearchRepository extends ElasticsearchRepository<BlockList, Long> {
}

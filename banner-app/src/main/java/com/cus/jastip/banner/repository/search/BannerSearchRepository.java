package com.cus.jastip.banner.repository.search;

import com.cus.jastip.banner.domain.Banner;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Banner entity.
 */
public interface BannerSearchRepository extends ElasticsearchRepository<Banner, Long> {
}

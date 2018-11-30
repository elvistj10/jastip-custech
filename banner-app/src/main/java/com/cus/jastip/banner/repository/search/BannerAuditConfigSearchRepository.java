package com.cus.jastip.banner.repository.search;

import com.cus.jastip.banner.domain.BannerAuditConfig;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the BannerAuditConfig entity.
 */
public interface BannerAuditConfigSearchRepository extends ElasticsearchRepository<BannerAuditConfig, Long> {
}

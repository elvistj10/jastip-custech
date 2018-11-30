package com.cus.jastip.banner.repository.search;

import com.cus.jastip.banner.domain.BannerAudit;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the BannerAudit entity.
 */
public interface BannerAuditSearchRepository extends ElasticsearchRepository<BannerAudit, Long> {
}

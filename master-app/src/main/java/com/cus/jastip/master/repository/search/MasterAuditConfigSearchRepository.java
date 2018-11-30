package com.cus.jastip.master.repository.search;

import com.cus.jastip.master.domain.MasterAuditConfig;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the MasterAuditConfig entity.
 */
public interface MasterAuditConfigSearchRepository extends ElasticsearchRepository<MasterAuditConfig, Long> {
}

package com.cus.jastip.master.repository.search;

import com.cus.jastip.master.domain.MasterAudit;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the MasterAudit entity.
 */
public interface MasterAuditSearchRepository extends ElasticsearchRepository<MasterAudit, Long> {
}

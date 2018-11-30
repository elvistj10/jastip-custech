package com.cus.jastip.profile.repository.search;

import com.cus.jastip.profile.domain.ProfilesAudit;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ProfilesAudit entity.
 */
public interface ProfilesAuditSearchRepository extends ElasticsearchRepository<ProfilesAudit, Long> {
}

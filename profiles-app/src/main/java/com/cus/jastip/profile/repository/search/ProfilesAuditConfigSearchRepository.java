package com.cus.jastip.profile.repository.search;

import com.cus.jastip.profile.domain.ProfilesAuditConfig;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ProfilesAuditConfig entity.
 */
public interface ProfilesAuditConfigSearchRepository extends ElasticsearchRepository<ProfilesAuditConfig, Long> {
}

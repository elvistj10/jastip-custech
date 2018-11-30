package com.cus.jastip.master.repository.search;

import com.cus.jastip.master.domain.PackageSize;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the PackageSize entity.
 */
public interface PackageSizeSearchRepository extends ElasticsearchRepository<PackageSize, Long> {
}

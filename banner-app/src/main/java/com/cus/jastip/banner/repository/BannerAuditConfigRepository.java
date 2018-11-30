package com.cus.jastip.banner.repository;

import com.cus.jastip.banner.domain.BannerAuditConfig;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the BannerAuditConfig entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BannerAuditConfigRepository extends JpaRepository<BannerAuditConfig, Long> {
	
	BannerAuditConfig findByEntityName(String Entity);

}

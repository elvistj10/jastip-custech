package com.cus.jastip.banner.repository;

import com.cus.jastip.banner.domain.BannerAudit;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the BannerAudit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BannerAuditRepository extends JpaRepository<BannerAudit, Long> {

}

package com.cus.jastip.master.repository;

import com.cus.jastip.master.domain.MasterAuditConfig;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the MasterAuditConfig entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MasterAuditConfigRepository extends JpaRepository<MasterAuditConfig, Long> {
	MasterAuditConfig findByEntityName(String entityName);
}

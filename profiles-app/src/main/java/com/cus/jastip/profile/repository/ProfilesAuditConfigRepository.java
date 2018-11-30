package com.cus.jastip.profile.repository;

import com.cus.jastip.profile.domain.ProfilesAuditConfig;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the ProfilesAuditConfig entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProfilesAuditConfigRepository extends JpaRepository<ProfilesAuditConfig, Long> {
	ProfilesAuditConfig findByEntityName(String entityName);
}

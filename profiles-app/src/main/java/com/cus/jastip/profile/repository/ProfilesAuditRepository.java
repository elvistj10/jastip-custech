package com.cus.jastip.profile.repository;

import com.cus.jastip.profile.domain.ProfilesAudit;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ProfilesAudit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProfilesAuditRepository extends JpaRepository<ProfilesAudit, Long> {

}

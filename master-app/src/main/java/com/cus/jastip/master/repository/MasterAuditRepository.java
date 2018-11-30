package com.cus.jastip.master.repository;

import com.cus.jastip.master.domain.MasterAudit;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the MasterAudit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MasterAuditRepository extends JpaRepository<MasterAudit, Long> {

}

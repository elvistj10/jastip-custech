package com.cus.jastip.transaction.repository;

import com.cus.jastip.transaction.domain.TransactionAudit;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the TransactionAudit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransactionAuditRepository extends JpaRepository<TransactionAudit, Long> {

}

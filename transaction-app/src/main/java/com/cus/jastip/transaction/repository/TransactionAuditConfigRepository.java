package com.cus.jastip.transaction.repository;

import com.cus.jastip.transaction.domain.TransactionAuditConfig;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the TransactionAuditConfig entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransactionAuditConfigRepository extends JpaRepository<TransactionAuditConfig, Long> {
	TransactionAuditConfig findByEntityName(String entityName);
}

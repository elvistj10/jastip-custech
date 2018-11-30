package com.cus.jastip.payment.repository;

import com.cus.jastip.payment.domain.PaymentAuditConfig;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the PaymentAuditConfig entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentAuditConfigRepository extends JpaRepository<PaymentAuditConfig, Long> {
	PaymentAuditConfig findByEntityName(String entityName);
}

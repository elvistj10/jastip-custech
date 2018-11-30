package com.cus.jastip.payment.repository;

import com.cus.jastip.payment.domain.PaymentAudit;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the PaymentAudit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentAuditRepository extends JpaRepository<PaymentAudit, Long> {

}

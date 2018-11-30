package com.cus.jastip.payment.repository;

import com.cus.jastip.payment.domain.PaymentTransferHistory;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the PaymentTransferHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentTransferHistoryRepository extends JpaRepository<PaymentTransferHistory, Long> {

}

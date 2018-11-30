package com.cus.jastip.payment.repository;

import com.cus.jastip.payment.domain.PaymentTransferUnmatched;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the PaymentTransferUnmatched entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentTransferUnmatchedRepository extends JpaRepository<PaymentTransferUnmatched, Long> {

}

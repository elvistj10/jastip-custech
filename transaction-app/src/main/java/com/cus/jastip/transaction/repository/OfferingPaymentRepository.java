package com.cus.jastip.transaction.repository;

import com.cus.jastip.transaction.domain.OfferingPayment;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the OfferingPayment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OfferingPaymentRepository extends JpaRepository<OfferingPayment, Long> {

}

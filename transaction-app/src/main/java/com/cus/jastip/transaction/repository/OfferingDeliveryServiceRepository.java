package com.cus.jastip.transaction.repository;

import com.cus.jastip.transaction.domain.OfferingDeliveryService;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the OfferingDeliveryService entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OfferingDeliveryServiceRepository extends JpaRepository<OfferingDeliveryService, Long> {

}

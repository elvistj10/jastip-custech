package com.cus.jastip.transaction.repository;

import com.cus.jastip.transaction.domain.OfferingDeliveryInfo;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the OfferingDeliveryInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OfferingDeliveryInfoRepository extends JpaRepository<OfferingDeliveryInfo, Long> {

}

package com.cus.jastip.transaction.repository;

import com.cus.jastip.transaction.domain.OfferingCourier;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the OfferingCourier entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OfferingCourierRepository extends JpaRepository<OfferingCourier, Long> {

}

package com.cus.jastip.transaction.repository;

import com.cus.jastip.transaction.domain.OfferingPuchase;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the OfferingPuchase entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OfferingPuchaseRepository extends JpaRepository<OfferingPuchase, Long> {

}

package com.cus.jastip.master.repository;

import com.cus.jastip.master.domain.ServiceFee;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ServiceFee entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ServiceFeeRepository extends JpaRepository<ServiceFee, Long> {

}

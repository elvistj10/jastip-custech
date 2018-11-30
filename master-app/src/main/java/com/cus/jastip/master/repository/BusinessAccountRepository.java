package com.cus.jastip.master.repository;

import com.cus.jastip.master.domain.BusinessAccount;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the BusinessAccount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BusinessAccountRepository extends JpaRepository<BusinessAccount, Long> {

}

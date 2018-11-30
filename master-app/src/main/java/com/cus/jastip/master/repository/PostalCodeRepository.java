package com.cus.jastip.master.repository;

import com.cus.jastip.master.domain.PostalCode;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the PostalCode entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PostalCodeRepository extends JpaRepository<PostalCode, Long> {

}

package com.cus.jastip.profile.repository;

import com.cus.jastip.profile.domain.CreditCard;
import com.cus.jastip.profile.domain.Profile;

import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the CreditCard entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {
	List<CreditCard> findByProfile(Profile profile, Pageable pageable);
}

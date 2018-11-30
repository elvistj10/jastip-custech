package com.cus.jastip.profile.repository;

import com.cus.jastip.profile.domain.Address;
import com.cus.jastip.profile.domain.Profile;

import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the Address entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
	List<Address> findByProfile(Profile profile, Pageable pageable);
}

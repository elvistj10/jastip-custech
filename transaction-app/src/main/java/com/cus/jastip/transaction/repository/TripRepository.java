package com.cus.jastip.transaction.repository;

import com.cus.jastip.transaction.domain.Trip;
import com.cus.jastip.transaction.domain.enumeration.TripStatus;

import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the Trip entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
	List<Trip> findByOwnerId(Long id, Pageable pageable);

	List<Trip> findByStatus(TripStatus status, Pageable pageable);

	List<Trip> findByOwnerIdInAndStatusIn(Long id, TripStatus status, Pageable pageable);

	List<Trip> findByOriginCountryIdInAndDestCountryIdIn(Long originCountryId, Long destinationCountryId,
			Pageable pageable);

	@Query("SELECT t from Trip t WHERE (t.status = com.cus.jastip.transaction.domain.enumeration.TripStatus.ONGOING) OR (t.status = com.cus.jastip.transaction.domain.enumeration.TripStatus.UPCOMING)")
	List<Trip> getPostingOngoingOrUpcoming();
}

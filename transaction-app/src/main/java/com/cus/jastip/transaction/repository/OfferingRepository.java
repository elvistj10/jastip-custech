package com.cus.jastip.transaction.repository;

import com.cus.jastip.transaction.domain.Offering;
import com.cus.jastip.transaction.domain.Posting;
import com.cus.jastip.transaction.domain.enumeration.ActorType;
import com.cus.jastip.transaction.domain.enumeration.OfferingStatus;

import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Offering entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OfferingRepository extends JpaRepository<Offering, Long> {
	List<Offering> findByPosting(Posting posting, Pageable pageable);
	List<Offering> findByActorIdInAndActorTypeIn(Long id,ActorType actorType, Pageable pageable);
	List<Offering> findByStatus(OfferingStatus status, Pageable pageable);
	@Query("SELECT o from Offering o WHERE o.status = com.cus.jastip.transaction.domain.enumeration.OfferingStatus.NEW")
	List<Offering> getStatusNew();
}

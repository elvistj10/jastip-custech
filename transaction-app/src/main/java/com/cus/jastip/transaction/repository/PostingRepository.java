package com.cus.jastip.transaction.repository;

import com.cus.jastip.transaction.domain.Posting;
import com.cus.jastip.transaction.domain.enumeration.PostingStatus;
import com.cus.jastip.transaction.domain.enumeration.PostingType;

import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Posting entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PostingRepository extends JpaRepository<Posting, Long> {
	List<Posting> findByOwnerIdInAndTypeIn(Long id,PostingType type, Pageable pageable);
	List<Posting> findByStatusInAndTypeIn(PostingStatus status,PostingType type, Pageable pageable);
	/*@Query("SELECT o from Posting o WHERE o.status <> com.cus.jastip.transaction.domain.enumeration.PostingStatus.EXPIRED")
	List<Posting> getPostingWihoutExpired();*/
	@Query("SELECT p from Posting p WHERE p.status = com.cus.jastip.transaction.domain.enumeration.PostingStatus.OPEN")
	List<Posting> getPostingByStatusOpen();
}  

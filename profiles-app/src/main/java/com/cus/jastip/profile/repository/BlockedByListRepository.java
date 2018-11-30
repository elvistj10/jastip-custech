package com.cus.jastip.profile.repository;

import com.cus.jastip.profile.domain.BlockedByList;
import com.cus.jastip.profile.domain.Profile;

import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the BlockedByList entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BlockedByListRepository extends JpaRepository<BlockedByList, Long> {
	List<BlockedByList> findByProfile(Profile profile, Pageable pageable);

	BlockedByList findByblokerProfileIdInAndProfileIn(Long blokerProfileId, Profile profile);

}

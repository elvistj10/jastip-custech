package com.cus.jastip.profile.repository;

import com.cus.jastip.profile.domain.BlockList;
import com.cus.jastip.profile.domain.Profile;

import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the BlockList entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BlockListRepository extends JpaRepository<BlockList, Long> {
	List<BlockList> findByProfile(Profile profile, Pageable pageable);

	BlockList findByblockedProfileIdInAndProfileIn(Long blockProfileId, Profile profile);

}

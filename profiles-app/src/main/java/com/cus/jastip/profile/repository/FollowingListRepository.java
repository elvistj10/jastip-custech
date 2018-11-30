package com.cus.jastip.profile.repository;

import com.cus.jastip.profile.domain.FollowingList;
import com.cus.jastip.profile.domain.Profile;

import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the FollowingList entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FollowingListRepository extends JpaRepository<FollowingList, Long> {
	FollowingList findByFollowingProfileIdInAndProfileIn(Long followingId, Profile profile);

	List<FollowingList> findByProfile(Profile profile, Pageable pageable);
}

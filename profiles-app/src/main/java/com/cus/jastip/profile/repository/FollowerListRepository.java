package com.cus.jastip.profile.repository;

import com.cus.jastip.profile.domain.FollowerList;
import com.cus.jastip.profile.domain.Profile;

import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the FollowerList entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FollowerListRepository extends JpaRepository<FollowerList, Long> {
	FollowerList findByFollowerProfileIdInAndProfileIn(Long followerId, Profile profile);

	List<FollowerList> findByProfile(Profile profile, Pageable pageable);
}

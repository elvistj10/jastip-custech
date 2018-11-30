package com.cus.jastip.profile.service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cus.jastip.profile.domain.FollowerList;
import com.cus.jastip.profile.domain.FollowingList;
import com.cus.jastip.profile.domain.Profile;
import com.cus.jastip.profile.repository.FollowerListRepository;
import com.cus.jastip.profile.repository.FollowingListRepository;
import com.cus.jastip.profile.repository.ProfileRepository;
import com.cus.jastip.profile.repository.search.FollowerListSearchRepository;
import com.cus.jastip.profile.repository.search.FollowingListSearchRepository;
import com.cus.jastip.profile.repository.search.ProfileSearchRepository;

@Service
public class FollowService {

	@Autowired
	private ProfileRepository profileRepository;

	@Autowired
	private FollowingListRepository followingListRepository;

	@Autowired
	private FollowerListRepository followerListRepository;

	@Autowired
	private ProfileSearchRepository profileSearchRepository;

	@Autowired
	private FollowingListSearchRepository followingListSearchRepository;

	@Autowired
	private FollowerListSearchRepository followerListSearchRepository;

	// FOLLOWING
	public void updateFollowing(FollowingList folList) {
		if (folList.getProfile().getId() != folList.getFollowingProfileId()) {
			Profile profile = profileRepository.findOne(folList.getProfile().getId());
			FollowingList fol = followingListRepository
					.findByFollowingProfileIdInAndProfileIn(folList.getFollowingProfileId(), profile);
			FollowingList followingList = null;
			if (fol == null) {
				followingList = new FollowingList();
				followingList.setFollowingProfileId(folList.getFollowingProfileId());
				followingList.setStatus(true);
				followingList.setFollowingDate(Instant.now());
				followingList.setProfile(profile);
				FollowingList elasticSave = followingListRepository.save(followingList);
				followingListSearchRepository.save(elasticSave);
				updateFollower(folList.getFollowingProfileId(), folList.getProfile().getId(), 0);
				updatefollowingCount(profile);
			} else if (fol.isStatus() == false && folList.isStatus() == true) {
				followingList = fol;
				followingList.setStatus(true);
				followingList.setFollowingDate(Instant.now());
				FollowingList elasticSave = followingListRepository.save(followingList);
				followingListSearchRepository.save(elasticSave);
				updateFollower(folList.getFollowingProfileId(), folList.getProfile().getId(), 1);
				updatefollowingCount(profile);
			} else if (fol.isStatus() == true && folList.isStatus() == false) {
				followingList = fol;
				updateUnFollowing(folList.getProfile().getId(), folList.getFollowingProfileId());
			}
		}

	}

	public void updateFollower(Long profileId, Long followerId, Integer status) {
		Profile profile = profileRepository.findOne(profileId);
		FollowerList followerList = new FollowerList();
		if (status == 1) {
			followerList = followerListRepository.findByFollowerProfileIdInAndProfileIn(followerId, profile);
		} else {
			followerList.setFollowerProfileId(followerId);
			followerList.setProfile(profile);
		}
		followerList.setFollowedDate(Instant.now());
		followerList.setStatus(true);
		FollowerList elasticSave = followerListRepository.save(followerList);
		followerListSearchRepository.save(elasticSave);
		updatefollowerCount(profile);
	}

	public void updatefollowingCount(Profile profile) {
		if (profile.getFollowingCount() != null) {
			profile.setFollowingCount(profile.getFollowingCount() + 1);
		} else {
			profile.setFollowingCount(1);
		}
		Profile elasticSave = profileRepository.save(profile);
		profileSearchRepository.save(elasticSave);
	}

	public void updatefollowerCount(Profile profile) {
		if (profile.getFollowerCount() != null) {
			profile.setFollowerCount(profile.getFollowerCount() + 1);
		} else {
			profile.setFollowerCount(1);
		}
		Profile elasticSave = profileRepository.save(profile);
		profileSearchRepository.save(elasticSave);
	}

	// FOLLOWER
	public void updateUnFollowing(Long profileId, Long followingId) {
		if (profileId != followingId) {
			Profile profileFollower = profileRepository.findOne(profileId);
			Profile profileFollowing = profileRepository.findOne(followingId);
			FollowingList followerList = followingListRepository.findByFollowingProfileIdInAndProfileIn(followingId,
					profileFollower);
			FollowerList followingList = followerListRepository.findByFollowerProfileIdInAndProfileIn(profileId,
					profileFollowing);

			if (followerList.isStatus() == true && followingList.isStatus() == true) {
				followingList.setStatus(false);
				followerList.setStatus(false);
				FollowingList elasticFollowing = followingListRepository.save(followerList);
				FollowerList elasticFollower = followerListRepository.save(followingList);
				followingListSearchRepository.save(elasticFollowing);
				followerListSearchRepository.save(elasticFollower);
				unFollowingCount(profileFollower);
				unFollowerCount(profileFollowing);
			}
		}

	}

	public void unFollowingCount(Profile profile) {
		if (profile.getFollowingCount() != null) {
			profile.setFollowingCount(profile.getFollowingCount() - 1);
			Profile elasticProfile = profileRepository.save(profile);
			profileSearchRepository.save(elasticProfile);
		}
	}

	public void unFollowerCount(Profile profile) {
		if (profile.getFollowerCount() != null) {
			profile.setFollowerCount(profile.getFollowerCount() - 1);
			Profile elasticProfile = profileRepository.save(profile);
			profileSearchRepository.save(elasticProfile);
		}
	}

}

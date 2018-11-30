package com.cus.jastip.profile.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cus.jastip.profile.domain.Profile;
import com.cus.jastip.profile.repository.ProfileRepository;
import com.cus.jastip.profile.repository.search.ProfileSearchRepository;

@Service
public class FeedbackService {

	@Autowired
	private ProfileRepository profileRepository;

	@Autowired
	private ProfileSearchRepository profileSearhRepository;

	private Integer oneStar = 0;
	private Integer twoStar = 0;
	private Integer threeStar = 0;
	private Integer fourStar = 0;
	private Integer fiveStar = 0;
	private Integer oneStarCount = 0;
	private Integer twoStarCount = 0;
	private Integer threeStarCount = 0;
	private Integer fourStarCount = 0;
	private Integer fiveStarCount = 0;

	public void addStarCount(Long profileId, Integer rating) {
		Profile profile = profileRepository.findOne(profileId);
		if (rating == 5) {
			if (profile.getFiveStarCount() != null) {
				profile.setFiveStarCount(profile.getFiveStarCount() + 1);
				profile.setAverageRating(averageFiveStarCount(profileId));
			} else {
				profile.setFiveStarCount(1);
				profile.setAverageRating(averageFiveStarCount(profileId));
			}
		} else if (rating == 4) {
			if (profile.getFourStarCount() != null) {
				profile.setFourStarCount(profile.getFourStarCount() + 1);
				profile.setAverageRating(averageFourStarCount(profileId));
			} else {
				profile.setFourStarCount(1);
				profile.setAverageRating(averageFourStarCount(profileId));
			}
		} else if (rating == 3) {
			if (profile.getThreeStarCount() != null) {
				profile.setThreeStarCount(profile.getThreeStarCount() + 1);
				profile.setAverageRating(averageThreeStarCount(profileId));
			} else {
				profile.setThreeStarCount(1);
				profile.setAverageRating(averageThreeStarCount(profileId));
			}
		} else if (rating == 2) {
			if (profile.getTwoStarCount() != null) {
				profile.setTwoStarCount(profile.getTwoStarCount() + 1);
				profile.setAverageRating(averageTwoStarCount(profileId));
			} else {
				profile.setTwoStarCount(1);
				profile.setAverageRating(averageTwoStarCount(profileId));
			}
		} else {
			if (profile.getOneStarCount() != null) {
				profile.setOneStarCount(profile.getOneStarCount() + 1);
				profile.setAverageRating(averageOneStarCount(profileId));
			} else {
				profile.setOneStarCount(1);
				profile.setAverageRating(averageOneStarCount(profileId));
			}
		}

		Profile elasticSave = profileRepository.save(profile);
		profileSearhRepository.save(elasticSave);

	}

	public Double averageOneStarCount(Long profileId) {
		Profile profile = profileRepository.findOne(profileId);

		if (profile.getOneStarCount() != null) {
			oneStar = (profile.getOneStarCount() + 1);
			oneStarCount = profile.getOneStarCount() + 1;
		} else {
			oneStar = 1;
			oneStarCount = 1;
		}
		if (profile.getTwoStarCount() != null) {
			twoStar = profile.getTwoStarCount() * 2;
			twoStarCount = profile.getTwoStarCount();
		}
		if (profile.getThreeStarCount() != null) {
			threeStar = profile.getThreeStarCount() * 3;
			threeStarCount = profile.getThreeStarCount();
		}
		if (profile.getFourStarCount() != null) {
			fourStar = profile.getFourStarCount() * 4;
			fourStarCount = profile.getFourStarCount();
		}
		if (profile.getFiveStarCount() != null) {
			fiveStar = profile.getFiveStarCount() * 5;
			fiveStarCount = profile.getFiveStarCount();
		}

		Double sum = (double) (oneStar + twoStar + threeStar + fourStar + fiveStar);
		Double n = (double) (oneStarCount + twoStarCount + threeStarCount + fourStarCount + fiveStarCount);
		Double avg = sum / n;
		return avg;
	}

	public Double averageTwoStarCount(Long profileId) {
		Profile profile = profileRepository.findOne(profileId);

		if (profile.getOneStarCount() != null) {
			oneStar = profile.getOneStarCount();
			oneStarCount = profile.getOneStarCount();
		}
		if (profile.getTwoStarCount() != null) {
			twoStar = (profile.getTwoStarCount() + 1) * 2;
			twoStarCount = profile.getTwoStarCount() + 1;
		} else {
			twoStar = 2;
			twoStarCount = 1;
		}
		if (profile.getThreeStarCount() != null) {
			threeStar = profile.getThreeStarCount() * 3;
			threeStarCount = profile.getThreeStarCount();
		}
		if (profile.getFourStarCount() != null) {
			fourStar = profile.getFourStarCount() * 4;
			fourStarCount = profile.getFourStarCount();
		}
		if (profile.getFiveStarCount() != null) {
			fiveStar = profile.getFiveStarCount() * 5;
			fiveStarCount = profile.getFiveStarCount();
		}

		Double sum = (double) (oneStar + twoStar + threeStar + fourStar + fiveStar);
		Double n = (double) (oneStarCount + twoStarCount + threeStarCount + fourStarCount + fiveStarCount);
		Double avg = sum / n;
		return avg;
	}

	public Double averageThreeStarCount(Long profileId) {
		Profile profile = profileRepository.findOne(profileId);

		if (profile.getOneStarCount() != null) {
			oneStar = profile.getOneStarCount();
			oneStarCount = profile.getOneStarCount();
		}
		if (profile.getTwoStarCount() != null) {
			twoStar = profile.getTwoStarCount() * 2;
			twoStarCount = profile.getTwoStarCount();
		}
		if (profile.getThreeStarCount() != null) {
			threeStar = (profile.getThreeStarCount() + 1) * 3;
			threeStarCount = profile.getThreeStarCount() + 1;
		} else {
			threeStar = 3;
			threeStarCount = 1;
		}
		if (profile.getFourStarCount() != null) {
			fourStar = profile.getFourStarCount() * 4;
			fourStarCount = profile.getFourStarCount();
		}
		if (profile.getFiveStarCount() != null) {
			fiveStar = profile.getFiveStarCount() * 5;
			fiveStarCount = profile.getFiveStarCount();
		}

		Double sum = (double) (oneStar + twoStar + threeStar + fourStar + fiveStar);
		Double n = (double) (oneStarCount + twoStarCount + threeStarCount + fourStarCount + fiveStarCount);
		Double avg = sum / n;
		return avg;
	}

	public Double averageFourStarCount(Long profileId) {
		Profile profile = profileRepository.findOne(profileId);

		if (profile.getOneStarCount() != null) {
			oneStar = profile.getOneStarCount();
			oneStarCount = profile.getOneStarCount();
		}
		if (profile.getTwoStarCount() != null) {
			twoStar = profile.getTwoStarCount() * 2;
			twoStarCount = profile.getTwoStarCount();
		}
		if (profile.getThreeStarCount() != null) {
			threeStar = profile.getThreeStarCount() * 3;
			threeStarCount = profile.getThreeStarCount();
		}
		if (profile.getFourStarCount() != null) {
			fourStar = (profile.getFourStarCount() + 1) * 4;
			fourStarCount = profile.getFourStarCount() + 1;
		} else {
			fourStar = 4;
			fourStarCount = 1;
		}
		if (profile.getFiveStarCount() != null) {
			fiveStar = profile.getFiveStarCount() * 5;
			fiveStarCount = profile.getFiveStarCount();
		}

		Double sum = (double) (oneStar + twoStar + threeStar + fourStar + fiveStar);
		Double n = (double) (oneStarCount + twoStarCount + threeStarCount + fourStarCount + fiveStarCount);
		Double avg = sum / n;
		return avg;
	}

	public Double averageFiveStarCount(Long profileId) {
		Profile profile = profileRepository.findOne(profileId);

		if (profile.getOneStarCount() != null) {
			oneStar = profile.getOneStarCount();
			oneStarCount = profile.getOneStarCount();
		}
		if (profile.getTwoStarCount() != null) {
			twoStar = profile.getTwoStarCount() * 2;
			twoStarCount = profile.getTwoStarCount();
		}
		if (profile.getThreeStarCount() != null) {
			threeStar = profile.getThreeStarCount() * 3;
			threeStarCount = profile.getThreeStarCount();
		}
		if (profile.getFourStarCount() != null) {
			fourStar = profile.getFourStarCount() * 4;
			fourStarCount = profile.getFourStarCount();
		}
		if (profile.getFiveStarCount() != null) {
			fiveStar = (profile.getFiveStarCount() + 1) * 5;
			fiveStarCount = profile.getFiveStarCount() + 1;
		} else {
			fiveStar = 5;
			fiveStarCount = 1;
		}

		Double sum = (double) (oneStar + twoStar + threeStar + fourStar + fiveStar);
		Double n = (double) (oneStarCount + twoStarCount + threeStarCount + fourStarCount + fiveStarCount);
		Double avg = sum / n;
		return avg;
	}

}

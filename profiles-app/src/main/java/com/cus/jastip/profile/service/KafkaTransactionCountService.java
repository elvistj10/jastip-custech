package com.cus.jastip.profile.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cus.jastip.profile.domain.Profile;
import com.cus.jastip.profile.domain.enumeration.PostingType;
import com.cus.jastip.profile.messaging.model.TransactionCountMessageModel;
import com.cus.jastip.profile.repository.ProfileRepository;

@Service
public class KafkaTransactionCountService {

	@Autowired
	private ProfileRepository profileRepository;

	public void postingCount(TransactionCountMessageModel message) {
		if (message.getType().equals(PostingType.REQUESTOR)) {
			Profile profile = profileRepository.findOne(message.getProfileId());
			if (profile != null) {
				if (profile.getRequestCount() == null) {
					profile.setRequestCount(1);
				} else {
					profile.setRequestCount(profile.getRequestCount() + 1);
				}
				profileRepository.save(profile);
			}
		} else if (message.getType().equals(PostingType.TRAVELLER)) {
			Profile profile = profileRepository.findOne(message.getProfileId());
			if (profile != null) {
				if (profile.getPreOrderCount() == null) {
					profile.setPreOrderCount(1);
				} else {
					profile.setPreOrderCount(profile.getPreOrderCount() + 1);
				}
				profileRepository.save(profile);
			}
		}
	}

	public void tripCount(TransactionCountMessageModel message) {
		Profile profile = profileRepository.findOne(message.getProfileId());
		if (profile != null) {
			if (profile.getTripCount() == null) {
				profile.setTripCount(1);
			} else {
				profile.setTripCount(profile.getTripCount() + 1);
			}
			profileRepository.save(profile);
		}
	}

	public void offersCount(TransactionCountMessageModel message) {
		Profile profile = profileRepository.findOne(message.getProfileId());
		if (profile != null) {
			if (profile.getOffersCount() == null) {
				profile.setOffersCount(1);
			} else {
				profile.setOffersCount(profile.getOffersCount() + 1);
			}
			profileRepository.save(profile);
		}
	}

}

package com.cus.jastip.profile.messaging;

import java.net.URISyntaxException;
import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.cus.jastip.profile.domain.Profile;
import com.cus.jastip.profile.domain.enumeration.ProfileStatus;
import com.cus.jastip.profile.messaging.model.ProfileMessageModel;
import com.cus.jastip.profile.repository.ProfileRepository;
import com.cus.jastip.profile.web.rest.ProfileResource;

@Service
public class ReceiverProfile {

	private static final Logger LOG = LoggerFactory.getLogger(ReceiverProfile.class);

	@Autowired
	private ProfileResource profileResource;

	@Autowired
	private ProfileRepository profileRepository;

	@KafkaListener(topics = "${app.topic.foo}", containerFactory = "kafkaListenerContainerFactoryProfile")
	public void listen(@Payload ProfileMessageModel message) throws URISyntaxException {
		if (message.getStatus().equals(ProfileStatus.NEW)) {
			Profile profile = new Profile();
			profile.setName(message.getLogin());
			profile.setEmail(message.getLogin());
			profile.setUsername(message.getLogin());
			profile.setUserId(message.getUserId());
			profile.setUserId(message.getUserId());
			profile.setName(message.getFirstname());
			profile.status(message.getStatus());
			profileResource.createProfile(profile);
		} else if (message.getStatus().equals(ProfileStatus.ACTIVE)) {
			Profile profile = profileRepository.findByUsername(message.getLogin());
			profile.setActiveDate(Instant.now());
			profile.setStatus(message.getStatus());
			profileResource.updateProfile(profile);
		}

		LOG.info("received message='{}'", message.getLogin());

	}

}

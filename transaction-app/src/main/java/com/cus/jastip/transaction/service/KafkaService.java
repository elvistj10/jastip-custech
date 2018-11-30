package com.cus.jastip.transaction.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cus.jastip.transaction.domain.Offering;
import com.cus.jastip.transaction.domain.Posting;
import com.cus.jastip.transaction.domain.Trip;
import com.cus.jastip.transaction.domain.enumeration.PostingType;
import com.cus.jastip.transaction.messaging.TransactionCountSender;
import com.cus.jastip.transaction.messaging.model.TransactionCountModel;

@Service
public class KafkaService {

	private static final String ENTITY_POSTING = "posting";
	private static final String ENTITY_TRIP = "trip";
	private static final String ENTITY_OFFERING = "offering";

	@Autowired
	private TransactionCountSender transactionCountSender;

	public void postingCountProcess(Posting result) {
		TransactionCountModel message = new TransactionCountModel();
		if (result.getType().equals(PostingType.TRAVELLER)) {
			message.setProfileId(result.getOwnerId());
			message.setEntity(ENTITY_POSTING);
			message.setType(PostingType.TRAVELLER);
		} else if (result.getType().equals(PostingType.REQUESTOR)) {
			message.setProfileId(result.getOwnerId());
			message.setEntity(ENTITY_POSTING);
			message.setType(PostingType.REQUESTOR);
		}
		transactionCountSender.send(message);
	}

	public void tripCountProcess(Trip result) {
		TransactionCountModel message = new TransactionCountModel();
		message.setProfileId(result.getOwnerId());
		message.setEntity(ENTITY_TRIP);
		transactionCountSender.send(message);
	}

	public void offeringCountProcess(Offering result) {
		TransactionCountModel message = new TransactionCountModel();
		message.setProfileId(result.getActorId());
		message.setEntity(ENTITY_OFFERING);
		transactionCountSender.send(message);
	}
}

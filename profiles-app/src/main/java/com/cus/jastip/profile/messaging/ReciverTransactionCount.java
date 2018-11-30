package com.cus.jastip.profile.messaging;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;


import com.cus.jastip.profile.messaging.model.TransactionCountMessageModel;
import com.cus.jastip.profile.service.KafkaTransactionCountService;

@Service
public class ReciverTransactionCount {

	private static final Logger LOG = LoggerFactory.getLogger(ReciverTransactionCount.class);

	private static final String ENTITY_POSTING = "posting";
	private static final String ENTITY_TRIP = "trip";
	private static final String ENTITY_OFFERING = "offering";

	@Autowired
	private KafkaTransactionCountService kafkaTransactionCountService;

	@KafkaListener(topics = "${app.topic.foo2}", containerFactory = "kafkaListenerContainerFactoryTransactionCount")
	public void listen(@Payload TransactionCountMessageModel message)  {
		if (message.getEntity().equals(ENTITY_POSTING)) {
			kafkaTransactionCountService.postingCount(message);
		} else if (message.getEntity().equals(ENTITY_TRIP)) {
			kafkaTransactionCountService.tripCount(message);
		} else if (message.getEntity().equals(ENTITY_OFFERING)) {
			kafkaTransactionCountService.offersCount(message);
		}

		LOG.info("received message transaction count with profile = ", message.getProfileId(), " and entity = ",
				message.getEntity());

	}

}

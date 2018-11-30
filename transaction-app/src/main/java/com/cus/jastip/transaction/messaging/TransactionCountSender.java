package com.cus.jastip.transaction.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.cus.jastip.transaction.messaging.model.TransactionCountModel;

@Service
public class TransactionCountSender {

	private static final Logger LOG = LoggerFactory.getLogger(TransactionCountSender.class);

	@Autowired
	private KafkaTemplate<String, TransactionCountModel> kafkaProfileTemplate;

	@Value("${app.topic.foo}")
	private String topic;

	public void send(TransactionCountModel message) {
		LOG.info("sending message='{}' to topic='{}'", message, topic);
		kafkaProfileTemplate.send(topic, message);
	}

}

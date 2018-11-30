package com.cus.jastip.transaction.config.kafka;

import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import com.cus.jastip.transaction.messaging.TransactionCountSender;
import com.cus.jastip.transaction.messaging.model.TransactionCountModel;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class SenderConfig {

	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServers;

	// Profile Config
	@Bean
	public Map<String, Object> producerTranscantionCountConfigs() {
		Map<String, Object> props = new HashMap<>();
		props.put("bootstrap.servers", bootstrapServers);
		props.put("key.serializer", StringSerializer.class);
		props.put("value.serializer", TransactionCountSerializer.class);
		return props;
	}

	@Bean
	public ProducerFactory<String, TransactionCountModel> producerTransactionCOuntFactory() {
		return new DefaultKafkaProducerFactory<String, TransactionCountModel>(producerTranscantionCountConfigs());
	}

	@Bean
	public KafkaTemplate<String, TransactionCountModel> kafkaTransactionCountTemplate() {
		return new KafkaTemplate<>(producerTransactionCOuntFactory());
	}

	@Bean
	public TransactionCountSender sender() {
		return new TransactionCountSender();
	}

}

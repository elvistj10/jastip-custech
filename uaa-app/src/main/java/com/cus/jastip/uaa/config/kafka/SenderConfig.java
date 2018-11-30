package com.cus.jastip.uaa.config.kafka;

import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import com.cus.jastip.uaa.messaging.ProfileSender;
import com.cus.jastip.uaa.messaging.model.ProfileMessageModel;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class SenderConfig {

	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServers;

	//Profile Config
	@Bean
	public Map<String, Object> producerProfileConfigs() {
		Map<String, Object> props = new HashMap<>();
		props.put("bootstrap.servers", bootstrapServers);
		props.put("key.serializer", StringSerializer.class);
		props.put("value.serializer", ProfileSerializer.class);
		return props;
	}

	@Bean
	public ProducerFactory<String, ProfileMessageModel> producerProfileFactory() {
		return new DefaultKafkaProducerFactory<String, ProfileMessageModel>(producerProfileConfigs());
	}

	@Bean
	public KafkaTemplate<String, ProfileMessageModel> kafkaProfileTemplate() {
		return new KafkaTemplate<>(producerProfileFactory());
	}

	@Bean
	public ProfileSender sender() {
		return new ProfileSender();
	}

}

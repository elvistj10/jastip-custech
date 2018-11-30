package com.cus.jastip.profile.config.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.cus.jastip.profile.messaging.ReceiverProfile;
import com.cus.jastip.profile.messaging.ReciverTransactionCount;
import com.cus.jastip.profile.messaging.model.ProfileMessageModel;
import com.cus.jastip.profile.messaging.model.TransactionCountMessageModel;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class ReceiverConfig {

	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServers;

	// profile config

	public Map<String, Object> profileConfigs() {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ProfileDeserializer.class);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "profile.t");
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		return props;
	}

	public ConsumerFactory<String, ProfileMessageModel> profileFactory() {
		return new DefaultKafkaConsumerFactory<>(profileConfigs(), new StringDeserializer(),
				new JsonDeserializer<>(ProfileMessageModel.class));
	}

	@Bean("kafkaListenerContainerFactoryProfile")
	public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, ProfileMessageModel>> kafkaListenerContainerFactoryProfile() {
		ConcurrentKafkaListenerContainerFactory<String, ProfileMessageModel> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(profileFactory());
		return factory;
	}

	@Bean
	public ReceiverProfile receiverProfile() {
		return new ReceiverProfile();
	}

	// transaction count config
	
	public Map<String, Object> transactionCountConfigs() {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, TransactionCountDeserializer.class);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "transaction-count.t");
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		return props;
	}

	public ConsumerFactory<String, TransactionCountMessageModel> transactionCountFactory() {
		return new DefaultKafkaConsumerFactory<>(transactionCountConfigs(), new StringDeserializer(),
				new JsonDeserializer<>(TransactionCountMessageModel.class));
	}

	@Bean("kafkaListenerContainerFactoryTransactionCount")
	public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, TransactionCountMessageModel>> kafkaListenerContainerFactoryTransactionCount() {
		ConcurrentKafkaListenerContainerFactory<String, TransactionCountMessageModel> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(transactionCountFactory());
		return factory;
	}
	
	@Bean
	public ReciverTransactionCount reciverTransactionCount() {
		return new ReciverTransactionCount();
	}

}

package com.cus.jastip.transaction.config.kafka;

import java.util.Map;

import org.apache.kafka.common.serialization.Serializer;

import com.cus.jastip.transaction.messaging.model.TransactionCountModel;
import com.fasterxml.jackson.databind.ObjectMapper;


public class TransactionCountSerializer implements Serializer<TransactionCountModel> {

	public byte[] serialize(String arg0, TransactionCountModel obj) {
		byte[] serializedBytes = null;
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			serializedBytes = objectMapper.writeValueAsString(obj).getBytes();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return serializedBytes;
	}

	public void close() {
		// TODO Auto-generated method stub
	}

	public void configure(Map<String, ?> arg0, boolean arg1) {
		// TODO Auto-generated method stub
	}
}

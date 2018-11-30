package com.cus.jastip.profile.config.kafka;

import java.util.Map;

import com.cus.jastip.profile.messaging.model.TransactionCountMessageModel;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TransactionCountDeserializer {
	
	public TransactionCountMessageModel deserialize(String arg0, byte[] devBytes) {
		ObjectMapper mapper = new ObjectMapper();
		TransactionCountMessageModel developer = null;
		try {
			developer = mapper.readValue(devBytes, TransactionCountMessageModel.class);
		} catch (Exception e) {

			e.printStackTrace();
		}
		return developer;
	}

	public void close() {
		// TODO Auto-generated method stub

	}

	public void configure(Map<String, ?> arg0, boolean arg1) {
		// TODO Auto-generated method stub

	}
}

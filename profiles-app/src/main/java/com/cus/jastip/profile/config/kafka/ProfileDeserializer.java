package com.cus.jastip.profile.config.kafka;

import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;
import com.cus.jastip.profile.messaging.model.ProfileMessageModel;

import com.fasterxml.jackson.databind.ObjectMapper;



public class ProfileDeserializer implements Deserializer<ProfileMessageModel> {

	public ProfileMessageModel deserialize(String arg0, byte[] devBytes) {
		ObjectMapper mapper = new ObjectMapper();
		ProfileMessageModel developer = null;
		try {
			developer = mapper.readValue(devBytes, ProfileMessageModel.class);
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

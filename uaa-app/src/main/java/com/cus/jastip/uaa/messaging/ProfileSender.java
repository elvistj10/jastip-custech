package com.cus.jastip.uaa.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;


import com.cus.jastip.uaa.messaging.model.ProfileMessageModel;



@Service
public class ProfileSender {
	
	private static final Logger LOG = LoggerFactory.getLogger(ProfileSender.class);
	
	@Autowired
    private KafkaTemplate<String, ProfileMessageModel> kafkaProfileTemplate;

    @Value("${app.topic.foo}")
    private String topic;

  
    public void send(ProfileMessageModel message){
        LOG.info("sending message='{}' to topic='{}'", message, topic);
        kafkaProfileTemplate.send(topic, message);
    }
  
	

}

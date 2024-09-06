package com.example.gr.kafka;

import com.example.gr.service.TopicSubscriberRegistry;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import com.example.gr.service.EmailService;

import java.io.IOException;
import java.util.Map;

@Service
public class AnimalInfoConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(AnimalInfoConsumer.class);

    @Autowired
    private EmailService emailService;
    @Autowired
    private TopicSubscriberRegistry topicSubscriberRegistry;


    @KafkaListener(topics = "animalGroupId", groupId = "animalGroupId")
    public void listen(ConsumerRecord<String, String> record) throws IOException {

        Map<String, Object> messageData = new ObjectMapper().readValue(record.value(), Map.class);
        String topic = record.topic();

        String templateName = (String) messageData.get("templateName");
        Map<String, Object> params = (Map<String, Object>) messageData.get("templateData");

        String subject = (String) messageData.get("subject");
        topicSubscriberRegistry.getSubscribers(topic)
                .forEach(email -> {
                    params.put("name", email);
                    params.put("unsubscribeLink", "http://localhost:8080/unsubscribe/" + email);
                    emailService.sendSimpleMessage(email, subject, templateName, params);
                });
    }
}

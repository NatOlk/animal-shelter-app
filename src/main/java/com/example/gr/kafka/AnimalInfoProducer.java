package com.example.gr.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AnimalInfoProducer {

    private static final Logger LOG = LoggerFactory.getLogger(AnimalInfoProducer.class);

    private KafkaTemplate<String, String> kafkaTemplate;

    public AnimalInfoProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String subject, String templateName, Map<String, Object> templateData) throws JsonProcessingException {

        Map<String, Object> messageData = new HashMap<>();

        messageData.put("templateName", templateName);
        messageData.put("templateData", templateData);
        messageData.put("subject", subject);
        String messageJson = new ObjectMapper().writeValueAsString(messageData);
        kafkaTemplate.send("animalGroupId", messageJson);
    }
}

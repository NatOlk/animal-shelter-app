package com.example.ansh.notifications;

import com.example.ansh.service.EmailService;
import com.example.ansh.service.TopicSubscriberRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class AnimalInfoConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(AnimalInfoConsumer.class);

    @Autowired
    private EmailService emailService;
    @Autowired
    private TopicSubscriberRegistry topicSubscriberRegistry;

    @Value("${animalGroupTopicId}")
    private String animalGroupTopicId;

    @KafkaListener(topics = "animalGroupId", groupId = "animalGroupId")
    public void listen(ConsumerRecord<String, String> record) throws IOException {

        Map<String, Object> messageData = new ObjectMapper().readValue(record.value(), Map.class);

        String eventType = (String) messageData.get("eventType");
        Map<String, Object> params = (Map<String, Object>) messageData.get("data");

        String subject;
        String templateName;

        switch (eventType) {
            case "addAnimal":
                subject = "New Animal Added";
                templateName = "addAnimalTemplate";
                break;
            case "removeAnimal":
                subject = "Animal Removed";
                templateName = "removeAnimalTemplate";
                break;
            case "updateAnimal":
                subject = "Update Animal";
                templateName = "updateAnimalTemplate";
                break;
            case "addVaccine":
                subject = "New Vaccine Added";
                templateName = "addVaccineTemplate";
                break;
            default:
                LOG.warn("Unknown event type: {}", eventType);
                return;
        }
        topicSubscriberRegistry.getSubscribers("animalGroupId")
                .forEach(email -> {
                    params.put("name", email);
                    //TODO update it
                    params.put("unsubscribeLink", "http://localhost:8081/unsubscribe/" + email);
                    emailService.sendSimpleMessage(email, subject, templateName, params);
                });
    }
}

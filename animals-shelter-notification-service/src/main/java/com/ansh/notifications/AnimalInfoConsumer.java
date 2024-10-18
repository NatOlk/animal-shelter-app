package com.ansh.notifications;

import static com.ansh.notifications.SubscriptionMessages.ADD_ANIMAL_EVENT;
import static com.ansh.notifications.SubscriptionMessages.ADD_VACCINE_EVENT;
import static com.ansh.notifications.SubscriptionMessages.REMOVE_ANIMAL_EVENT;
import static com.ansh.notifications.SubscriptionMessages.REMOVE_VACCINE_EVENT;

import com.ansh.service.EmailService;
import com.ansh.service.TopicSubscriberRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class AnimalInfoConsumer {

  private static final Logger LOG = LoggerFactory.getLogger(AnimalInfoConsumer.class);

  @Autowired
  private EmailService emailService;
  @Autowired
  private TopicSubscriberRegistry topicSubscriberRegistry;

  @Value("${animalGroupTopicId}")
  private String animalGroupTopicId;

  @Value("${animalShelterNotificationApp}")
  private String animalShelterNotificationApp;

  @KafkaListener(topics = "animalGroupId", groupId = "animalGroupId")
  public void listen(ConsumerRecord<String, String> record) throws IOException {

    Map<String, Object> messageData = new ObjectMapper().readValue(record.value(), Map.class);

    String eventType = (String) messageData.get("eventType");
    Map<String, Object> params = (Map<String, Object>) messageData.get("data");

    String subject;
    String templateName;

    switch (eventType) {
      case ADD_ANIMAL_EVENT:
        subject = "New Animal Added";
        templateName = "addAnimalTemplate";
        break;
      case REMOVE_ANIMAL_EVENT:
        subject = "Animal Removed";
        templateName = "removeAnimalTemplate";
        break;
      case ADD_VACCINE_EVENT:
        subject = "New Vaccine Added";
        templateName = "addVaccineTemplate";
        break;
      case REMOVE_VACCINE_EVENT:
        subject = "Vaccine Removed";
        templateName = "removeVaccineTemplate";
        break;
      default:
        LOG.warn("Unknown event type: {}", eventType);
        return;
    }
    topicSubscriberRegistry.getSubscribers("animalGroupId")
        .forEach(email -> {
          params.put("name", email);
          params.put("unsubscribeLink", animalShelterNotificationApp
              + "/unsubscribe/" + email);
          emailService.sendSimpleMessage(email, subject, templateName, params);
        });
  }
}

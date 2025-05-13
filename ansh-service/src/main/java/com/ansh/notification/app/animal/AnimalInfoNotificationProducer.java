package com.ansh.notification.app.animal;

import com.ansh.event.AnimalShelterTopic;
import com.ansh.notification.app.AbstractNotificationProducer;
import com.ansh.notification.app.AnimalShelterNotificationProducer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class AnimalInfoNotificationProducer extends AbstractNotificationProducer
    implements AnimalShelterNotificationProducer {

  private static final Logger LOG = LoggerFactory.getLogger(AnimalInfoNotificationProducer.class);

  public AnimalInfoNotificationProducer(KafkaTemplate<String, String> kafkaTemplate,
      ObjectMapper objectMapper) {
    super(kafkaTemplate, objectMapper);
  }

  @Override
  public String getTopicId() {
    return AnimalShelterTopic.ANIMAL_INFO.getTopicName();
  }

  @Override
  protected String getLogPrefix() {
    return "[animal info topic]";
  }
}

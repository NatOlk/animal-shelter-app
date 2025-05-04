package com.ansh.notification.app.vaccination;

import com.ansh.event.AnimalShelterTopic;
import com.ansh.notification.app.AbstractNotificationProducer;
import com.ansh.notification.app.AnimalShelterNotificationProducer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class VaccinationInfoNotificationProducer extends AbstractNotificationProducer
    implements AnimalShelterNotificationProducer {

  public VaccinationInfoNotificationProducer(KafkaTemplate<String, String> kafkaTemplate,
      ObjectMapper objectMapper) {
    super(kafkaTemplate, objectMapper);
  }

  @Override
  public String getTopicId() {
    return AnimalShelterTopic.VACCINATION_INFO.getTopicName();
  }

  @Override
  protected String getLogPrefix() {
    return "[vaccination topic]";
  }
}
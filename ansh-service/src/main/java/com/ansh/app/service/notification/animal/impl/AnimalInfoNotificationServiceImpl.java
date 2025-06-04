package com.ansh.app.service.notification.animal.impl;

import static com.ansh.event.AnimalEventKeys.BASE_URL_METADATA;

import com.ansh.app.service.notification.animal.AnimalInfoNotificationService;
import com.ansh.entity.animal.Animal;
import com.ansh.entity.animal.Vaccination;
import com.ansh.event.AnimalShelterEvent;
import com.ansh.event.AnimalShelterTopic;
import com.ansh.event.animal.AddAnimalEvent;
import com.ansh.event.animal.RemoveAnimalEvent;
import com.ansh.event.news.AnimalShelterNewsEvent;
import com.ansh.event.vaccination.AddVaccinationEvent;
import com.ansh.event.vaccination.RemoveVaccinationEvent;
import com.ansh.notification.strategy.NotificationProducerStrategy;
import com.ansh.utils.BaseUrlProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnimalInfoNotificationServiceImpl implements AnimalInfoNotificationService {

  private static final Logger LOG = LoggerFactory.getLogger(
      AnimalInfoNotificationServiceImpl.class);

  @Autowired
  private BaseUrlProvider baseUrlProvider;

  @Autowired
  private NotificationProducerStrategy notificationProducerStrategy;

  @Override
  public void sendAddAnimalMessage(Animal animal) {
    AnimalShelterEvent plainEvent = new AddAnimalEvent(animal);
    AnimalShelterNewsEvent withMessageEvent = new AnimalShelterNewsEvent();
    withMessageEvent.setMessage("Hallo! New animal was added to our shelter");

    sendNotification(plainEvent, AnimalShelterTopic.ANIMAL_SHELTER_NEWS.getTopicName());
    sendNotification(plainEvent, AnimalShelterTopic.ANIMAL_INFO.getTopicName());
  }

  @Override
  public void sendRemoveAnimalMessage(Animal animal, String reason) {
    AnimalShelterEvent event = new RemoveAnimalEvent(animal, reason);
    sendNotification(event, AnimalShelterTopic.ANIMAL_INFO.getTopicName());
  }

  @Override
  public void sendAddVaccinationMessage(Vaccination vaccination) {
    AnimalShelterEvent event = new AddVaccinationEvent(vaccination.getAnimal(), vaccination);
    sendNotification(event, AnimalShelterTopic.VACCINATION_INFO.getTopicName());
  }

  @Override
  public void sendRemoveVaccinationMessage(Vaccination vaccination) {
    AnimalShelterEvent event = new RemoveVaccinationEvent(vaccination.getAnimal(), vaccination);
    sendNotification(event, AnimalShelterTopic.VACCINATION_INFO.getTopicName());
  }

  private void sendNotification(AnimalShelterEvent event, String topicId) {
    event.addMetadata(BASE_URL_METADATA.getKey(), baseUrlProvider.getBaseUrl());
    notificationProducerStrategy.getServiceByTopic(topicId)
        .ifPresentOrElse(
            producer -> producer.sendNotification(event),
            () -> LOG.warn(STR."No producer found for topic: \{topicId}")
        );
  }
}

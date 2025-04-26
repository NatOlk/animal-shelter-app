package com.ansh.service.impl;

import com.ansh.service.SubscriberRegistryService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Qualifier("animalShelterNewsSubscriber")
public class AnimalShelterNewsTopicSubscriberRegistryServiceImpl extends AbstractSubscriberRegistryService
    implements SubscriberRegistryService {

  @Value("${animalShelterNewsTopicId}")
  private String animalShelterNewsTopicId;

  @Override
  public String getTopicId() {
    return animalShelterNewsTopicId;
  }

  protected void setAnimalShelterNewsTopicId(String animalShelterNewsTopicId) {
    this.animalShelterNewsTopicId = animalShelterNewsTopicId;
  }
}

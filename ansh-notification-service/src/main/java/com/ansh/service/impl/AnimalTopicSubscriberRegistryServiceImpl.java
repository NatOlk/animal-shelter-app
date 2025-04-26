package com.ansh.service.impl;

import com.ansh.service.SubscriberRegistryService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Qualifier("animalTopicSubscriber")
public class AnimalTopicSubscriberRegistryServiceImpl extends AbstractSubscriberRegistryService
    implements SubscriberRegistryService {

  @Value("${animalTopicId}")
  private String animalTopicId;

  @Override
  public String getTopicId() {
    return animalTopicId;
  }

  protected void setAnimalTopicId(String animalTopicId) {
    this.animalTopicId = animalTopicId;
  }
}

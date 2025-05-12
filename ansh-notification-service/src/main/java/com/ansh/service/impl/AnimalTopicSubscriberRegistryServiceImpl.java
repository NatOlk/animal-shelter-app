package com.ansh.service.impl;

import com.ansh.event.AnimalShelterTopic;
import com.ansh.service.SubscriberRegistryService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("animalTopicSubscriber")
public class AnimalTopicSubscriberRegistryServiceImpl extends AbstractSubscriberRegistryService
    implements SubscriberRegistryService {

  @Override
  public String getTopicId() {
    return AnimalShelterTopic.ANIMAL_INFO.getTopicName();
  }

  @Override
  protected boolean isAutoAccept() {
    return true;
  }
}

package com.ansh.service.impl;

import com.ansh.event.AnimalShelterTopic;
import com.ansh.service.SubscriberRegistryService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("vaccinationTopicSubscriber")
public class VaccinationTopicSubscriberRegistryServiceImpl extends AbstractSubscriberRegistryService
    implements SubscriberRegistryService {

  @Override
  public String getTopicId() {
    return AnimalShelterTopic.VACCINATION_INFO.getTopicName();
  }

  protected boolean isAutoAccept() {
    return true;
  }
}

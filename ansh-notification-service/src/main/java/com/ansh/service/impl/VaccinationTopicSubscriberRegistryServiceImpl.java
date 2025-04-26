package com.ansh.service.impl;

import com.ansh.service.SubscriberRegistryService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Qualifier("vaccinationTopicSubscriber")
public class VaccinationTopicSubscriberRegistryServiceImpl extends AbstractSubscriberRegistryService
    implements SubscriberRegistryService {

  @Value("${vaccinationTopicId}")
  private String vaccinationTopicId;

  @Override
  public String getTopicId() {
    return vaccinationTopicId;
  }

  protected void setVaccinationTopicId(String vaccinationTopicId) {
    this.vaccinationTopicId = vaccinationTopicId;
  }
}

package com.ansh.facade.impl;

import com.ansh.entity.subscription.Subscription;
import com.ansh.facade.SubscriptionFacade;
import com.ansh.service.SubscriberRegistryService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionFacadeImpl implements SubscriptionFacade {

  @Autowired
  private List<SubscriberRegistryService> subscriberRegistryServices;

  @Override
  public List<Subscription> getAllSubscriptionByApprover(String approver) {
    List<Subscription> allSubscriptions = new ArrayList<>();

    for (SubscriberRegistryService service : subscriberRegistryServices) {
      allSubscriptions.addAll(service.getAllSubscriptions(approver));
    }

    return allSubscriptions;
  }
}

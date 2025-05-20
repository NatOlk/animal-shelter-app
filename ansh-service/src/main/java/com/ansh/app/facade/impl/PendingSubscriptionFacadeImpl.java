package com.ansh.app.facade.impl;

import com.ansh.app.facade.PendingSubscriptionFacade;
import com.ansh.notification.strategy.PendingSubscriptionServiceStrategy;
import com.ansh.repository.entity.PendingSubscriber;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PendingSubscriptionFacadeImpl implements PendingSubscriptionFacade {

  @Autowired
  private PendingSubscriptionServiceStrategy pendingSubscriptionServiceStrategy;

  @Override
  public void approveSubscriber(String topic, String email, String approver) {
    var service = pendingSubscriptionServiceStrategy.getServiceByTopic(topic)
        .orElseThrow(() -> new IllegalArgumentException(
            STR."No service found for topic: \{topic}"));

    service.approveSubscriber(email, approver);
  }

  @Override
  public void rejectSubscriber(String topic, String email, String approver) {
    var service = pendingSubscriptionServiceStrategy.getServiceByTopic(topic)
        .orElseThrow(() -> new IllegalArgumentException(
            STR."No service found for topic: \{topic}"));

    service.rejectSubscriber(email, approver);
  }

  @Override
  public List<PendingSubscriber> getSubscribersByApprover(String account) {
    return pendingSubscriptionServiceStrategy.getAllServices().stream()
        .flatMap(service -> service.getSubscribersByApprover(account).stream())
        .toList();
  }

  @Override
  public List<PendingSubscriber> getPendingSubscribersWithoutApprover() {
    return pendingSubscriptionServiceStrategy.getAllServices().stream()
        .flatMap(service -> service.getPendingSubscribersWithoutApprover().stream())
        .toList();
  }
}

package com.ansh.app.facade.impl;

import com.ansh.app.facade.PendingSubscriptionFacade;
import com.ansh.app.service.notification.subscription.PendingSubscriptionService;
import com.ansh.notification.strategy.PendingSubscriptionServiceStrategy;
import com.ansh.repository.entity.PendingSubscriber;
import com.ansh.utils.IdentifierMasker;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PendingSubscriptionFacadeImpl implements PendingSubscriptionFacade {

  private static final Logger LOG = LoggerFactory.getLogger(PendingSubscriptionFacadeImpl.class);
  @Autowired
  private PendingSubscriptionServiceStrategy pendingSubscriptionServiceStrategy;

  @Override
  public void saveSubscriber(String topic, String email, String approver) {
    getServiceOrThrow(topic).saveSubscriber(email, approver);
    if (LOG.isDebugEnabled()) {
      LOG.debug("[{} subscription] Successfully processed subscription for {} by {}",
          topic, IdentifierMasker.maskEmail(email), IdentifierMasker.maskEmail(approver));
    }
  }

  @Override
  public void approveSubscriber(String topic, String email, String approver) {
    getServiceOrThrow(topic).approveSubscriber(email, approver);
  }

  @Override
  public void rejectSubscriber(String topic, String email, String approver) {
    getServiceOrThrow(topic).rejectSubscriber(email, approver);
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

  private PendingSubscriptionService getServiceOrThrow(String topic) {
    return pendingSubscriptionServiceStrategy.getServiceByTopic(topic)
        .orElseThrow(() -> new IllegalArgumentException(
            STR."No service found for topic: \{topic}"));
  }
}

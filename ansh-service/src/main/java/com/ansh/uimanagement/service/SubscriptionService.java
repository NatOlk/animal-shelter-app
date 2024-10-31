package com.ansh.uimanagement.service;

import com.ansh.notification.AnimalNotificationUserSubscribedProducer;
import com.ansh.repository.PendingSubscriberRepository;
import com.ansh.repository.entity.PendingSubscriber;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionService {

  @Autowired
  private AnimalNotificationUserSubscribedProducer animalNotificationUserSubscribedProducer;

  @Autowired
  private PendingSubscriberRepository pendingSubscriberRepository;

  public void savePendingSubscriber(String email, String approver) {
    PendingSubscriber pendingSubscriber = new PendingSubscriber();
    pendingSubscriber.setEmail(email);
    pendingSubscriber.setApprover(approver);
    pendingSubscriberRepository.save(pendingSubscriber);
  }

  @Transactional
  public void approveSubscriber(String email) {
    PendingSubscriber subscriber = pendingSubscriberRepository.findByEmail(email).orElse(null);

    animalNotificationUserSubscribedProducer.sendApprove(subscriber.getEmail(),
        subscriber.getApprover());
    pendingSubscriberRepository.deleteByEmail(email);
  }

  public List<PendingSubscriber> getPendingSubscribers() {
    return pendingSubscriberRepository.findAll();
  }
}

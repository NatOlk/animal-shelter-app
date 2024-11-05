package com.ansh.uimanagement.service;

import static com.ansh.entity.animal.UserProfile.AnimalNotificationSubscriptionStatus.PENDING;

import com.ansh.auth.service.UserProfileService;
import com.ansh.entity.subscription.Subscription;
import com.ansh.notification.AnimalNotificationUserSubscribedProducer;
import com.ansh.repository.PendingSubscriberRepository;
import com.ansh.repository.entity.PendingSubscriber;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AnimalTopicSubscriptionService {

  @Value("${animalTopicId}")
  private String animalTopicId;

  @Autowired
  private AnimalNotificationUserSubscribedProducer animalNotificationUserSubscribedProducer;

  @Autowired
  private PendingSubscriberRepository pendingSubscriberRepository;
  @Autowired
  private UserProfileService userProfileService;


  @Transactional
  public void approveSubscriber(String email, String approver) {
    pendingSubscriberRepository.findByEmailAndTopic(email, animalTopicId)
        .ifPresent(subscriber -> {
          subscriber.setApprover(approver);
          animalNotificationUserSubscribedProducer.sendApprove(subscriber.getEmail(),
              subscriber.getApprover(), subscriber.getTopic());
          pendingSubscriberRepository.deleteByEmailAndTopic(email, animalTopicId);
        });
  }

  @Transactional
  public void rejectSubscriber(String email) {
    pendingSubscriberRepository.deleteByEmailAndTopic(email, animalTopicId);
  }
}

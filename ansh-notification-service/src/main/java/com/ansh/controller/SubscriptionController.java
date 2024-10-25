package com.ansh.controller;

import com.ansh.repository.entity.Subscription;
import com.ansh.service.TopicSubscriberRegistryService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SubscriptionController {

  private static final Logger LOG = LoggerFactory.getLogger(SubscriptionController.class);

  @Autowired
  private TopicSubscriberRegistryService topicSubscriberRegistry;

  @PostMapping("/animal-notify-subscribe")
  public void subscribe(@RequestBody String email) {
    email = email.replace("\"", "");
    topicSubscriberRegistry.registerSubscriber(email);
  }

  @GetMapping("/animal-notify-unsubscribe/{token}")
  public String unsubscribe(@PathVariable String token) {
    topicSubscriberRegistry.unregisterSubscriber(token);

    return "Subscription with token " + token + " removed";
  }

  @GetMapping("/animal-notify-subscribes")
  public List<Subscription> subscribers() {
    return topicSubscriberRegistry.getSubscribers();
  }

  @GetMapping("/animal-notify-all-subscribes")
  public List<Subscription> allSubscribers() {
    return topicSubscriberRegistry.getAllSubscribers();
  }

  @GetMapping("/animal-notify-subscribe-check/{token}")
  public String checkSubscription(@PathVariable String token) {
    boolean isAccepted = topicSubscriberRegistry.confirmSubscription(token);

    return "Subscription with token " + token + " is " + (isAccepted ? "valid" : "invalid");
  }
}

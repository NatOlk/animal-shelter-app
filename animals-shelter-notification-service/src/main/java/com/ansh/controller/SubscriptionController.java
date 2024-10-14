package com.ansh.controller;

import com.ansh.service.SubscriptionService;
import com.ansh.service.TopicSubscriberRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SubscriptionController {
    private static final Logger LOG = LoggerFactory.getLogger(SubscriptionController.class);
    @Value("${animalGroupTopicId}")
    private String animalGroupTopicId;
    @Autowired
    private TopicSubscriberRegistry topicSubscriberRegistry;
    @Autowired
    private SubscriptionService subscriptionService;

    @PostMapping("/subscribe")
    public void subscribe(@RequestBody String email) {
        email = email.replace("\"", "");
        topicSubscriberRegistry.registerSubscriber(animalGroupTopicId, email);
    }

    @PostMapping("/unsubscribe/{email}")
    public void unsubscribe(@PathVariable String email) {
        email = email.replace("\"", "");
        topicSubscriberRegistry.unregisterSubscriber(animalGroupTopicId, email);
    }


    @GetMapping("/subscribers")
    public List<String> subscribers() {
        return topicSubscriberRegistry.getSubscribers(animalGroupTopicId);
    }

    @GetMapping("/checkSubscription/{token}")
    public String checkSubscription(@PathVariable String token) {
        boolean isAccepted = topicSubscriberRegistry.confirmSubscription(token);

        return "Subscription with token " + token + " is " + (isAccepted ? "valid" : "invalid");
    }
}

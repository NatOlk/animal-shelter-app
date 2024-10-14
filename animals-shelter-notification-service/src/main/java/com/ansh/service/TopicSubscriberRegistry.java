package com.ansh.service;


import com.ansh.repository.SubscriptionRepository;
import com.ansh.repository.data.Subscription;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TopicSubscriberRegistry {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private EmailService emailService;

    @Transactional
    public void registerSubscriber(String topic, String email) {
        Subscription sb = new Subscription();
        sb.setTopic(topic);
        sb.setEmail(email);
        sb.setAccepted(false);
        subscriptionRepository.save(sb);

        subscriptionService.sendTokenConfirmation(email);
    }

    @Transactional
    public void unregisterSubscriber(String topic, String email) {
        subscriptionRepository.removeAllByEmailAndTopic(email, topic);
    }

    public List<String> getSubscribers(String topic) {
        return subscriptionRepository.getAllSubscriptionsByTopic(topic)
                .stream()
                .filter(Subscription::isAccepted)
                .map(Subscription::getEmail)
                .collect(Collectors.toList());
    }

    public boolean confirmSubscription(String token) {
        String email = subscriptionService.confirmSubscription(token);
        if (email != null) {
            List<Subscription> subscriptions = subscriptionRepository.getSubscriptionByEmail(email);

            subscriptions.forEach(subscription -> {
                subscription.setAccepted(true);
                subscriptionRepository.save(subscription);
            });
            return true;
        }
        return false;
    }

}

package com.example.ansh.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.example.ansh.notifications.SubscriptionMessages.SUBSCRIPTION_SUBJECT;

@Service
public class SubscriptionService {

    private final Map<String, String> tokenMap = new HashMap<>();

    @Autowired
    private EmailService emailService;

    public String confirmSubscription(String token) {
        return tokenMap.get(token);
    }
    public void sendTokenConfirmation(String email) {
        if (tokenMap.get(email) == null) {
            String token = generateConfirmationToken(email);
            Map<String, Object> params = new HashMap<>(2);
            params.put("name", email);
            //TODO: change it
            params.put("confirmationLink", "http://localhost:8081/checkSubscription/" + token);
            params.put("subscriptionLink", "http://localhost:8081/checkSubscription/" + token);
            emailService.sendSimpleMessage(email, SUBSCRIPTION_SUBJECT,
                    "acceptSubscription", params);
        }
    }

    public String generateConfirmationToken(String email) {
        String token = UUID.randomUUID().toString();
        tokenMap.put(token, email);
        return token;
    }
}

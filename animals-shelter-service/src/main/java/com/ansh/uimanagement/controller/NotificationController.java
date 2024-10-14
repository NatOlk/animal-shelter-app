package com.ansh.uimanagement.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {
    @Value("${animal-shelter-notification-app}")
    private String notificationAppUrl;

    @GetMapping("/notification-url")
    public String getNotificationAppUrl() {
        return notificationAppUrl;
    }
}

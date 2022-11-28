package org.fastlink.userservice.notif.controller;


import lombok.AllArgsConstructor;
import org.fastlink.userservice.notif.model.SubNotification;
import org.fastlink.userservice.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller @AllArgsConstructor
public class NotifController
{

    private final NotificationService notificationService;

    @GetMapping("/user/notification")
    public ResponseEntity<List<SubNotification>> getAllUnreadNotifications(Long userId)
    {
        return ResponseEntity.ok(notificationService.getAllUnreadNotifications(userId));
    }
}

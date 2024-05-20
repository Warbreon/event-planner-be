package com.cognizant.EventPlanner.controller;

import com.cognizant.EventPlanner.services.AttendeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class NotificationController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final AttendeeService attendeeService;

    @MessageMapping("/notify")
    public void notifyEventCreator(String email) {
        int activeNotifications = attendeeService.countActiveNotifications(email);
        simpMessagingTemplate.convertAndSendToUser(email, "/queue/notifications", activeNotifications);
    }
}

package com.cyberguardian.cyberguardian_service.service.impl;

import com.cyberguardian.cyberguardian_service.dto.alert.AlertMessage;
import com.cyberguardian.cyberguardian_service.service.AlertingService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlertingServiceImpl implements AlertingService {

    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void sendCriticalAlert(AlertMessage message) {
        // Diffusion vers tous les abonnés au topic "/topic/alerts"
        messagingTemplate.convertAndSend("/topic/alerts", message);
    }
}

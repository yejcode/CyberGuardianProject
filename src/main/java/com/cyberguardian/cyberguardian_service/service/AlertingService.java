package com.cyberguardian.cyberguardian_service.service;

import com.cyberguardian.cyberguardian_service.dto.alert.AlertMessage;

public interface AlertingService {
    void sendCriticalAlert(AlertMessage message);
}

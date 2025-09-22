package com.cyberguardian.cyberguardian_service.controller;

import com.cyberguardian.cyberguardian_service.entity.RequestLog;
import com.cyberguardian.cyberguardian_service.service.RequestLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
public class RequestsController {

    private final RequestLogService logService;

    @GetMapping("/latest")
    public List<RequestLog> latest() {
        // Retourne les 100 derniers logs
        return logService.latest(100);
    }
}

package com.cyberguardian.cyberguardian_service.controller;

import com.cyberguardian.cyberguardian_service.entity.RequestLog;
import com.cyberguardian.cyberguardian_service.entity.enums.AttackType;
import com.cyberguardian.cyberguardian_service.service.RequestLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
public class RequestsController {

    private final RequestLogService logService;

    @GetMapping("/latest")
    public List<RequestLog> latest(@RequestParam(defaultValue = "100") int limit) {
        return logService.latest(limit);
    }

    @GetMapping("/malicious")
    public List<RequestLog> malicious(@RequestParam(defaultValue = "100") int limit) {
        return logService.latestMalicious(limit);
    }

    @GetMapping("/blocked")
    public List<RequestLog> blocked(@RequestParam(defaultValue = "100") int limit) {
        return logService.latestBlocked(limit);
    }

    @GetMapping("/attack/{type}")
    public List<RequestLog> byAttack(@PathVariable AttackType type,
                                     @RequestParam(defaultValue = "100") int limit) {
        return logService.latestByAttack(type, limit);
    }

    @GetMapping("/stats")
    public Map<String, Object> stats() {
        return logService.getStats();
    }
}

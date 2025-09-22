package com.cyberguardian.cyberguardian_service.service;

import com.cyberguardian.cyberguardian_service.engine.RequestData;
import com.cyberguardian.cyberguardian_service.engine.RuleAnalysisResult;
import com.cyberguardian.cyberguardian_service.entity.RequestLog;
import com.cyberguardian.cyberguardian_service.entity.enums.AttackType;

import java.util.List;
import java.util.Map;

public interface RequestLogService {
    RequestLog logRequest(RequestData data, RuleAnalysisResult result, long processingTimeMs);
    List<RequestLog> latest(int limit);
    List<RequestLog> latestMalicious(int limit);
    List<RequestLog> latestBlocked(int limit);
    List<RequestLog> latestByAttack(AttackType attackType, int limit);

    Map<String, Object> getStats();
}

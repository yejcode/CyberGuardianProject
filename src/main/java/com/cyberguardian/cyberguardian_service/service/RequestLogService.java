package com.cyberguardian.cyberguardian_service.service;

import com.cyberguardian.cyberguardian_service.engine.RequestData;
import com.cyberguardian.cyberguardian_service.engine.RuleAnalysisResult;
import com.cyberguardian.cyberguardian_service.entity.RequestLog;

import java.util.List;

public interface RequestLogService {
    RequestLog logRequest(RequestData data, RuleAnalysisResult result, long processingTimeMs);
    List<RequestLog> latest(int limit);
}

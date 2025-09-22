package com.cyberguardian.cyberguardian_service.service;

import com.cyberguardian.cyberguardian_service.engine.RequestData;
import com.cyberguardian.cyberguardian_service.engine.RuleAnalysisResult;

public interface RuleEngineService {
    RuleAnalysisResult analyzeRequest(RequestData data);
}

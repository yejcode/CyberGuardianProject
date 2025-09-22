package com.cyberguardian.cyberguardian_service.service.impl;

import com.cyberguardian.cyberguardian_service.engine.RequestData;
import com.cyberguardian.cyberguardian_service.engine.RuleAnalysisResult;
import com.cyberguardian.cyberguardian_service.entity.RequestLog;
import com.cyberguardian.cyberguardian_service.repository.RequestLogRepository;
import com.cyberguardian.cyberguardian_service.service.RequestLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestLogServiceImpl implements RequestLogService {

    private final RequestLogRepository repo;

    @Override
    public RequestLog logRequest(RequestData data, RuleAnalysisResult result, long processingTimeMs) {
        RequestLog log = RequestLog.builder()
                .timestamp(LocalDateTime.now())
                .sourceIp(data.sourceIp())
                .httpMethod(data.method())
                .endpoint(data.endpoint())
                .headers(data.headers() != null ? data.headers().toString() : null)
                .bodyHash(data.bodyHash())
                .userAgent(data.userAgent())
                .isMalicious(result.malicious())
                .attackType(result.attackType())
                .ruleMatched(result.ruleMatched())
                .blocked(result.malicious())
                .processingTimeMs(processingTimeMs)
                .build();

        return repo.save(log);
    }

    @Override
    public List<RequestLog> latest(int limit) {
        return repo.findTop100ByOrderByIdDesc(); // TODO: limiter réellement
    }
}

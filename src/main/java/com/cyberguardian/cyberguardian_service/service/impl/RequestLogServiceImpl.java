package com.cyberguardian.cyberguardian_service.service.impl;

import com.cyberguardian.cyberguardian_service.engine.RequestData;
import com.cyberguardian.cyberguardian_service.engine.RuleAnalysisResult;
import com.cyberguardian.cyberguardian_service.entity.RequestLog;
import com.cyberguardian.cyberguardian_service.entity.enums.AttackType;
import com.cyberguardian.cyberguardian_service.repository.RequestLogRepository;
import com.cyberguardian.cyberguardian_service.service.RequestLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private PageRequest page(int limit) {
        int size = Math.max(1, Math.min(limit, 1000));
        return PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public List<RequestLog> latest(int limit) {
        return repo.findAll(page(limit)).getContent();
    }

    @Override
    public List<RequestLog> latestMalicious(int limit) {
        return repo.findByIsMaliciousTrue(page(limit)).getContent();
    }

    @Override
    public List<RequestLog> latestBlocked(int limit) {
        return repo.findByBlockedTrue(page(limit)).getContent();
    }

    @Override
    public List<RequestLog> latestByAttack(AttackType attackType, int limit) {
        return repo.findByAttackType(attackType, page(limit)).getContent();
    }

    @Override
    public Map<String, Object> getStats() {
        long total = repo.count();
        long malicious = repo.countByIsMaliciousTrue();

        Map<String, Long> distribution = new HashMap<>();
        for (Object[] row : repo.countByAttackType()) {
            distribution.put(row[0] == null ? "OTHER" : row[0].toString(), (Long) row[1]);
        }

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalRequests", total);
        stats.put("maliciousRequests", malicious);
        stats.put("maliciousRate", total > 0 ? (malicious * 100.0 / total) : 0.0);
        stats.put("attackDistribution", distribution);
        return stats;
    }
}

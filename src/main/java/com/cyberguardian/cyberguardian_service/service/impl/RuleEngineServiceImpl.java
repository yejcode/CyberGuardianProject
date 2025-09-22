package com.cyberguardian.cyberguardian_service.service.impl;

import com.cyberguardian.cyberguardian_service.engine.RequestData;
import com.cyberguardian.cyberguardian_service.engine.RuleAnalysisResult;
import com.cyberguardian.cyberguardian_service.entity.SecurityRule;
import com.cyberguardian.cyberguardian_service.entity.enums.AttackType;
import com.cyberguardian.cyberguardian_service.repository.SecurityRuleRepository;
import com.cyberguardian.cyberguardian_service.service.RuleEngineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

@Service
@RequiredArgsConstructor
public class RuleEngineServiceImpl implements RuleEngineService {

    private final SecurityRuleRepository repo;

    @Override
    public RuleAnalysisResult analyzeRequest(RequestData data) {
        List<SecurityRule> rules = repo.findByEnabledTrue();

        StringBuilder haystackBuilder = new StringBuilder();

        if (data.query() != null) {
            String decodedQuery = URLDecoder.decode(data.query(), StandardCharsets.UTF_8);
            haystackBuilder.append(decodedQuery).append(" ");
        }

        if (data.endpoint() != null) {
            haystackBuilder.append(data.endpoint()).append(" ");
        }

        // 🔹 Décodage UTF-8 pour rawBody
        if (data.rawBody() != null) {
            String decodedBody = URLDecoder.decode(data.rawBody(), StandardCharsets.UTF_8);
            haystackBuilder.append(decodedBody).append(" ");
        }

        if (data.bodyHash() != null) {
            haystackBuilder.append(data.bodyHash()).append(" ");
        }

        if (data.userAgent() != null) {
            haystackBuilder.append(data.userAgent()).append(" ");
        }

        String haystack = haystackBuilder.toString();

        for (SecurityRule r : rules) {
            try {
                Pattern pattern = Pattern.compile(r.getPattern(), Pattern.CASE_INSENSITIVE);
                if (pattern.matcher(haystack).find()) {
                    return new RuleAnalysisResult(true, r.getAttackType(), r.getName());
                }
            } catch (PatternSyntaxException e) {
                // Regex invalide en DB → on ignore cette règle
            }
        }
        return new RuleAnalysisResult(false, AttackType.OTHER, null);
    }
}
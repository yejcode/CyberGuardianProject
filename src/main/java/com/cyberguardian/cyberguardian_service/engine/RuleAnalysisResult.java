package com.cyberguardian.cyberguardian_service.engine;

import com.cyberguardian.cyberguardian_service.entity.enums.AttackType;

public record RuleAnalysisResult(
        boolean malicious,
        AttackType attackType,
        String ruleMatched
) {
}

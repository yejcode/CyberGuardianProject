package com.cyberguardian.cyberguardian_service.dto.rule;

import com.cyberguardian.cyberguardian_service.entity.enums.AttackType;
import com.cyberguardian.cyberguardian_service.entity.enums.Severity;
import lombok.Data;

@Data
public class UpdateSecurityRuleRequest {
    private String name;
    private String description;
    private String pattern;
    private AttackType attackType;
    private Severity severity;
    private Boolean enabled;
}

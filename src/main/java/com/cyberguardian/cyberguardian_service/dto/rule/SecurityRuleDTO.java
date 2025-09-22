package com.cyberguardian.cyberguardian_service.dto.rule;

import com.cyberguardian.cyberguardian_service.entity.enums.AttackType;
import com.cyberguardian.cyberguardian_service.entity.enums.Severity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SecurityRuleDTO {
    private Long id;
    private String name;
    private String description;
    private String pattern;
    private AttackType attackType;
    private Severity severity;
    private Boolean enabled;
}

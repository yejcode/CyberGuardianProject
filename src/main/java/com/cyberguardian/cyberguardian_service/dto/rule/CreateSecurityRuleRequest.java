package com.cyberguardian.cyberguardian_service.dto.rule;

import com.cyberguardian.cyberguardian_service.entity.enums.AttackType;
import com.cyberguardian.cyberguardian_service.entity.enums.Severity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class CreateSecurityRuleRequest {
    @NotBlank
    private String name;

    private String description;

    @NotBlank
    private String pattern;

    @NotNull
    private AttackType attackType;

    @NotNull
    private Severity severity;

    @NotNull
    private Boolean enabled;

}
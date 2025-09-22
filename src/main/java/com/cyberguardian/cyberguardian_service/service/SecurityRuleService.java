package com.cyberguardian.cyberguardian_service.service;

import com.cyberguardian.cyberguardian_service.dto.rule.CreateSecurityRuleRequest;
import com.cyberguardian.cyberguardian_service.dto.rule.SecurityRuleDTO;
import com.cyberguardian.cyberguardian_service.dto.rule.UpdateSecurityRuleRequest;

import java.util.List;

public interface SecurityRuleService {
    List<SecurityRuleDTO> findAll();
    SecurityRuleDTO findById(Long id);
    SecurityRuleDTO create(CreateSecurityRuleRequest request);
    SecurityRuleDTO update(Long id, UpdateSecurityRuleRequest request);
    void delete(Long id);

}

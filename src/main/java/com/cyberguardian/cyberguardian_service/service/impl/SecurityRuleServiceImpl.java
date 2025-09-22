package com.cyberguardian.cyberguardian_service.service.impl;

import com.cyberguardian.cyberguardian_service.dto.rule.CreateSecurityRuleRequest;
import com.cyberguardian.cyberguardian_service.dto.rule.SecurityRuleDTO;
import com.cyberguardian.cyberguardian_service.dto.rule.UpdateSecurityRuleRequest;
import com.cyberguardian.cyberguardian_service.entity.SecurityRule;
import com.cyberguardian.cyberguardian_service.repository.SecurityRuleRepository;
import com.cyberguardian.cyberguardian_service.service.SecurityRuleService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SecurityRuleServiceImpl implements SecurityRuleService {

    private final SecurityRuleRepository repo;

    @Override
    public List<SecurityRuleDTO> findAll() {
        return repo.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public SecurityRuleDTO findById(Long id) {
        SecurityRule rule = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rule not found with id " + id));
        return toDto(rule);
    }

    @Override
    public SecurityRuleDTO create(CreateSecurityRuleRequest req) {
        SecurityRule rule = SecurityRule.builder()
                .name(req.getName())
                .description(req.getDescription())
                .pattern(req.getPattern())
                .attackType(req.getAttackType())
                .severity(req.getSeverity())
                .enabled(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        return toDto(repo.save(rule));
    }

    @Override
    public SecurityRuleDTO update(Long id, UpdateSecurityRuleRequest req) {
        SecurityRule rule = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rule not found with id " + id));

        if (req.getName() != null) rule.setName(req.getName());
        if (req.getDescription() != null) rule.setDescription(req.getDescription());
        if (req.getPattern() != null) rule.setPattern(req.getPattern());
        if (req.getAttackType() != null) rule.setAttackType(req.getAttackType());
        if (req.getSeverity() != null) rule.setSeverity(req.getSeverity());
        if (req.getEnabled() != null) rule.setEnabled(req.getEnabled());

        rule.setUpdatedAt(LocalDateTime.now());
        return toDto(repo.save(rule));
    }

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new EntityNotFoundException("Rule not found with id " + id);
        }
        repo.deleteById(id);
    }

    private SecurityRuleDTO toDto(SecurityRule r) {
        return new SecurityRuleDTO(
                r.getId(),
                r.getName(),
                r.getDescription(),
                r.getPattern(),
                r.getAttackType(),
                r.getSeverity(),
                r.getEnabled()
        );
    }
}

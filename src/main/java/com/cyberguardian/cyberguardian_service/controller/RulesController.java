package com.cyberguardian.cyberguardian_service.controller;

import com.cyberguardian.cyberguardian_service.dto.rule.CreateSecurityRuleRequest;
import com.cyberguardian.cyberguardian_service.dto.rule.SecurityRuleDTO;
import com.cyberguardian.cyberguardian_service.dto.rule.UpdateSecurityRuleRequest;
import com.cyberguardian.cyberguardian_service.service.SecurityRuleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rules")
@RequiredArgsConstructor
public class RulesController {

    private final SecurityRuleService service;

    @GetMapping
    public List<SecurityRuleDTO> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SecurityRuleDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<SecurityRuleDTO> create(@Valid @RequestBody CreateSecurityRuleRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SecurityRuleDTO> update(@PathVariable Long id,
                                                  @RequestBody UpdateSecurityRuleRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

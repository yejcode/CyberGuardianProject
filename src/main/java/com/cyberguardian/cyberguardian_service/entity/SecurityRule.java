package com.cyberguardian.cyberguardian_service.entity;

import com.cyberguardian.cyberguardian_service.entity.enums.AttackType;
import com.cyberguardian.cyberguardian_service.entity.enums.Severity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "security_rules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SecurityRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name; // ex: "SQL Injection Rule"

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String pattern; // regex à tester sur la requête

    @Enumerated(EnumType.STRING)
    private AttackType attackType;

    @Enumerated(EnumType.STRING)
    private Severity severity;

    private Boolean enabled;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

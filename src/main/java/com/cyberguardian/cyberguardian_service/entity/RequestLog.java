package com.cyberguardian.cyberguardian_service.entity;

import com.cyberguardian.cyberguardian_service.entity.enums.AttackType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "request_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime timestamp;

    private String sourceIp;
    private String httpMethod;
    private String endpoint;

    @Column(columnDefinition = "TEXT")
    private String headers;
    private String bodyHash;
    private String userAgent;

    private Boolean isMalicious;
    @Enumerated(EnumType.STRING)
    private AttackType attackType;
    private String ruleMatched;

    private Boolean blocked;
    private Long processingTimeMs;
}

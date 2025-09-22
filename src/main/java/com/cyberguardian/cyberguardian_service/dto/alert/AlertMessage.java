package com.cyberguardian.cyberguardian_service.dto.alert;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AlertMessage {
    private String type;      // ex: "Blocked"
    private String rule;      // ex: "SQLi-basic"
    private String attackType;// ex: "SQL_INJECTION"
    private String endpoint;  // ex: "/api/requests/latest"
    private String sourceIp;  // ex: "127.0.0.1"
}
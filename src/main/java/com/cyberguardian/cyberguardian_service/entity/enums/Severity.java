package com.cyberguardian.cyberguardian_service.entity.enums;

public enum Severity {
    LOW,
    MEDIUM,
    HIGH,
    CRITICAL;

    public boolean isCriticalOrHigh() {
        return this == HIGH || this == CRITICAL;
    }
}

package com.cyberguardian.cyberguardian_service.engine;

import java.util.Map;

public record RequestData(
        String method,
        String endpoint,
        String bodyHash,
        String sourceIp,
        String userAgent,
        Map<String, String> headers,
        Long contentLength,
        String query,
        String rawBody
) {}

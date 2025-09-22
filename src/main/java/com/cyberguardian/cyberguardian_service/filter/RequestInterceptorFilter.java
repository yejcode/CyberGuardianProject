package com.cyberguardian.cyberguardian_service.filter;

import com.cyberguardian.cyberguardian_service.dto.alert.AlertMessage;
import com.cyberguardian.cyberguardian_service.engine.RequestData;
import com.cyberguardian.cyberguardian_service.engine.RuleAnalysisResult;
import com.cyberguardian.cyberguardian_service.repository.SecurityRuleRepository;
import com.cyberguardian.cyberguardian_service.service.AlertingService;
import com.cyberguardian.cyberguardian_service.service.RequestLogService;
import com.cyberguardian.cyberguardian_service.service.RuleEngineService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.apache.commons.codec.digest.DigestUtils;


import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class RequestInterceptorFilter extends OncePerRequestFilter {

    private final RuleEngineService ruleEngine;
    private final RequestLogService logService;
    private final AlertingService alertingService;
    private final SecurityRuleRepository ruleRepo;

    @Override

    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        long start = System.currentTimeMillis();
        String body = new String(request.getInputStream().readAllBytes(), StandardCharsets.UTF_8);


        // Lire le body → dans cette version simple on ne lit pas le flux complet
        String bodyHash = body.isEmpty() ? null : DigestUtils.sha256Hex(body);


        Map<String, String> headers = Collections.list(request.getHeaderNames())
                .stream()
                .collect(Collectors.toMap(h -> h, request::getHeader));

        RequestData data = new RequestData(
                request.getMethod(),                // method
                request.getRequestURI(),            // endpoint
                bodyHash,                           // bodyHash
                request.getRemoteAddr(),            // sourceIp
                request.getHeader("User-Agent"),    // userAgent
                headers,                            // headers
                request.getContentLengthLong(),     // contentLength
                request.getQueryString(),           // query
                body

        );

        // Analyse par le moteur de règles
        RuleAnalysisResult result = ruleEngine.analyzeRequest(data);

        log.info("=== REQUEST ANALYSIS ===");
        log.info("Endpoint: {}", data.endpoint());
        log.info("Method: {}", data.method());
        log.info("Is Malicious: {}", result.malicious());
        if (result.malicious()) {
            log.info("Rule Matched: {}", result.ruleMatched());
            log.info("Attack Type: {}", result.attackType());
            log.info("Body Hash: {}", data.bodyHash());
        }
        log.info("========================");

        // Loguer la requête
        logService.logRequest(data, result, System.currentTimeMillis() - start);

        // Bloquer si malicieuse
        if (result.malicious()) {
            // ✅ Vérifier sévérité de la règle
            ruleRepo.findByName(result.ruleMatched()).ifPresent(rule -> {
                if (rule.getSeverity().isCriticalOrHigh()) { // à implémenter dans enum Severity
                    AlertMessage msg = new AlertMessage(
                            "Blocked",
                            result.ruleMatched(),
                            result.attackType().name(),
                            data.endpoint(),
                            data.sourceIp()
                    );
                    alertingService.sendCriticalAlert(msg);
                }
            });
            // ✅ Réponse JSON claire
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            String json = String.format(
                    "{ \"error\": \"Blocked by WAF\", \"rule\": \"%s\", \"attackType\": \"%s\" }",
                    result.ruleMatched(), result.attackType()
            );
            response.getWriter().write(json);
            return;
        }


        // sinon → laisser passer
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return  path.startsWith("/swagger-ui")
                || path.startsWith("/api/auth")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/ws-alerts");
    }
}

package com.cyberguardian.cyberguardian_service.service.impl;

import com.cyberguardian.cyberguardian_service.engine.RequestData;
import com.cyberguardian.cyberguardian_service.engine.RuleAnalysisResult;
import com.cyberguardian.cyberguardian_service.entity.SecurityRule;
import com.cyberguardian.cyberguardian_service.entity.enums.AttackType;
import com.cyberguardian.cyberguardian_service.repository.SecurityRuleRepository;
import com.cyberguardian.cyberguardian_service.service.RuleEngineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

@Service
@RequiredArgsConstructor
public class RuleEngineServiceImpl implements RuleEngineService {

//    private final SecurityRuleRepository repo;
//
//    @Override
//    public RuleAnalysisResult analyzeRequest(RequestData data) {
//        List<SecurityRule> rules = repo.findByEnabledTrue();
//
//        StringBuilder haystackBuilder = new StringBuilder();
//
//        if (data.query() != null) {
//            String decodedQuery = URLDecoder.decode(data.query(), StandardCharsets.UTF_8);
//            haystackBuilder.append(decodedQuery).append(" ");
//        }
//
//        if (data.endpoint() != null) {
//            haystackBuilder.append(data.endpoint()).append(" ");
//        }
//
//        // 🔹 Décodage UTF-8 pour rawBody
//        if (data.rawBody() != null) {
//            String decodedBody = URLDecoder.decode(data.rawBody(), StandardCharsets.UTF_8);
//            haystackBuilder.append(decodedBody).append(" ");
//        }
//
//        if (data.bodyHash() != null) {
//            haystackBuilder.append(data.bodyHash()).append(" ");
//        }
//
//        if (data.userAgent() != null) {
//            haystackBuilder.append(data.userAgent()).append(" ");
//        }
//
//        String haystack = haystackBuilder.toString();
//
////        for (SecurityRule r : rules) {
////            try {
////                Pattern pattern = Pattern.compile(r.getPattern(), Pattern.CASE_INSENSITIVE);
////                if (pattern.matcher(haystack).find()) {
////                    return new RuleAnalysisResult(true, r.getAttackType(), r.getName());
////                }
////            } catch (PatternSyntaxException e) {
////                // Regex invalide en DB → on ignore cette règle
////            }
////        }
////        return new RuleAnalysisResult(false, AttackType.OTHER, null);
//        List<SecurityRule> matched = rules.stream().filter(r -> {
//            try {
//                // Cas spécial : ACCESS_CONTROL → matcher uniquement sur l’endpoint
//                if (r.getAttackType() == AttackType.ACCESS_CONTROL) {
//                    if (data.endpoint() == null) return false;
//                    return Pattern.compile(r.getPattern(), Pattern.CASE_INSENSITIVE)
//                            .matcher(data.endpoint()).find();
//                }
//
//                // Cas spécial : SECURITY_MISCONFIGURATION → vérifier uniquement les headers
//                if (r.getAttackType() == AttackType.SECURITY_MISCONFIGURATION) {
//                    if (data.headers() == null) return false;
//                    String headers = data.headers().toString().toLowerCase();
//                    // Vérifie si des en-têtes essentiels sont manquants
//                    boolean missing =
//                            !headers.contains("x-frame-options") ||
//                                    !headers.contains("content-security-policy") ||
//                                    !headers.contains("x-content-type-options");
//                    return missing;
//                }
//
//                // Cas normal → matcher sur le haystack global
//                return Pattern.compile(r.getPattern(), Pattern.CASE_INSENSITIVE)
//                        .matcher(haystack).find();
//
//            } catch (PatternSyntaxException e) {
//                return false;
//            }
//        }).toList();
//
//        if (matched.isEmpty()) return new RuleAnalysisResult(false, AttackType.OTHER, null);
//
//        SecurityRule mostSevere = matched.stream()
//                .max(java.util.Comparator.comparingInt(r -> severityLevel(String.valueOf(r.getSeverity()))))
//                .get();
//
//        return new RuleAnalysisResult(true, mostSevere.getAttackType(), mostSevere.getName());
//
//    }
//
//    private int severityLevel(String s) {
//        if (s == null) return 0;
//        s = s.toUpperCase();
//        return switch (s) {
//            case "CRITICAL" -> 4;
//            case "HIGH" -> 3;
//            case "MEDIUM" -> 2;
//            case "LOW" -> 1;
//            default -> 0;
//        };

private final SecurityRuleRepository repo;

    @Override
    public RuleAnalysisResult analyzeRequest(RequestData data) {
        List<SecurityRule> rules = repo.findByEnabledTrue()
                .stream()
                .filter(SecurityRule::isEnabled) // ✅ sécurité supplémentaire
                .toList();

        // Construire le haystack
        StringBuilder haystackBuilder = new StringBuilder();

        if (data.query() != null) {
            haystackBuilder.append(URLDecoder.decode(data.query(), StandardCharsets.UTF_8)).append(" ");
        }
        if (data.endpoint() != null) {
            haystackBuilder.append(data.endpoint()).append(" ");
        }
        if (data.rawBody() != null) {
            haystackBuilder.append(URLDecoder.decode(data.rawBody(), StandardCharsets.UTF_8)).append(" ");
        }
        if (data.bodyHash() != null) {
            haystackBuilder.append(data.bodyHash()).append(" ");
        }
        if (data.userAgent() != null) {
            haystackBuilder.append(data.userAgent()).append(" ");
        }

        String haystack = haystackBuilder.toString();

        // DEBUG: Afficher le haystack
        System.out.println("HAYSTACK: '" + haystack + "'");

        // 🔥 ÉTAPE 1: Parcours des règles CRITIQUES et HIGH en premier
        List<SecurityRule> matched = rules.stream()
                .filter(r -> {
                    // Exclure SECURITY_MISCONFIGURATION du traitement principal
                    if (r.getAttackType() == AttackType.SECURITY_MISCONFIGURATION) {
                        return false;
                    }

                    try {
                        // Cas spécial pour ACCESS_CONTROL: matcher seulement sur l'endpoint
                        if (r.getAttackType() == AttackType.ACCESS_CONTROL) {
                            if (data.endpoint() == null) return false;
                            return Pattern.compile(r.getPattern(), Pattern.CASE_INSENSITIVE)
                                    .matcher(data.endpoint()).find();
                        }

                        // Cas spécial : PROTOCOL_ATTACK → matcher uniquement sur les headers
                        if (r.getAttackType() == AttackType.PROTOCOL_ATTACK) {
                            if (data.headers() == null) return false;
                            String headers = data.headers().toString().toLowerCase();
                            return Pattern.compile(r.getPattern(), Pattern.CASE_INSENSITIVE)
                                    .matcher(headers).find();
                        }

                        // Cas normal: matcher sur le haystack global
                        return Pattern.compile(r.getPattern(), Pattern.CASE_INSENSITIVE)
                                .matcher(haystack).find();

                    } catch (PatternSyntaxException e) {
                        return false;
                    }
                })
                .toList();

        // 🎯 ÉTAPE 2: Si des règles matchent, retourner la plus sévère
        if (!matched.isEmpty()) {
            SecurityRule mostSevere = matched.stream()
                    .max(java.util.Comparator.comparingInt(r -> severityLevel(String.valueOf(r.getSeverity()))))
                    .get();

            return new RuleAnalysisResult(true, mostSevere.getAttackType(), mostSevere.getName());
        }

        // 🔸 ÉTAPE 3: SEULEMENT si aucune autre règle ne matche, vérifier Security Headers
        boolean misconfigDetected = checkSecurityHeaders(data);
        if (misconfigDetected) {
            return new RuleAnalysisResult(true, AttackType.SECURITY_MISCONFIGURATION, "Security-Headers-Check");
        }

        // 🔹 ÉTAPE 4: Aucune menace détectée
        return new RuleAnalysisResult(false, AttackType.OTHER, null);
    }

    /**
     * Vérifie si les headers essentiels de sécurité sont présents.
     */
    private boolean checkSecurityHeaders(RequestData data) {
        if (data.headers() == null) return false;

        String headers = data.headers().toString().toLowerCase();

        boolean hasXFO = headers.contains("x-frame-options");
        boolean hasCSP = headers.contains("content-security-policy");
        boolean hasXCTO = headers.contains("x-content-type-options");

        // On considère "misconfiguration" seulement si les 3 sont absents
        return !(hasXFO || hasCSP || hasXCTO);
    }

    private int severityLevel(String s) {
        if (s == null) return 0;
        return switch (s.toUpperCase()) {
            case "CRITICAL" -> 4;
            case "HIGH" -> 3;
            case "MEDIUM" -> 2;
            case "LOW" -> 1;
            default -> 0;
        };
    }
}
package com.cyberguardian.cyberguardian_service.repository;

import com.cyberguardian.cyberguardian_service.entity.RequestLog;
import com.cyberguardian.cyberguardian_service.entity.enums.AttackType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestLogRepository extends JpaRepository<RequestLog, Long> {
    List<RequestLog> findTop100ByOrderByIdDesc();

    List<RequestLog> findTop100ByIsMaliciousTrueOrderByIdDesc();

    List<RequestLog> findTop100ByBlockedTrueOrderByIdDesc();

    List<RequestLog> findTop100ByAttackTypeOrderByIdDesc(AttackType attackType);

    @Query("SELECT r.attackType, COUNT(r) FROM RequestLog r GROUP BY r.attackType")
    List<Object[]> countByAttackType();

    @Query("SELECT COUNT(r) FROM RequestLog r WHERE r.isMalicious = true")
    long countMalicious();

    @Query("SELECT COUNT(r) FROM RequestLog r")
    long countAll();
}
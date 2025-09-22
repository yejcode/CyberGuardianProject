package com.cyberguardian.cyberguardian_service.repository;

import com.cyberguardian.cyberguardian_service.entity.RequestLog;
import com.cyberguardian.cyberguardian_service.entity.enums.AttackType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestLogRepository extends JpaRepository<RequestLog, Long> {
    List<RequestLog> findTop100ByOrderByIdDesc();

    Page<RequestLog> findAll(Pageable pageable);

    Page<RequestLog> findByIsMaliciousTrue(Pageable pageable);

    Page<RequestLog> findByBlockedTrue(Pageable pageable);

    Page<RequestLog> findByAttackType(AttackType attackType, Pageable pageable);

    @Query("SELECT r.attackType, COUNT(r) FROM RequestLog r GROUP BY r.attackType")
    java.util.List<Object[]> countByAttackType();

    long countByIsMaliciousTrue();
}
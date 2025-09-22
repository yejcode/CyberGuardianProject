package com.cyberguardian.cyberguardian_service.repository;

import com.cyberguardian.cyberguardian_service.entity.SecurityRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SecurityRuleRepository extends JpaRepository<SecurityRule, Long> {

    Optional<SecurityRule> findByName(String name);

    List<SecurityRule> findByEnabledTrue();
}

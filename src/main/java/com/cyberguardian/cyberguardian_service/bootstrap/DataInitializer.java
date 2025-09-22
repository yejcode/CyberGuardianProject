package com.cyberguardian.cyberguardian_service.bootstrap;

import com.cyberguardian.cyberguardian_service.entity.User;
import com.cyberguardian.cyberguardian_service.entity.enums.Role;
import com.cyberguardian.cyberguardian_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.findByEmail("admin@local").isEmpty()) {
            User admin = User.builder()
                    .email("admin@local")
                    .password(passwordEncoder.encode("admin123"))
                    .role(Role.ADMIN)
                    .active(true)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            userRepository.save(admin);
            System.out.println("✅ Admin user created: admin@local / admin123");
        }
    }
}

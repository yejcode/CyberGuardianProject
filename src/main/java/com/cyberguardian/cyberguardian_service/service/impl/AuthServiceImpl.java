package com.cyberguardian.cyberguardian_service.service.impl;

import com.cyberguardian.cyberguardian_service.dto.auth.JwtResponse;
import com.cyberguardian.cyberguardian_service.dto.auth.LoginRequest;
import com.cyberguardian.cyberguardian_service.dto.auth.UserProfileDTO;
import com.cyberguardian.cyberguardian_service.entity.User;
import com.cyberguardian.cyberguardian_service.repository.UserRepository;
import com.cyberguardian.cyberguardian_service.security.JwtService;
import com.cyberguardian.cyberguardian_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public JwtResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + request.getEmail()));

        if (!Boolean.TRUE.equals(user.getActive())) {
            throw new BadCredentialsException("User is disabled");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        String token = jwtService.generate(user.getEmail());
        return new JwtResponse(token);
    }

    @Override
    public UserProfileDTO me(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        return new UserProfileDTO(
                user.getId(),
                user.getEmail(),
                user.getRole().name(),
                user.getActive()
        );
    }
}

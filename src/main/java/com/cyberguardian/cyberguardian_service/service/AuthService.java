package com.cyberguardian.cyberguardian_service.service;

import com.cyberguardian.cyberguardian_service.dto.auth.JwtResponse;
import com.cyberguardian.cyberguardian_service.dto.auth.LoginRequest;
import com.cyberguardian.cyberguardian_service.dto.auth.UserProfileDTO;

public interface AuthService {
    JwtResponse login(LoginRequest request);
    UserProfileDTO me(String email);
}

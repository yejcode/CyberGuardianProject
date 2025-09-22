package com.cyberguardian.cyberguardian_service.controller;

import com.cyberguardian.cyberguardian_service.dto.auth.JwtResponse;
import com.cyberguardian.cyberguardian_service.dto.auth.LoginRequest;
import com.cyberguardian.cyberguardian_service.dto.auth.UserProfileDTO;
import com.cyberguardian.cyberguardian_service.security.JwtService;
import com.cyberguardian.cyberguardian_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileDTO> me(@RequestHeader("Authorization") String bearer) {
        String email = jwtService.validateAndGetSubject(bearer.replace("Bearer ", ""));
        return ResponseEntity.ok(authService.me(email));
    }
}

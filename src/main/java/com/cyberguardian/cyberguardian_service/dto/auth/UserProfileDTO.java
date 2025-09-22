package com.cyberguardian.cyberguardian_service.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserProfileDTO {
    private Long id;
    private String email;
    private String role;
    private Boolean active;
}
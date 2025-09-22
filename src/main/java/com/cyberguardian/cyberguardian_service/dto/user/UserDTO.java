package com.cyberguardian.cyberguardian_service.dto.user;

import com.cyberguardian.cyberguardian_service.entity.enums.Role;

public record UserDTO(
        Long id,
        String email,
        Role role,
        Boolean active
) {
}

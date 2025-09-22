package com.cyberguardian.cyberguardian_service.dto.user;

import com.cyberguardian.cyberguardian_service.entity.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateUserRequest {
    private String email;
    private String password;
    private Role role;
    private Boolean active;
}

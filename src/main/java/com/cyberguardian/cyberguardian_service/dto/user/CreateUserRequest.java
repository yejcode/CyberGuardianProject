package com.cyberguardian.cyberguardian_service.dto.user;

import com.cyberguardian.cyberguardian_service.entity.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateUserRequest {
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @NotNull
    private Role role = Role.USER;
}

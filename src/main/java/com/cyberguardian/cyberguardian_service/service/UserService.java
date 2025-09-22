package com.cyberguardian.cyberguardian_service.service;

import com.cyberguardian.cyberguardian_service.dto.user.CreateUserRequest;
import com.cyberguardian.cyberguardian_service.dto.user.UpdateUserRequest;
import com.cyberguardian.cyberguardian_service.dto.user.UserDTO;

import java.util.List;

public interface UserService {
    List<UserDTO> findAll();
    UserDTO findById(Long id);
    UserDTO create(CreateUserRequest req);
    UserDTO update(Long id, UpdateUserRequest req);
    void delete(Long id);
}

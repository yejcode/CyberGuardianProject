package com.cyberguardian.cyberguardian_service.service.impl;

import com.cyberguardian.cyberguardian_service.dto.user.CreateUserRequest;
import com.cyberguardian.cyberguardian_service.dto.user.UpdateUserRequest;
import com.cyberguardian.cyberguardian_service.dto.user.UserDTO;
import com.cyberguardian.cyberguardian_service.entity.User;
import com.cyberguardian.cyberguardian_service.repository.UserRepository;
import com.cyberguardian.cyberguardian_service.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UserDTO> findAll() {
        return repo.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public UserDTO findById(Long id) {
        return repo.findById(id).map(this::toDto)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + id));
    }

    @Override
    public UserDTO create(CreateUserRequest req) {
        User user = User.builder()
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .role(req.getRole())
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        return toDto(repo.save(user));
    }

    @Override
    public UserDTO update(Long id, UpdateUserRequest req) {
        User user = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + id));

        if (req.getPassword() != null)
            user.setPassword(passwordEncoder.encode(req.getPassword()));
        if (req.getRole() != null)
            user.setRole(req.getRole());
        if (req.getActive() != null)
            user.setActive(req.getActive());

        user.setUpdatedAt(LocalDateTime.now());
        return toDto(repo.save(user));
    }

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new EntityNotFoundException("User not found");
        repo.deleteById(id);
    }

    private UserDTO toDto(User u) {
        return new UserDTO(u.getId(), u.getEmail(), u.getRole(), u.getActive());
    }
}

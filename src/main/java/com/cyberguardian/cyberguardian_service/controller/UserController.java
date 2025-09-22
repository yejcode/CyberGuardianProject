package com.cyberguardian.cyberguardian_service.controller;

import com.cyberguardian.cyberguardian_service.dto.user.CreateUserRequest;
import com.cyberguardian.cyberguardian_service.dto.user.UpdateUserRequest;
import com.cyberguardian.cyberguardian_service.dto.user.UserDTO;
import com.cyberguardian.cyberguardian_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping
    public List<UserDTO> all() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public UserDTO get(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public UserDTO create(@Valid @RequestBody CreateUserRequest req) {
        return service.create(req);
    }

    @PutMapping("/{id}")
    public UserDTO update(@PathVariable Long id, @RequestBody UpdateUserRequest req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
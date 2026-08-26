package com.sivaji.aisupportcopilot.controller;

import com.sivaji.aisupportcopilot.dto.UserCreateRequest;
import com.sivaji.aisupportcopilot.dto.UserResponse;
import com.sivaji.aisupportcopilot.entity.User;
import com.sivaji.aisupportcopilot.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody UserCreateRequest request) {

        User user = userService.createUser(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(UserResponse.from(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(
            @PathVariable UUID id) {

        User user = userService.getUserById(id);

        return ResponseEntity.ok(UserResponse.from(user));
    }
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(
            @AuthenticationPrincipal User user) {

        return ResponseEntity.ok(
                UserResponse.from(user)
        );
    }

}

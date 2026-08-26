package com.sivaji.aisupportcopilot.service;

import com.sivaji.aisupportcopilot.dto.UserCreateRequest;
import com.sivaji.aisupportcopilot.entity.User;

import java.util.UUID;

public interface UserService {
    User createUser(UserCreateRequest request);

    User getUserById(UUID id);

    User getUserByEmail(String email);

    boolean existUserByEmail(String email);
}


package com.sivaji.aisupportcopilot.service;


import com.sivaji.aisupportcopilot.dto.LoginRequest;
import com.sivaji.aisupportcopilot.dto.LoginResponse;
import com.sivaji.aisupportcopilot.entity.User;
import com.sivaji.aisupportcopilot.exception.InvalidCredentialsException;
import com.sivaji.aisupportcopilot.exception.UserNotFoundException;
import com.sivaji.aisupportcopilot.repository.UserRepository;
import com.sivaji.aisupportcopilot.security.JwtService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() ->
                        new InvalidCredentialsException(
                                "Invalid email or password"
                        )
                );
        if (!passwordEncoder.matches(
                request.password(),
                user.getPasswordHash())) {

            throw new InvalidCredentialsException(
                    "Invalid email or password"
            );
        }

        String token = jwtService.generateToken(user);

        return new LoginResponse(
                user.getId(),
                user.getEmail(),
                user.getRole(),
                token
                );
    }
}
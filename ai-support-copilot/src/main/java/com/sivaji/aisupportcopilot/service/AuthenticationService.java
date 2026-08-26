package com.sivaji.aisupportcopilot.service;

import com.sivaji.aisupportcopilot.dto.LoginRequest;
import com.sivaji.aisupportcopilot.dto.LoginResponse;

public interface AuthenticationService {

    LoginResponse login(LoginRequest request);
}

package com.sivaji.aisupportcopilot.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @GetMapping("/test")
    public Map<String, Object> adminTest(
            Authentication authentication) {

        return Map.of(
                "message", "You have admin access",
                "user", authentication.getName(),
                "authorities", authentication.getAuthorities()
        );
    }
}

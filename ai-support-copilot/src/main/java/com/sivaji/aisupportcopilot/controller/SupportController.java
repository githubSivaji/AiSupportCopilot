package com.sivaji.aisupportcopilot.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/support")
public class SupportController {

    @GetMapping("/test")
    public Map<String, Object> supportTest(
            Authentication authentication) {

        return Map.of(
                "message", "You have support access",
                "user", authentication.getName(),
                "authorities", authentication.getAuthorities()
        );
    }
}

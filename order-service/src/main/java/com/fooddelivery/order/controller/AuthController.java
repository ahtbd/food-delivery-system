package com.fooddelivery.order.controller;

import com.fooddelivery.order.config.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public Mono<Map<String, String>> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        // Hardcoded user for demo (in real app, check database)
        if ("admin".equals(username) && "admin123".equals(password)) {
            String token = jwtTokenProvider.generateToken(username, "ADMIN");
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("username", username);
            response.put("role", "ADMIN");
            return Mono.just(response);
        } else {
            return Mono.error(new RuntimeException("Invalid credentials"));
        }
    }
}
package com.vetapi.application.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private Long userId;
    private String name;
    private String lastName;
    private String email;
    private String role;
    private boolean success;
    private String message;
    private String token;
    private String refreshToken;
    private LocalDateTime expiresAt;
}
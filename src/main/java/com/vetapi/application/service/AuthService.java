package com.vetapi.application.service;

import com.vetapi.application.dto.auth.LoginRequest;
import com.vetapi.application.dto.auth.LoginResponse;
import com.vetapi.domain.entity.User;
import com.vetapi.domain.repository.UserRepository;
import com.vetapi.infrastructure.security.BcryptHashService;
import com.vetapi.infrastructure.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final BcryptHashService bcryptHashService;
    private final JwtService jwtService;

    // Simple in-memory storage for password reset tokens (use Redis in production)
    private final ConcurrentHashMap<String, String> resetTokens = new ConcurrentHashMap<>();

    @Transactional
    public LoginResponse login(LoginRequest request) {
        log.info("Attempting login for email: {}", request.getEmail());
        
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());

        if (userOptional.isEmpty()) {
            log.warn("User not found for email: {}", request.getEmail());
            return LoginResponse.builder()
                    .success(false)
                    .message("User not found")
                    .build();
        }

        User user = userOptional.get();
        log.info("User found - ID: {}, Active: {}, Role: {}", user.getId(), user.isActive(), user.getRole());

        boolean passwordMatches = bcryptHashService.verifyPassword(request.getPassword(), user.getPassword());
        log.info("Password verification result: {}", passwordMatches);
        
        if (!passwordMatches) {
            log.warn("Invalid credentials for user: {}", request.getEmail());
            return LoginResponse.builder()
                    .success(false)
                    .message("Invalid credentials")
                    .build();
        }

        log.info("Login successful for user: {}", request.getEmail());

        // Update last access time
        user.updateLastAccess();
        userRepository.save(user);

        // Generate tokens
        String token = jwtService.generateToken(user.getEmail(), user.getId(), user.getRole());
        String refreshToken = jwtService.generateRefreshToken(user.getEmail());
        LocalDateTime expiresAt = LocalDateTime.ofInstant(
                jwtService.getExpirationDate(token).toInstant(),
                ZoneId.systemDefault()
        );

        return LoginResponse.builder()
                .userId(user.getId())
                .name(user.getName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .success(true)
                .message("Login successful")
                .token(token)
                .refreshToken(refreshToken)
                .expiresAt(expiresAt)
                .build();
    }

    @Transactional
    public LoginResponse refreshToken(String refreshToken) {
        try {
            if (!jwtService.isTokenValid(refreshToken)) {
                return LoginResponse.builder()
                        .success(false)
                        .message("Invalid or expired refresh token")
                        .build();
            }

            String email = jwtService.extractEmail(refreshToken);
            Optional<User> userOptional = userRepository.findByEmail(email);

            if (userOptional.isEmpty()) {
                return LoginResponse.builder()
                        .success(false)
                        .message("User not found")
                        .build();
            }

            User user = userOptional.get();
            String newToken = jwtService.generateToken(user.getEmail(), user.getId(), user.getRole());
            String newRefreshToken = jwtService.generateRefreshToken(user.getEmail());
            LocalDateTime expiresAt = LocalDateTime.ofInstant(
                    jwtService.getExpirationDate(newToken).toInstant(),
                    ZoneId.systemDefault()
            );

            return LoginResponse.builder()
                    .userId(user.getId())
                    .name(user.getName())
                    .lastName(user.getLastName())
                    .email(user.getEmail())
                    .role(user.getRole())
                    .success(true)
                    .message("Token renewed")
                    .token(newToken)
                    .refreshToken(newRefreshToken)
                    .expiresAt(expiresAt)
                    .build();

        } catch (Exception e) {
            return LoginResponse.builder()
                    .success(false)
                    .message("Error refreshing token")
                    .build();
        }
    }

    public Map<String, Object> forgotPassword(String email) {
        Map<String, Object> response = new HashMap<>();

        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            // Don't reveal if email exists
            response.put("success", true);
            response.put("message", "If the email exists, reset instructions have been sent");
            return response;
        }

        // Generate reset token
        String resetToken = UUID.randomUUID().toString();
        resetTokens.put(resetToken, email);

        // In production, send email here
        // For now, just return success
        response.put("success", true);
        response.put("message", "Password reset instructions sent to email");
        // In development, include token for testing
        response.put("resetToken", resetToken);

        return response;
    }

    public Map<String, Object> resetPassword(String token, String newPassword, String confirmPassword) {
        Map<String, Object> response = new HashMap<>();

        if (!newPassword.equals(confirmPassword)) {
            response.put("success", false);
            response.put("message", "Passwords do not match");
            return response;
        }

        String email = resetTokens.get(token);
        if (email == null) {
            response.put("success", false);
            response.put("message", "Invalid or expired reset token");
            return response;
        }

        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            response.put("success", false);
            response.put("message", "User not found");
            return response;
        }

        // Update password
        String hashedPassword = bcryptHashService.hashPassword(newPassword);
        userRepository.updatePassword(userOptional.get().getId(), hashedPassword);

        // Remove used token
        resetTokens.remove(token);

        response.put("success", true);
        response.put("message", "Password reset successful");
        return response;
    }
}
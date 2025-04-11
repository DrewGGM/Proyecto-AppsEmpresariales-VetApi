package com.vetapi.application.service;

import com.vetapi.application.dto.auth.LoginRequest;
import com.vetapi.application.dto.auth.LoginResponse;
import com.vetapi.domain.entity.User;
import com.vetapi.domain.repository.UserRepository;
import com.vetapi.infrastructure.security.BcryptHashService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final BcryptHashService bcryptHashService;

    @Transactional
    public LoginResponse login(LoginRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());

        if (userOptional.isEmpty()) {
            return LoginResponse.builder()
                    .success(false)
                    .message("User not found")
                    .build();
        }

        User user = userOptional.get();

        // Verify password using BCrypt
        if (!bcryptHashService.verifyPassword(request.getPassword(), user.getPassword())) {
            return LoginResponse.builder()
                    .success(false)
                    .message("Invalid credentials")
                    .build();
        }

        // Update last access time
        user.updateLastAccess();
        userRepository.save(user);

        return LoginResponse.builder()
                .userId(user.getId())
                .name(user.getName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .success(true)
                .message("Login successful")
                .build();
    }

    @Transactional
    public LoginResponse refreshToken(String refreshToken) {
        // This will be implemented when JWT is added
        return LoginResponse.builder()
                .success(false)
                .message("Refresh token functionality not yet implemented")
                .build();
    }
}
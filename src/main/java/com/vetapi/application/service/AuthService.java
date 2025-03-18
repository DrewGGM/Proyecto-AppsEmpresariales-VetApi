package com.vetapi.application.service;

import com.vetapi.application.dto.auth.LoginRequest;
import com.vetapi.application.dto.auth.LoginResponse;
import com.vetapi.domain.entity.User;
import com.vetapi.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    public LoginResponse login(LoginRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());

        if (userOptional.isEmpty()) {
            return LoginResponse.builder()
                    .success(false)
                    .message("User not found")
                    .build();
        }

        User user = userOptional.get();

        // Comparación directa de contraseña (sin encriptación por ahora)
        if (!user.getPassword().equals(request.getPassword())) {
            return LoginResponse.builder()
                    .success(false)
                    .message("Invalid credentials")
                    .build();
        }

        // Actualizar el último acceso
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
}
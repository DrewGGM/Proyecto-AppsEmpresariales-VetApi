package com.vetapi.web.controller;

import com.vetapi.application.dto.auth.*;
import com.vetapi.application.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "API for user authentication")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "User Login",
            description = "Authenticate user credentials and generate login response with JWT tokens")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully authenticated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "401",
                    description = "Authentication failed - Invalid credentials",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "400",
                    description = "Bad request - Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content)
    })
    public ResponseEntity<LoginResponse> login(
            @Parameter(description = "Login credentials", required = true)
            @Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh Token",
            description = "Generate new access token using refresh token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Token refreshed successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "401",
                    description = "Invalid or expired refresh token",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "400",
                    description = "Bad request",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content)
    })
    public ResponseEntity<LoginResponse> refreshToken(
            @Parameter(description = "Refresh token request", required = true)
            @Valid @RequestBody RefreshTokenRequest request) {
        LoginResponse response = authService.refreshToken(request.getRefreshToken());

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "Forgot Password",
            description = "Request password reset link")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Reset instructions sent if email exists",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400",
                    description = "Bad request",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content)
    })
    public ResponseEntity<Map<String, Object>> forgotPassword(
            @Parameter(description = "Email for password reset", required = true)
            @Valid @RequestBody ForgotPasswordRequest request) {
        Map<String, Object> response = authService.forgotPassword(request.getEmail());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Reset Password",
            description = "Reset password using token from email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Password reset successful",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400",
                    description = "Invalid token or passwords don't match",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content)
    })
    public ResponseEntity<Map<String, Object>> resetPassword(
            @Parameter(description = "Password reset request", required = true)
            @Valid @RequestBody ResetPasswordRequest request) {
        Map<String, Object> response = authService.resetPassword(
                request.getToken(),
                request.getNewPassword(),
                request.getConfirmPassword()
        );

        Boolean success = (Boolean) response.get("success");
        if (Boolean.TRUE.equals(success)) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }
}
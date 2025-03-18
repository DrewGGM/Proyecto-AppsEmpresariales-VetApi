package com.vetapi.application.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String name;
    private String lastName;
    private String email;
    private String role;
    private LocalDateTime lastAccess;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean active;
}
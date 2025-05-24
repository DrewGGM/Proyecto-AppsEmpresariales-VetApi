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
public class UserActivityDTO {
    private Long id;
    private String type; // consultation, vaccination, login
    private String description;
    private String details;
    private LocalDateTime date;
}
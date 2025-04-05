package com.vetapi.application.dto.customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {
    private Long id;
    private String name;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private int petCount;
    private List<Long> petIds;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean active;
}
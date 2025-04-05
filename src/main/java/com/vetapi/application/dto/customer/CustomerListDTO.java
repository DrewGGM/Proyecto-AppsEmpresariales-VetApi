package com.vetapi.application.dto.customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerListDTO {
    private Long id;
    private String name;
    private String lastName;
    private String email;
    private String phone;
    private int petCount;
    private boolean active;
}
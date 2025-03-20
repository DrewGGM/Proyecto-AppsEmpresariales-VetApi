package com.vetapi.application.dto.consultation;

import com.vetapi.domain.entity.Pet;
import com.vetapi.domain.entity.Treatment;
import com.vetapi.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConsultationDTO {
    private long id;
    private String petName;
    private long  petId;
    private String veterinarianName;
    private long veterinarianId;
    private LocalDateTime date;
    private String reason;
    private String diagnosis;
    private String observations;
    private int treatmentCount;
    private int documentCount;
    private boolean isRecent;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

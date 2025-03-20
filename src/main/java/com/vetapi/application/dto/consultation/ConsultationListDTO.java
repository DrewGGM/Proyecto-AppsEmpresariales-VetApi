package com.vetapi.application.dto.consultation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConsultationListDTO {
    private long id;
    private String petName;
    private String veterinarianName;
    private LocalDateTime date;
    private int treatmentCount;
    private int documentCount;
    private boolean isRecent;
    private String reason;
}

package com.vetapi.domain.entity;

import com.vetapi.domain.entity.base.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Consultation extends BaseEntity {

    private Pet pet;
    private User veterinarian;
    private LocalDateTime date;
    private String reason;
    private String diagnosis;
    private String observations;
    @Builder.Default
    private List<Treatment> treatments = new ArrayList<>();
    @Builder.Default
    private List<Document> documents = new ArrayList<>();

    public void addDocument(Document document) {
        if (document != null && !documents.contains(document)) {
            documents.add(document);
            document.setConsultation(this);
        }
    }

    public void addTreatment(Treatment treatment) {
        if (treatment != null && !treatments.contains(treatment)) {
            treatments.add(treatment);
            treatment.setConsultation(this);
        }
    }

    public boolean isRecent() {
        if (date == null) {
            return false;
        }
        return (date.isAfter(LocalDateTime.now().minusDays(7)));
    }
}

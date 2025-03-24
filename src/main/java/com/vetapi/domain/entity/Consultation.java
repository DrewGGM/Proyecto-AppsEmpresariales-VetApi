package com.vetapi.domain.entity;

import com.vetapi.domain.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Consultation extends BaseEntity {

    private Pet pet;
    private User veterinarian;
    private LocalDateTime date;
    private String reason;
    private String diagnosis;
    private String observations;
    private List<Treatment> treatments = new ArrayList<>();
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

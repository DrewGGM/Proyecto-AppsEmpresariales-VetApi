package com.vetapi.application.service;

import com.vetapi.application.dto.treatment.TreatmentCreateDTO;
import com.vetapi.application.dto.treatment.TreatmentDTO;
import com.vetapi.application.dto.treatment.TreatmentUpdateDTO;
import com.vetapi.application.mapper.TreatmentDTOMapper;
import com.vetapi.domain.entity.Consultation;
import com.vetapi.domain.entity.Pet;
import com.vetapi.domain.entity.Treatment;
import com.vetapi.domain.exception.EntityNotFoundException;
import com.vetapi.domain.exception.InvalidDataException;
import com.vetapi.domain.repository.ConsultationRepository;
import com.vetapi.domain.repository.PetRepository;
import com.vetapi.domain.repository.TreatmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TreatmentService {
    private final TreatmentRepository repository;
    private final TreatmentDTOMapper mapper;
    private final ConsultationRepository consultationRepository;
    private final PetRepository petRepository;

    @Transactional
    public TreatmentDTO save(TreatmentCreateDTO treatmentDTO) {
        // Verificar que las entidades relacionadas existen
        Pet pet = petRepository.findById(treatmentDTO.getPetId())
                .orElseThrow(() -> new EntityNotFoundException("Pet not found with ID: " + treatmentDTO.getPetId()));

        Consultation consultation = consultationRepository.findById(treatmentDTO.getConsultationId())
                .orElseThrow(() -> new EntityNotFoundException("Consultation not found with ID: " + treatmentDTO.getConsultationId()));

        // Verificar que la mascota coincide con la de la consulta
        if (!pet.getId().equals(consultation.getPet().getId())) {
            throw new IllegalArgumentException("Pet ID in treatment does not match consultation's pet");
        }

        // Validar fechas
        if (treatmentDTO.getEndDate() != null &&
                treatmentDTO.getStartDate().isAfter(treatmentDTO.getEndDate())) {
            throw new InvalidDataException("Start date cannot be after end date");
        }

        Treatment treatment = mapper.toTreatment(treatmentDTO);
        treatment.setPet(pet);
        treatment.setConsultation(consultation);
        treatment.setActive(true);

        return mapper.toTreatmentDto(repository.save(treatment));
    }

    public TreatmentDTO findById(Long id){
         return repository.findById(id)
        .map(mapper::toTreatmentDto)
                 .orElseThrow(() -> new EntityNotFoundException("Treatment not found with ID: " + id));
    }
    public List<TreatmentDTO> findAll(){
        return repository.findAll()
                .stream()
                .map(mapper::toTreatmentDto)
                .collect(Collectors.toList());
    }
    public List<TreatmentDTO>findByPet(Long idPet){
        return repository.findByPet(idPet)
                .stream()
                .map(mapper::toTreatmentDto)
                .collect(Collectors.toList());
    }
    @Transactional
    public boolean delete(Long id){
        if (repository.findById(id).isPresent()){
            repository.delete(id);
            return true;
        }
        return false;
    }
    public List<TreatmentDTO>findByConsultation(Long idConsultation){
        return repository.findByConsultation(idConsultation)
                .stream()
                .map(mapper::toTreatmentDto)
                .collect(Collectors.toList());
    }
    @Transactional
    public TreatmentDTO update(TreatmentUpdateDTO updateDTO, Long id) {
        Treatment treatment = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Treatment not found with ID: " + id));

        mapper.updateTreatmentFromDTO(updateDTO, treatment);

        if (treatment.getStartDate() != null && treatment.getEndDate() != null) {
            if (treatment.getStartDate().isAfter(treatment.getEndDate())) {
                throw new InvalidDataException("La fecha de inicio no puede ser posterior a la fecha de fin");
            }
        }

        if (updateDTO.isCompleted()) {
            treatment.markAsCompleted();
        }

        return mapper.toTreatmentDto(repository.save(treatment));
    }
}

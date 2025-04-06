package com.vetapi.application.service;

import com.vetapi.application.dto.treatment.TreatmentCreateDTO;
import com.vetapi.application.dto.treatment.TreatmentDTO;
import com.vetapi.application.dto.treatment.TreatmentUpdateDTO;
import com.vetapi.application.mapper.TreatmentDTOMapper;
import com.vetapi.domain.entity.Consultation;
import com.vetapi.domain.entity.Pet;
import com.vetapi.domain.entity.Treatment;
import com.vetapi.domain.exception.EntityNotFoundException;
import com.vetapi.domain.repository.ConsultationRepository;
import com.vetapi.domain.repository.PetRepository;
import com.vetapi.domain.repository.TreatmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class TreatmentService {
    private TreatmentRepository repository;
    private TreatmentDTOMapper mapper;
    private ConsultationRepository consultationRepository;
    private PetRepository petRepository;

    public TreatmentDTO save(TreatmentCreateDTO treatment){
        Treatment treatment1= mapper.toTreatment(treatment);
        Optional<Pet> pet= petRepository.findById(treatment.getPetId());
        Optional<Consultation> consultation= consultationRepository.findById(treatment.getConsultationId());
        if (pet.isPresent() && consultation.isPresent()) {
            treatment1.setConsultation(consultation.get());
            treatment1.setPet(pet.get());
            return mapper.toTreatmentDto(repository.save(treatment1));
        }else {
            throw  new IllegalArgumentException("El pet o la consulta  no existen");
        }
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
    public TreatmentDTO update(TreatmentUpdateDTO treatment, Long id){
        return null;
    }
}

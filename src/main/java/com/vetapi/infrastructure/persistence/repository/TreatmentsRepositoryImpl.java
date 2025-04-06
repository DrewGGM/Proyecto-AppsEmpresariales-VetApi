package com.vetapi.infrastructure.persistence.repository;

import com.vetapi.domain.entity.Treatment;
import com.vetapi.domain.repository.TreatmentRepository;
import com.vetapi.infrastructure.persistence.crud.ConsultationCrudRepository;
import com.vetapi.infrastructure.persistence.crud.PetCrudRepository;
import com.vetapi.infrastructure.persistence.crud.TreatmentCrudRepository;
import com.vetapi.infrastructure.persistence.entity.ConsultationEntity;
import com.vetapi.infrastructure.persistence.entity.PetEntity;
import com.vetapi.infrastructure.persistence.entity.TreatmentEntity;
import com.vetapi.infrastructure.persistence.mapper.TreatmentEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class TreatmentsRepositoryImpl implements TreatmentRepository {
    private TreatmentCrudRepository crudRepository;
    private TreatmentEntityMapper mapper;
    private PetCrudRepository petCrudRepository;
    private ConsultationCrudRepository consultationCrudRepository;

    @Override
    public Treatment save(Treatment treatment){
        TreatmentEntity entity=mapper.toEntity(treatment);
       PetEntity petEntity= petCrudRepository.getReferenceById(treatment.getPet().getId());
        ConsultationEntity consultationEntity= consultationCrudRepository.getReferenceById(treatment.getConsultation().getId());
        if (petEntity.isActive() && consultationEntity.isActive()) {
            entity.setPet(petEntity);
            entity.setConsultation(consultationEntity);
            return mapper.toTreatment(crudRepository.save(entity));
        }
        return null;
    }
    @Override
    public Optional<Treatment> findById(Long id){
        return  crudRepository.findById(id)
                .map(mapper::toTreatment);
    }
    @Override
    public List<Treatment> findAll(){
        return crudRepository.findAll()
                .stream()
                .map(mapper::toTreatment)
                .collect(Collectors.toList());
    }
    @Override
    public List<Treatment>findByPet(Long idPet){
        return crudRepository.findByPetId(idPet)
                .stream()
                .map(mapper::toTreatment)
                .collect(Collectors.toList());

    }
    @Override
    public void delete(Long id){
        crudRepository.deleteById(id);
    }
    @Override
    public List<Treatment>findByConsultation(Long idConsultation){
        return crudRepository.findByConsultationId(idConsultation)
                .stream()
                .map(mapper::toTreatment)
                .collect(Collectors.toList());
    }
    @Override
    public Treatment update(Treatment treatment, Long id){
        return null;
    }
}

package com.vetapi.infrastructure.persistence.repository;

import com.vetapi.domain.entity.Consultation;
import com.vetapi.domain.repository.ConsultationRepository;
import com.vetapi.infrastructure.persistence.crud.ConsultationCrudRepository;
import com.vetapi.infrastructure.persistence.crud.PetCrudRepository;
import com.vetapi.infrastructure.persistence.crud.UserCrudRepository;
import com.vetapi.infrastructure.persistence.entity.ConsultationEntity;
import com.vetapi.infrastructure.persistence.entity.PetEntity;
import com.vetapi.infrastructure.persistence.entity.UserEntity;
import com.vetapi.infrastructure.persistence.mapper.ConsultationEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ConsultationRepositoryImpl implements ConsultationRepository {

    private ConsultationCrudRepository crudRepository;
    private PetCrudRepository petCrudRepository;
    private UserCrudRepository userCrudRepository;
    private ConsultationEntityMapper mapper;

    @Override
    public Consultation save(Consultation consultation){
        ConsultationEntity entity=mapper.toEntity(consultation);
        PetEntity petEntity = petCrudRepository.getReferenceById(consultation.getPet().getId());
        UserEntity userEntity= userCrudRepository.getReferenceById(consultation.getVeterinarian().getId());
        if (petEntity.isActive() && userEntity.isActive()) {
            entity.setPet(petEntity);
            entity.setVeterinarian(userEntity);
            return mapper.toConsultation(crudRepository.save(entity));
        }
        return null;
    }
    @Override
    public Optional<Consultation> findById(Long id){
        return crudRepository.findById(id)
                .map(mapper :: toConsultation);
    }
    @Override
    public List<Consultation> findAll(){
        return crudRepository.findAll()
                .stream()
                .map(mapper::toConsultation)
                .collect(Collectors.toList());
    }
    @Override
    public List<Consultation> findByVeterinarian(Long idVeterinarian){
         return crudRepository.findByVeterinarianId(idVeterinarian)
                 .stream()
                 .map(mapper::toConsultation)
                 .collect(Collectors.toList());
    }
    @Override
    public List<Consultation> findByPet(Long idPet){
        return crudRepository.findByPetId(idPet)
                .stream()
                .map(mapper::toConsultation)
                .collect(Collectors.toList());
    }
    @Override
    public List<Consultation>findByDate(LocalDate date){
        return crudRepository.findByDate(date)
                .stream()
                .map(mapper::toConsultation)
                .collect(Collectors.toList());
    }
    @Override
    public Consultation update(Consultation consultation, Long id){
        return null;
    }

    @Override
    public void delete(Long id){
        crudRepository.deleteById(id);
    }
}

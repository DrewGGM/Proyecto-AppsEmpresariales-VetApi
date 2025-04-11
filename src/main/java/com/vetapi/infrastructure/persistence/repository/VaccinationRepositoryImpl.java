package com.vetapi.infrastructure.persistence.repository;

import com.vetapi.domain.entity.Vaccination;
import com.vetapi.domain.repository.VaccinationRepository;
import com.vetapi.infrastructure.persistence.crud.PetCrudRepository;
import com.vetapi.infrastructure.persistence.crud.UserCrudRepository;
import com.vetapi.infrastructure.persistence.crud.VaccinationCrudRepository;
import com.vetapi.infrastructure.persistence.entity.PetEntity;
import com.vetapi.infrastructure.persistence.entity.UserEntity;
import com.vetapi.infrastructure.persistence.entity.VaccinationEntity;
import com.vetapi.infrastructure.persistence.mapper.VaccinationEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class VaccinationRepositoryImpl implements VaccinationRepository {
    private final VaccinationCrudRepository crudRepository;
    private final VaccinationEntityMapper mapper;
    private final UserCrudRepository userCrudRepository;
    private final PetCrudRepository petCrudRepository;

    public Vaccination save(Vaccination vaccination){
        VaccinationEntity entity=mapper.toEntity(vaccination);
        PetEntity petEntity = petCrudRepository.getReferenceById(vaccination.getPet().getId());
        UserEntity userEntity= userCrudRepository.getReferenceById(vaccination.getVeterinarian().getId());
        if (petEntity.isActive() && userEntity.isActive()) {
            entity.setPet(petEntity);
            entity.setVeterinarian(userEntity);
            return mapper.toVaccination(crudRepository.save(entity));
        }
        return null;
    }

    public Optional<Vaccination> findById(Long id){
        return crudRepository.findById(id)
                .map(mapper::toVaccination);
    }
    public List<Vaccination> findAll(){
        return crudRepository.findAll()
                .stream()
                .map(mapper::toVaccination)
                .collect(Collectors.toList());
    }
    public void delete(Long id){
        crudRepository.deleteById(id);
    }
    public boolean existVaccination(Long id){
        Optional<VaccinationEntity> vaccination= crudRepository.findById(id);
        if (vaccination.isPresent()){
            return true;
        }
        return false;
    }
    public List<Vaccination> findByPet(Long petId){
        return crudRepository.findByPetId(petId)
                .stream()
                .map(mapper::toVaccination)
                .collect(Collectors.toList());
    }
    public List<Vaccination> findByNextApplicationDate(LocalDate pending){
        return crudRepository.findByNextApplicationDate(pending)
                .stream()
                .map(mapper::toVaccination)
                .collect(Collectors.toList());
    }
    public List<Vaccination> findByDate(LocalDate date){
        return crudRepository.findByApplicationDate(date)
                .stream()
                .map(mapper::toVaccination)
                .collect(Collectors.toList());
    }
}

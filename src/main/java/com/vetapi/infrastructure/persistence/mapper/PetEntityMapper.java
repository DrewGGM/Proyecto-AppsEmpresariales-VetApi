package com.vetapi.infrastructure.persistence.mapper;

import com.vetapi.domain.entity.Pet;
import com.vetapi.infrastructure.persistence.entity.PetEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PetEntityMapper {

    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "consultations", ignore = true)
    @Mapping(target = "vaccinations", ignore = true)
    @Mapping(target = "appointments", ignore = true)
    Pet toPet(PetEntity entity);

    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "consultations", ignore = true)
    @Mapping(target = "vaccinations", ignore = true)
    @Mapping(target = "appointments", ignore = true)
    PetEntity toEntity(Pet pet);
}
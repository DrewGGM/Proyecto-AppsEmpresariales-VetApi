package com.vetapi.infrastructure.persistence.mapper;

import com.vetapi.domain.entity.Consultation;
import com.vetapi.infrastructure.persistence.entity.ConsultationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ConsultationEntityMapper {


    @Mapping(target = "documents", ignore = true)
    @Mapping(target = "treatments",ignore = true)
    @Mapping(target = "pet.consultations", ignore = true)
    @Mapping(target = "pet.vaccinations", ignore = true)
    @Mapping(target = "pet.appointments", ignore = true)
    @Mapping(target = "pet.customer.pets", ignore = true)
    @Mapping(target = "veterinarian.consultations", ignore = true)
    @Mapping(target = "veterinarian.vaccinations", ignore = true)
    Consultation toConsultation(ConsultationEntity entity);

    @Mapping(target = "documents", ignore = true)
    @Mapping(target = "treatments",ignore = true)
    @Mapping(target = "pet.consultations", ignore = true)
    @Mapping(target = "pet.vaccinations", ignore = true)
    @Mapping(target = "pet.appointments", ignore = true)
    @Mapping(target = "pet.customer.pets", ignore = true)
    @Mapping(target = "veterinarian.consultations", ignore = true)
    @Mapping(target = "veterinarian.vaccinations", ignore = true)
    ConsultationEntity toEntity (Consultation consultation);
}

package com.vetapi.application.mapper;

import com.vetapi.application.dto.treatment.TreatmentCreateDTO;
import com.vetapi.application.dto.treatment.TreatmentDTO;
import com.vetapi.application.dto.treatment.TreatmentListDTO;
import com.vetapi.application.dto.treatment.TreatmentUpdateDTO;
import com.vetapi.domain.entity.Treatment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


import java.util.List;

@Mapper(componentModel = "spring")
public interface TreatmentDTOMapper {


    @Mapping(target = "petName",source = "pet.name")
    @Mapping(target = "petId", source = "pet.id")
    @Mapping(target = "isActive",expression = "java(treatment.isActive())")
    @Mapping(target = "getDurationDays",expression = "java(treatment.getDurationDays())")
    TreatmentDTO toTreatmentDto(Treatment treatment);

    List<TreatmentDTO> toTreatmentDTOList(List<Treatment> treatments);

    @Mapping(target = "petName",source = "pet.name")
    @Mapping(target = "isActive",expression = "java(treatment.isActive())")
    TreatmentListDTO toTreatmentListDTO(Treatment treatment);
    List<TreatmentListDTO> toTreatmentListDTOList(List<Treatment> treatment);

    @Mapping(target = "pet", ignore = true)
    @Mapping(target = "consultation", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "active", constant = "true")
    Treatment toTreatment(TreatmentCreateDTO createDTO);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "pet", ignore = true)
    @Mapping(target = "consultation", ignore = true)
    @Mapping(target = "active", ignore = true)
    void updateTreatmentFromDTO(TreatmentUpdateDTO updateDTO, @MappingTarget Treatment treatment);


}
package com.vetapi.application.mapper;

import com.vetapi.application.dto.consultation.ConsultationCreateDTO;
import com.vetapi.application.dto.consultation.ConsultationDTO;
import com.vetapi.application.dto.consultation.ConsultationListDTO;
import com.vetapi.application.dto.consultation.ConsultationUpdateDTO;
import com.vetapi.domain.entity.Consultation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ConsultationDTOMapper {


    @Mapping(target = "petName",source = "pet.name")
    @Mapping(target = "veterinarianName", source = "veterinarian.name")
    @Mapping(target = "isRecent", expression = "java(consultation.isRecent())")
    ConsultationListDTO toConsultationListDTO(Consultation consultation);

    List<ConsultationListDTO> toConsultationListDTOList(List<Consultation> consultations);

    @Mapping(target = "petName",source = "pet.name")
    @Mapping(target = "petId",source = "pet.id")
    @Mapping(target = "veterinarianName", source = "veterinarian.name")
    @Mapping(target = "veterinarianId", source = "veterinarian.id")
    @Mapping(target = "isRecent", expression = "java(consultation.isRecent())")
    ConsultationDTO toConsultationDto(Consultation consultation);
    List<ConsultationDTO> toConsultationDTOList(List<Consultation> consultations);



    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "treatments", ignore = true)
    @Mapping(target = "documents",ignore = true)
    @Mapping(target = "active", constant = "true")
    @Mapping(target = "pet", ignore = true)
    @Mapping(target = "veterinarian", ignore = true)
    Consultation toConsultation(ConsultationCreateDTO createDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "pet", ignore = true)
    @Mapping(target = "veterinarian", ignore = true)
    @Mapping(target = "treatments", ignore = true)
    @Mapping(target = "documents",ignore = true)
    @Mapping(target = "active", ignore = true)
    void updateConsultationFromDTO(ConsultationUpdateDTO updateDTO, @MappingTarget Consultation consultation);
}

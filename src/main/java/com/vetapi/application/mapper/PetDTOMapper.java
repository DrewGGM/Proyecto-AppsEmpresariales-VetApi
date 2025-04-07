package com.vetapi.application.mapper;

import com.vetapi.application.dto.pet.PetCreateDTO;
import com.vetapi.application.dto.pet.PetDTO;
import com.vetapi.application.dto.pet.PetListDTO;
import com.vetapi.application.dto.pet.PetUpdateDTO;
import com.vetapi.domain.entity.Pet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PetDTOMapper {

    @Mapping(target = "customerName", source = "customerName")
    @Mapping(target = "customerId", source = "customerId")
    @Mapping(target = "consultationCount", expression = "java(pet.getConsultations().size())")
    @Mapping(target = "vaccinationCount", expression = "java(pet.getVaccinations().size())")
    @Mapping(target = "appointmentCount", expression = "java(pet.getAppointments().size())")
    @Mapping(target = "age", expression = "java(pet.calculateAge())")
    @Mapping(target = "isAdult", expression = "java(pet.isAdult())")
    PetDTO toPetDTO(Pet pet);

    List<PetDTO> toPetDTOList(List<Pet> pets);

    @Mapping(target = "customerName", source = "customerName")
    @Mapping(target = "age", expression = "java(pet.calculateAge())")
    PetListDTO toPetListDTO(Pet pet);

    List<PetListDTO> toPetListDTOList(List<Pet> pets);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "active", constant = "true")
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "consultations", ignore = true)
    @Mapping(target = "vaccinations", ignore = true)
    @Mapping(target = "appointments", ignore = true)
    Pet toPet(PetCreateDTO createDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "consultations", ignore = true)
    @Mapping(target = "vaccinations", ignore = true)
    @Mapping(target = "appointments", ignore = true)
    void updatePetFromDTO(PetUpdateDTO updateDTO, @MappingTarget Pet pet);
}
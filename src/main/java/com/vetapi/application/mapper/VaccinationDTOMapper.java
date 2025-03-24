package com.vetapi.application.mapper;

import com.vetapi.application.dto.vaccination.VaccinationCreateDTO;
import com.vetapi.application.dto.vaccination.VaccinationDTO;
import com.vetapi.application.dto.vaccination.VaccinationListDTO;
import com.vetapi.application.dto.vaccination.VaccinationUpdateDTO;
import com.vetapi.domain.entity.Vaccination;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VaccinationDTOMapper {

    @Mapping(target = "petName",source = "pet.name")
    @Mapping(target = "petId", source = "pet.id")
    @Mapping(target = "veterinarianName", source = "veterinarian.name")
    @Mapping(target = "veterinarianId", source = "veterinarian.id")
    @Mapping(target = "upToDate", expression = "java(vaccination.isUpToDate())")
    @Mapping(target = "daysUntilNextApplication", expression = "java(vaccination.daysUntilNextApplication())")
    VaccinationDTO toVaccinationDTO(Vaccination vaccination);

    @Mapping(target = "petName",source = "pet.name")
    @Mapping(target = "veterinarianName", source = "veterinarian.name")
    @Mapping(target = "upToDate", expression = "java(vaccination.isUpToDate())")
    VaccinationListDTO toVaccinationListDTO(Vaccination vaccination);

    List<VaccinationDTO> toVaccinationDTOList(List<Vaccination> vaccinations);
    List<VaccinationListDTO> toVaccinationListDTOList(List<Vaccination> vaccinations);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "pet", ignore = true)
    @Mapping(target = "veterinarian", ignore = true)
    @Mapping(target = "active", ignore = true)
    void updateVaccinationFromDTO(VaccinationUpdateDTO updateDTO, @MappingTarget Vaccination vaccination);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "active", constant = "true")
    @Mapping(target = "pet", ignore = true)
    @Mapping(target = "veterinarian", ignore = true)
    Vaccination toVaccination(VaccinationCreateDTO createDTO);
}

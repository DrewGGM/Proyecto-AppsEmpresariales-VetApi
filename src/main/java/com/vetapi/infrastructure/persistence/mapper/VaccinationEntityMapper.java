package com.vetapi.infrastructure.persistence.mapper;

import com.vetapi.domain.entity.Vaccination;
import com.vetapi.infrastructure.persistence.entity.VaccinationEntity;

public interface VaccinationEntityMapper {

    Vaccination toVaccination(VaccinationEntity entity);
    VaccinationEntity toEntity(Vaccination vaccination);
}

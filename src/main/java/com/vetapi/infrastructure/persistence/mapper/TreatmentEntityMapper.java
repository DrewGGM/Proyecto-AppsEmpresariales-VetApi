package com.vetapi.infrastructure.persistence.mapper;

import com.vetapi.domain.entity.Treatment;
import com.vetapi.infrastructure.persistence.entity.TreatmentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TreatmentEntityMapper {

    @Mapping(target = "pet", ignore = true)
    @Mapping(target = "consultation", ignore = true)
    Treatment toTreatment(TreatmentEntity entity);

    @Mapping(target = "pet", ignore = true)
    @Mapping(target = "consultation", ignore = true)
    TreatmentEntity toEntity (Treatment treatment);
}

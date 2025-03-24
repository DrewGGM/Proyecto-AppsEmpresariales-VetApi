package com.vetapi.infrastructure.persistence.mapper;

import com.vetapi.domain.entity.User;
import com.vetapi.infrastructure.persistence.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserEntityMapper {

    @Mapping(target = "consultations", ignore = true)
    @Mapping(target = "vaccinations", ignore = true)
    User toUser(UserEntity entity);

    @Mapping(target = "consultations", ignore = true)
    @Mapping(target = "vaccinations", ignore = true)
    UserEntity toEntity(User user);
}
package com.vetapi.application.mapper;

import com.vetapi.application.dto.user.UserCreateDTO;
import com.vetapi.application.dto.user.UserDTO;
import com.vetapi.application.dto.user.UserListDTO;
import com.vetapi.application.dto.user.UserUpdateDTO;
import com.vetapi.domain.entity.User;
import com.vetapi.infrastructure.persistence.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserDTOMapper {

    // Mappings between domain and DTO
    UserDTO toUserDTO(User user);

    List<UserDTO> toUserDTOList(List<User> users);

    UserListDTO toUserListDTO(User user);

    List<UserListDTO> toUserListDTOList(List<User> users);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "lastAccess", ignore = true)
    @Mapping(target = "active", constant = "true")
    @Mapping(target = "consultations", ignore = true)
    @Mapping(target = "vaccinations", ignore = true)
    User toUser(UserCreateDTO createDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "lastAccess", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "consultations", ignore = true)
    @Mapping(target = "vaccinations", ignore = true)
    void updateUserFromDTO(UserUpdateDTO updateDTO, @MappingTarget User user);

    // Mappings between domain and JPA entity
    @Mapping(target = "consultations", ignore = true)
    @Mapping(target = "vaccinations", ignore = true)
    User toUser(UserEntity entity);

    @Mapping(target = "consultations", ignore = true)
    @Mapping(target = "vaccinations", ignore = true)
    UserEntity toEntity(User user);
}
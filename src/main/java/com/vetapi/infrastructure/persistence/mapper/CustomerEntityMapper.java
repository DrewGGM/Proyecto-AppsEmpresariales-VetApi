package com.vetapi.infrastructure.persistence.mapper;

import com.vetapi.domain.entity.Customer;
import com.vetapi.infrastructure.persistence.entity.CustomerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerEntityMapper {

    @Mapping(target = "pets", ignore = true)
    Customer toCustomer(CustomerEntity entity);

    @Mapping(target = "pets", ignore = true)
    CustomerEntity toEntity(Customer customer);
}
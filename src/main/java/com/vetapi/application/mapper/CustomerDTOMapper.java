package com.vetapi.application.mapper;

import com.vetapi.application.dto.customer.CustomerCreateDTO;
import com.vetapi.application.dto.customer.CustomerDTO;
import com.vetapi.application.dto.customer.CustomerListDTO;
import com.vetapi.application.dto.customer.CustomerUpdateDTO;
import com.vetapi.domain.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerDTOMapper {

    @Mapping(target = "petCount", expression = "java(customer.getPets().size())")
    @Mapping(target = "petIds", expression = "java(customer.getPets().stream().map(pet -> pet.getId()).toList())")
    CustomerDTO toCustomerDTO(Customer customer);

    List<CustomerDTO> toCustomerDTOList(List<Customer> customers);

    @Mapping(target = "petCount", expression = "java(customer.getPets().size())")
    CustomerListDTO toCustomerListDTO(Customer customer);

    List<CustomerListDTO> toCustomerListDTOList(List<Customer> customers);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "active", constant = "true")
    @Mapping(target = "pets", ignore = true)
    Customer toCustomer(CustomerCreateDTO createDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "pets", ignore = true)
    void updateCustomerFromDTO(CustomerUpdateDTO updateDTO, @MappingTarget Customer customer);
}
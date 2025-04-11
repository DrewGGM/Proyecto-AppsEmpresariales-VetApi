package com.vetapi.application.service;

import com.vetapi.application.dto.customer.CustomerCreateDTO;
import com.vetapi.application.dto.customer.CustomerDTO;
import com.vetapi.application.dto.customer.CustomerListDTO;
import com.vetapi.application.dto.customer.CustomerUpdateDTO;
import com.vetapi.application.mapper.CustomerDTOMapper;
import com.vetapi.domain.entity.Customer;
import com.vetapi.domain.exception.EntityNotFoundException;
import com.vetapi.domain.exception.InvalidDataException;
import com.vetapi.domain.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerDTOMapper mapper;

    public List<CustomerListDTO> findAll() {
        return mapper.toCustomerListDTOList(customerRepository.findAll());
    }

    public CustomerDTO findById(Long id) {
        return customerRepository.findById(id)
                .map(mapper::toCustomerDTO)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with ID: " + id));
    }

    public CustomerDTO findByEmail(String email) {
        return customerRepository.findByEmail(email)
                .map(mapper::toCustomerDTO)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with email: " + email));
    }

    public List<CustomerListDTO> findByNameOrLastName(String query) {
        return mapper.toCustomerListDTOList(customerRepository.findByNameOrLastNameContaining(query));
    }

    @Transactional
    public CustomerDTO create(CustomerCreateDTO createDTO) {
        if (customerRepository.existsByEmail(createDTO.getEmail())) {
            throw new InvalidDataException("Email already in use: " + createDTO.getEmail());
        }

        Customer customer = mapper.toCustomer(createDTO);
        return mapper.toCustomerDTO(customerRepository.save(customer));
    }

    @Transactional
    public CustomerDTO update(Long id, CustomerUpdateDTO updateDTO) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with ID: " + id));

        // Check if email is being changed and if the new email is already in use by a different customer
        if (!customer.getEmail().equals(updateDTO.getEmail()) &&
                customerRepository.existsByEmail(updateDTO.getEmail())) {
            throw new InvalidDataException("Email already in use: " + updateDTO.getEmail());
        }

        mapper.updateCustomerFromDTO(updateDTO, customer);
        return mapper.toCustomerDTO(customerRepository.save(customer));
    }

    @Transactional
    public void delete(Long id) {
        if (!customerRepository.findById(id).isPresent()) {
            throw new EntityNotFoundException("Customer not found with ID: " + id);
        }
        customerRepository.delete(id);
    }

    public int countPets(Long customerId) {
        return customerRepository.countPets(customerId);
    }
}
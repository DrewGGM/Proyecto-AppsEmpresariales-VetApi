package com.vetapi.infrastructure.persistence.repository;

import com.vetapi.domain.entity.Customer;
import com.vetapi.domain.repository.CustomerRepository;
import com.vetapi.infrastructure.persistence.crud.CustomerCrudRepository;
import com.vetapi.infrastructure.persistence.entity.CustomerEntity;
import com.vetapi.infrastructure.persistence.mapper.CustomerEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CustomerRepositoryImpl implements CustomerRepository {

    private final CustomerCrudRepository crudRepository;
    private final CustomerEntityMapper mapper;

    @Override
    public List<Customer> findAll() {
        return crudRepository.findByActiveTrue()
                .stream()
                .map(mapper::toCustomer)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Customer> findById(Long id) {
        return crudRepository.findById(id).map(mapper::toCustomer);
    }

    @Override
    public Optional<Customer> findByEmail(String email) {
        return crudRepository.findByEmail(email).map(mapper::toCustomer);
    }

    @Override
    public List<Customer> findByNameOrLastNameContaining(String query) {
        return crudRepository.findByNameContainingOrLastNameContainingAndActiveTrue(query, query)
                .stream()
                .map(mapper::toCustomer)
                .collect(Collectors.toList());
    }

    @Override
    public Customer save(Customer customer) {
        CustomerEntity entity = mapper.toEntity(customer);
        return mapper.toCustomer(crudRepository.save(entity));
    }

    @Override
    public void delete(Long id) {
        crudRepository.findById(id).ifPresent(entity -> {
            entity.setActive(false);
            crudRepository.save(entity);
        });
    }

    @Override
    public boolean existsByEmail(String email) {
        return crudRepository.existsByEmail(email);
    }

    @Override
    public int countPets(Long customerId) {
        return crudRepository.countPetsByCustomerId(customerId);
    }

    @Override
    public boolean existsById(Long id) {
        return crudRepository.existsById(id);
    }
}
package com.vetapi.infrastructure.persistence.crud;

import com.vetapi.infrastructure.persistence.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerCrudRepository extends JpaRepository<CustomerEntity, Long> {

    Optional<CustomerEntity> findByEmail(String email);

    List<CustomerEntity> findByActiveTrue();

    List<CustomerEntity> findByNameContainingOrLastNameContainingAndActiveTrue(String name, String lastName);

    boolean existsByEmail(String email);

    @Query("SELECT COUNT(p) FROM PetEntity p WHERE p.customer.id = :customerId")
    int countPetsByCustomerId(@Param("customerId") Long customerId);
}
package com.vetapi.infrastructure.persistence.repository;

import com.vetapi.application.mapper.UserDTOMapper;
import com.vetapi.domain.entity.User;
import com.vetapi.domain.repository.UserRepository;
import com.vetapi.infrastructure.persistence.crud.UserCrudRepository;
import com.vetapi.infrastructure.persistence.entity.UserEntity;
import com.vetapi.infrastructure.persistence.mapper.UserEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserCrudRepository crudRepository;
    private final UserEntityMapper mapper;

    @Override
    public List<User> findAll() {
        return crudRepository.findByActiveTrue()
                .stream()
                .map(mapper::toUser)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<User> findById(Long id) {
        return crudRepository.findById(id).map(mapper::toUser);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return crudRepository.findByEmail(email).map(mapper::toUser);
    }

    @Override
    public List<User> findByRole(String role) {
        return crudRepository.findByRoleAndActiveTrue(role)
                .stream()
                .map(mapper::toUser)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findByLastAccessAfter(LocalDateTime date) {
        return crudRepository.findByLastAccessAfterAndActiveTrue(date)
                .stream()
                .map(mapper::toUser)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findByNameOrLastNameContaining(String query) {
        return crudRepository.findByNameContainingOrLastNameContainingAndActiveTrue(query, query)
                .stream()
                .map(mapper::toUser)
                .collect(Collectors.toList());
    }

    @Override
    public User save(User user) {
        UserEntity entity = mapper.toEntity(user);
        return mapper.toUser(crudRepository.save(entity));
    }

    @Override
    public void delete(Long id) {
        crudRepository.findById(id).ifPresent(entity -> {
            entity.setActive(false);
            crudRepository.save(entity);
        });
    }

    @Override
    public boolean hardDelete(Long id) {
        if (crudRepository.existsById(id)) {
            crudRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean existsByEmail(String email) {
        return crudRepository.existsByEmail(email);
    }

    @Override
    public boolean updatePassword(Long userId, String newPassword) {
        Optional<UserEntity> optionalEntity = crudRepository.findById(userId);
        if (optionalEntity.isPresent()) {
            UserEntity entity = optionalEntity.get();
            entity.setPassword(newPassword);
            crudRepository.save(entity);
            return true;
        }
        return false;
    }

    @Override
    public int countConsultations(Long userId) {
        return crudRepository.countConsultationsByVeterinarianId(userId);
    }

    @Override
    public int countVaccinations(Long userId) {
        return crudRepository.countVaccinationsByVeterinarianId(userId);
    }
}
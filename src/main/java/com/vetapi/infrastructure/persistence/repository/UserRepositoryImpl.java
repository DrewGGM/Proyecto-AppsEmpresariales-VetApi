package com.vetapi.infrastructure.persistence.repository;

import com.vetapi.application.mapper.UserDTOMapper;
import com.vetapi.domain.entity.Consultation;
import com.vetapi.domain.entity.Pet;
import com.vetapi.domain.entity.User;
import com.vetapi.domain.entity.Vaccination;
import com.vetapi.domain.repository.UserRepository;
import com.vetapi.infrastructure.persistence.crud.UserCrudRepository;
import com.vetapi.infrastructure.persistence.entity.UserEntity;
import com.vetapi.infrastructure.persistence.mapper.ConsultationEntityMapper;
import com.vetapi.infrastructure.persistence.mapper.UserEntityMapper;
import com.vetapi.infrastructure.persistence.mapper.VaccinationEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
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
    private final ConsultationEntityMapper consultationMapper;
    private final VaccinationEntityMapper vaccinationMapper;
    private final JdbcTemplate jdbcTemplate;

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

    @Override
    public int countAppointments(Long userId) {
        String sql = "SELECT COUNT(*) FROM appointments WHERE veterinarian_id = ? AND active = true";
        return jdbcTemplate.queryForObject(sql, Integer.class, userId);
    }

    @Override
    public List<Consultation> findRecentConsultationsByUser(Long userId, int limit) {
        String sql = "SELECT c.* FROM consultations c WHERE c.veterinarian_id = ? " +
                "AND c.active = true ORDER BY c.date DESC LIMIT ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            // Simple mapping - in production, use proper entity mapping
            Consultation consultation = new Consultation();
            consultation.setId(rs.getLong("id"));
            consultation.setDate(rs.getTimestamp("date").toLocalDateTime());
            consultation.setReason(rs.getString("reason"));
            // Set basic pet info
            Pet pet = new Pet();
            pet.setId(rs.getLong("pet_id"));
            pet.setName("Pet " + rs.getLong("pet_id")); // In production, join with pets table
            consultation.setPet(pet);
            return consultation;
        }, userId, limit);
    }

    @Override
    public List<Vaccination> findRecentVaccinationsByUser(Long userId, int limit) {
        String sql = "SELECT v.* FROM vaccinations v WHERE v.veterinarian_id = ? " +
                "AND v.active = true ORDER BY v.application_date DESC LIMIT ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Vaccination vaccination = new Vaccination();
            vaccination.setId(rs.getLong("id"));
            vaccination.setApplicationDate(rs.getDate("application_date").toLocalDate());
            vaccination.setVaccineType(rs.getString("vaccine_type"));
            // Set basic pet info
            Pet pet = new Pet();
            pet.setId(rs.getLong("pet_id"));
            pet.setName("Pet " + rs.getLong("pet_id")); // In production, join with pets table
            vaccination.setPet(pet);
            return vaccination;
        }, userId, limit);
    }

    @Override
    public Page<User> searchUsers(String search, String role, Boolean active, Pageable pageable) {
        return crudRepository.searchUsers(search, role, active, pageable)
                .map(mapper::toUser);
    }

    @Override
    public void updatePhotoUrl(Long userId, String photoUrl) {
        crudRepository.findById(userId).ifPresent(entity -> {
            entity.setPhotoUrl(photoUrl);
            crudRepository.save(entity);
        });
    }

}
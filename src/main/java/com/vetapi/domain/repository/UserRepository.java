package com.vetapi.domain.repository;

import com.vetapi.domain.entity.User;
import com.vetapi.domain.entity.Consultation;
import com.vetapi.domain.entity.Vaccination;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository {

    List<User> findAll();
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    List<User> findByRole(String role);
    List<User> findByLastAccessAfter(LocalDateTime date);
    List<User> findByNameOrLastNameContaining(String query);
    User save(User user);
    void delete(Long id);
    boolean hardDelete(Long id);
    boolean existsByEmail(String email);
    boolean updatePassword(Long userId, String newPassword);
    int countConsultations(Long userId);
    int countVaccinations(Long userId);
    int countAppointments(Long userId);
    List<Consultation> findRecentConsultationsByUser(Long userId, int limit);
    List<Vaccination> findRecentVaccinationsByUser(Long userId, int limit);
    Page<User> searchUsers(String search, String role, Boolean active, Pageable pageable);
    void updatePhotoUrl(Long userId, String photoUrl);
}
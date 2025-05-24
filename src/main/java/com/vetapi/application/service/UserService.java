package com.vetapi.application.service;

import com.vetapi.application.dto.user.*;
import com.vetapi.application.mapper.UserDTOMapper;
import com.vetapi.domain.entity.User;
import com.vetapi.domain.exception.EntityNotFoundException;
import com.vetapi.domain.exception.InvalidDataException;
import com.vetapi.domain.repository.UserRepository;
import com.vetapi.infrastructure.security.BcryptHashService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserDTOMapper mapper;
    private final BcryptHashService bcryptHashService;
    private final StorageService storageService;

    public List<UserListDTO> findAll() {
        return mapper.toUserListDTOList(userRepository.findAll());
    }

    public UserDTO findById(Long id) {
        return userRepository.findById(id)
                .map(mapper::toUserDTO)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + id));
    }

    public UserDTO findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(mapper::toUserDTO)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));
    }

    @Transactional
    public UserDTO create(UserCreateDTO createDTO) {
        if (userRepository.existsByEmail(createDTO.getEmail())) {
            throw new InvalidDataException("Email already in use: " + createDTO.getEmail());
        }

        User user = mapper.toUser(createDTO);

        // Hash the password before saving
        user.setPassword(bcryptHashService.hashPassword(createDTO.getPassword()));

        return mapper.toUserDTO(userRepository.save(user));
    }

    @Transactional
    public UserDTO update(Long id, UserUpdateDTO updateDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + id));

        // Check if email is being changed and if the new email is already in use
        if (!user.getEmail().equals(updateDTO.getEmail()) &&
                userRepository.existsByEmail(updateDTO.getEmail())) {
            throw new InvalidDataException("Email already in use: " + updateDTO.getEmail());
        }

        // Update fields from DTO to the domain entity
        mapper.updateUserFromDTO(updateDTO, user);

        // Only hash and update password if it's provided in the DTO
        if (updateDTO.getPassword() != null && !updateDTO.getPassword().isEmpty()) {
            user.setPassword(bcryptHashService.hashPassword(updateDTO.getPassword()));
        } else {
            // Get the original user from repository again to ensure we have the current password
            User originalUser = userRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + id));
            user.setPassword(originalUser.getPassword());
        }

        return mapper.toUserDTO(userRepository.save(user));
    }

    @Transactional
    public void delete(Long id) {
        if (!userRepository.findById(id).isPresent()) {
            throw new EntityNotFoundException("User not found with ID: " + id);
        }
        userRepository.delete(id);
    }

    @Transactional
    public void hardDelete(Long id) {
        if (!userRepository.findById(id).isPresent()) {
            throw new EntityNotFoundException("User not found with ID: " + id);
        }

        // might want to add additional checks or validations here
        // For example, check if the user has related records before hard deletion

        boolean deleted = userRepository.hardDelete(id);
        if (!deleted) {
            throw new RuntimeException("Failed to delete user with ID: " + id);
        }
    }

    public List<UserActivityDTO> getUserActivity(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

        List<UserActivityDTO> activities = new ArrayList<>();

        // Get recent consultations
        userRepository.findRecentConsultationsByUser(userId, 10).forEach(consultation -> {
            activities.add(UserActivityDTO.builder()
                    .id(consultation.getId())
                    .type("consultation")
                    .description("Consulta realizada")
                    .details("Mascota: " + consultation.getPet().getName() + " - Cliente: " +
                            consultation.getPet().getCustomer().getName())
                    .date(consultation.getDate())
                    .build());
        });

        // Get recent vaccinations
        userRepository.findRecentVaccinationsByUser(userId, 10).forEach(vaccination -> {
            activities.add(UserActivityDTO.builder()
                    .id(vaccination.getId())
                    .type("vaccination")
                    .description("Vacunación aplicada")
                    .details("Mascota: " + vaccination.getPet().getName() + " - Vacuna: " +
                            vaccination.getVaccineType())
                    .date(vaccination.getApplicationDate().atStartOfDay())
                    .build());
        });

        // Add last login
        if (user.getLastAccess() != null) {
            activities.add(UserActivityDTO.builder()
                    .id(0L)
                    .type("login")
                    .description("Último acceso")
                    .details("Inicio de sesión")
                    .date(user.getLastAccess())
                    .build());
        }

        // Sort by date descending
        activities.sort((a, b) -> b.getDate().compareTo(a.getDate()));

        return activities;
    }

    public UserStatsDTO getUserStats(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

        // Only for veterinarians
        if (!"VETERINARIAN".equals(user.getRole())) {
            return UserStatsDTO.builder()
                    .consultations(0)
                    .vaccinations(0)
                    .appointments(0)
                    .build();
        }

        int consultations = userRepository.countConsultations(userId);
        int vaccinations = userRepository.countVaccinations(userId);
        int appointments = userRepository.countAppointments(userId);

        return UserStatsDTO.builder()
                .consultations(consultations)
                .vaccinations(vaccinations)
                .appointments(appointments)
                .build();
    }

    public Page<UserListDTO> searchUsers(String search, String role, Boolean active, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userRepository.searchUsers(search, role, active, pageable);
        return users.map(mapper::toUserListDTO);
    }

    @Transactional
    public Map<String, Object> uploadPhoto(Long userId, MultipartFile file) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

        // Validate file
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is required");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("File must be an image");
        }

        // Store file
        String filename = "user-" + userId + "-" + System.currentTimeMillis() +
                getFileExtension(file.getOriginalFilename());
        storageService.store(file, filename);

        // Update user photo URL
        String photoUrl = "/api/users/" + userId + "/photo/" + filename;
        user.setPhotoUrl(photoUrl);
        userRepository.save(user);

        Map<String, Object> response = new HashMap<>();
        response.put("photoUrl", photoUrl);
        response.put("success", true);

        return response;
    }

    private String getFileExtension(String filename) {
        if (filename == null) return "";
        int lastDot = filename.lastIndexOf('.');
        return lastDot > 0 ? filename.substring(lastDot) : "";
    }

}
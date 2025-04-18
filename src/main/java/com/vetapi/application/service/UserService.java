package com.vetapi.application.service;

import com.vetapi.application.dto.user.UserCreateDTO;
import com.vetapi.application.dto.user.UserDTO;
import com.vetapi.application.dto.user.UserListDTO;
import com.vetapi.application.dto.user.UserUpdateDTO;
import com.vetapi.application.mapper.UserDTOMapper;
import com.vetapi.domain.entity.User;
import com.vetapi.domain.exception.EntityNotFoundException;
import com.vetapi.domain.exception.InvalidDataException;
import com.vetapi.domain.repository.UserRepository;
import com.vetapi.infrastructure.security.BcryptHashService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserDTOMapper mapper;
    private final BcryptHashService bcryptHashService;

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
}
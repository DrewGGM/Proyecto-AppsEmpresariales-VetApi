package com.vetapi.application.service;

import com.vetapi.application.dto.user.UserCreateDTO;
import com.vetapi.application.dto.user.UserDTO;
import com.vetapi.application.dto.user.UserListDTO;
import com.vetapi.application.dto.user.UserUpdateDTO;
import com.vetapi.application.mapper.UserDTOMapper;
import com.vetapi.domain.entity.User;
import com.vetapi.domain.enums.Role;
import com.vetapi.domain.exception.EntityNotFoundException;
import com.vetapi.domain.exception.InvalidDataException;
import com.vetapi.domain.repository.UserRepository;
import com.vetapi.infrastructure.security.BcryptHashService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserDTOMapper mapper;

    @Mock
    private BcryptHashService bcryptHashService;

    @InjectMocks
    private UserService userService;

    private User user1;
    private User user2;
    private UserDTO userDTO1;
    private UserDTO userDTO2;
    private UserListDTO userListDTO1;
    private UserListDTO userListDTO2;
    private UserCreateDTO userCreateDTO;
    private UserUpdateDTO userUpdateDTO;
    private String hashedPassword;

    @BeforeEach
    void setUp() {
        // Setup timestamps
        LocalDateTime now = LocalDateTime.now();

        // Setup users
        user1 = new User();
        user1.setId(1L);
        user1.setName("John");
        user1.setLastName("Doe");
        user1.setEmail("john.doe@example.com");
        user1.setPassword("hashedpassword123");
        user1.setRole("VETERINARIAN");
        user1.setLastAccess(now);
        user1.setCreatedAt(now);
        user1.setUpdatedAt(now);
        user1.setActive(true);

        user2 = new User();
        user2.setId(2L);
        user2.setName("Jane");
        user2.setLastName("Smith");
        user2.setEmail("jane.smith@example.com");
        user2.setPassword("hashedpassword456");
        user2.setRole("ADMIN");
        user2.setLastAccess(now);
        user2.setCreatedAt(now);
        user2.setUpdatedAt(now);
        user2.setActive(true);

        // Setup DTOs
        userDTO1 = new UserDTO();
        userDTO1.setId(1L);
        userDTO1.setName("John");
        userDTO1.setLastName("Doe");
        userDTO1.setEmail("john.doe@example.com");
        userDTO1.setRole("VETERINARIAN");
        userDTO1.setLastAccess(now);
        userDTO1.setCreatedAt(now);
        userDTO1.setUpdatedAt(now);
        userDTO1.setActive(true);

        userDTO2 = new UserDTO();
        userDTO2.setId(2L);
        userDTO2.setName("Jane");
        userDTO2.setLastName("Smith");
        userDTO2.setEmail("jane.smith@example.com");
        userDTO2.setRole("ADMIN");
        userDTO2.setLastAccess(now);
        userDTO2.setCreatedAt(now);
        userDTO2.setUpdatedAt(now);
        userDTO2.setActive(true);

        userListDTO1 = new UserListDTO();
        userListDTO1.setId(1L);
        userListDTO1.setName("John");
        userListDTO1.setLastName("Doe");
        userListDTO1.setEmail("john.doe@example.com");
        userListDTO1.setRole("VETERINARIAN");
        userListDTO1.setActive(true);

        userListDTO2 = new UserListDTO();
        userListDTO2.setId(2L);
        userListDTO2.setName("Jane");
        userListDTO2.setLastName("Smith");
        userListDTO2.setEmail("jane.smith@example.com");
        userListDTO2.setRole("ADMIN");
        userListDTO2.setActive(true);

        // Setup create DTO
        userCreateDTO = new UserCreateDTO();
        userCreateDTO.setName("New");
        userCreateDTO.setLastName("User");
        userCreateDTO.setEmail("new.user@example.com");
        userCreateDTO.setPassword("password123");
        userCreateDTO.setRole(Role.RECEPTIONIST);

        // Setup update DTO
        userUpdateDTO = new UserUpdateDTO();
        userUpdateDTO.setName("Updated");
        userUpdateDTO.setLastName("User");
        userUpdateDTO.setEmail("john.doe@example.com");
        userUpdateDTO.setPassword("newpassword123");
        userUpdateDTO.setRole("VETERINARIAN");

        // Setup hashed password
        hashedPassword = "hashedpassword789";
    }

    @Test
    void findAll_ReturnsAllActiveUsers() {
        // Arrange
        List<User> users = Arrays.asList(user1, user2);
        List<UserListDTO> expectedDTOs = Arrays.asList(userListDTO1, userListDTO2);

        when(userRepository.findAll()).thenReturn(users);
        when(mapper.toUserListDTOList(users)).thenReturn(expectedDTOs);

        // Act
        List<UserListDTO> result = userService.findAll();

        // Assert
        assertEquals(expectedDTOs.size(), result.size());
        assertEquals(expectedDTOs, result);
        verify(userRepository, times(1)).findAll();
        verify(mapper, times(1)).toUserListDTOList(users);
    }

    @Test
    void findById_ExistingId_ReturnsUser() {
        // Arrange
        Long id = 1L;
        when(userRepository.findById(id)).thenReturn(Optional.of(user1));
        when(mapper.toUserDTO(user1)).thenReturn(userDTO1);

        // Act
        UserDTO result = userService.findById(id);

        // Assert
        assertNotNull(result);
        assertEquals(userDTO1, result);
        verify(userRepository, times(1)).findById(id);
        verify(mapper, times(1)).toUserDTO(user1);
    }

    @Test
    void findById_NonExistingId_ThrowsEntityNotFoundException() {
        // Arrange
        Long id = 999L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> userService.findById(id)
        );

        assertEquals("User not found with ID: " + id, exception.getMessage());
        verify(userRepository, times(1)).findById(id);
        verify(mapper, never()).toUserDTO(any(User.class));
    }

    @Test
    void findByEmail_ExistingEmail_ReturnsUser() {
        // Arrange
        String email = "john.doe@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user1));
        when(mapper.toUserDTO(user1)).thenReturn(userDTO1);

        // Act
        UserDTO result = userService.findByEmail(email);

        // Assert
        assertNotNull(result);
        assertEquals(userDTO1, result);
        verify(userRepository, times(1)).findByEmail(email);
        verify(mapper, times(1)).toUserDTO(user1);
    }

    @Test
    void findByEmail_NonExistingEmail_ThrowsEntityNotFoundException() {
        // Arrange
        String email = "nonexistent@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> userService.findByEmail(email)
        );

        assertEquals("User not found with email: " + email, exception.getMessage());
        verify(userRepository, times(1)).findByEmail(email);
        verify(mapper, never()).toUserDTO(any(User.class));
    }

    @Test
    void create_WithUniqueEmail_ReturnsCreatedUser() {
        // Arrange
        User newUser = new User();
        when(userRepository.existsByEmail(userCreateDTO.getEmail())).thenReturn(false);
        when(mapper.toUser(userCreateDTO)).thenReturn(newUser);
        when(bcryptHashService.hashPassword(userCreateDTO.getPassword())).thenReturn(hashedPassword);
        when(userRepository.save(newUser)).thenReturn(user1);
        when(mapper.toUserDTO(user1)).thenReturn(userDTO1);

        // Act
        UserDTO result = userService.create(userCreateDTO);

        // Assert
        assertNotNull(result);
        assertEquals(userDTO1, result);
        verify(userRepository, times(1)).existsByEmail(userCreateDTO.getEmail());
        verify(mapper, times(1)).toUser(userCreateDTO);
        verify(bcryptHashService, times(1)).hashPassword(userCreateDTO.getPassword());
        verify(userRepository, times(1)).save(newUser);
        verify(mapper, times(1)).toUserDTO(user1);
    }

    @Test
    void create_WithDuplicateEmail_ThrowsInvalidDataException() {
        // Arrange
        when(userRepository.existsByEmail(userCreateDTO.getEmail())).thenReturn(true);

        // Act & Assert
        InvalidDataException exception = assertThrows(
                InvalidDataException.class,
                () -> userService.create(userCreateDTO)
        );

        assertEquals("Email already in use: " + userCreateDTO.getEmail(), exception.getMessage());
        verify(userRepository, times(1)).existsByEmail(userCreateDTO.getEmail());
        verify(mapper, never()).toUser(any(UserCreateDTO.class));
        verify(bcryptHashService, never()).hashPassword(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void update_ExistingUser_WithoutChangingEmail_ReturnsUpdatedUser() {
        // Arrange
        Long id = 1L;
        when(userRepository.findById(id)).thenReturn(Optional.of(user1));
        when(userRepository.save(user1)).thenReturn(user1);
        when(mapper.toUserDTO(user1)).thenReturn(userDTO1);

        // Act
        UserDTO result = userService.update(id, userUpdateDTO);

        // Assert
        assertNotNull(result);
        assertEquals(userDTO1, result);
        verify(userRepository, times(1)).findById(id);
        verify(mapper, times(1)).updateUserFromDTO(userUpdateDTO, user1);
        verify(userRepository, times(1)).save(user1);
        verify(mapper, times(1)).toUserDTO(user1);
    }

    @Test
    void update_ExistingUser_WithChangingEmailToExistingOne_ThrowsInvalidDataException() {
        // Arrange
        Long id = 1L;
        UserUpdateDTO emailChangeDTO = new UserUpdateDTO();
        emailChangeDTO.setEmail("jane.smith@example.com"); // Already exists for user2

        when(userRepository.findById(id)).thenReturn(Optional.of(user1));
        when(userRepository.existsByEmail(emailChangeDTO.getEmail())).thenReturn(true);

        // Act & Assert
        InvalidDataException exception = assertThrows(
                InvalidDataException.class,
                () -> userService.update(id, emailChangeDTO)
        );

        assertEquals("Email already in use: " + emailChangeDTO.getEmail(), exception.getMessage());
        verify(userRepository, times(1)).findById(id);
        verify(userRepository, times(1)).existsByEmail(emailChangeDTO.getEmail());
        verify(mapper, never()).updateUserFromDTO(any(UserUpdateDTO.class), any(User.class));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void delete_ExistingUser_DeletesAndReturnsNothing() {
        // Arrange
        Long id = 1L;
        when(userRepository.findById(id)).thenReturn(Optional.of(user1));
        doNothing().when(userRepository).delete(id);

        // Act
        userService.delete(id);

        // Assert
        verify(userRepository, times(1)).findById(id);
        verify(userRepository, times(1)).delete(id);
    }

    @Test
    void delete_NonExistingUser_ThrowsEntityNotFoundException() {
        // Arrange
        Long id = 999L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> userService.delete(id)
        );

        assertEquals("User not found with ID: " + id, exception.getMessage());
        verify(userRepository, times(1)).findById(id);
        verify(userRepository, never()).delete(anyLong());
    }
}
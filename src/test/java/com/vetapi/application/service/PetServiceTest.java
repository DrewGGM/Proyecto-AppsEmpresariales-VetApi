package com.vetapi.application.service;

import com.vetapi.application.dto.pet.PetCreateDTO;
import com.vetapi.application.dto.pet.PetDTO;
import com.vetapi.application.dto.pet.PetListDTO;
import com.vetapi.application.dto.pet.PetUpdateDTO;
import com.vetapi.application.mapper.PetDTOMapper;
import com.vetapi.domain.entity.Customer;
import com.vetapi.domain.entity.Pet;
import com.vetapi.domain.exception.EntityNotFoundException;
import com.vetapi.domain.repository.CustomerRepository;
import com.vetapi.domain.repository.PetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PetServiceTest {

    @Mock
    private PetRepository petRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private PetDTOMapper mapper;

    @InjectMocks
    private PetService petService;

    private Pet pet1;
    private Pet pet2;
    private Customer customer;
    private PetDTO petDTO1;
    private PetDTO petDTO2;
    private PetListDTO petListDTO1;
    private PetListDTO petListDTO2;
    private PetCreateDTO petCreateDTO;
    private PetUpdateDTO petUpdateDTO;

    @BeforeEach
    void setUp() {
        // Setup timestamps
        LocalDateTime now = LocalDateTime.now();
        LocalDate birthDate = LocalDate.of(2020, 1, 1);

        // Setup customer
        customer = new Customer();
        customer.setId(1L);
        customer.setName("John");
        customer.setLastName("Doe");
        customer.setEmail("john.doe@example.com");
        customer.setPhone("1234567890");

        // Setup pets
        pet1 = new Pet();
        pet1.setId(1L);
        pet1.setName("Buddy");
        pet1.setBirthDate(birthDate);
        pet1.setSpecies("Dog");
        pet1.setBreed("Labrador");
        pet1.setGender("Male");
        pet1.setWeight(25.5f);
        pet1.setPhotoUrl("buddy.jpg");
        pet1.setCustomer(customer);
        pet1.setCreatedAt(now);
        pet1.setUpdatedAt(now);
        pet1.setActive(true);

        pet2 = new Pet();
        pet2.setId(2L);
        pet2.setName("Luna");
        pet2.setBirthDate(birthDate.plusYears(1));
        pet2.setSpecies("Cat");
        pet2.setBreed("Siamese");
        pet2.setGender("Female");
        pet2.setWeight(10.0f);
        pet2.setPhotoUrl("luna.jpg");
        pet2.setCustomer(customer);
        pet2.setCreatedAt(now);
        pet2.setUpdatedAt(now);
        pet2.setActive(true);

        // Setup DTOs
        petDTO1 = new PetDTO();
        petDTO1.setId(1L);
        petDTO1.setName("Buddy");
        petDTO1.setBirthDate(birthDate);
        petDTO1.setSpecies("Dog");
        petDTO1.setBreed("Labrador");
        petDTO1.setGender("Male");
        petDTO1.setWeight(25.5f);
        petDTO1.setPhotoUrl("buddy.jpg");
        petDTO1.setCustomerName("John Doe");
        petDTO1.setCustomerId(1L);
        petDTO1.setConsultationCount(2);
        petDTO1.setVaccinationCount(3);
        petDTO1.setAppointmentCount(1);
        petDTO1.setAge(3);
        petDTO1.setAdult(true);
        petDTO1.setCreatedAt(now);
        petDTO1.setUpdatedAt(now);
        petDTO1.setActive(true);

        petDTO2 = new PetDTO();
        petDTO2.setId(2L);
        petDTO2.setName("Luna");
        petDTO2.setBirthDate(birthDate.plusYears(1));
        petDTO2.setSpecies("Cat");
        petDTO2.setBreed("Siamese");
        petDTO2.setGender("Female");
        petDTO2.setWeight(10.0f);
        petDTO2.setPhotoUrl("luna.jpg");
        petDTO2.setCustomerName("John Doe");
        petDTO2.setCustomerId(1L);
        petDTO2.setConsultationCount(1);
        petDTO2.setVaccinationCount(2);
        petDTO2.setAppointmentCount(0);
        petDTO2.setAge(2);
        petDTO2.setAdult(true);
        petDTO2.setCreatedAt(now);
        petDTO2.setUpdatedAt(now);
        petDTO2.setActive(true);

        petListDTO1 = new PetListDTO();
        petListDTO1.setId(1L);
        petListDTO1.setName("Buddy");
        petListDTO1.setSpecies("Dog");
        petListDTO1.setBreed("Labrador");
        petListDTO1.setBirthDate(birthDate);
        petListDTO1.setCustomerName("John Doe");
        petListDTO1.setAge(3);
        petListDTO1.setActive(true);

        petListDTO2 = new PetListDTO();
        petListDTO2.setId(2L);
        petListDTO2.setName("Luna");
        petListDTO2.setSpecies("Cat");
        petListDTO2.setBreed("Siamese");
        petListDTO2.setBirthDate(birthDate.plusYears(1));
        petListDTO2.setCustomerName("John Doe");
        petListDTO2.setAge(2);
        petListDTO2.setActive(true);

        // Setup create DTO
        petCreateDTO = new PetCreateDTO();
        petCreateDTO.setName("Max");
        petCreateDTO.setBirthDate(LocalDate.of(2021, 5, 10));
        petCreateDTO.setSpecies("Dog");
        petCreateDTO.setBreed("Golden Retriever");
        petCreateDTO.setGender("Male");
        petCreateDTO.setWeight(20.0f);
        petCreateDTO.setPhotoUrl("max.jpg");
        petCreateDTO.setCustomerId(1L);

        // Setup update DTO
        petUpdateDTO = new PetUpdateDTO();
        petUpdateDTO.setName("Buddy Updated");
        petUpdateDTO.setBirthDate(birthDate);
        petUpdateDTO.setSpecies("Dog");
        petUpdateDTO.setBreed("Labrador Retriever");
        petUpdateDTO.setGender("Male");
        petUpdateDTO.setWeight(27.0f);
        petUpdateDTO.setPhotoUrl("buddy_updated.jpg");
    }

    @Test
    void findAll_ReturnsAllPets() {
        // Arrange
        List<Pet> pets = Arrays.asList(pet1, pet2);
        List<PetListDTO> expectedDTOs = Arrays.asList(petListDTO1, petListDTO2);

        when(petRepository.findAll()).thenReturn(pets);
        when(mapper.toPetListDTOList(pets)).thenReturn(expectedDTOs);

        // Act
        List<PetListDTO> result = petService.findAll();

        // Assert
        assertEquals(expectedDTOs.size(), result.size());
        assertEquals(expectedDTOs, result);
        verify(petRepository, times(1)).findAll();
        verify(mapper, times(1)).toPetListDTOList(pets);
    }

    @Test
    void findById_ExistingId_ReturnsPet() {
        // Arrange
        Long id = 1L;
        when(petRepository.findById(id)).thenReturn(Optional.of(pet1));
        when(mapper.toPetDTO(pet1)).thenReturn(petDTO1);

        // Act
        PetDTO result = petService.findById(id);

        // Assert
        assertNotNull(result);
        assertEquals(petDTO1, result);
        verify(petRepository, times(1)).findById(id);
        verify(mapper, times(1)).toPetDTO(pet1);
    }

    @Test
    void findById_NonExistingId_ThrowsEntityNotFoundException() {
        // Arrange
        Long id = 999L;
        when(petRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> petService.findById(id)
        );

        assertEquals("Pet not found with ID: " + id, exception.getMessage());
        verify(petRepository, times(1)).findById(id);
        verify(mapper, never()).toPetDTO(any(Pet.class));
    }

    @Test
    void findByCustomerId_ExistingCustomerId_ReturnsPets() {
        // Arrange
        Long customerId = 1L;
        List<Pet> pets = Arrays.asList(pet1, pet2);
        List<PetListDTO> expectedDTOs = Arrays.asList(petListDTO1, petListDTO2);

        when(customerRepository.existsById(customerId)).thenReturn(true);
        when(petRepository.findByCustomerId(customerId)).thenReturn(pets);
        when(mapper.toPetListDTOList(pets)).thenReturn(expectedDTOs);

        // Act
        List<PetListDTO> result = petService.findByCustomerId(customerId);

        // Assert
        assertEquals(expectedDTOs.size(), result.size());
        assertEquals(expectedDTOs, result);
        verify(customerRepository, times(1)).existsById(customerId);
        verify(petRepository, times(1)).findByCustomerId(customerId);
        verify(mapper, times(1)).toPetListDTOList(pets);
    }

    @Test
    void findByCustomerId_NonExistingCustomerId_ThrowsEntityNotFoundException() {
        // Arrange
        Long customerId = 999L;
        when(customerRepository.existsById(customerId)).thenReturn(false);

        // Act & Assert
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> petService.findByCustomerId(customerId)
        );

        assertEquals("Customer not found with ID: " + customerId, exception.getMessage());
        verify(customerRepository, times(1)).existsById(customerId);
        verify(petRepository, never()).findByCustomerId(anyLong());
        verify(mapper, never()).toPetListDTOList(anyList());
    }

    @Test
    void create_WithValidData_ReturnsCreatedPet() {
        // Arrange
        Pet newPet = new Pet();
        when(customerRepository.findById(petCreateDTO.getCustomerId())).thenReturn(Optional.of(customer));
        when(mapper.toPet(petCreateDTO)).thenReturn(newPet);
        when(petRepository.save(newPet)).thenReturn(pet1);
        when(mapper.toPetDTO(pet1)).thenReturn(petDTO1);

        // Act
        PetDTO result = petService.create(petCreateDTO);

        // Assert
        assertNotNull(result);
        assertEquals(petDTO1, result);
        verify(customerRepository, times(1)).findById(petCreateDTO.getCustomerId());
        verify(mapper, times(1)).toPet(petCreateDTO);
        verify(petRepository, times(1)).save(newPet);
        verify(mapper, times(1)).toPetDTO(pet1);
    }

    @Test
    void create_WithNonExistingCustomer_ThrowsEntityNotFoundException() {
        // Arrange
        when(customerRepository.findById(petCreateDTO.getCustomerId())).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> petService.create(petCreateDTO)
        );

        assertEquals("Customer not found with ID: " + petCreateDTO.getCustomerId(), exception.getMessage());
        verify(customerRepository, times(1)).findById(petCreateDTO.getCustomerId());
        verify(mapper, never()).toPet(any(PetCreateDTO.class));
        verify(petRepository, never()).save(any(Pet.class));
    }

    @Test
    void create_WithNegativeWeight_ThrowsIllegalArgumentException() {
        // Arrange
        petCreateDTO.setWeight(-5.0f);
        when(customerRepository.findById(petCreateDTO.getCustomerId())).thenReturn(Optional.of(customer));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> petService.create(petCreateDTO)
        );

        assertEquals("El peso debe ser un número positivo.", exception.getMessage());
        verify(customerRepository, times(1)).findById(anyLong());
        verify(mapper, never()).toPet(any(PetCreateDTO.class));
        verify(petRepository, never()).save(any(Pet.class));
    }

    @Test
    void create_WithFutureBirthDate_ThrowsIllegalArgumentException() {
        // Arrange
        petCreateDTO.setBirthDate(LocalDate.now().plusDays(1));
        when(customerRepository.findById(petCreateDTO.getCustomerId())).thenReturn(Optional.of(customer));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> petService.create(petCreateDTO)
        );

        assertEquals("La fecha de nacimiento no puede ser futura.", exception.getMessage());
        verify(customerRepository, times(1)).findById(anyLong());
        verify(mapper, never()).toPet(any(PetCreateDTO.class));
        verify(petRepository, never()).save(any(Pet.class));
    }


    @Test
    void update_ExistingPet_ReturnsUpdatedPet() {
        // Arrange
        Long id = 1L;
        when(petRepository.findById(id)).thenReturn(Optional.of(pet1));
        when(petRepository.update(eq(id), any(Pet.class))).thenReturn(pet1);
        when(mapper.toPetDTO(pet1)).thenReturn(petDTO1);

        // Act
        PetDTO result = petService.update(id, petUpdateDTO);

        // Assert
        assertNotNull(result);
        assertEquals(petDTO1, result);
        verify(petRepository, times(1)).findById(id);
        verify(mapper, times(1)).updatePetFromDTO(petUpdateDTO, pet1);
        verify(petRepository, times(1)).update(eq(id), any(Pet.class));
        verify(mapper, times(1)).toPetDTO(pet1);
    }

    @Test
    void update_NonExistingPet_ThrowsEntityNotFoundException() {
        // Arrange
        Long id = 999L;
        when(petRepository.findById(id)).thenThrow(new EntityNotFoundException("Pet not found with ID: " + id));

        // Act & Assert
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> petService.update(id, petUpdateDTO)
        );

        assertEquals("Pet not found with ID: " + id, exception.getMessage());
        verify(petRepository, times(1)).findById(id);
        verify(mapper, never()).updatePetFromDTO(any(PetUpdateDTO.class), any(Pet.class));
        verify(petRepository, never()).update(anyLong(), any(Pet.class));
    }

    @Test
    void delete_ExistingPetWithNoConsultations_DeletesAndReturnsNothing() {
        // Arrange
        Long id = 1L;
        when(petRepository.findById(id)).thenReturn(Optional.of(pet1));
        when(petRepository.hasConsultations(id)).thenReturn(false);
        doNothing().when(petRepository).delete(id);

        // Act
        petService.delete(id);

        // Assert
        verify(petRepository, times(1)).findById(id);
        verify(petRepository, times(1)).hasConsultations(id);
        verify(petRepository, times(1)).delete(id);
    }

    @Test
    void delete_ExistingPetWithConsultations_ThrowsIllegalStateException() {
        // Arrange
        Long id = 1L;
        when(petRepository.findById(id)).thenReturn(Optional.of(pet1));
        when(petRepository.hasConsultations(id)).thenReturn(true);

        // Act & Assert
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> petService.delete(id)
        );

        assertEquals("No se puede eliminar la mascota porque tiene consultas asociadas.", exception.getMessage());
        verify(petRepository, times(1)).findById(id);
        verify(petRepository, times(1)).hasConsultations(id);
        verify(petRepository, never()).delete(anyLong());
    }

    @Test
    void delete_NonExistingPet_ThrowsEntityNotFoundException() {
        // Arrange
        Long id = 999L;
        when(petRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> petService.delete(id)
        );

        assertEquals("Pet not found with ID: " + id, exception.getMessage());
        verify(petRepository, times(1)).findById(id);
        verify(petRepository, never()).hasConsultations(anyLong());
        verify(petRepository, never()).delete(anyLong());
    }

    @Test
    void countConsultations_ExistingPet_ReturnsCount() {
        // Arrange
        Long id = 1L;
        int expectedCount = 2;
        when(petRepository.findById(id)).thenReturn(Optional.of(pet1));
        when(petRepository.countConsultations(id)).thenReturn(expectedCount);

        // Act
        int result = petService.countConsultations(id);

        // Assert
        assertEquals(expectedCount, result);
        verify(petRepository, times(1)).findById(id);
        verify(petRepository, times(1)).countConsultations(id);
    }

    @Test
    void countVaccinations_ExistingPet_ReturnsCount() {
        // Arrange
        Long id = 1L;
        int expectedCount = 3;
        when(petRepository.findById(id)).thenReturn(Optional.of(pet1));
        when(petRepository.countVaccinations(id)).thenReturn(expectedCount);

        // Act
        int result = petService.countVaccinations(id);

        // Assert
        assertEquals(expectedCount, result);
        verify(petRepository, times(1)).findById(id);
        verify(petRepository, times(1)).countVaccinations(id);
    }
}
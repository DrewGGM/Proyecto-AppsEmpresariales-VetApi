package com.vetapi.application.service;

import com.vetapi.application.dto.consultation.ConsultationCreateDTO;
import com.vetapi.application.dto.consultation.ConsultationDTO;
import com.vetapi.application.dto.consultation.ConsultationUpdateDTO;
import com.vetapi.application.dto.document.DocumentDTO;
import com.vetapi.application.dto.treatment.TreatmentCreateDTO;
import com.vetapi.application.dto.treatment.TreatmentDTO;
import com.vetapi.application.mapper.ConsultationDTOMapper;
import com.vetapi.application.mapper.DocumentDTOMapper;
import com.vetapi.application.mapper.TreatmentDTOMapper;
import com.vetapi.domain.entity.Consultation;
import com.vetapi.domain.entity.Document;
import com.vetapi.domain.entity.Pet;
import com.vetapi.domain.entity.Treatment;
import com.vetapi.domain.entity.User;
import com.vetapi.domain.exception.EntityNotFoundException;
import com.vetapi.domain.repository.ConsultationRepository;
import com.vetapi.domain.repository.DocumentRepository;
import com.vetapi.domain.repository.PetRepository;
import com.vetapi.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConsultationService {
    private final ConsultationRepository repository;
    private final ConsultationDTOMapper mapper;
    private final TreatmentDTOMapper treatmentMapper;
    private final DocumentDTOMapper documentMapper;
    private final UserRepository userRepository;
    private final DocumentRepository documentRepository;
    private final PetRepository petRepository;
    private final TreatmentService treatmentService;
    private final StorageService storageService;

    @Transactional
    public ConsultationDTO save(ConsultationCreateDTO consultation) {
        // Validar la fecha
        if (consultation.getDate() != null && consultation.getDate().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha de consulta no puede ser una fecha futura");
        }

        Consultation consultation1 = mapper.toConsultation(consultation);

        Pet pet = petRepository.findById(consultation.getPetId())
                .orElseThrow(() -> new EntityNotFoundException("Pet not found with ID: " + consultation.getPetId()));

        User userV = userRepository.findById(consultation.getVeterinarianId())
                .orElseThrow(() -> new EntityNotFoundException("Veterinarian not found with ID: " + consultation.getVeterinarianId()));

        consultation1.setPet(pet);
        consultation1.setVeterinarian(userV);
        return mapper.toConsultationDto(repository.save(consultation1));
    }

    public ConsultationDTO findById(Long id) {
        return repository.findById(id)
                .map(mapper::toConsultationDto)
                .orElseThrow(() -> new EntityNotFoundException("Consultation not found with ID: " + id));
    }

    public List<ConsultationDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toConsultationDto)
                .collect(Collectors.toList());
    }

    public List<ConsultationDTO> findByVeterinarian(Long idVeterinarian) {
        // Verificar que el veterinario existe
        userRepository.findById(idVeterinarian)
                .orElseThrow(() -> new EntityNotFoundException("Veterinarian not found with ID: " + idVeterinarian));

        return repository.findByVeterinarian(idVeterinarian)
                .stream()
                .map(mapper::toConsultationDto)
                .collect(Collectors.toList());
    }

    public List<ConsultationDTO> findByPet(Long idPet) {
        // Verificar que la mascota existe
        petRepository.findById(idPet)
                .orElseThrow(() -> new EntityNotFoundException("Pet not found with ID: " + idPet));

        return repository.findByPet(idPet)
                .stream()
                .map(mapper::toConsultationDto)
                .collect(Collectors.toList());
    }

    public List<ConsultationDTO> findByDate(LocalDate date) {
        return repository.findByDate(date)
                .stream()
                .map(mapper::toConsultationDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ConsultationDTO update(ConsultationUpdateDTO updateDTO, Long id) {
        Consultation consultation = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Consultation not found with ID: " + id));

        // Validar la fecha
        if (updateDTO.getDate() != null && updateDTO.getDate().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha de consulta no puede ser una fecha futura");
        }

        mapper.updateConsultationFromDTO(updateDTO, consultation);
        return mapper.toConsultationDto(repository.save(consultation));
    }

    @Transactional
    public void delete(Long id) {
        Consultation consultation = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Consultation not found with ID: " + id));

        repository.delete(id);
    }

    @Transactional
    public TreatmentDTO addTreatment(Long consultationId, TreatmentCreateDTO treatmentDTO) {
        // Verificar que la consulta existe
        Consultation consultation = repository.findById(consultationId)
                .orElseThrow(() -> new EntityNotFoundException("Consultation not found with ID: " + consultationId));

        // Verificar que el consultationId en el DTO coincide con el ID de la URL
        if (!treatmentDTO.getConsultationId().equals(consultationId)) {
            throw new IllegalArgumentException("Consultation ID in treatment does not match URL consultation ID");
        }

        // Verificar que la mascota coincide con la de la consulta
        if (!treatmentDTO.getPetId().equals(consultation.getPet().getId())) {
            throw new IllegalArgumentException("Pet ID in treatment does not match consultation's pet");
        }

        // Delegar al servicio de tratamientos
        return treatmentService.save(treatmentDTO);
    }

    @Transactional
    public DocumentDTO addDocument(Long consultationId, MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is required");
        }

        // Verificar extensiones permitidas
        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null) {
            String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
            if (extension.equals("exe") || extension.equals("bat") || extension.equals("sh")) {
                throw new IllegalArgumentException("File format not allowed");
            }
        }

        // Verificar que la consulta existe
        Consultation consultation = repository.findById(consultationId)
                .orElseThrow(() -> new EntityNotFoundException("Consultation not found with ID: " + consultationId));

        // Almacenar el archivo y obtener la URL
        String fileUrl = storageService.store(file);

        // Crear el objeto Document
        Document document = new Document();
        document.setConsultation(consultation);
        document.setName(file.getOriginalFilename());
        document.setType(file.getContentType());
        document.setUrl(fileUrl);
        document.setSize(file.getSize());
        document.setActive(true); // Establecer activo a true

        // Guardar en base de datos usando el repositorio
        document = documentRepository.save(document);

        // Devolver el DTO
        return documentMapper.toDocumentDTO(document);
    }
}
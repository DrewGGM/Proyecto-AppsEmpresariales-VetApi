package com.vetapi.application.service;

import com.vetapi.application.dto.document.DocumentCreateDTO;
import com.vetapi.application.dto.document.DocumentDTO;
import com.vetapi.application.dto.document.DocumentListDTO;
import com.vetapi.application.dto.document.DocumentUpdateDTO;
import com.vetapi.application.mapper.DocumentDTOMapper;
import com.vetapi.domain.entity.Consultation;
import com.vetapi.domain.entity.Document;
import com.vetapi.domain.exception.EntityNotFoundException;
import com.vetapi.domain.exception.StorageException;
import com.vetapi.domain.repository.ConsultationRepository;
import com.vetapi.domain.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final ConsultationRepository consultationRepository;
    private final StorageService storageService;
    private final DocumentDTOMapper mapper;

    public List<DocumentListDTO> findAll() {
        return mapper.toDocumentListDTOList(documentRepository.findAll());
    }

    public DocumentDTO findById(Long id) {
        return documentRepository.findById(id)
                .map(mapper::toDocumentDTO)
                .orElseThrow(() -> new EntityNotFoundException("Document not found with ID: " + id));
    }

    public List<DocumentListDTO> findByConsultationId(Long consultationId) {
        return mapper.toDocumentListDTOList(documentRepository.findByConsultationId(consultationId));
    }

    @Transactional
    public DocumentDTO uploadFile(Long consultationId, MultipartFile file) {
        Consultation consultation = consultationRepository.findById(consultationId)
                .orElseThrow(() -> new EntityNotFoundException("Consultation not found with ID: " + consultationId));

        // Store the file and get the generated filename
        String filename = storageService.store(file);

        // Create document entity with file metadata
        Document document = new Document();
        document.setConsultation(consultation);
        document.setName(file.getOriginalFilename());
        document.setType(file.getContentType());
        document.setUrl(filename); // Use the stored filename as URL
        document.setSize(file.getSize());

        return mapper.toDocumentDTO(documentRepository.save(document));
    }

    @Transactional
    public DocumentDTO create(DocumentCreateDTO createDTO) {
        Consultation consultation = consultationRepository.findById(createDTO.getConsultationId())
                .orElseThrow(() -> new EntityNotFoundException("Consultation not found with ID: " + createDTO.getConsultationId()));

        Document document = mapper.toDocument(createDTO);
        document.setConsultation(consultation);

        return mapper.toDocumentDTO(documentRepository.save(document));
    }

    @Transactional
    public DocumentDTO update(Long id, DocumentUpdateDTO updateDTO) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Document not found with ID: " + id));

        mapper.updateDocumentFromDTO(updateDTO, document);
        return mapper.toDocumentDTO(documentRepository.save(document));
    }

    @Transactional
    public void delete(Long id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Document not found with ID: " + id));

        // Delete the physical file
        try {
            storageService.delete(document.getUrl());
        } catch (StorageException e) {
            // Log the error but continue with DB deletion
            System.err.println("Failed to delete physical file: " + e.getMessage());
        }

        // Delete DB record (soft delete)
        documentRepository.delete(id);
    }

    public Resource getFileAsResource(Long documentId) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new EntityNotFoundException("Document not found with ID: " + documentId));

        return storageService.loadAsResource(document.getUrl());
    }

    public long getTotalDocumentSize(Long consultationId) {
        return documentRepository.getTotalDocumentSize(consultationId);
    }
}
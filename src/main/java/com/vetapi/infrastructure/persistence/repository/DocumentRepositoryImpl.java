package com.vetapi.infrastructure.persistence.repository;

import com.vetapi.domain.entity.Document;
import com.vetapi.domain.repository.DocumentRepository;
import com.vetapi.infrastructure.persistence.crud.DocumentCrudRepository;
import com.vetapi.infrastructure.persistence.entity.DocumentEntity;
import com.vetapi.infrastructure.persistence.mapper.DocumentEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class DocumentRepositoryImpl implements DocumentRepository {

    private final DocumentCrudRepository crudRepository;
    private final DocumentEntityMapper mapper;

    @Override
    public List<Document> findAll() {
        return crudRepository.findByActiveTrue()
                .stream()
                .map(mapper::toDocument)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Document> findById(Long id) {
        return crudRepository.findById(id).map(mapper::toDocument);
    }

    @Override
    public List<Document> findByConsultationId(Long consultationId) {
        return crudRepository.findByConsultationIdAndActiveTrue(consultationId)
                .stream()
                .map(mapper::toDocument)
                .collect(Collectors.toList());
    }

    @Override
    public List<Document> findByType(String type) {
        return crudRepository.findByTypeAndActiveTrue(type)
                .stream()
                .map(mapper::toDocument)
                .collect(Collectors.toList());
    }

    @Override
    public Document save(Document document) {
        DocumentEntity entity = mapper.toEntity(document);

        // Set consultation relationship if document has a consultation
        if (document.getConsultation() != null) {
            entity.setConsultation(crudRepository.getReferenceById(document.getConsultation().getId()).getConsultation());
        }

        return mapper.toDocument(crudRepository.save(entity));
    }

    @Override
    public void delete(Long id) {
        crudRepository.findById(id).ifPresent(entity -> {
            entity.setActive(false);
            crudRepository.save(entity);
        });
    }

    @Override
    public int countByConsultationId(Long consultationId) {
        return crudRepository.countByConsultationIdAndActiveTrue(consultationId);
    }

    @Override
    public long getTotalDocumentSize(Long consultationId) {
        return crudRepository.sumSizeByConsultationId(consultationId);
    }
}
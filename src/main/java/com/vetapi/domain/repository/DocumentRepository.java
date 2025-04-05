package com.vetapi.domain.repository;

import com.vetapi.domain.entity.Document;

import java.util.List;
import java.util.Optional;

public interface DocumentRepository {

    // Get all active documents
    List<Document> findAll();

    // Get a document by its id
    Optional<Document> findById(Long id);

    // Get documents by consultation id
    List<Document> findByConsultationId(Long consultationId);

    // Get documents by type
    List<Document> findByType(String type);

    // Save a document (create or update)
    Document save(Document document);

    // Soft delete a document
    void delete(Long id);

    // Count documents by consultation id
    int countByConsultationId(Long consultationId);

    // Get total size of documents for a consultation
    long getTotalDocumentSize(Long consultationId);
}
package com.vetapi.web.controller;

import com.vetapi.application.dto.document.DocumentCreateDTO;
import com.vetapi.application.dto.document.DocumentDTO;
import com.vetapi.application.dto.document.DocumentListDTO;
import com.vetapi.application.dto.document.DocumentUpdateDTO;
import com.vetapi.application.service.DocumentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @GetMapping
    public ResponseEntity<List<DocumentListDTO>> findAll() {
        return ResponseEntity.ok(documentService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(documentService.findById(id));
    }

    @GetMapping("/consultation/{consultationId}")
    public ResponseEntity<List<DocumentListDTO>> findByConsultationId(@PathVariable Long consultationId) {
        return ResponseEntity.ok(documentService.findByConsultationId(consultationId));
    }

    @GetMapping("/consultation/{consultationId}/size")
    public ResponseEntity<Map<String, Long>> getTotalDocumentSize(@PathVariable Long consultationId) {
        long totalSize = documentService.getTotalDocumentSize(consultationId);
        return ResponseEntity.ok(Map.of("totalSize", totalSize));
    }

    @PostMapping
    public ResponseEntity<DocumentDTO> create(@Valid @RequestBody DocumentCreateDTO createDTO) {
        return new ResponseEntity<>(documentService.create(createDTO), HttpStatus.CREATED);
    }

    @PostMapping("/upload/{consultationId}")
    public ResponseEntity<DocumentDTO> uploadFile(
            @PathVariable Long consultationId,
            @RequestParam("file") MultipartFile file) {

        DocumentDTO documentDTO = documentService.uploadFile(consultationId, file);
        return new ResponseEntity<>(documentDTO, HttpStatus.CREATED);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) {
        Resource file = documentService.getFileAsResource(id);
        DocumentDTO document = documentService.findById(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + document.getName() + "\"")
                .contentLength(document.getSize())
                .contentType(MediaType.parseMediaType(document.getType()))
                .body(file);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DocumentDTO> update(@PathVariable Long id, @Valid @RequestBody DocumentUpdateDTO updateDTO) {
        return ResponseEntity.ok(documentService.update(id, updateDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        documentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
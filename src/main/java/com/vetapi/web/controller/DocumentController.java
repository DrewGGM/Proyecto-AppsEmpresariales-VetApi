package com.vetapi.web.controller;

import com.vetapi.application.dto.document.DocumentCreateDTO;
import com.vetapi.application.dto.document.DocumentDTO;
import com.vetapi.application.dto.document.DocumentListDTO;
import com.vetapi.application.dto.document.DocumentUpdateDTO;
import com.vetapi.application.service.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Documentos", description = "API para la gestión de documentos y archivos médicos")
public class DocumentController {

    private final DocumentService documentService;

    @GetMapping
    @Operation(summary = "Obtener todos los documentos",
            description = "Devuelve una lista resumida de todos los documentos registrados en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Lista de documentos obtenida correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DocumentListDTO.class))),
            @ApiResponse(responseCode = "204",
                    description = "Lista vacía - No hay documentos registrados",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content)
    })
    public ResponseEntity<List<DocumentListDTO>> findAll() {
        return ResponseEntity.ok(documentService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener documento por ID",
            description = "Devuelve un documento específico según su ID con información detallada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Documento encontrado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DocumentDTO.class))),
            @ApiResponse(responseCode = "404",
                    description = "Documento no encontrado",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content)
    })
    public ResponseEntity<DocumentDTO> findById(
            @Parameter(description = "ID del documento", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(documentService.findById(id));
    }

    @GetMapping("/consultation/{consultationId}")
    @Operation(summary = "Buscar documentos por consulta",
            description = "Devuelve todos los documentos asociados a una consulta médica específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Lista de documentos obtenida correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DocumentListDTO.class))),
            @ApiResponse(responseCode = "404",
                    description = "Consulta no encontrada",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content)
    })
    public ResponseEntity<List<DocumentListDTO>> findByConsultationId(
            @Parameter(description = "ID de la consulta médica", required = true)
            @PathVariable Long consultationId) {
        return ResponseEntity.ok(documentService.findByConsultationId(consultationId));
    }

    @GetMapping("/consultation/{consultationId}/size")
    @Operation(summary = "Obtener tamaño total de documentos de una consulta",
            description = "Calcula y devuelve el tamaño total en bytes de todos los documentos asociados a una consulta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Tamaño total calculado correctamente",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404",
                    description = "Consulta no encontrada",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content)
    })
    public ResponseEntity<Map<String, Long>> getTotalDocumentSize(
            @Parameter(description = "ID de la consulta médica", required = true)
            @PathVariable Long consultationId) {
        long totalSize = documentService.getTotalDocumentSize(consultationId);
        return ResponseEntity.ok(Map.of("totalSize", totalSize));
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo documento",
            description = "Registra un nuevo documento con los datos proporcionados (metadatos sin archivo)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Documento creado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DocumentDTO.class))),
            @ApiResponse(responseCode = "400",
                    description = "Datos inválidos o consulta no encontrada",
                    content = @Content),
            @ApiResponse(responseCode = "422",
                    description = "Entidad no procesable - datos no cumplen validaciones",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content)
    })
    public ResponseEntity<DocumentDTO> create(@Valid @RequestBody DocumentCreateDTO createDTO) {
        return new ResponseEntity<>(documentService.create(createDTO), HttpStatus.CREATED);
    }

    @PostMapping("/upload/{consultationId}")
    @Operation(summary = "Subir archivo a una consulta",
            description = "Carga un archivo y lo asocia a una consulta médica específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Archivo subido correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DocumentDTO.class))),
            @ApiResponse(responseCode = "400",
                    description = "Archivo inválido o consulta no encontrada",
                    content = @Content),
            @ApiResponse(responseCode = "413",
                    description = "Archivo demasiado grande",
                    content = @Content),
            @ApiResponse(responseCode = "415",
                    description = "Tipo de archivo no soportado",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content)
    })
    public ResponseEntity<DocumentDTO> uploadFile(
            @Parameter(description = "ID de la consulta médica", required = true)
            @PathVariable Long consultationId,
            @Parameter(description = "Archivo a subir", required = true)
            @RequestParam("file") MultipartFile file) {

        DocumentDTO documentDTO = documentService.uploadFile(consultationId, file);
        return new ResponseEntity<>(documentDTO, HttpStatus.CREATED);
    }

    @GetMapping("/download/{id}")
    @Operation(summary = "Descargar documento",
            description = "Descarga el archivo asociado a un documento específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Archivo encontrado y listo para descarga",
                    content = @Content(mediaType = "application/octet-stream")),
            @ApiResponse(responseCode = "404",
                    description = "Documento no encontrado",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Error interno del servidor o archivo no disponible",
                    content = @Content)
    })
    public ResponseEntity<Resource> downloadFile(
            @Parameter(description = "ID del documento", required = true)
            @PathVariable Long id) {
        Resource file = documentService.getFileAsResource(id);
        DocumentDTO document = documentService.findById(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + document.getName() + "\"")
                .contentLength(document.getSize())
                .contentType(MediaType.parseMediaType(document.getType()))
                .body(file);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un documento",
            description = "Actualiza los metadatos de un documento existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Documento actualizado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DocumentDTO.class))),
            @ApiResponse(responseCode = "400",
                    description = "Datos inválidos",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Documento no encontrado",
                    content = @Content),
            @ApiResponse(responseCode = "422",
                    description = "Entidad no procesable - datos no cumplen validaciones",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content)
    })
    public ResponseEntity<DocumentDTO> update(
            @Parameter(description = "ID del documento", required = true)
            @PathVariable Long id,
            @Valid @RequestBody DocumentUpdateDTO updateDTO) {
        return ResponseEntity.ok(documentService.update(id, updateDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un documento",
            description = "Elimina un documento y su archivo asociado según su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Documento eliminado correctamente",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Documento no encontrado",
                    content = @Content),
            @ApiResponse(responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content)
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID del documento", required = true)
            @PathVariable Long id) {
        documentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
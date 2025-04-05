package com.vetapi.application.dto.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentDTO {
    private Long id;
    private Long consultationId;
    private String consultationDate;
    private String petName;
    private String name;
    private String type;
    private String url;
    private Long size;
    private String extension;
    private boolean isImage;
    private boolean isPDF;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean active;
}
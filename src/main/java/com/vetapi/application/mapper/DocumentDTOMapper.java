package com.vetapi.application.mapper;

import com.vetapi.application.dto.document.DocumentCreateDTO;
import com.vetapi.application.dto.document.DocumentDTO;
import com.vetapi.application.dto.document.DocumentListDTO;
import com.vetapi.application.dto.document.DocumentUpdateDTO;
import com.vetapi.domain.entity.Document;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DocumentDTOMapper {

    @Mapping(target = "consultationId", source = "consultation.id")
    @Mapping(target = "consultationDate", expression = "java(document.getConsultation() != null ? document.getConsultation().getDate().toString() : null)")
    @Mapping(target = "petName", expression = "java(document.getConsultation() != null && document.getConsultation().getPet() != null ? document.getConsultation().getPet().getName() : null)")
    @Mapping(target = "extension", expression = "java(document.getExtension())")
    @Mapping(target = "isImage", expression = "java(document.isImage())")
    @Mapping(target = "isPDF", expression = "java(document.isPDF())")
    DocumentDTO toDocumentDTO(Document document);

    List<DocumentDTO> toDocumentDTOList(List<Document> documents);

    @Mapping(target = "petName", expression = "java(document.getConsultation() != null && document.getConsultation().getPet() != null ? document.getConsultation().getPet().getName() : null)")
    @Mapping(target = "extension", expression = "java(document.getExtension())")
    @Mapping(target = "isImage", expression = "java(document.isImage())")
    @Mapping(target = "isPDF", expression = "java(document.isPDF())")
    DocumentListDTO toDocumentListDTO(Document document);

    List<DocumentListDTO> toDocumentListDTOList(List<Document> documents);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "active", constant = "true")
    @Mapping(target = "consultation", ignore = true)
    Document toDocument(DocumentCreateDTO createDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "consultation", ignore = true)
    void updateDocumentFromDTO(DocumentUpdateDTO updateDTO, @MappingTarget Document document);
}
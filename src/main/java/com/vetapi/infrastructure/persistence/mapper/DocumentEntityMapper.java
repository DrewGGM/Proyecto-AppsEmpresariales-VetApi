package com.vetapi.infrastructure.persistence.mapper;

import com.vetapi.domain.entity.Document;
import com.vetapi.infrastructure.persistence.entity.DocumentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DocumentEntityMapper {

    @Mapping(target = "consultation", ignore = true)
    Document toDocument(DocumentEntity entity);

    @Mapping(target = "consultation", ignore = true)
    DocumentEntity toEntity(Document document);
}
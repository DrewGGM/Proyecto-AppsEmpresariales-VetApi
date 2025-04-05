package com.vetapi.infrastructure.persistence.crud;

import com.vetapi.infrastructure.persistence.entity.DocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentCrudRepository extends JpaRepository<DocumentEntity, Long> {

    List<DocumentEntity> findByActiveTrue();

    List<DocumentEntity> findByConsultationIdAndActiveTrue(Long consultationId);

    List<DocumentEntity> findByTypeAndActiveTrue(String type);

    int countByConsultationIdAndActiveTrue(Long consultationId);

    @Query("SELECT COALESCE(SUM(d.size), 0) FROM DocumentEntity d WHERE d.consultation.id = :consultationId AND d.active = true")
    long sumSizeByConsultationId(@Param("consultationId") Long consultationId);
}
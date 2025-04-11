package com.vetapi.domain.entity;

import com.vetapi.domain.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Document extends BaseEntity {
    private Consultation consultation;
    private String name;
    private String type;
    private String url;
    private Long size;

    public String getExtension() {
        if (this.name == null) return null;
        int lastDotIndex = this.name.lastIndexOf('.');
        return lastDotIndex > 0 ? this.name.substring(lastDotIndex + 1) : null;
    }

    public boolean isImage() {
        String ext = getExtension();
        return ext != null && (ext.equalsIgnoreCase("jpg") ||
                ext.equalsIgnoreCase("jpeg") ||
                ext.equalsIgnoreCase("png") ||
                ext.equalsIgnoreCase("gif"));
    }

    public boolean isPDF() {
        String ext = getExtension();
        return ext != null && ext.equalsIgnoreCase("pdf");
    }
}
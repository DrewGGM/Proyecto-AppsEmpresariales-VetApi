package com.vetapi.infrastructure.storage;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
public class SupabaseStorageService {

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.anon.key}")
    private String supabaseAnonKey;

    @Value("${supabase.storage.bucket:user-photos}")
    private String bucketName;

    private final RestTemplate restTemplate;

    public SupabaseStorageService() {
        this.restTemplate = new RestTemplate();
    }

    /**
     * Sube una imagen a Supabase Storage y devuelve la URL pública
     */
    public String uploadImage(MultipartFile file, Long userId) throws IOException {
        // Validar el archivo
        validateImageFile(file);

        // Generar nombre único para el archivo
        String fileName = generateFileName(file, userId);

        // Construir la URL del endpoint de Supabase Storage
        String uploadUrl = String.format("%s/storage/v1/object/%s/%s",
                supabaseUrl, bucketName, fileName);

        // Configurar headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + supabaseAnonKey);
        headers.setContentType(MediaType.parseMediaType(file.getContentType()));
        headers.set("x-upsert", "true"); // Sobrescribir si existe

        // Crear la request
        HttpEntity<byte[]> request = new HttpEntity<>(file.getBytes(), headers);

        try {
            // Subir el archivo
            ResponseEntity<String> response = restTemplate.exchange(
                    uploadUrl,
                    HttpMethod.POST,
                    request,
                    String.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                // Devolver la URL pública del archivo
                return getPublicUrl(fileName);
            } else {
                throw new RuntimeException("Error uploading file to Supabase: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error uploading file to Supabase", e);
        }
    }

    /**
     * Elimina una imagen de Supabase Storage
     */
    public void deleteImage(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return;
        }

        // Extraer solo el nombre del archivo si es una URL completa
        String fileNameOnly = extractFileName(fileName);

        String deleteUrl = String.format("%s/storage/v1/object/%s/%s",
                supabaseUrl, bucketName, fileNameOnly);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + supabaseAnonKey);

        HttpEntity<String> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    deleteUrl,
                    HttpMethod.DELETE,
                    request,
                    String.class
            );
            System.out.println("File deleted successfully from Supabase: " + fileNameOnly);
        } catch (Exception e) {
            // Log as info, not error - file might not exist (which is fine)
            if (e.getMessage().contains("not_found") || e.getMessage().contains("404")) {
                System.out.println("Previous file not found in Supabase (normal): " + fileNameOnly);
            } else {
                System.err.println("Error deleting file from Supabase: " + e.getMessage());
            }
        }
    }

    /**
     * Obtiene la URL pública de un archivo
     */
    public String getPublicUrl(String fileName) {
        return String.format("%s/storage/v1/object/public/%s/%s",
                supabaseUrl, bucketName, fileName);
    }

    /**
     * Valida que el archivo sea una imagen válida
     */
    private void validateImageFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("El archivo está vacío");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("El archivo debe ser una imagen");
        }

        // Validar tamaño (máximo 10MB)
        long maxSize = 10 * 1024 * 1024; // 10MB
        if (file.getSize() > maxSize) {
            throw new IllegalArgumentException("El archivo excede el tamaño máximo de 10MB");
        }

        // Validar extensiones permitidas
        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null) {
            String extension = getFileExtension(originalFilename).toLowerCase();
            if (!isValidImageExtension(extension)) {
                throw new IllegalArgumentException("Formato de imagen no permitido. Use: jpg, jpeg, png, gif, webp");
            }
        }
    }

    /**
     * Genera un nombre único para el archivo
     */
    private String generateFileName(MultipartFile file, Long userId) {
        String extension = getFileExtension(file.getOriginalFilename());
        String timestamp = String.valueOf(System.currentTimeMillis());
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        return String.format("user-%d-%s-%s%s", userId, timestamp, uuid, extension);
    }

    /**
     * Obtiene la extensión del archivo
     */
    private String getFileExtension(String filename) {
        if (filename == null) return "";
        int lastDot = filename.lastIndexOf('.');
        return lastDot > 0 ? filename.substring(lastDot) : "";
    }

    /**
     * Verifica si la extensión es válida para imágenes
     */
    private boolean isValidImageExtension(String extension) {
        return extension.equals(".jpg") ||
                extension.equals(".jpeg") ||
                extension.equals(".png") ||
                extension.equals(".gif") ||
                extension.equals(".webp");
    }

    /**
     * Extrae el nombre del archivo de una URL completa
     */
    private String extractFileName(String urlOrFileName) {
        if (urlOrFileName.contains("/")) {
            return urlOrFileName.substring(urlOrFileName.lastIndexOf("/") + 1);
        }
        return urlOrFileName;
    }
}
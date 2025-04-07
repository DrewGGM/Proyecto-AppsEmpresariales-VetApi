package com.vetapi.web.exception;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.vetapi.domain.enums.Role;
import com.vetapi.domain.exception.EntityNotFoundException;
import com.vetapi.domain.exception.InvalidDataException;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleEntityNotFoundException(EntityNotFoundException ex) {
        logger.error("Entidad no encontrada: {}", ex.getMessage());

        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Recurso no encontrado");
        errorResponse.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<Map<String, String>> handleInvalidDataException(InvalidDataException ex) {
        logger.error("Datos inválidos: {}", ex.getMessage());

        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Datos inválidos");
        errorResponse.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolationException(ConstraintViolationException ex) {
        logger.error("Violación de restricciones: {}", ex.getMessage());

        Map<String, Object> errorResponse = new HashMap<>();
        Map<String, String> validationErrors = new HashMap<>();

        ex.getConstraintViolations().forEach(violation -> {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            validationErrors.put(fieldName, errorMessage);
        });

        errorResponse.put("error", "Error de validación");
        errorResponse.put("details", validationErrors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        logger.error("Error de validación en argumentos: {}", ex.getMessage());

        Map<String, Object> errorResponse = new HashMap<>();
        Map<String, String> validationErrors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            validationErrors.put(fieldName, errorMessage);
        });

        errorResponse.put("error", "Error de validación");
        errorResponse.put("details", validationErrors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        logger.error("Error de tipo de argumento: {}", ex.getMessage());

        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Parámetro inválido");
        errorResponse.put("message", "El valor proporcionado para '" + ex.getName() + "' debe ser de tipo " + ex.getRequiredType().getSimpleName());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidFormatException(InvalidFormatException ex) {
        logger.error("Formato inválido al deserializar JSON: {}", ex.getMessage());

        Map<String, Object> errorResponse = new HashMap<>();

        String fieldName = "unknown";
        if (!ex.getPath().isEmpty()) {
            JsonMappingException.Reference reference = ex.getPath().get(0);
            fieldName = reference.getFieldName();
        }

        String errorMessage;
        Class<?> targetType = ex.getTargetType();

        if (targetType.isEnum()) {
            // Si es un enum, mostrar los valores permitidos
            errorMessage = "El valor proporcionado para el campo '" + fieldName +
                    "' no es válido. Valores permitidos: " + Arrays.toString(targetType.getEnumConstants());
        } else if (targetType == LocalDate.class || targetType == LocalDateTime.class) {
            // Si es una fecha, mostrar el formato esperado
            String expectedFormat = targetType == LocalDate.class ? "YYYY-MM-DD" : "YYYY-MM-DDTHH:MM:SS";
            errorMessage = "El formato de fecha para el campo '" + fieldName +
                    "' no es válido. Formato esperado: " + expectedFormat;
        } else {
            // Para otros tipos, mensaje genérico
            errorMessage = "El valor proporcionado para el campo '" + fieldName +
                    "' no es válido para el tipo " + targetType.getSimpleName();
        }

        errorResponse.put("error", "Formato inválido");
        errorResponse.put("message", errorMessage);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<Map<String, String>> handleMultipartException(MultipartException ex) {
        logger.error("Error de multipart: {}", ex.getMessage());

        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Archivo no proporcionado");
        errorResponse.put("message", "La solicitud debe incluir un archivo en formato multipart.");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }


    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Map<String, String>> handleMaxSizeException(MaxUploadSizeExceededException ex) {
        logger.error("Archivo excede el tamaño máximo permitido: {}", ex.getMessage());

        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Archivo demasiado grande");
        errorResponse.put("message", "El archivo excede el tamaño máximo permitido.");

        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(errorResponse);
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        logger.error("Mensaje JSON no legible: {}", ex.getMessage());

        Throwable cause = ex.getCause();
        if (cause instanceof InvalidFormatException ife) {
            return handleInvalidFormatException((InvalidFormatException) cause);
        }

        // Si no es un InvalidFormatException, devolver error de formato genérico
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "JSON mal formado");
        errorResponse.put("message", "El cuerpo de la solicitud no es válido o contiene errores de formato.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, String>> handleMissingParams(MissingServletRequestParameterException ex) {
        logger.warn("Falta un parámetro requerido: {}", ex.getParameterName());

        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Parámetro requerido faltante");
        errorResponse.put("message", "El parámetro '" + ex.getParameterName() + "' es obligatorio.");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        logger.warn("Argumento ilegal: {}", ex.getMessage());

        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Datos inválidos");
        errorResponse.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> handleIllegalStateException(IllegalStateException ex) {
        logger.warn("Operación inválida: {}", ex.getMessage());

        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Operación no permitida");
        errorResponse.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        logger.error("Error inesperado: {}", ex.getMessage(), ex);

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "Error interno del servidor");
        errorResponse.put("message", "Se ha producido un error inesperado. Por favor contacte al administrador.");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

}

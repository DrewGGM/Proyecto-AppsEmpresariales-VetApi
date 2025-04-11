package com.vetapi.domain.exception;

// Excepción personalizada para errores de reglas de negocio
public class BusinessException extends RuntimeException {

    private final String code;

    // Constructor con mensaje de error
    public BusinessException(String message) {
        super(message);
        this.code = "BUSINESS_ERROR";
    }

    // Constructor con mensaje de error y código de error
    public BusinessException(String message, String code) {
        super(message);
        this.code = code;
    }

    // Constructor con mensaje de error, causa y código de error
    public BusinessException(String message, Throwable cause, String code) {
        super(message, cause);
        this.code = code;
    }

    // Obtiene el código de error
    public String getCode() {
        return code;
    }
}
package com.municipalidad.municipalidad.exception;

import com.municipalidad.municipalidad.dto.ErrorResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String message = "Error de base de datos: Posible duplicado de DNI o Email.";

        // Check for specific constraint names if known, or just generic message
        if (ex.getMessage() != null
                && (ex.getMessage().contains("UK_") || ex.getMessage().contains("Duplicate entry"))) {
            message = "El DNI o Email ya se encuentra registrado.";
        }

        return new ResponseEntity<>(new ErrorResponse(message), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        return new ResponseEntity<>(new ErrorResponse("Ocurrió un error inesperado: " + ex.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

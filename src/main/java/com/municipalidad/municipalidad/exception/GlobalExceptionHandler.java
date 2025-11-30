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
        String detail = ex.getMostSpecificCause().getMessage();

        if (detail != null && (detail.contains("UK_") || detail.contains("Duplicate entry"))) {
            message = "El DNI o Email ya se encuentra registrado.";
        }

        return new ResponseEntity<>(new ErrorResponse(message), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        String message = "Ocurrió un error inesperado: " + ex.getMessage();

        // Fallback for wrapped exceptions
        if (ex.getMessage() != null
                && (ex.getMessage().contains("Duplicate entry") || ex.getMessage().contains("UK_"))) {
            return new ResponseEntity<>(new ErrorResponse("El DNI o Email ya se encuentra registrado."),
                    HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new ErrorResponse(message), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

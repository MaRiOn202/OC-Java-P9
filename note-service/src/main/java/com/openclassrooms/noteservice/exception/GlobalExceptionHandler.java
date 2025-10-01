package com.openclassrooms.noteservice.exception;


import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ConfigError> errorResponse(Exception e, HttpStatus status, HttpServletRequest request) {


        ConfigError error = ConfigError.builder()
                .localDateTime(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(e.getMessage())
                .pathEndpoint(request.getRequestURI())
                .build();

        log.error("Erreur interceptée : {}", error);
        return ResponseEntity.status(status).body(error);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ConfigError> handleGenericException(Exception e, HttpServletRequest request) {
        log.error("Erreur inattendue", e);
        return errorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }


    @ExceptionHandler(IdNoteNotFoundException.class)
    public ResponseEntity<ConfigError> handleIdNoteNotFound(IdNoteNotFoundException e, HttpServletRequest request) {
        log.error("Id note non trouvé", e);
        return errorResponse(e, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(NoteNotFoundException.class)
    public ResponseEntity<ConfigError> handleNoteNotFound(NoteNotFoundException e, HttpServletRequest request) {
        log.error("Note non trouvée", e);
        return errorResponse(e, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(PatientNotFoundException.class)
    public ResponseEntity<ConfigError> handlePatientNotFound(PatientNotFoundException e, HttpServletRequest request) {
        log.error("Patient non trouvé", e);
        return errorResponse(e, HttpStatus.NOT_FOUND, request);
    }


    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ConfigError> handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        String message = String.format("Le paramètre '%s' doit être du type %s. Or la valeur récupérée est : %s",
                ex.getName(),
                ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "Inconnu",
                ex.getValue());
        log.error("Erreur de type argument : {}", message);
        return errorResponse(ex, HttpStatus.BAD_REQUEST, request);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ConfigError> handleValidationException(MethodArgumentNotValidException e, HttpServletRequest request) {
        String errors = e.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> error.getField() + " => " + error.getDefaultMessage())
                .reduce((s1, s2) -> s1 + "\n" + s2)
                .orElse("Erreur de validation");

        log.warn("Erreur de validation : \n{}", errors);
        return errorResponse(new Exception(errors.replace("\n", ", ")), HttpStatus.BAD_REQUEST, request);
    }


}

package com.openclassrooms.noteservice.exception;


import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ConfigError> handleValidationException(MethodArgumentNotValidException e, HttpServletRequest request) {
        String errors = e.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> error.getField() + " : " + error.getDefaultMessage())
                .reduce((s1, s2) -> s1 + ", " + s2)
                .orElse("Erreur de validation");

        log.warn("Erreur de validation : {}", errors);
        return errorResponse(new Exception(errors), HttpStatus.BAD_REQUEST, request);
    }


}

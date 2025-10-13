package com.openclassrooms.front.exceptions;

import feign.RetryableException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;




@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {



    @ExceptionHandler(RetryableException.class)           // errors feign
    public String handleConnxionErrorWithFeign(RetryableException ex, Model model) {
        log.error("Patient-service indisponible", ex);
        model.addAttribute("errorCode", 503);
        model.addAttribute("errorMsg", "Le pateint-service est indisponible. Veuillez réessayer plus tard");
        return "error";
    }


    @ExceptionHandler(ServiceUnavailableException.class)
    public String handleServiceUnavailable(ServiceUnavailableException ex, Model model, HttpServletRequest request) {
        log.error("Le service indisponible", ex);

        model.addAttribute("errorCode", 503);
        model.addAttribute("errorMsg", "Le service est indisponible");
        model.addAttribute("path", request.getRequestURI());

        return "error";
    }


    @ExceptionHandler(Exception.class)
    public String handleGenericException(Exception ex, Model model, HttpServletRequest request) {
        log.error("Erreur inattendue", ex);

        model.addAttribute("errorCode", 500);
        model.addAttribute("errorMsg", ex.getMessage());
        model.addAttribute("path", request.getRequestURI());

        return "error";
    }
}

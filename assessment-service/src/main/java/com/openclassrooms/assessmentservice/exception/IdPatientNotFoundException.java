package com.openclassrooms.assessmentservice.exception;




public class IdPatientNotFoundException extends RuntimeException {


    public IdPatientNotFoundException(String message) {

        super(message);

    }

    public IdPatientNotFoundException(String message, Throwable cause) {

        super(message, cause);

    }



}

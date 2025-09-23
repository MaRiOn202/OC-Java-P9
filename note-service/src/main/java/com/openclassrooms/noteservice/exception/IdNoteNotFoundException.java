package com.openclassrooms.noteservice.exception;

public class IdNoteNotFoundException extends RuntimeException {


    public IdNoteNotFoundException(String message) {

        super(message);

    }

    public IdNoteNotFoundException(String message, Throwable cause) {

        super(message, cause);

    }


}

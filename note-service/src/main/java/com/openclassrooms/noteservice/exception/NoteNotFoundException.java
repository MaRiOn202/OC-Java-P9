package com.openclassrooms.noteservice.exception;




public class NoteNotFoundException extends RuntimeException{


    public NoteNotFoundException(String message) {

        super(message);

    }

    public NoteNotFoundException(String message, Throwable cause) {

        super(message, cause);

    }


}

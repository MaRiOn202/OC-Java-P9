package com.openclassrooms.noteservice.service;


import com.openclassrooms.noteservice.dto.NoteDto;

import java.util.List;

public interface NoteService {

    List<NoteDto> getAllNotes();

    NoteDto getNoteById(String id);

    NoteDto createNote(NoteDto newNoteDto);

    NoteDto updateNote(NoteDto noteDto);

    void deleteNote(final String id);


    List<NoteDto> getNoteByPatientId(Long patientId);


}

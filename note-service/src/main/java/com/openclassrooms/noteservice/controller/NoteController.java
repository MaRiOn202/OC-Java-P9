package com.openclassrooms.noteservice.controller;


import com.openclassrooms.noteservice.dto.NoteDto;
import com.openclassrooms.noteservice.service.NoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/notes")    // car collection
public class NoteController {


    private final NoteService noteService;


    @GetMapping
    public List<NoteDto> getAllNotes() {

        return noteService.getAllNotes();
    }


    @GetMapping("/{id}")
    public NoteDto getNoteById(@PathVariable String id) {

        return noteService.getNoteById(id);
    }


    @PostMapping
    public ResponseEntity<NoteDto> createNote(@RequestBody @Valid NoteDto newNoteDto) {

        NoteDto noteDtoSaved = noteService.createNote(newNoteDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(noteDtoSaved);
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateNote(@PathVariable("id") String id,
                                           @Valid @RequestBody NoteDto noteDto) {

        log.info("Requête updateNote avec id : {}", id);
        log.info("Body : {}", noteDto);

        noteDto.setId(id);

        NoteDto updatedDtoNote = noteService.updateNote(noteDto);
        log.info("Note mise à jour : {}", updatedDtoNote);

        return ResponseEntity.ok(updatedDtoNote);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNote(@Valid @PathVariable("id") String id) {

        log.info("Supp. d'une note avec id : {}", id);
        noteService.deleteNote(id);
        log.info("Supp. effectuée avec id : {}", id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/patient/{patientId}/notes")
    public ResponseEntity<List<NoteDto>> getNoteByPatientId(@PathVariable("patientId") Long patientId) {

        log.info("Récupération des notes d'un seul patient avec l'id suivant: {}", patientId);
        List<NoteDto> notesDtoList = noteService.getNoteByPatientId(patientId);

        if (notesDtoList.isEmpty()) {
            log.info("Pas de note trouvée pour le patientId suivant : {}", patientId);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(notesDtoList);
    }




}

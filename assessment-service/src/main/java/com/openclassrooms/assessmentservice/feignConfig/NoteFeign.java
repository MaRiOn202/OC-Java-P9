package com.openclassrooms.assessmentservice.feignConfig;


import com.openclassrooms.assessmentservice.dto.NoteDto;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@FeignClient(name = "note-service", url = "http://localhost:8081")
public interface NoteFeign {


    @GetMapping("/notes")
    List<NoteDto> getAllNotes();


    @GetMapping("/notes/{id}")
    NoteDto getNoteById(@PathVariable("id") String id);


    @PostMapping("/notes")
    NoteDto createNote(@RequestBody NoteDto newNoteDto);


    @PutMapping("/notes/{id}")
    NoteDto updateNote(@PathVariable("id") String id, @RequestBody NoteDto newNoteDto);


    @DeleteMapping("/notes/{id}")
    void deleteNote(@PathVariable("id") String id);


    @GetMapping("/notes/patient/{patientId}/notes")
    List<NoteDto> getNoteByPatientId(@PathVariable("patientId") Long patientId);









}

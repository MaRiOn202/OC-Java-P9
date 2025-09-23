package com.openclassrooms.noteservice.repository;


import com.openclassrooms.noteservice.entity.NoteEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface NoteRepository extends MongoRepository<NoteEntity, String> {

    // recup toutes le snotes
    List<NoteEntity> findAllNoteByPatientId(Long patientId);



}

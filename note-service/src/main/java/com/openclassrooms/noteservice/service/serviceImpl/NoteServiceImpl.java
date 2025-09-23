package com.openclassrooms.noteservice.service.serviceImpl;


import com.openclassrooms.noteservice.dto.NoteDto;
import com.openclassrooms.noteservice.entity.NoteEntity;
import com.openclassrooms.noteservice.exception.IdNoteNotFoundException;
import com.openclassrooms.noteservice.exception.NoteNotFoundException;
import com.openclassrooms.noteservice.mapper.NoteMapper;
import com.openclassrooms.noteservice.repository.NoteRepository;
import com.openclassrooms.noteservice.service.NoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;



@Slf4j
@Service
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;

    private final NoteMapper noteMapper;



    public NoteServiceImpl(NoteRepository noteRepository, NoteMapper noteMapper) {
        this.noteRepository = noteRepository;
        this.noteMapper = noteMapper;
    }




    @Override
    public List<NoteDto> getAllNotes() {

        log.info("Renvoie une liste de toutes les notes");
        List<NoteEntity> noteEntityList = noteRepository.findAll();
        log.info("Liste des notes récupérées : {}", noteEntityList); // .size ?
        log.info("Nombre de notes récupérées : {}", noteEntityList.size());

        return noteEntityList
                .stream()
                .map(noteMapper::mapToNoteDto)
                .toList();
    }



    @Override
    public NoteDto getNoteById(String id) {

        log.info("Renvoie une note via son id");
        NoteEntity noteEntity = noteRepository.findById(id)
                .orElseThrow(() -> {
                    log.info("Note non trouvée avec l'Id : {}", id);
                    return  new NoteNotFoundException("Note non trouvée avec l'Id : " + id);
                });
        log.info("Note récupérée : {}", noteEntity);

        return noteMapper.mapToNoteDto(noteEntity);
    }



    @Transactional
    @Override
    public NoteDto createNote(NoteDto newNoteDto) {

        log.info("Création d'une nouvelle note");
        if (newNoteDto == null) {
            log.info("La note est null");
            throw new IllegalArgumentException("La note ne peut pas être null");
        }

        NoteEntity noteEntity = noteMapper.mapToNoteEntity(newNoteDto);
        log.info("Avant sauvegarde : patientId= {}, contenuNote= {}, Date de création= {}", noteEntity.getPatientId(),
                noteEntity.getContenuNote(), noteEntity.getDateCreation());

        noteEntity = noteRepository.save(noteEntity);

        NoteDto noteDtoSaved = noteMapper.mapToNoteDto(noteEntity);
        log.info("La nouvelle note a bien été enregistrée avec l'id : {}", noteDtoSaved.getId());

        return noteDtoSaved;


    }

    @Transactional
    @Override
    public NoteDto updateNote(NoteDto noteDto) {

        log.info("Mise à jour de la note avec id : {}", noteDto.getId());

        if (noteDto.getId() == null) {
            log.info("L'id de la note est obligatoire pour la mise à jour");
            throw new IdNoteNotFoundException("L'id de la note est obligatoire pour la mise à jour");
        }
        NoteEntity noteEntity = noteRepository.findById(noteDto.getId())
                .orElseThrow(() -> {
                    log.info("Note non trouvée avec l'Id : {}", noteDto.getId());
                    return new NoteNotFoundException("Note non trouvée avec l'Id : " + noteDto.getId());
                });

        noteEntity.setContenuNote(noteDto.getContenuNote());
        noteEntity.setPatientId(noteDto.getPatientId());
        //noteEntity.setDateCreation(LocalDateTime.now());

        NoteEntity noteEntityUpdated = noteRepository.save(noteEntity);
        NoteDto noteResult = noteMapper.mapToNoteDto(noteEntityUpdated);
        log.info("Note mise à jour avec succès : {}", noteResult.getId());
        return noteResult;
    }

    @Transactional
    @Override
    public void deleteNote(String id) {

        log.info("Suppression de la note avec l'id : {}", id);

        NoteEntity noteEntity = noteRepository.findById(id)
                .orElseThrow(() -> new NoteNotFoundException("Note non trouvée avec l'id " + id));

        noteRepository.delete(noteEntity);
        log.info("La note id {} a bien été supprimée", id);

    }

    @Override
    public List<NoteDto> getNoteByPatientId(Long patientId) {

        log.info("Récupération des notes concernant le aptient avec id : {} ", patientId);
        List<NoteEntity> notesEntityList = noteRepository.findAllNoteByPatientId(patientId);

        if (notesEntityList.isEmpty()) {
            log.info("La liste est vide pour le patientId : {}", patientId);

        } else {
            log.info("{} notes relevées pour le patientId : {}", notesEntityList.size(), patientId);
        }
        return notesEntityList
                .stream()
                .map(noteMapper::mapToNoteDto)
                .toList();
    }
}

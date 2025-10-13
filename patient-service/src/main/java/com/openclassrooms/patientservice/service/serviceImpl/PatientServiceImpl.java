package com.openclassrooms.patientservice.service.serviceImpl;


import com.openclassrooms.patientservice.dto.PatientDto;
import com.openclassrooms.patientservice.entity.PatientEntity;
import com.openclassrooms.patientservice.exception.IdPatientNotFoundException;
import com.openclassrooms.patientservice.exception.PatientNotFoundException;
import com.openclassrooms.patientservice.mapper.PatientMapper;
import com.openclassrooms.patientservice.repository.PatientRepository;
import com.openclassrooms.patientservice.service.PatientService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;



@Slf4j
@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;


    private final PatientMapper patientMapper;


    // constr
    public PatientServiceImpl(PatientRepository patientRepository, PatientMapper patientMapper) {
        this.patientRepository = patientRepository;
        this.patientMapper = patientMapper;
    }





    @Override
    public List<PatientDto> getAllPatients() {

        log.info("Renvoie une liste de tous les patients");
        List<PatientEntity> patientEntityList = patientRepository.findAll();
        log.info("Liste des patients récdupérés : {}", patientEntityList); // .size ?

        return patientEntityList
                .stream()
                .map(patientMapper::mapToPatientDto)
                .toList();

    }


    @Override
    public PatientDto getPatientById(Long id) {

        log.info("Renvoie un patient via son id");
        PatientEntity patientEntity = patientRepository.findById(id)
                .orElseThrow(() -> {
                    return  new PatientNotFoundException("Patient non trouvé avec l'Id : " + id);
                });
        log.info("Patient récupéré : {}", patientEntity);

        return patientMapper.mapToPatientDto(patientEntity);
    }


    @Transactional
    @Override
    public PatientDto createPatient(PatientDto newPatientDto) {

        log.info("Création d'un nouveau patient");
        if (newPatientDto == null) {
            log.info("Le patient est null");
            throw new IllegalArgumentException("Le patient ne peut pas être null");
        }

        if (newPatientDto.getNom() == null || newPatientDto.getNom().isBlank() ||
                newPatientDto.getPrenom() == null || newPatientDto.getPrenom().isBlank()
                ) {
            log.info("Le nom et le prénom du patient est obligatoire");
            throw new IllegalArgumentException("Le nom et le prénom du patient sont obligatoires");

        }

        PatientEntity patientEntity = patientMapper.mapToPatientEntity(newPatientDto);
        log.info("Avant sauvegarde : Nom= {}, Prénom= {}, Date de naissance= {}, Genre= {}, Adresse= {}, Téléphone={}", patientEntity.getNom(),
                patientEntity.getPrenom(), patientEntity.getDateNaissance(),patientEntity.getGenre(), patientEntity.getAdresse(),
                patientEntity.getTelephone());

        patientEntity = patientRepository.save(patientEntity);

        PatientDto patientDtoSaved = patientMapper.mapToPatientDto(patientEntity);
        log.info("Le nouveau patient a bien été enregistré avec l'id : {}", patientDtoSaved.getId());
        log.info("Après sauvegarde : Nom= {}, Prénom= {}, Date de naissance= {}, Genre= {}, Adresse= {}, Téléphone={}", patientDtoSaved.getNom(),
                patientDtoSaved.getPrenom(), patientDtoSaved.getDateNaissance(),patientDtoSaved.getGenre(), patientDtoSaved.getAdresse(),
                patientDtoSaved.getTelephone());

        return patientDtoSaved;
    }



    @Override
    @Transactional
    public PatientDto updatePatient(PatientDto patientDto) {

        log.info("Modification des données d'un patient avec l'id : {}", patientDto.getId());
        if (patientDto.getId() == null) {
            log.info("L'id du patient est obligatoire pour la mise à jour");
            throw new IdPatientNotFoundException("L'id du patient est obligatoire pour la mise à jour");
        }

        PatientEntity existingPatientEntity = patientRepository.findById(patientDto.getId())
                .orElseThrow(() -> new PatientNotFoundException("Patient non trouvé avec l'Id : " + patientDto.getId()));

        //éléments à modifer
        existingPatientEntity.setNom(patientDto.getNom());
        existingPatientEntity.setPrenom(patientDto.getPrenom());
        existingPatientEntity.setDateNaissance(patientDto.getDateNaissance());
        existingPatientEntity.setGenre(patientDto.getGenre());
        existingPatientEntity.setAdresse(patientDto.getAdresse());
        existingPatientEntity.setTelephone(patientDto.getTelephone());

        PatientEntity patientEntityUpdated = patientRepository.save(existingPatientEntity);
        PatientDto patientResult = patientMapper.mapToPatientDto(patientEntityUpdated);
        log.info("Le patient a été mis à jour avec succès : {}", patientResult.getId());
        return patientResult;
    }


    @Override
    @Transactional
    public void deletePatient(Long id) {

        log.info("Suppression du patient avec l'id : {}", id);
        PatientEntity patientEntity = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Patient non trouvé avec l'id " + id));

        log.info("Suppression du patient : {}", patientEntity);

        patientRepository.deleteById(id);
        log.info("Le patient a bien été supprimé");

    }






}

package com.openclassrooms.assessmentservice.service.serviceImpl;


import com.openclassrooms.assessmentservice.dto.NoteDto;
import com.openclassrooms.assessmentservice.dto.PatientDto;
import com.openclassrooms.assessmentservice.dto.RisksCat;
import com.openclassrooms.assessmentservice.exception.IdPatientNotFoundException;
import com.openclassrooms.assessmentservice.feignConfig.NoteFeign;
import com.openclassrooms.assessmentservice.feignConfig.PatientFeign;
import com.openclassrooms.assessmentservice.service.AssessmentRisksService;
import com.openclassrooms.assessmentservice.utils.AgePatient;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
public class AssessmentRisksServiceImpl implements AssessmentRisksService {

    // Feign
    private final PatientFeign patientFeign;
    private final NoteFeign noteFeign;


    public AssessmentRisksServiceImpl(PatientFeign patientFeign, NoteFeign noteFeign) {
        this.patientFeign = patientFeign;
        this.noteFeign = noteFeign;
    }


    // 1- déclencheurs
    private static final List<String> declencheurs =  List.of(
            "Hémoglobine A1C",
                    "Microalbumine",
                    "Taille",
                    "Poids",
                    "Fumeur", "Fumeuse",
                    "Anormal",
                    "Cholestérol",
                    "Vertiges",
                    "Rechute",
                    "Réaction",
                    "Anticorps"
    );




    // 2- compter le nb de déclencheurs
    private long nbDeclencheurs(List<NoteDto> notesDtoList) {

        long nb = 0;

        for (NoteDto note : notesDtoList) {
            String termes = note.getContenuNote().toLowerCase();

            for (String declencheur : declencheurs) {

                if (termes.contains(declencheur.toLowerCase())) {
                    nb++;
                }
            }
        }

        return nb;
    }



    // 3- méthode getRisksCatégorie
    @Override
    public RisksCat getRisksCat(Long patientId) {

        PatientDto patientDto;
        List<NoteDto> noteDtoList;

        try {
            patientDto = patientFeign.getPatientById(patientId);
            noteDtoList = noteFeign.getNoteByPatientId(patientId);
        } catch (FeignException.NotFound ex)  {
            log.info("Patient non trouvé avec l'id : {}", patientId);
            throw new IdPatientNotFoundException(String.valueOf(patientId));
        }

        if (noteDtoList == null) {
            log.info("Aucune note trouvée pour le patient id={}", patientId);
            noteDtoList = List.of();
        }

        // uitls
        int age = AgePatient.calculateAgePatient(patientDto.getDateNaissance());

        long nbDeclencheurs = nbDeclencheurs(noteDtoList);

        // 1
        if (nbDeclencheurs == 0) {
            return RisksCat.NONE;
        }
        // 2
        if (nbDeclencheurs >= 2 && nbDeclencheurs <= 5 && age >= 30  ) {
            return RisksCat.BORDERLINE;
        }
        // 3
        if (patientDto.getGenre().equalsIgnoreCase("M") && age < 30 && nbDeclencheurs == 3 ) {
            return RisksCat.IN_DANGER;
        }
        if (patientDto.getGenre().equalsIgnoreCase("F") && age < 30 && nbDeclencheurs == 4 ) {
            return RisksCat.IN_DANGER;
        }
        if (age >= 30 && (nbDeclencheurs == 6 || nbDeclencheurs == 7 )) {
            return RisksCat.IN_DANGER;
        }
        // 4
        if (patientDto.getGenre().equalsIgnoreCase("M") && age < 30 && nbDeclencheurs >= 5) {
            return RisksCat.EARLY_ONSET;
        }
        if (patientDto.getGenre().equalsIgnoreCase("F") && age < 30 && nbDeclencheurs >= 7) {
            return RisksCat.EARLY_ONSET;
        }
        if (age >= 30 && nbDeclencheurs >= 8) {
            return RisksCat.EARLY_ONSET;
        }
        // default
        return RisksCat.NONE;
    }


}

package com.openclassrooms.assessmentservice.service;


import com.openclassrooms.assessmentservice.dto.NoteDto;
import com.openclassrooms.assessmentservice.dto.PatientDto;
import com.openclassrooms.assessmentservice.dto.RisksCat;
import com.openclassrooms.assessmentservice.feignConfig.NoteFeign;
import com.openclassrooms.assessmentservice.feignConfig.PatientFeign;
import com.openclassrooms.assessmentservice.service.serviceImpl.AssessmentRisksServiceImpl;
import com.openclassrooms.assessmentservice.utils.AgePatient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class AssessmentRisksServiceImplTest {


    @InjectMocks
    private AssessmentRisksServiceImpl assessmentRisksService;

    @Mock
    private PatientFeign patientFeign;

    @Mock
    private NoteFeign noteFeign;


    private AgePatient agePatient;



    @Test
    void testGetRisksCatWithCaseNONE() {

        Long patientId = 2L;

        // Pas de déclencheur
        PatientDto patientDto = new PatientDto();
        patientDto.setId(patientId);
        patientDto.setGenre("F");
        patientDto.setDateNaissance(LocalDate.of(1989, 7, 21));

        List<NoteDto> noteDtoList = List.of();

        when(patientFeign.getPatientById(patientId)).thenReturn(patientDto);
        when(noteFeign.getNoteByPatientId(patientId)).thenReturn(noteDtoList);
        RisksCat result = assessmentRisksService.getRisksCat(patientId);

        assertEquals(RisksCat.NONE, result);

    }

    @Test
    void testGetRisksCatWithCaseBORDERLINE() {
        Long patientId = 2L;

        // + de 30 ans & entre 2 et 5 déclencheurs
        PatientDto patientDto = new PatientDto();
        patientDto.setId(patientId);
        patientDto.setGenre("F");
        patientDto.setDateNaissance(LocalDate.of(1989, 7, 21)); // >= 30 ans

        List<NoteDto> noteDtoList = List.of(
                new NoteDto(null, null, "Anormal Poids Vertiges", null)
        );

        when(patientFeign.getPatientById(patientId)).thenReturn(patientDto);
        when(noteFeign.getNoteByPatientId(patientId)).thenReturn(noteDtoList);
        RisksCat result = assessmentRisksService.getRisksCat(patientId);

        // 3 déclencheurs
        assertEquals(RisksCat.BORDERLINE, result);

    }

    @Test
    void testGetRisksCatWithCaseINDANGER_youngMan() {

        Long patientId = 2L;
        // homme - de 30 ans & 3 déclencheurs
        PatientDto patientDto = new PatientDto();
        patientDto.setId(patientId);
        patientDto.setGenre("M");
        patientDto.setDateNaissance(LocalDate.of(2000, 7, 21)); // < 30 ans

        List<NoteDto> noteDtoList = List.of(
                new NoteDto(null, null, "Anormal Poids Vertiges", null)
        );
        when(patientFeign.getPatientById(patientId)).thenReturn(patientDto);
        when(noteFeign.getNoteByPatientId(patientId)).thenReturn(noteDtoList);
        RisksCat result = assessmentRisksService.getRisksCat(patientId);

        // 3 déclencheurs
        assertEquals(RisksCat.IN_DANGER, result);

    }

    @Test
    void testGetRisksCatWithCaseINDANGER_youngWoman() {
        Long patientId = 2L;
        // femme - de 30 ans & 4 déclencheurs
        PatientDto patientDto = new PatientDto();
        patientDto.setId(patientId);
        patientDto.setGenre("F");
        patientDto.setDateNaissance(LocalDate.of(2000, 7, 21)); // < 30 ans

        List<NoteDto> noteDtoList = List.of(
                new NoteDto(null, null, "Anormal Poids Vertiges Anticorps", null)
        );
        when(patientFeign.getPatientById(patientId)).thenReturn(patientDto);
        when(noteFeign.getNoteByPatientId(patientId)).thenReturn(noteDtoList);
        RisksCat result = assessmentRisksService.getRisksCat(patientId);

        // 4 déclencheurs
        assertEquals(RisksCat.IN_DANGER, result);

    }


    @Test
    void testGetRisksCatWithCaseINDANGER_sup30() {
        Long patientId = 2L;
        // + de 30 ans & 6 ou 7 déclencheurs
        PatientDto patientDto = new PatientDto();
        patientDto.setId(patientId);
        patientDto.setGenre("F");
        patientDto.setDateNaissance(LocalDate.of(1989, 7, 21)); // >= 30 ans

        List<NoteDto> noteDtoList = List.of(
                new NoteDto(null, null, "Anormal Poids Vertiges Anticorps Hémoglobine A1C Taille", null)
        );
        when(patientFeign.getPatientById(patientId)).thenReturn(patientDto);
        when(noteFeign.getNoteByPatientId(patientId)).thenReturn(noteDtoList);
        RisksCat result = assessmentRisksService.getRisksCat(patientId);


        // 6 déclencheurs
        assertEquals(RisksCat.IN_DANGER, result);

    }

    @Test
    void testGetRisksCatWithCaseINDANGER_CasLimiteEquals30() {
        Long patientId = 2L;
        // == 30 ans & 6 ou 7 déclencheurs
        PatientDto patientDto = new PatientDto();
        patientDto.setId(patientId);
        patientDto.setGenre("F");
        patientDto.setDateNaissance(LocalDate.of(1995, 7, 21)); // == 30 ans

        List<NoteDto> noteDtoList = List.of(
                new NoteDto(null, null, "Anormal Poids Vertiges Anticorps Hémoglobine A1C Taille", null)
        );
        when(patientFeign.getPatientById(patientId)).thenReturn(patientDto);
        when(noteFeign.getNoteByPatientId(patientId)).thenReturn(noteDtoList);
        RisksCat result = assessmentRisksService.getRisksCat(patientId);

        // 6 déclencheurs
        assertEquals(RisksCat.IN_DANGER, result);

    }


    @Test
    void testGetRisksCatWithCaseEARLYONSET_YoungMan() {
        Long patientId = 2L;
        // homme de < 30 ans & nb déclencheurs >= 5
        PatientDto patientDto = new PatientDto();
        patientDto.setId(patientId);
        patientDto.setGenre("M");
        patientDto.setDateNaissance(LocalDate.of(2000, 7, 21)); // < 30 ans

        List<NoteDto> noteDtoList = List.of(
                new NoteDto(null, null, "Anormal Poids Vertiges Anticorps Hémoglobine A1C Taille", null)
        );
        when(patientFeign.getPatientById(patientId)).thenReturn(patientDto);
        when(noteFeign.getNoteByPatientId(patientId)).thenReturn(noteDtoList);
        RisksCat result = assessmentRisksService.getRisksCat(patientId);

        // 6 déclencheurs
        assertEquals(RisksCat.EARLY_ONSET, result);

    }

    @Test
    void testGetRisksCatWithCaseEARLYONSET_YoungWoman() {
        Long patientId = 2L;
        // femme de < 30 ans & nb déclencheurs >= 7
        PatientDto patientDto = new PatientDto();
        patientDto.setId(patientId);
        patientDto.setGenre("F");
        patientDto.setDateNaissance(LocalDate.of(2000, 7, 21)); // < 30 ans

        List<NoteDto> noteDtoList = List.of(
                new NoteDto(null, null, "Réaction Anormal Poids Vertiges Anticorps Hémoglobine A1C Taille", null)
        );
        when(patientFeign.getPatientById(patientId)).thenReturn(patientDto);
        when(noteFeign.getNoteByPatientId(patientId)).thenReturn(noteDtoList);
        RisksCat result = assessmentRisksService.getRisksCat(patientId);

        // 7 déclencheurs
        assertEquals(RisksCat.EARLY_ONSET, result);

    }

    @Test
    void testGetRisksCatWithCaseEARLYONSET_Sup30Ans() {
        Long patientId = 2L;
        // >= 30 ans & nb déclencheurs >= 8
        PatientDto patientDto = new PatientDto();
        patientDto.setId(patientId);
        patientDto.setGenre("F");
        patientDto.setDateNaissance(LocalDate.of(1989, 7, 21)); // >= 30 ans

        List<NoteDto> noteDtoList = List.of(
                new NoteDto(null, null, "Microalbumine Réaction Anormal Poids Vertiges Anticorps Hémoglobine A1C Taille", null)
        );
        when(patientFeign.getPatientById(patientId)).thenReturn(patientDto);
        when(noteFeign.getNoteByPatientId(patientId)).thenReturn(noteDtoList);
        RisksCat result = assessmentRisksService.getRisksCat(patientId);

        // 8 déclencheurs
        assertEquals(RisksCat.EARLY_ONSET, result);

    }

    @Test
    void testGetRisksCatWithCasLimite_Equals30Ans() {
        Long patientId = 2L;
        // == 30 ans & nb déclencheurs >= 8
        PatientDto patientDto = new PatientDto();
        patientDto.setId(patientId);
        patientDto.setGenre("F");
        patientDto.setDateNaissance(LocalDate.of(1995, 7, 21)); // == 30 ans

        List<NoteDto> noteDtoList = List.of(
                new NoteDto(null, null, "Cholestérol Microalbumine Réaction Anormal Poids Vertiges Anticorps Hémoglobine A1C Taille", null)
        );
        when(patientFeign.getPatientById(patientId)).thenReturn(patientDto);
        when(noteFeign.getNoteByPatientId(patientId)).thenReturn(noteDtoList);
        RisksCat result = assessmentRisksService.getRisksCat(patientId);

        // 9 déclencheurs
        assertEquals(RisksCat.EARLY_ONSET, result);

    }

    @Test
    void testGetRisksCatWithCasDEFAULT() {
        Long patientId = 2L;
        // femme < 30 ans & 1 déclencheur
        PatientDto patientDto = new PatientDto();
        patientDto.setId(patientId);
        patientDto.setGenre("F");
        patientDto.setDateNaissance(LocalDate.of(2000, 7, 21)); // < 30 ans

        List<NoteDto> noteDtoList = List.of(
                new NoteDto(null, null, "Cholestérol", null));
        when(patientFeign.getPatientById(patientId)).thenReturn(patientDto);
        when(noteFeign.getNoteByPatientId(patientId)).thenReturn(noteDtoList);
        RisksCat result = assessmentRisksService.getRisksCat(patientId);

        // 1 déclencheur
        assertEquals(RisksCat.NONE, result);

    }


}

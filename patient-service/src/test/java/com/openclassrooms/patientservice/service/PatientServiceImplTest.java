package com.openclassrooms.patientservice.service;


import com.openclassrooms.patientservice.dto.PatientDto;
import com.openclassrooms.patientservice.entity.PatientEntity;
import com.openclassrooms.patientservice.exception.IdPatientNotFoundException;
import com.openclassrooms.patientservice.exception.PatientNotFoundException;
import com.openclassrooms.patientservice.mapper.PatientMapper;
import com.openclassrooms.patientservice.repository.PatientRepository;
import com.openclassrooms.patientservice.service.serviceImpl.PatientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;



import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class PatientServiceImplTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private PatientMapper patientMapper;

    @InjectMocks
    private PatientServiceImpl patientService;


    private PatientEntity patientEntity;
    private PatientDto patientDto;


    @BeforeEach
    void setUp() {

        // Entity
        patientEntity = new PatientEntity();
        patientEntity.setId(1L);
        patientEntity.setNom("Durand");
        patientEntity.setPrenom("Michel");
        patientEntity.setDateNaissance(LocalDate.of(1985, 5, 15));
        patientEntity.setGenre("M");
        patientEntity.setAdresse("12 rue des Lilas");
        patientEntity.setTelephone("0102030405");

        // DTO
        patientDto = new PatientDto();
        patientDto.setId(1L);
        patientDto.setNom("Durand");
        patientDto.setPrenom("Michel");
        patientDto.setDateNaissance(LocalDate.of(1985, 5, 15));
        patientDto.setGenre("M");
        patientDto.setAdresse("12 rue des Lilas");
        patientDto.setTelephone("0102030405");
    }



    @Test
    void testGetAllPatientsIsSuccess() {

        when(patientRepository.findAll()).thenReturn(List.of(patientEntity));
        when(patientMapper.mapToPatientDto(patientEntity)).thenReturn(patientDto);

        List<PatientDto> result = patientService.getAllPatients();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(patientRepository, times(1)).findAll();
    }


    @Test
    void testGetPatientByIdIsSuccess() {

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patientEntity));
        when(patientMapper.mapToPatientDto(patientEntity)).thenReturn(patientDto);

        PatientDto result = patientService.getPatientById(1L);

        assertNotNull(result);
        assertEquals("Durand", result.getNom());
        assertEquals("Michel", result.getPrenom());
        verify(patientRepository, times(1)).findById(1L);


    }

    @Test
    void testGetPatientByIdWithPatientNotFoundException() {

        when(patientRepository.findById(25L)).thenReturn(Optional.empty());
        PatientNotFoundException ex = assertThrows(PatientNotFoundException.class, () ->
                patientService.getPatientById(25L));

        assertEquals("Patient non trouvé avec l'Id : 25", ex.getMessage());
        verify(patientRepository, times(1)).findById(25L);



    }


    @Test
    void testCreatePatientSuccess() {

        when(patientMapper.mapToPatientEntity(patientDto)).thenReturn(patientEntity);
        when(patientRepository.save(patientEntity)).thenReturn(patientEntity);
        when(patientMapper.mapToPatientDto(patientEntity)).thenReturn(patientDto);

        PatientDto result = patientService.createPatient(patientDto);

        assertNotNull(result);
        assertEquals("Durand", result.getNom());
        assertEquals("Michel", result.getPrenom());
        verify(patientRepository, times(1)).save(patientEntity);


    }


@Test
void testCreatePatientWithIllegalArgumentException() {

        PatientDto patientWithoutName = new PatientDto();
        patientWithoutName.setNom("");
        patientWithoutName.setPrenom("");
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
            patientService.createPatient(patientWithoutName));

    assertEquals("Le nom et le prénom du patient sont obligatoires", e.getMessage());


}


    @Test
    void testUpdatePatientIsSuccess() {

        when(patientRepository.findById(patientDto.getId())).thenReturn(Optional.of(patientEntity));
        when(patientRepository.save(any(PatientEntity.class))).thenReturn(patientEntity);
        when(patientMapper.mapToPatientDto(patientEntity)).thenReturn(patientDto);
        PatientDto result = patientService.updatePatient(patientDto);

        assertNotNull(result);
        assertEquals("Michel", result.getPrenom());
        verify(patientMapper, times(1)).mapToPatientDto(patientEntity);


    }

    @Test
    void testUpdatePatientWithIdNullRetunrIdPatientNotFoundException() {

        patientDto.setId(null);

        IdPatientNotFoundException ex = assertThrows(IdPatientNotFoundException.class, () ->
                patientService.updatePatient(patientDto));

        assertEquals("L'id du patient est obligatoire pour la mise à jour", ex.getMessage());

    }


    @Test
    void testDeletePatientSuccess() {
        Long patientId = 1L;

        when(patientRepository.existsById(patientId)).thenReturn(true);

        assertDoesNotThrow(() -> patientService.deletePatient(patientId));
        verify(patientRepository, times(1)).deleteById(patientId);
    }

    @Test
    void testDeletePatientWithPatientNotFoundExceptionThrows() {
        Long patientId = 25L;

        when(patientRepository.existsById(patientId)).thenReturn(false);

        PatientNotFoundException excep = assertThrows(PatientNotFoundException.class,
                () -> patientService.deletePatient(patientId));

        assertEquals("Patient non trouvé avec l'id 25", excep.getMessage());

        verify(patientRepository, times(1)).existsById(patientId);
        verify(patientRepository, never()).deleteById(anyLong());
    }



}

package com.openclassrooms.patientservice.service;

import com.openclassrooms.patientservice.dto.PatientDto;



import java.util.List;

public interface PatientService {


    List<PatientDto> getAllPatients();

    PatientDto createPatient(PatientDto newPatientDto);

    PatientDto getPatientById(Long id);

    PatientDto updatePatient(PatientDto patientDto);

    void deletePatient(final Long id);


}

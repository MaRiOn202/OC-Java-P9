package com.openclassrooms.assessmentservice.feignConfig;


import com.openclassrooms.assessmentservice.dto.PatientDto;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@FeignClient(name = "patient-service", url = "http://localhost:8080")
public interface PatientFeign {


    @GetMapping("/patient/getAllPatients")
    List<PatientDto> getAllPatients();


    @GetMapping("/patient/getPatientById/{id}")
    PatientDto getPatientById(@PathVariable("id") Long id);


    @PostMapping("/patient/createPatient")
    PatientDto createPatient(@RequestBody PatientDto patientDto);


    @PutMapping("/patient/updatePatient/{id}")
    PatientDto updatePatient(@PathVariable("id") Long id,
                             @RequestBody PatientDto patientDto);


    @DeleteMapping("/patient/deletePatient/{id}")
    void deletePatient(@PathVariable("id") Long id);


}

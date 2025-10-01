package com.openclassrooms.noteservice.feignConfig;


import com.openclassrooms.noteservice.dto.PatientDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "patient-service", url = "http://localhost:8080")
public interface PatientFeign {


    @GetMapping("/patient/getPatientById/{id}")
    PatientDto getPatientById(@PathVariable("id") Long id);







}

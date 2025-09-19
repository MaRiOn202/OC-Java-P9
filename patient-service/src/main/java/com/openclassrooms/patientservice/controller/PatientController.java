package com.openclassrooms.patientservice.controller;



import com.openclassrooms.patientservice.dto.PatientDto;
import com.openclassrooms.patientservice.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/patient")
public class PatientController {


    private final PatientService patientService;



    @GetMapping("/getAllPatients")
    public List<PatientDto> getAllPatients() {

        return patientService.getAllPatients();
    }


    @GetMapping("/getPatientById/{id}")
    public PatientDto getPatientById(@PathVariable Long id) {

        return patientService.getPatientById(id);
    }


    @PostMapping("/createPatient")
    public ResponseEntity<?> createPatient(@RequestBody @Valid PatientDto patientDto, BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());

        }

        PatientDto patientDtoSaved = patientService.createPatient(patientDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(patientDtoSaved);
    }


    @PutMapping("/updatePatient/{id}")
    public ResponseEntity<?> updatePatient(@PathVariable("id") Long id,
                                          @Valid @RequestBody PatientDto patientDto) {

        log.info("Requête PUT updatePatient avec id : {}", id);
        log.info("Body : {}", patientDto);

        patientDto.setId(id);

        PatientDto updatedPatient = patientService.updatePatient(patientDto);
        log.info("Patient mis à jour : {}", updatedPatient);

        return ResponseEntity.ok(updatedPatient);
    }


    @DeleteMapping("/deletePatient/{id}")
    public ResponseEntity<?> deletePatient(@Valid @PathVariable("id") Long id) {

        log.info("Supp. d'un patient avec id : {}", id);
        patientService.deletePatient(id);
        log.info("Supp. effectuée avec id : {}", id);
        return ResponseEntity.noContent().build();
    }

}

package com.openclassrooms.assessmentservice.controller;



import com.openclassrooms.assessmentservice.dto.RisksCat;
import com.openclassrooms.assessmentservice.service.AssessmentRisksService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;



@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/assessment")
public class AssessmentRisksController {

    private final AssessmentRisksService assessmentRisksService;


    @GetMapping("/{patientId}")
    public RisksCat getRisksCat(@PathVariable Long patientId) {

        log.info("Calcul du risque de diabète pour le patient id={}", patientId);
        return assessmentRisksService.getRisksCat(patientId);

    }










}

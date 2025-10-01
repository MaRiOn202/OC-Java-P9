package com.openclassrooms.assessmentservice.service;


import com.openclassrooms.assessmentservice.dto.NoteDto;
import com.openclassrooms.assessmentservice.dto.PatientDto;
import com.openclassrooms.assessmentservice.dto.RisksCat;

import java.util.List;

public interface AssessmentRisksService {


    RisksCat getRisksCat(Long patientId);


}

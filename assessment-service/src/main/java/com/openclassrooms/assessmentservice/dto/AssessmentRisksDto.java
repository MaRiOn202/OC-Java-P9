package com.openclassrooms.assessmentservice.dto;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AssessmentRisksDto {


    private Long patientId;

    private String nom;
    private String prenom;

    private String age;

    // Cat : None, Borderline, In Danger & Early onset
    private RisksCat risksCat;



}

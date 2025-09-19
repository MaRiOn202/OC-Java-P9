package com.openclassrooms.patientservice.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"dateNaissance", "adresse", "telephone"})
public class PatientDto {

    private Long id;

    @NotBlank(message = "Le nom du patient est obligatoire")
    private String nom;
    @NotBlank(message = "Le prénom du patient est obligatoire")
    private String prenom;
    @NotNull(message = "La date de naissance du patient est obligatoire")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateNaissance;
    @NotBlank(message = "Le genre du patient est obligatoire")
    private String genre;
    private String adresse;
    private String telephone;

}

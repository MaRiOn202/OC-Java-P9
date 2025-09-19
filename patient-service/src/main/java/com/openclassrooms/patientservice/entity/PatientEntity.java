package com.openclassrooms.patientservice.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Table(name= "patient")
public class PatientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "prenom", nullable = false)
    private String prenom;

    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(nullable = false, name = "date_naissance")
    private LocalDate dateNaissance;

    @Column(name = "genre", nullable = false)
    private String genre;

    // Champs optionnels cf. consignes Sp1
    @Column(name = "adresse")
    private String adresse;

    @Column(name = "telephone")
    private String telephone;


}

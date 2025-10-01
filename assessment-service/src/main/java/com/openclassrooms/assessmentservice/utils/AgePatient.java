package com.openclassrooms.assessmentservice.utils;


import java.time.LocalDate;
import java.time.Period;



public class AgePatient {

    public static int calculateAgePatient(LocalDate dateNaissance) {

        LocalDate currentAge = LocalDate.now();

        return Period.between(dateNaissance, currentAge).getYears();
    }






}

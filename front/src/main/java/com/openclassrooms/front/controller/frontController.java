package com.openclassrooms.front.controller;


import com.openclassrooms.front.dto.PatientDto;
import com.openclassrooms.front.feignConfig.PatientFeign;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Slf4j
@Controller
@AllArgsConstructor
public class frontController {

    private final PatientFeign patientFeign;


    // Liste des patients
    @GetMapping("/patients")
    public String patientsList(Model model) {

        List<PatientDto> patientsList = patientFeign.getAllPatients();
        model.addAttribute("patients", patientsList);

        return "patients";

    }

    // Accès au form d'ajout patient
    @GetMapping("/add")
    public String addPatientForm(Model model) {

        model.addAttribute("patient", new PatientDto());

        return "add";

    }

    // Ajouter patient
    @PostMapping("/patient/save")
    public String addPatient(@Valid @ModelAttribute("patient") PatientDto newPatientDto,
                             BindingResult result,
                             RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "add";
        }

        try  {
            patientFeign.createPatient(newPatientDto);
            log.info("Nouveau patient ajouté : {}", newPatientDto);
            redirectAttributes.addAttribute("created", true);
            return "redirect:/patients";

        } catch (Exception ex) {
            redirectAttributes.addAttribute("error", true);
            log.info("Erreur lors de la création d'un nouveau patient", ex);

        }

        return "redirect:/patients";

    }


    // Afficher profile du patient
    @GetMapping("/patient/{id}")
    public String profilePatient(@PathVariable Long id, Model model) {

        PatientDto patientDto = patientFeign.getPatientById(id);
        model.addAttribute("patient", patientDto);

        return "profile-patient";

    }


    // Accès form pour modif patient
    @GetMapping("/patient/edit/{id}")
    public String updatePatientForm(@PathVariable Long id, Model model) {

        PatientDto patientDto = patientFeign.getPatientById(id);
        model.addAttribute("patient", patientDto);

        return "update-patient";

    }

    // Update patient post
    @PostMapping("/patient/update")
    public String updatePatient(@Valid @ModelAttribute("patient") PatientDto updatePatientDto,
                                BindingResult result, RedirectAttributes redirectAttributes,
                                Model model) {

        if (result.hasErrors()) {
            model.addAttribute("patient", updatePatientDto);
            return "update-patient";
        }

        try  {
            patientFeign.updatePatient(updatePatientDto.getId(), updatePatientDto);
            log.info("Nouveau patient modifié : {}", updatePatientDto);
            redirectAttributes.addAttribute("updated", true);

        } catch (Exception ex) {
            redirectAttributes.addAttribute("error", true);
            log.info("Erreur lors de la modification d'un patient", ex);
        }

        return "redirect:/patients";

    }


    // Spprimer ptient
    @GetMapping("/patient/delete/{id}")
    public String notifDeletePatient(@PathVariable Long id, Model model) {

        PatientDto patient = patientFeign.getPatientById(id);
        model.addAttribute("patient", patient);
        return "delete";
    }


    @PostMapping("/patient/delete/{id}")
    public String deletePatient(@PathVariable Long id, RedirectAttributes redirectAttributes) {

        patientFeign.deletePatient(id);
        log.info("Patient avec l'id {} supprimé", id);
        redirectAttributes.addFlashAttribute("deleted", "Le patient a bien été supprimé.");
        return "redirect:/patients";
    }




}

package com.clinicapi.domain.patient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {

    @Autowired
    PatientRepository patientRepository;

    @PostMapping
    public void register(@RequestBody Patient patient) {
        patientRepository.save(patient);
    }

    @GetMapping
    public List<Patient> list(){
        return patientRepository.findAll();
    }
}

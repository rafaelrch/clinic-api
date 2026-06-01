package com.clinicapi.domain.patient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
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

    @PutMapping
    @Transactional
    public void update(@RequestBody PatientUpdateData data){
        Patient patient = patientRepository.getReferenceById(data.getId());
        if(data.getName() != null){
            patient.setName(data.getName());
        }


        if(data.getPhone() != null){
            patient.setPhone(data.getPhone());
        }


    }

    @DeleteMapping("/{id}")
    @Transactional
    public void delete(@PathVariable Long id){
        patientRepository.deleteById(id);
    }
}

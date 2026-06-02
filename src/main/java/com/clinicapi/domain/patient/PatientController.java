package com.clinicapi.domain.patient;

import jakarta.validation.Valid;
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
    public void register(@RequestBody @Valid PatientCreateData data) {
        Patient patient = new Patient(data.name(), data.email(), data.phone(), data.cpf());
        patientRepository.save(patient);
    }

    @GetMapping
    public List<PatientResponseData> list(){
        return patientRepository.findAll()
                .stream()
                .map(PatientResponseData::new)
                .toList();
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

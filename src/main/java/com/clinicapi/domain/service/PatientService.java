package com.clinicapi.domain.service;

import com.clinicapi.domain.patient.*;
import com.clinicapi.domain.service.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class PatientService {

    @Autowired
    PatientRepository patientRepository;

    @Transactional
    public PatientResponseData register(PatientCreateData data) {
        Patient patient = new Patient(data.name(), data.email(), data.phone(), data.cpf());
        patientRepository.save(patient);
        return new PatientResponseData(patient);
    }

    public Page<PatientResponseData> list(Pageable pageable){
        return patientRepository.findAll(pageable)
                .map(PatientResponseData::new);
    }

    public PatientResponseData findById(Long id){
        Optional<Patient> obj =  patientRepository.findById(id);
        PatientResponseData patient = new PatientResponseData(obj.orElseThrow(() -> new ResourceNotFoundException(id)));
        return patient;
    }

    @Transactional
    public PatientResponseData update(Long id, PatientUpdateData data){
        Patient patient = patientRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
        if(data.getName() != null){
            patient.setName(data.getName());
        }

        if(data.getPhone() != null){
            patient.setPhone(data.getPhone());
        }

        PatientResponseData patientResponseData = new PatientResponseData(patient);
        return patientResponseData;
    }

    @Transactional
    public void delete(Long id){
        patientRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
        patientRepository.deleteById(id);
    }
}

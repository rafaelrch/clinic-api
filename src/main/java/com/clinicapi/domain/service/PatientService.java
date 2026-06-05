package com.clinicapi.domain.service;

import com.clinicapi.domain.patient.*;
import com.clinicapi.domain.service.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    @Autowired
    PatientRepository patientRepository;

    @Transactional
    public void register(PatientCreateData data) {
        Patient patient = new Patient(data.name(), data.email(), data.phone(), data.cpf());
        patientRepository.save(patient);
    }

    public List<PatientResponseData> list(){
        return patientRepository.findAll()
                .stream()
                .map(PatientResponseData::new)
                .toList();
    }

    public PatientResponseData findById(Long id){
        Optional<Patient> obj =  patientRepository.findById(id);
        PatientResponseData patient = new PatientResponseData(obj.orElseThrow(() -> new ResourceNotFoundException(id)));
        return patient;
    }

    @Transactional
    public void update(Long id, PatientUpdateData data){
        Patient patient = patientRepository.getReferenceById(id);
        if(data.getName() != null){
            patient.setName(data.getName());
        }

        if(data.getPhone() != null){
            patient.setPhone(data.getPhone());
        }
    }

    @Transactional
    public void delete(Long id){
        Patient patient = patientRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
        patientRepository.deleteById(id);
    }
}

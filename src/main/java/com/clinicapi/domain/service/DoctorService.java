package com.clinicapi.domain.service;

import com.clinicapi.domain.doctor.*;
import com.clinicapi.domain.service.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class DoctorService {

    @Autowired
    DoctorRepository doctorRepository;

    public void register(DoctorCreateData data) {
        Doctor doctor = new Doctor(data.name(), data.email(), data.phone(), data.crm(), data.specialty(), true);
        doctorRepository.save(doctor);
    }

    public List<DoctorResponseData> list(){
        return doctorRepository.findAllByActiveTrue()
                .stream()
                .map(DoctorResponseData::new)
                .toList();
    }

    public DoctorResponseData findById(Long id){
        Optional<Doctor> obj = doctorRepository.findById(id);
        DoctorResponseData doctor = new DoctorResponseData(obj.orElseThrow(() -> new ResourceNotFoundException(id)));
        return doctor;
    }

    @Transactional
    public void update(Long id, DoctorUpdateData data){
        Doctor doctor = doctorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
        if(data.getName() != null){
            doctor.setName(data.getName());
        }

        if(data.getSpecialty() != null){
            doctor.setSpecialty(data.getSpecialty());
        }

        if(data.getPhone() != null){
            doctor.setPhone(data.getPhone());
        }
    }

    @Transactional
    public void delete(Long id){
        Doctor doctor = doctorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
        doctor.setActive(false);
    }
}

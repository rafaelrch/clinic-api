package com.clinicapi.domain.service;

import com.clinicapi.domain.doctor.*;
import com.clinicapi.domain.service.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class DoctorService {

    @Autowired
    DoctorRepository doctorRepository;

    @Transactional
    public DoctorResponseData register(DoctorCreateData data) {
        Doctor doctor = new Doctor(data.name(), data.email(), data.phone(), data.crm(), data.specialty());
        doctorRepository.save(doctor);
        return new DoctorResponseData(doctor);
    }

    public Page<DoctorResponseData> list(Pageable pageable){
        return doctorRepository.findAllByActiveTrue(pageable)
                .map(DoctorResponseData::new);
    }

    public DoctorResponseData findById(Long id){
        Optional<Doctor> obj = doctorRepository.findByIdAndActiveTrue(id);
        return new DoctorResponseData(obj.orElseThrow(() -> new ResourceNotFoundException(id)));
    }

    @Transactional
    public DoctorResponseData update(Long id, DoctorUpdateData data){
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

        return new DoctorResponseData(doctor);
    }

    @Transactional
    public void delete(Long id){
        Doctor doctor = doctorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
        doctor.setActive(false);
    }
}

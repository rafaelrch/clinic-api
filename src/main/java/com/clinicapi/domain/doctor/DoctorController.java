package com.clinicapi.domain.doctor;

import com.clinicapi.domain.patient.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctors")
public class DoctorController {

    @Autowired
    DoctorRepository doctorRepository;

    @PostMapping
    public void register(@RequestBody Doctor doctor) {
        doctorRepository.save(doctor);
    }

    @GetMapping
    public List<Doctor> list(){
        return doctorRepository.findAll();
    }

    @PutMapping
    @Transactional
    public void update(@RequestBody DoctorUpdateData data){
        Doctor doctor = doctorRepository.getReferenceById(data.getId());
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

    @DeleteMapping("/{id}")
    @Transactional
    public void delete(@PathVariable Long id){
        doctorRepository.deleteById(id);
    }
}

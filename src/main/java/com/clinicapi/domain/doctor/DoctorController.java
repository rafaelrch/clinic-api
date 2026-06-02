package com.clinicapi.domain.doctor;

import com.clinicapi.domain.patient.Patient;
import jakarta.validation.Valid;
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
    public void register(@RequestBody @Valid DoctorCreateData data) {
        Doctor doctor = new Doctor(data.name(), data.email(), data.phone(), data.crm(), data.specialty());
        doctorRepository.save(doctor);
    }

    @GetMapping
    public List<DoctorResponseData> list(){
        return doctorRepository.findAll()
                .stream()
                .map(DoctorResponseData::new)
                .toList();
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

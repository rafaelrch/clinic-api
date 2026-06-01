package com.clinicapi.domain.doctor;

import org.springframework.beans.factory.annotation.Autowired;
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
        return
                doctorRepository.findAll();
    }
}

package com.clinicapi.domain.appointment;

import com.clinicapi.domain.doctor.Doctor;
import com.clinicapi.domain.doctor.DoctorRepository;
import com.clinicapi.domain.patient.Patient;
import com.clinicapi.domain.patient.PatientRepository;
import com.clinicapi.domain.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    AppointmentService service;

    @PostMapping
    public void register(@RequestBody @Valid AppointmentCreateData data) {
        service.register(data);
    }

    @GetMapping
    public List<AppointmentResponseData> list(){
        return service.list();
    }

    @GetMapping("/{id}")
    public AppointmentResponseData findById(@PathVariable Long id){
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @RequestBody AppointmentUpdateData data){
        service.update(id, data);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        service.delete(id);
    }

}

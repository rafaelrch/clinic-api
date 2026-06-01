package com.clinicapi.domain.appointment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    AppointmentRepository appointmentRepository;

    @PostMapping
    public void register(@RequestBody Appointment appointment) {
        appointmentRepository.save(appointment);
    }

    @GetMapping
    public List<Appointment> list(){
        return appointmentRepository.findAll();
    }

    @PutMapping
    @Transactional
    public void update(@RequestBody AppointmentUpdateData data){
        Appointment appointment = appointmentRepository.getReferenceById(data.getId());
        if(data.getDateTime() != null){
            appointment.setDateTime(data.getDateTime());
        }
    }

    @DeleteMapping("/{id}")
    @Transactional
    public void delete(@PathVariable Long id){
        appointmentRepository.deleteById(id);
    }

}

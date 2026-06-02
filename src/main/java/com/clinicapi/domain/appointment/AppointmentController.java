package com.clinicapi.domain.appointment;

import com.clinicapi.domain.doctor.Doctor;
import com.clinicapi.domain.doctor.DoctorRepository;
import com.clinicapi.domain.patient.Patient;
import com.clinicapi.domain.patient.PatientRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    DoctorRepository doctorRepository;

    @PostMapping
    public void register(@RequestBody @Valid AppointmentCreateData data) {

        Patient patient = patientRepository.getReferenceById(data.patientId());
        Doctor doctor = doctorRepository.getReferenceById(data.doctorId());

        Appointment appointment = new Appointment(patient, doctor, data.dateTime());
        appointmentRepository.save(appointment);
    }

    @GetMapping
    public List<AppointmentResponseData> list(){
        return appointmentRepository.findAll()
                .stream()
                .map(AppointmentResponseData::new)
                .toList();
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

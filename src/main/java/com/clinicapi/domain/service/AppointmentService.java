package com.clinicapi.domain.service;

import com.clinicapi.domain.appointment.*;
import com.clinicapi.domain.doctor.*;
import com.clinicapi.domain.patient.Patient;
import com.clinicapi.domain.patient.PatientRepository;
import com.clinicapi.domain.service.exceptions.BusinessRuleException;
import com.clinicapi.domain.service.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    DoctorRepository doctorRepository;

    @Autowired
    AppointmentRepository appointmentRepository;

    public void register(AppointmentCreateData data) {

        Patient patient = patientRepository.findById(data.patientId()).orElseThrow(() -> new ResourceNotFoundException(data.patientId()));

        Doctor doctor = doctorRepository.findById(data.doctorId()).orElseThrow(() -> new ResourceNotFoundException(data.doctorId()));

        if (doctor.getActive() == null || !doctor.getActive()){
            throw new BusinessRuleException("Doctor is not active");
        }

        if(appointmentRepository.existsByDoctorIdAndDateTime(doctor.getId(), data.dateTime())) {
            throw new BusinessRuleException("Doctor already has an appointment at this time");
        }

        if(appointmentRepository.existsByPatientIdAndDateTime(patient.getId(), data.dateTime())){
            throw new BusinessRuleException("Patient already has an appointment at this time");
        }

        Appointment appointment = new Appointment(patient, doctor, data.dateTime());

        appointmentRepository.save(appointment);
    }

    public List<AppointmentResponseData> list(){
        return appointmentRepository.findAll()
                .stream()
                .map(AppointmentResponseData::new)
                .toList();
    }

    public AppointmentResponseData findById(Long id){
        Optional<Appointment> obj = appointmentRepository.findById(id);
        AppointmentResponseData appointment = new AppointmentResponseData(obj.orElseThrow(() -> new ResourceNotFoundException(id)));
        return appointment;
    }

    @Transactional
    public void update(Long id, AppointmentUpdateData data){
        Appointment appointment = appointmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));

        if(data.getDateTime() != null){
            appointment.setDateTime(data.getDateTime());
        }
    }

    @Transactional
    public void delete(Long id){
        Appointment appointment = appointmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
        appointmentRepository.deleteById(id);
    }
}

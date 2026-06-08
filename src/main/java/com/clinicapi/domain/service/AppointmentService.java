package com.clinicapi.domain.service;

import com.clinicapi.domain.appointment.*;
import com.clinicapi.domain.doctor.*;
import com.clinicapi.domain.patient.Patient;
import com.clinicapi.domain.patient.PatientRepository;
import com.clinicapi.domain.service.exceptions.BusinessRuleException;
import com.clinicapi.domain.service.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

    public Page<AppointmentResponseData> list(Pageable pageable){
        return appointmentRepository.findAll(pageable)
                .map(AppointmentResponseData::new);
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
            LocalDateTime newDateTime = data.getDateTime();
            boolean doctorHasConflict = appointmentRepository.existsByDoctorIdAndDateTimeAndIdNot(appointment.getDoctor().getId(),
                    newDateTime,
                    appointment.getId());

            if(doctorHasConflict){
                throw new BusinessRuleException("Doctor already has an appointment at this time");
            }

            boolean patientHasConflict = appointmentRepository.existsByPatientIdAndDateTimeAndIdNot(appointment.getPatient().getId(),
                    newDateTime,
                    appointment.getId());

            if(patientHasConflict){
                throw new BusinessRuleException("Patient already has an appointment at this time");
            }

            appointment.setDateTime(newDateTime);
        }

    }

    @Transactional
    public void delete(Long id){
        appointmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
        appointmentRepository.deleteById(id);
    }
}

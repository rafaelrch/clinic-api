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

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.clinicapi.domain.appointment.AppointmentStatus.CONFIRMED;
import static com.clinicapi.domain.appointment.AppointmentStatus.SCHEDULED;

@Service
public class AppointmentService {

    private static final AppointmentStatus CANCELED_STATUS = AppointmentStatus.CANCELLED;

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

        if(appointmentRepository.existsByDoctorIdAndDateTimeAndStatusNot(doctor.getId(), data.dateTime(), CANCELED_STATUS)) {
            throw new BusinessRuleException("Doctor already has an appointment at this time");
        }

        if(appointmentRepository.existsByPatientIdAndDateTimeAndStatusNot(patient.getId(), data.dateTime(), CANCELED_STATUS)){
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
            boolean doctorHasConflict = appointmentRepository.existsByDoctorIdAndDateTimeAndIdNotAndStatusNot(appointment.getDoctor().getId(),
                    newDateTime,
                    appointment.getId(),
                    CANCELED_STATUS
            );

            if(doctorHasConflict){
                throw new BusinessRuleException("Doctor already has an appointment at this time");
            }

            boolean patientHasConflict = appointmentRepository.existsByPatientIdAndDateTimeAndIdNotAndStatusNot(appointment.getPatient().getId(),
                    newDateTime,
                    appointment.getId(),
                    CANCELED_STATUS
            );

            if(patientHasConflict){
                throw new BusinessRuleException("Patient already has an appointment at this time");
            }

            appointment.setDateTime(newDateTime);
        }

    }

    @Transactional
    public void cancel(Long id){
        Appointment appointment = appointmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));

        if(appointment.getStatus() != SCHEDULED && appointment.getStatus() != CONFIRMED) {
            throw new BusinessRuleException("Only scheduled or confirmed appointments can be cancelled!");
        }

        long hours = Duration.between(LocalDateTime.now(), appointment.getDateTime()).toHours();

        if(hours < 24){
            throw new BusinessRuleException("Appointments must be cancelled at least 24 hours in advance");
        }

        appointment.setStatus(CANCELED_STATUS);

    }
}

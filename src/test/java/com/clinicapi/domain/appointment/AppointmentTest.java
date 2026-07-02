package com.clinicapi.domain.appointment;

import com.clinicapi.domain.doctor.Doctor;
import com.clinicapi.domain.patient.Patient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class AppointmentTest {

    @Test
    @DisplayName("Should set status as SCHEDULED when appointment is created")
    void shouldSetStatusAsScheduledWhenAppointmentIsCreated(){

        // ARRANGE
        Patient patient = new Patient();
        Doctor doctor = new Doctor();
        LocalDateTime date = LocalDateTime.now().plusDays(1);

        // ACT
        Appointment appointment = new Appointment(patient, doctor, date);

        // ASSERT
        assertThat(appointment.getStatus()).isEqualTo(AppointmentStatus.SCHEDULED);

    }

    @Test
    @DisplayName("Should assign Patient, Doctor and Date when appointment is created")
    void shouldAssignPatientDoctorAndDateWhenAppointmentIsCreated(){

        //ARRANGE
        Patient patient = new Patient();
        Doctor doctor = new Doctor();
        LocalDateTime date = LocalDateTime.now().plusDays(1);

        // ACT
        Appointment appointment = new Appointment(patient, doctor, date);

        // ASSERT
        assertThat(appointment.getPatient()).isEqualTo(patient);
        assertThat(appointment.getDoctor()).isEqualTo(doctor);
        assertThat(appointment.getDateTime()).isEqualTo(date);

    }
}

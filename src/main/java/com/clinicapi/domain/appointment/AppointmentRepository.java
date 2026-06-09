package com.clinicapi.domain.appointment;


import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    boolean existsByDoctorIdAndDateTimeAndStatusNot(Long doctorId, LocalDateTime dateTime, AppointmentStatus status);

    boolean existsByPatientIdAndDateTimeAndStatusNot(Long patientId, LocalDateTime dateTime, AppointmentStatus status);

    boolean existsByDoctorIdAndDateTimeAndIdNotAndStatusNot(Long doctorId, LocalDateTime dateTime, Long appointmentId, AppointmentStatus status);

    boolean existsByPatientIdAndDateTimeAndIdNotAndStatusNot(Long patientId, LocalDateTime dateTime, Long appointmentId, AppointmentStatus status);
}

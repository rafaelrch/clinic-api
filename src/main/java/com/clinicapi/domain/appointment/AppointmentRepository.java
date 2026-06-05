package com.clinicapi.domain.appointment;


import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    boolean existsByDoctorIdAndDateTime(Long doctorId, LocalDateTime dateTime);

    boolean existsByPatientIdAndDateTime(Long patientId, LocalDateTime dateTime);
}

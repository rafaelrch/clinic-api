package com.clinicapi.domain.appointment;

import java.time.LocalDateTime;

public record AppointmentResponseData(Long id, String patientName, String doctorName, LocalDateTime dateTime, AppointmentStatus status) {
    public AppointmentResponseData(Appointment appointment) {
        this(appointment.getId(), appointment.getPatient().getName(), appointment.getDoctor().getName(), appointment.getDateTime(), appointment.getStatus());
    }
}

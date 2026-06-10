package com.clinicapi.domain.appointment;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record AppointmentResponseData(

        @Schema(description = "Appointment ID", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
        Long id,

        @Schema(description = "Patient full name", example = "Rafael West Rocha", accessMode = Schema.AccessMode.READ_ONLY)
        String patientName,

        @Schema(description = "Doctor full name", example = "Dr. Leopoldo Rocha", accessMode = Schema.AccessMode.READ_ONLY)
        String doctorName,

        @Schema(description = "Appointment date", example = "2027-06-15T10:00:00", accessMode = Schema.AccessMode.READ_ONLY)
        LocalDateTime dateTime,

        @Schema(description = "Appointment Status", example = "SCHEDULED", accessMode = Schema.AccessMode.READ_ONLY)
        AppointmentStatus status) {

    public AppointmentResponseData(Appointment appointment) {
        this(appointment.getId(), appointment.getPatient().getName(), appointment.getDoctor().getName(), appointment.getDateTime(), appointment.getStatus());
    }
}

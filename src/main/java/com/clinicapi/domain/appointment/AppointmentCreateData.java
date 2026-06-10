package com.clinicapi.domain.appointment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AppointmentCreateData(

        @Schema(description = "Patient ID", example = "1")
        @NotNull Long patientId,

        @Schema(description = "Doctor ID", example = "5")
        @NotNull Long doctorId,

        @Schema(description = "Appointment date", example = "2027-06-15T10:00:00")
        @NotNull @Future LocalDateTime dateTime) {
}

package com.clinicapi.domain.appointment;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AppointmentCreateData(@NotNull Long patientId,@NotNull Long doctorId,@NotNull @Future LocalDateTime dateTime) {
}

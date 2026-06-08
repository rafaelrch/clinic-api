package com.clinicapi.domain.appointment;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class AppointmentUpdateData {

    @NotNull
    @Future
    private LocalDateTime dateTime;

    public LocalDateTime getDateTime() {
        return dateTime;
    }
}

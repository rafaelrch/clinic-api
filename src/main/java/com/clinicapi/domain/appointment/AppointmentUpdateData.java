package com.clinicapi.domain.appointment;

import java.time.LocalDateTime;

public class AppointmentUpdateData {

    private Long id;
    private LocalDateTime dateTime;

    public Long getId() {
        return id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }
}

package com.clinicapi.domain.doctor;

import io.swagger.v3.oas.annotations.media.Schema;

public class DoctorUpdateData {

    @Schema(description = "Doctor full name", example = "Dr. Leopoldo Rocha")
    private String name;

    @Schema(description = "Doctor phone", example = "71999682356")
    private String phone;

    @Schema(description = "Doctor specialty", example = "CARDIOLOGY")
    private Specialty specialty;

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public Specialty getSpecialty() {
        return specialty;
    }

}

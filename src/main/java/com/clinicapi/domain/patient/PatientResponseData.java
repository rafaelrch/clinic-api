package com.clinicapi.domain.patient;

import io.swagger.v3.oas.annotations.media.Schema;

public record PatientResponseData(

        @Schema(description = "Patient Id", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
        Long id,

        @Schema(description = "Patient full name", example = "Rafael West Rocha", accessMode = Schema.AccessMode.READ_ONLY)
        String name,

        @Schema(description = "Patient email address", example = "rafael@gmail.com", accessMode = Schema.AccessMode.READ_ONLY)
        String email,

        @Schema(description = "Patient phone", example = "71999682356", accessMode = Schema.AccessMode.READ_ONLY)
        String phone) {

    public PatientResponseData(Patient patient){
        this(patient.getId(), patient.getName(), patient.getEmail(), patient.getPhone());
    }
}
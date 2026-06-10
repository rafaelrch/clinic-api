package com.clinicapi.domain.doctor;

import io.swagger.v3.oas.annotations.media.Schema;

public record DoctorResponseData(

        @Schema(description = "Doctor Id", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
        Long id,

        @Schema(description = "Doctor full name", example = "Dr. Leopoldo Rocha", accessMode = Schema.AccessMode.READ_ONLY)
        String name,

        @Schema(description = "Doctor email address", example = "leopoldo@gmail.com", accessMode = Schema.AccessMode.READ_ONLY)
        String email,

        @Schema(description = "Doctor CRM used", example = "Doctoralia", accessMode = Schema.AccessMode.READ_ONLY)
        String crm,

        @Schema(description = "Doctor specialty", example = "CARDIOLOGY", accessMode = Schema.AccessMode.READ_ONLY)
        Specialty specialty,

        @Schema(description = "Doctor active or deactivated", example = "true", accessMode = Schema.AccessMode.READ_ONLY)
        Boolean active) {


    public DoctorResponseData(Doctor doctor){
        this(doctor.getId(), doctor.getName(), doctor.getEmail(), doctor.getCrm(), doctor.getSpecialty(), doctor.getActive());
    }

}

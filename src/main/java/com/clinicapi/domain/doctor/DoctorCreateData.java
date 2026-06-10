package com.clinicapi.domain.doctor;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DoctorCreateData(

        @Schema(description = "Doctor full name", example = "Dr. Leopoldo Rocha")
        @NotBlank String name,

        @Schema(description = "Doctor email address", example = "leopoldo@gmail.com")
        @NotBlank @Email String email,

        @Schema(description = "Doctor phone", example = "71992837465")
        @NotBlank String phone,

        @Schema(description = "Doctor CRM registration number", example = "CRM-BA 12345")
        @NotBlank String crm,

        @Schema(description = "Doctor specialty", example = "CARDIOLOGY")
        @NotNull Specialty specialty) {
}

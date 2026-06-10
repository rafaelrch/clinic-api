package com.clinicapi.domain.patient;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record PatientCreateData(
        @Schema(description = "Patient full name", example = "Rafael West Rocha")
        @NotBlank String name,

        @Schema(description = "Patient email address", example = "rafael@gmail.com")
        @NotBlank @Email String email,

        @Schema(description = "Patient phone", example = "71999682356")
        @NotBlank String phone,

        @Schema(description = "Patient CPF", example = "07720394856")
        @NotBlank String cpf) {
}

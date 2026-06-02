package com.clinicapi.domain.patient;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record PatientCreateData(
        @NotBlank String name,
        @NotBlank @Email String email,
        @NotBlank String phone,
        @NotBlank String cpf) {
}

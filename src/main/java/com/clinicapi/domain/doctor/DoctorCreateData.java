package com.clinicapi.domain.doctor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DoctorCreateData(@NotBlank String name, @NotBlank @Email String email, @NotBlank String phone, @NotBlank String crm, @NotNull Specialty specialty) {
}

package com.clinicapi.domain.patient;

import io.swagger.v3.oas.annotations.media.Schema;

public class PatientUpdateData {

    @Schema(description = "Patient ID", example = "1")
    private Long id;

    @Schema(description = "Patient full name", example = "Rafael West Rocha")
    private String name;

    @Schema(description = "Patient phone", example = "719994837465")
    private String phone;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public PatientUpdateData() {
    }

    public PatientUpdateData(Long id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }
}

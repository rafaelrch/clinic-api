package com.clinicapi.domain.patient;

public record PatientResponseData(Long id, String name, String email, String phone) {
    public PatientResponseData(Patient patient){
        this(patient.getId(), patient.getName(), patient.getEmail(), patient.getPhone());
    }
}
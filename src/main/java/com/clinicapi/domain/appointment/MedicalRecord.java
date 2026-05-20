package com.clinicapi.domain.appointment;

import java.time.LocalDateTime;

public class MedicalRecord {
    private Long id;
    private Appointment appointment;
    private String diagnosis;
    private String prescription;
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public String getPrescription() {
        return prescription;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public MedicalRecord() {
    }

    public MedicalRecord(Appointment appointment, String diagnosis, String prescription) {
        this.appointment = appointment;
        this.diagnosis = diagnosis;
        this.prescription = prescription;
        this.createdAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Appointment: " + appointment
                + " | Diagnosis: " + diagnosis;
    }
}

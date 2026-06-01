package com.clinicapi.domain.doctor;

public class DoctorUpdateData {

    private Long id;
    private String name;
    private String phone;
    private Specialty specialty;

    public Long getId() {
        return id;
    }

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

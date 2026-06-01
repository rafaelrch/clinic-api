package com.clinicapi.domain.patient;

public class PatientUpdateData {
    private Long id;
    private String name;
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

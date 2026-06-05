package com.clinicapi.domain.doctor;

public record DoctorResponseData(Long id, String name, String email, String crm, Specialty specialty, Boolean active) {


    public DoctorResponseData(Doctor doctor){
        this(doctor.getId(), doctor.getName(), doctor.getEmail(), doctor.getCrm(), doctor.getSpecialty(), doctor.getActive());
    }

}

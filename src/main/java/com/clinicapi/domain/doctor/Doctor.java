package com.clinicapi.domain.doctor;

import com.clinicapi.domain.person.Person;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "doctors")
public class Doctor extends Person {

    private String crm;
    private Specialty specialty;
    private Boolean active;


    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }

    public Specialty getSpecialty() {
        return specialty;
    }

    public void setSpecialty(Specialty specialty) {
        this.specialty = specialty;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Doctor() {
    }

    public Doctor(String name, String email, String phone, String crm, Specialty specialty, Boolean active) {
        super(name, email, phone);
        this.crm = crm;
        this.specialty = specialty;
        this.active = true;
    }

    @Override
    public String toString() {
        return "Name: " + getName() + " | CRM: " + crm;
    }
}

package com.clinicapi.domain.patient;

import com.clinicapi.domain.person.Person;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "patients")
public class Patient extends Person {
    private String cpf;

    public String getCpf() {

        return cpf;
    }

    public void setCpf(String cpf) {

        this.cpf = cpf;
    }

    public Patient(){

    }

    public Patient(String name, String email, String phone, String cpf){
        super(name, email, phone);
        this.cpf = cpf;
    }

    @Override
    public String toString() {
        return "Patient: " + getName() +
                " | cpf: " + cpf;
    }
}

package com.clinicapi.domain.patient;

import com.clinicapi.domain.person.Person;
import java.time.LocalDate;

public class Patient extends Person {
    private String cpf;
    private LocalDate dateOfBirth;

    public String getCpf() {

        return cpf;
    }

    public void setCpf(String cpf) {

        this.cpf = cpf;
    }

    public LocalDate getDateOfBirth() {

        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {

        this.dateOfBirth = dateOfBirth;
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
                " | cpf: " + cpf +
                " | dateOfBirth= " + dateOfBirth;
    }
}

package com.clinicapi;

import com.clinicapi.domain.appointment.Appointment;
import com.clinicapi.domain.doctor.Doctor;
import com.clinicapi.domain.patient.Patient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;

import static com.clinicapi.domain.doctor.Specialty.NEUROLOGY;

@SpringBootApplication
public class ClinicApiApplication {

	public static void main(String[] args) {

		SpringApplication.run(ClinicApiApplication.class, args);


	}
}

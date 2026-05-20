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

		Doctor doctor = new Doctor("Dr Leopoldo", "leopoldo@gmail.com", "719994827452", "Doctoralia", NEUROLOGY);
		Patient patient = new Patient("Rafael Rocha", "rafael@gmail.com", "71999682356", "07726098538");
		Appointment appointment = new Appointment(patient, doctor, LocalDateTime.of(2025, 6, 15, 14, 30));

		System.out.println(doctor);
		System.out.println(patient);
		System.out.println(appointment);
	}


}

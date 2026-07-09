package com.clinicapi.domain.service;

import com.clinicapi.domain.appointment.Appointment;
import com.clinicapi.domain.appointment.AppointmentRepository;
import com.clinicapi.domain.appointment.AppointmentResponseData;
import com.clinicapi.domain.doctor.Doctor;
import com.clinicapi.domain.doctor.DoctorRepository;
import com.clinicapi.domain.doctor.Specialty;
import com.clinicapi.domain.patient.Patient;
import com.clinicapi.domain.patient.PatientRepository;
import com.clinicapi.domain.service.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.clinicapi.domain.appointment.AppointmentStatus.SCHEDULED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private AppointmentService service;

    @Test
    @DisplayName("Should Return Appointment When Appointment Exist")
    void shouldReturnAppointmentWhenAppointmentExist(){

        //ARRANGE
        LocalDateTime dataFixa = LocalDateTime.of(2026, 7, 10, 14, 0, 0);

        Doctor doctor = new Doctor("Dr. Name", "old@email.com", "71000000000", "CRM001", Specialty.PEDIATRICS);
        Patient patient = new Patient("Joao Silva", "joao@email.com", "11000000", "0339485769");
        Appointment appointment = new Appointment(patient, doctor, dataFixa);
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));

        //ACT
        AppointmentResponseData result = service.findById(1L);

        //ASSERT
        assertThat(result).isNotNull();
        assertThat(result.id()).isNull();
        assertThat(result.doctorName()).isEqualTo("Dr. Name");
        assertThat(result.patientName()).isEqualTo("Joao Silva");
        assertThat(result.dateTime()).isEqualTo(dataFixa);
        assertThat(result.status()).isEqualTo(SCHEDULED);
    }

    @Test
    @DisplayName("Should Throw ResourceNotFoundException When Appointment Not Exist")
    void shouldThrowResourceNotFoundExceptionWhenAppointmentNotExist(){
        //ARRANGE
        when(appointmentRepository.findById(99L)).thenReturn(Optional.empty());

        //ACT & ASSERT
        assertThatThrownBy(() -> service.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }
}

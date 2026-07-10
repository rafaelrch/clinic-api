package com.clinicapi.domain.service;

import com.clinicapi.domain.appointment.*;
import com.clinicapi.domain.doctor.Doctor;
import com.clinicapi.domain.doctor.DoctorRepository;
import com.clinicapi.domain.doctor.Specialty;
import com.clinicapi.domain.patient.Patient;
import com.clinicapi.domain.patient.PatientRepository;
import com.clinicapi.domain.service.exceptions.BusinessRuleException;
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
        LocalDateTime dataFixa = LocalDateTime.of(2036, 7, 10, 14, 0, 0);

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

    @Test
    @DisplayName("Should Throw ResourceNotFoundException When Patient Not Exist")
    void shouldThrowResourceNotFoundExceptionWhenPatientNotExist(){
        //ARRANGE
        LocalDateTime dataFixa = LocalDateTime.of(2036, 7, 10, 14, 0, 0);
        AppointmentCreateData data = new AppointmentCreateData(1L, 2L, dataFixa);
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        //ACT & ASSERT
        assertThatThrownBy(() -> service.register(data))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("1");
    }

    @Test
    @DisplayName("Should Throw ResourceNotFoundException When Doctor Not Exist")
    void shouldThrowResourceNotFoundExceptionWhenDoctorNotExist(){
        //ARRANGE
        Patient patient = new Patient("Joao Silva", "joao@email.com", "11000000", "0339485769");
        LocalDateTime dataFixa = LocalDateTime.of(2036, 7, 10, 14, 0, 0);

        AppointmentCreateData data = new AppointmentCreateData(1L, 2L, dataFixa);
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(2L)).thenReturn(Optional.empty());

        //ACT & ASSERT
        assertThatThrownBy(() -> service.register(data))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("2");

    }

    @Test
    @DisplayName("Should Throw BusinessRuleException When Doctor Is Inactive")
    void shouldThrowBusinessRuleExceptionWhenDoctorIsInactive(){
        //ARRANGE
        Patient patient = new Patient("Joao Silva", "joao@email.com", "11000000", "0339485769");
        Doctor doctor = new Doctor("Dr. Test", "test@email.com", "11999999999", "CRM123", Specialty.PEDIATRICS);
        doctor.setActive(false);
        LocalDateTime dataFixa = LocalDateTime.of(2036, 7, 10, 14, 0, 0);

        AppointmentCreateData data = new AppointmentCreateData(1L, 2L, dataFixa);
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(2L)).thenReturn(Optional.of(doctor));


        //ACT & ASSERT
        assertThatThrownBy(() -> service.register(data))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("Doctor is not active");
        assertThat(doctor.getActive()).isFalse();
    }

    @Test
    @DisplayName("Should Throw BusinessRuleException When Doctor Has Schedule Conflict")
    void shouldThrowBusinessRuleExceptionWhenDoctorHasScheduleConflict(){
        //ARRANGE
        Patient patient = new Patient("Joao Silva", "joao@email.com", "11000000", "0339485769");
        Doctor doctor = new Doctor("Dr. Test", "test@email.com", "11999999999", "CRM123", Specialty.PEDIATRICS);
        LocalDateTime dataFixa = LocalDateTime.of(2036, 7, 10, 14, 0, 0);

        AppointmentCreateData data = new AppointmentCreateData(1L, 2L, dataFixa);
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(2L)).thenReturn(Optional.of(doctor));
        when(appointmentRepository.existsByDoctorIdAndDateTimeAndStatusNot(any(), any(), any())).thenReturn(true);


        //ACT & ASSERT
        assertThatThrownBy(() -> service.register(data))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("Doctor already has an appointment at this time");
        verify(appointmentRepository, never()).save(any(Appointment.class));
    }

    @Test
    @DisplayName("Should Throw BusinessRuleException When Patient Has Schedule Conflict")
    void shouldThrowBusinessRuleExceptionWhenPatientHasScheduleConflict(){
        //ARRANGE
        Patient patient = new Patient("Joao Silva", "joao@email.com", "11000000", "0339485769");
        Doctor doctor = new Doctor("Dr. Test", "test@email.com", "11999999999", "CRM123", Specialty.PEDIATRICS);
        LocalDateTime dataFixa = LocalDateTime.of(2036, 7, 10, 14, 0, 0);

        AppointmentCreateData data = new AppointmentCreateData(1L, 2L, dataFixa);
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(2L)).thenReturn(Optional.of(doctor));
        when(appointmentRepository.existsByPatientIdAndDateTimeAndStatusNot(any(), any(), any())).thenReturn(true);


        //ACT & ASSERT
        assertThatThrownBy(() -> service.register(data))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("Patient already has an appointment at this time");
        verify(appointmentRepository, never()).save(any(Appointment.class));
    }

    @Test
    @DisplayName("Should Register Appointment When Data Is Valid")
    void shouldRegisterAppointmentWhenDataIsValid(){
        //ARRANGE
        Patient patient = new Patient("Joao Silva", "joao@email.com", "11000000", "0339485769");
        Doctor doctor = new Doctor("Dr. Test", "test@email.com", "11999999999", "CRM123", Specialty.PEDIATRICS);
        LocalDateTime dataFixa = LocalDateTime.of(2036, 7, 10, 14, 0, 0);

        AppointmentCreateData data = new AppointmentCreateData(1L, 2L, dataFixa);
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(2L)).thenReturn(Optional.of(doctor));

        //ACT
        AppointmentResponseData result = service.register(data);

        //ASSERT
        verify(appointmentRepository).save(any(Appointment.class));
        assertThat(result).isNotNull();
        assertThat(result.patientName()).isEqualTo("Joao Silva");
        assertThat(result.doctorName()).isEqualTo("Dr. Test");
        assertThat(result.status()).isEqualTo(SCHEDULED);
        assertThat(result.dateTime()).isEqualTo(dataFixa);
    }

    @Test
    @DisplayName("Should Update appointment when data is valid")
    void shouldUpdateAppointmentWhenDataIsValid(){
        //ARRANGE
        Patient patient = new Patient("Joao Silva", "joao@email.com", "11000000", "0339485769");
        Doctor doctor = new Doctor("Dr. Test", "test@email.com", "11999999999", "CRM123", Specialty.PEDIATRICS);
        LocalDateTime dataFixa = LocalDateTime.of(2036, 7, 10, 14, 0, 0);
        Appointment appointment = new Appointment(patient, doctor, dataFixa);

        AppointmentUpdateData data = new AppointmentUpdateData();
        LocalDateTime novaData = LocalDateTime.of(2036, 8, 11, 14, 0, 0);
        data.setDateTime(novaData);
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));

        //ACT
        AppointmentResponseData result = service.update(1L, data);

        //ASSERT
        verify(appointmentRepository, never()).save(any(Appointment.class));
        assertThat(result.dateTime()).isEqualTo(novaData);
        assertThat(appointment.getDoctor().getName()).isEqualTo("Dr. Test");
        assertThat(appointment.getPatient().getName()).isEqualTo("Joao Silva");
        assertThat(appointment.getDateTime()).isEqualTo(novaData);
    }

    @Test
    @DisplayName("Should Throw BusinessRuleException When Doctor Has Conflict On Update")
    void shouldThrowBusinessRuleExceptionWhenDoctorHasConflictOnUpdate(){
        //ARRANGE
        Patient patient = new Patient("Joao Silva", "joao@email.com", "11000000", "0339485769");
        Doctor doctor = new Doctor("Dr. Test", "test@email.com", "11999999999", "CRM123", Specialty.PEDIATRICS);
        LocalDateTime dataFixa = LocalDateTime.of(2036, 7, 10, 14, 0, 0);
        Appointment appointment = new Appointment(patient, doctor, dataFixa);

        AppointmentUpdateData data = new AppointmentUpdateData();
        LocalDateTime novaData = LocalDateTime.of(2036, 8, 11, 14, 0, 0);
        data.setDateTime(novaData);
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.existsByDoctorIdAndDateTimeAndIdNotAndStatusNot(any(), any(), any(), any())).thenReturn(true);

        //ACT & ASSERT
        assertThatThrownBy(() -> service.update(1L, data))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("Doctor already has an appointment at this time");
        verify(appointmentRepository, never()).save(any(Appointment.class));
    }

    @Test
    @DisplayName("Should Throw BusinessRuleException When Patient Has Conflict On Update")
    void shouldThrowBusinessRuleExceptionWhenPatientHasConflictOnUpdate(){
        //ARRANGE
        Patient patient = new Patient("Joao Silva", "joao@email.com", "11000000", "0339485769");
        Doctor doctor = new Doctor("Dr. Test", "test@email.com", "11999999999", "CRM123", Specialty.PEDIATRICS);
        LocalDateTime dataFixa = LocalDateTime.of(2036, 7, 10, 14, 0, 0);
        Appointment appointment = new Appointment(patient, doctor, dataFixa);

        AppointmentUpdateData data = new AppointmentUpdateData();
        LocalDateTime novaData = LocalDateTime.of(2036, 8, 11, 14, 0, 0);
        data.setDateTime(novaData);
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.existsByPatientIdAndDateTimeAndIdNotAndStatusNot(any(), any(), any(), any())).thenReturn(true);

        //ACT & ASSERT
        assertThatThrownBy(() -> service.update(1L, data))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("Patient already has an appointment at this time");
        verify(appointmentRepository, never()).save(any(Appointment.class));
    }

    @Test
    @DisplayName("Should Throw ResourceNotFoundException When Update Appointment Not Exist")
    void shouldThrowResourceNotFoundExceptionWhenUpdateAppointmentNotExist(){
        //ARRANGE
        AppointmentUpdateData data = new AppointmentUpdateData();
        when(appointmentRepository.findById(99L)).thenReturn(Optional.empty());

        //ACT & ASSERT
        assertThatThrownBy(() -> service.update(99L, data))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
        verify(appointmentRepository, never()).save(any(Appointment.class));
    }
}

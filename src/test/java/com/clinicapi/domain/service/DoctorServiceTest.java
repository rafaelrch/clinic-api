package com.clinicapi.domain.service;

import com.clinicapi.domain.doctor.*;
import com.clinicapi.domain.service.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DoctorServiceTest {

    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
    private DoctorService service;

    @Captor
    private ArgumentCaptor<Doctor> doctorCaptor;

    @Test
    @DisplayName("Should return DoctorResponseData when doctor exists and is active")
    void shouldReturnDoctorResponseDataWhenDoctorExists(){

        // ARRANGE
        Doctor doctor = new Doctor("Dr. Test", "test@email.com", "11999999999", "CRM123", Specialty.CARDIOLOGY);
        when(doctorRepository.findByIdAndActiveTrue(1L)).thenReturn(Optional.of(doctor));

        // ACT
        DoctorResponseData result = service.findById(1L);

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo("Dr. Test");
        assertThat(result.email()).isEqualTo("test@email.com");
        assertThat(result.specialty()).isEqualTo(Specialty.CARDIOLOGY);


    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when doctor does not exist or is inactive")
    void shouldThrowResourceNotFoundExceptionWhenDoctorDoesNotExist(){

        //ARRANGE
        when(doctorRepository.findByIdAndActiveTrue(99L)).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThatThrownBy(() -> service.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    @DisplayName("Should deactivate doctor when doctor exists")
    void shouldDeactivateDoctorWhenDoctorExists(){

        //ARRANGE
        Doctor doctor = new Doctor("Dr. Test 2", "test2@email.com", "11999999999", "CRM123", Specialty.PEDIATRICS);
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));

        //ACT
        service.delete(1L);

        //ASSERT
        assertThat(doctor.getActive()).isFalse();
        verify(doctorRepository).findById(1L);

    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when trying to delete non existent doctor")
    void shouldThrowResourceNotFoundExceptionWhenTryingToDeleteNonExistentDoctor(){

        //ARRANGE
        when(doctorRepository.findById(99L)).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThatThrownBy(() -> service.delete(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    @DisplayName("Should register a doctor when data is valid")
    void shouldRegisterDoctorWhenDataIsValid(){

        //ARRANGE
        DoctorCreateData data = new DoctorCreateData("Dr. Test", "test@gmail.com", "11999999999", "CRM123", Specialty.PEDIATRICS);

        //ACT
        DoctorResponseData result = service.register(data);

        //ASSERTS
        verify(doctorRepository).save(doctorCaptor.capture());
        Doctor captured = doctorCaptor.getValue();

        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo("Dr. Test");
        assertThat(result.email()).isEqualTo("test@gmail.com");
        assertThat(result.crm()).isEqualTo("CRM123");
        assertThat(result.specialty()).isEqualTo(Specialty.PEDIATRICS);

        assertThat(captured).isNotNull();
        assertThat(captured.getName()).isEqualTo("Dr. Test");
        assertThat(captured.getEmail()).isEqualTo("test@gmail.com");
        assertThat(captured.getCrm()).isEqualTo("CRM123");
        assertThat(captured.getSpecialty()).isEqualTo(Specialty.PEDIATRICS);
        assertThat(captured.getActive()).isTrue();
    }

    @Test
    @DisplayName("Should Update doctor when doctor exist and data is valid")
    void shouldUpdateDoctorWhenDoctorExistAndDataIsValid(){

        //ARRANGE
        Doctor doctor = new Doctor("Dr. Old Name", "old@email.com", "11000000000", "CRM000", Specialty.CARDIOLOGY);
        DoctorUpdateData data = new DoctorUpdateData();
        data.setName("Dr. New Name");
        data.setPhone("71999999999");
        data.setSpecialty(Specialty.PEDIATRICS);
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));

        //ACT
        DoctorResponseData result = service.update(1L, data);

        //ASSERTS
        verify(doctorRepository, never()).save(any(Doctor.class));
        assertThat(result.name()).isEqualTo("Dr. New Name");
        assertThat(result.phone()).isEqualTo("71999999999");
        assertThat(result.specialty()).isEqualTo(Specialty.PEDIATRICS);

        assertThat(doctor.getName()).isEqualTo("Dr. New Name");
        assertThat(doctor.getPhone()).isEqualTo("71999999999");
        assertThat(doctor.getSpecialty()).isEqualTo(Specialty.PEDIATRICS);

        assertThat(doctor.getEmail()).isEqualTo("old@email.com");
        assertThat(doctor.getCrm()).isEqualTo("CRM000");
    }

    @Test
    @DisplayName("Should Update Only Provide Fields When Partial Data Is Given")
    void shouldUpdateOnlyProvideFieldsWhenPartialDataIsGiven(){

        //ARRANGE
        Doctor doctor = new Doctor("Dr. Old Name 2", "old2@email.com", "71000000000", "CRM001", Specialty.PEDIATRICS);
        DoctorUpdateData data = new DoctorUpdateData();
        data.setName("Dr. New Name 2");
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));

        //ACT
        DoctorResponseData result = service.update(1L, data);

        //ASSERTS
        verify(doctorRepository, never()).save(any(Doctor.class));
        assertThat(result.name()).isEqualTo("Dr. New Name 2");
        assertThat(result.phone()).isEqualTo("71000000000");
        assertThat(result.specialty()).isEqualTo(Specialty.PEDIATRICS);

        assertThat(doctor.getName()).isEqualTo("Dr. New Name 2");
        assertThat(doctor.getPhone()).isEqualTo("71000000000");
        assertThat(doctor.getSpecialty()).isEqualTo(Specialty.PEDIATRICS);
    }

    @Test
    @DisplayName("should Throw ResourceNotFoundException When Try Update Non Existent Doctor")
    void shouldThrowResourceNotFoundExceptionWhenTryUpdateNonExistentDoctor(){

        //ARRANGE
        DoctorUpdateData data = new DoctorUpdateData();
        data.setName("Nome Test");
        data.setPhone("718283947");
        when(doctorRepository.findById(99L)).thenReturn(Optional.empty());

        //ACT & ASSERT
        assertThatThrownBy(() -> service.update(99L, data))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }



}

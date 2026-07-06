package com.clinicapi.domain.service;

import com.clinicapi.domain.doctor.Doctor;
import com.clinicapi.domain.doctor.DoctorRepository;
import com.clinicapi.domain.doctor.DoctorResponseData;
import com.clinicapi.domain.doctor.Specialty;
import com.clinicapi.domain.service.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class DoctorServiceTest {

    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
    private DoctorService service;

    @Test
    @DisplayName("Should throw ResourceNotFoundException when doctor does not exist or is inactive")
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
    @DisplayName("Should return empty when throw Resource Not Found Exception when doctor does not exist")
    void shouldThrowResourceNotFoundExceptionWhenDoctorDoesNotExist(){

        //ARRANGE
        when(doctorRepository.findByIdAndActiveTrue(99L)).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThatThrownBy(() -> service.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    @DisplayName("Should deactive doctor when doctor exists")
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


}

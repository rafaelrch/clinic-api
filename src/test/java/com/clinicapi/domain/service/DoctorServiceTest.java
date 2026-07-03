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
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class DoctorServiceTest {

    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
    private DoctorService service;

    @Test
    @DisplayName("Should return Doctor when Doctor exists and is active")
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


}

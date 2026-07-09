package com.clinicapi.domain.service;

import com.clinicapi.domain.patient.*;
import com.clinicapi.domain.service.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientService service;

    @Test
    @DisplayName("Should Return Patient Response Data When Patient Exists")
    void shouldReturnPatientResponseDataWhenPatientExists(){

        //ARRANGE
        Patient patient = new Patient("Joao Silva", "joao@email.com", "11000000", "0339485769");
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        //ACT
        PatientResponseData result = service.findById(1L);

        //ASSERTS
        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo("Joao Silva");
        assertThat(result.email()).isEqualTo("joao@email.com");
        assertThat(result.phone()).isEqualTo("11000000");
    }

    @Test
    @DisplayName("Should Throw ResourceNotFoundException When Patient Does Not Exist")
    void shouldThrowResourceNotFoundExceptionWhenPatientDoesNotExist(){

        //ARRANGE
        when(patientRepository.findById(99L)).thenReturn(Optional.empty());

        //ACT & ASSERT
        assertThatThrownBy(() -> service.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    @DisplayName("Should Delete Patient When Patient Exist")
    void shouldDeletePatientWhenPatientExist(){

        //ARRANGE
        Patient patient = new Patient("Joao Silva", "joao@email.com", "11000000", "0339485769");
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        //ACT
        service.delete(1L);

        //ASSERT
        verify(patientRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when try to delete Patient does not exist")
    void shouldThrowResourceNotFoundExceptionWhenTryToDeletePatientDoesNotExist(){

        //ARRANGE
        when(patientRepository.findById(99L)).thenReturn(Optional.empty());

        //ACT & ASSERT

        assertThatThrownBy(() -> service.delete(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
        verify(patientRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Should Register Patient When Data Is Valid")
    void shouldRegisterPatientWhenDataIsValid(){

        //ARRANGE
        PatientCreateData patient = new PatientCreateData("Joao Silva", "joao@email.com", "11000000", "0339485769");

        //ACT
        PatientResponseData result = service.register(patient);

        //ASSERT
        verify(patientRepository).save(any(Patient.class));
        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo("Joao Silva");
        assertThat(result.email()).isEqualTo("joao@email.com");
        assertThat(result.phone()).isEqualTo("11000000");
    }

    @Test
    @DisplayName("Should Update Patient When Data Is Valid")
    void shouldUpdatePatientWhenDataIsValid(){

        //ARRANGE
        Patient patient = new Patient("Joao Silva", "joao@email.com", "11000000", "0339485769");
        PatientUpdateData data = new PatientUpdateData(1L,"Silva Joao", "71988887777");
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        //ACT
        PatientResponseData result = service.update(1L, data);

        //ASSERT
        verify(patientRepository, never()).save(any(Patient.class));
        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo("Silva Joao");
        assertThat(result.email()).isEqualTo("joao@email.com");
        assertThat(result.phone()).isEqualTo("71988887777");

        assertThat(patient).isNotNull();
        assertThat(patient.getName()).isEqualTo("Silva Joao");
        assertThat(patient.getEmail()).isEqualTo("joao@email.com");
        assertThat(patient.getPhone()).isEqualTo("71988887777");
        assertThat(patient.getCpf()).isEqualTo("0339485769");
    }

    @Test
    @DisplayName("Should Update Only Provide Fields When Partial Data Is Given")
    void shouldUpdateOnlyProvideFieldsWhenPartialDataIsGiven(){

        //ARRANGE
        Patient patient = new Patient("Joao Silva", "joao@email.com", "11000423000", "0339485769");
        PatientUpdateData data = new PatientUpdateData(1L,"Silva Joao", null);
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        //ACT
        PatientResponseData result = service.update(1L, data);

        //ASSERT
        verify(patientRepository, never()).save(any(Patient.class));
        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo("Silva Joao");
        assertThat(result.email()).isEqualTo("joao@email.com");
        assertThat(result.phone()).isEqualTo("11000423000");

        assertThat(patient).isNotNull();
        assertThat(patient.getName()).isEqualTo("Silva Joao");
        assertThat(patient.getEmail()).isEqualTo("joao@email.com");
        assertThat(patient.getPhone()).isEqualTo("11000423000");
        assertThat(patient.getCpf()).isEqualTo("0339485769");
    }

    @Test
    @DisplayName("Should Throw ResourceNotFoundException When Try Update a Patient Not Exist")
    void shouldThrowResourceNotFoundExceptionWhenTryUpdatePatientNotExist(){
        //ARRANGE
        PatientUpdateData data = new PatientUpdateData(1L,"Silva Joao", null);
        when(patientRepository.findById(99L)).thenReturn(Optional.empty());

        //ACT & ASSERTS
        assertThatThrownBy(() -> service.update(99L, data))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
        verify(patientRepository, never()).save(any(Patient.class));
    }
}

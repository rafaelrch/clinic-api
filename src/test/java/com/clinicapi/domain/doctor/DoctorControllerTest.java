package com.clinicapi.domain.doctor;

import com.clinicapi.domain.service.DoctorService;
import com.clinicapi.domain.user.UserRepository;
import com.clinicapi.infra.security.SecurityConfig;
import com.clinicapi.infra.security.TokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@WebMvcTest(DoctorController.class)
@Import(SecurityConfig.class)
class DoctorControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    DoctorService service;

    @MockitoBean
    TokenService tokenService;

    @MockitoBean
    UserRepository userRepository;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("Should return 200 and doctor data when id exists")
    @WithMockUser(roles = "ADMIN")
    void shouldReturn200AndDoctorWhenIdExists() throws Exception{
        //ARRANGE
        DoctorResponseData data = new DoctorResponseData(1L, "Dr. Test", "test@email.com", "11999999999", "CRM123", Specialty.CARDIOLOGY, true);
        when(service.findById(1L)).thenReturn(data);

        //ACT & ASSERT
        mockMvc.perform(get("/doctors/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Dr. Test"))
                .andExpect(jsonPath("$.email").value("test@email.com"))
                .andExpect(jsonPath("$.phone").value("11999999999"))
                .andExpect(jsonPath("$.crm").value("CRM123"))
                .andExpect(jsonPath("$.specialty").value("CARDIOLOGY"));
    }

    @Test
    @DisplayName("Should return 403 when role is not allowed")
    @WithMockUser(roles = "DOCTOR")
    void shouldReturn403WhenUserHasNoPermission() throws Exception{
        //ACT & ASSERT
        mockMvc.perform(get("/doctors/1"))
                .andExpect(status().isForbidden());

    }

    @Test
    @DisplayName("Should return 201 and doctor data when data is valid")
    @WithMockUser(roles = "ADMIN")
    void shouldReturn201AndDoctorWhenDataIsValid() throws Exception{
        //ARRANGE
        DoctorCreateData doctor = new DoctorCreateData("Dr. Test", "test@email.com", "11999999999", "CRM123", Specialty.CARDIOLOGY);
        DoctorResponseData response = new DoctorResponseData(1L, "Dr. Test", "test@email.com", "11999999999", "CRM123", Specialty.CARDIOLOGY, true);
        when(service.register(any())).thenReturn(response);

        //ACT & ASSERT
        mockMvc.perform(post("/doctors").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(doctor)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Dr. Test"))
                .andExpect(jsonPath("$.email").value("test@email.com"))
                .andExpect(jsonPath("$.phone").value("11999999999"))
                .andExpect(jsonPath("$.crm").value("CRM123"))
                .andExpect(jsonPath("$.specialty").value("CARDIOLOGY"));
    }

    @Test
    @DisplayName("Should return 400 when data is invalid")
    @WithMockUser(roles = "ADMIN")
    void shouldReturn400WhenDataIsInvalid() throws Exception{
        //ARRANGE
        DoctorCreateData doctor = new DoctorCreateData("", "test@email.com", "11999999999", "CRM123", Specialty.CARDIOLOGY);

        //ACT & ASSERT

        mockMvc.perform(post("/doctors").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(doctor)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].field").value("name"));
        verify(service, never()).register(any());
    }

    @Test
    @DisplayName("Should return 403 when not admin")
    @WithMockUser(roles = "PATIENT")
    void shouldReturn403WhenNotAdmin() throws Exception{
        //ARRANGE
        DoctorCreateData doctor = new DoctorCreateData("Dr. Test", "test@email.com", "11999999999", "CRM123", Specialty.CARDIOLOGY);

        //ACT & ASSERT
        mockMvc.perform(post("/doctors").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(doctor)))
                .andExpect(status().isForbidden());
        verify(service, never()).register(any());
    }
}

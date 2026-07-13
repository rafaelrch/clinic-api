package com.clinicapi.domain.doctor;

import com.clinicapi.domain.service.DoctorService;
import com.clinicapi.domain.user.UserRepository;
import com.clinicapi.infra.security.SecurityConfig;
import com.clinicapi.infra.security.TokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.mockito.Mockito.when;


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
}

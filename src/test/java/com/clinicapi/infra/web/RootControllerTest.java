package com.clinicapi.infra.web;

import com.clinicapi.domain.user.UserRepository;
import com.clinicapi.infra.security.SecurityConfig;
import com.clinicapi.infra.security.TokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RootController.class)
@Import(SecurityConfig.class)
class RootControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    TokenService tokenService;

    @MockitoBean
    UserRepository userRepository;

    @Test
    @DisplayName("Should redirect to Swagger UI when accessing root without authentication")
    void shouldRedirectToSwaggerUiWhenAccessingRootUnauthenticated() throws Exception{
        //ACT & ASSERT
        mockMvc.perform(get("/"))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", "/swagger-ui/index.html"));
    }
}

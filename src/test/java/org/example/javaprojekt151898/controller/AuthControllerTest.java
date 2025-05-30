package org.example.javaprojekt151898.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.javaprojekt151898.interfaces.*;
import org.example.javaprojekt151898.service.AuthService;
import org.example.javaprojekt151898.security.JwtAuthFilter;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = AuthController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthFilter.class)
)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    @WithMockUser
    void login_shouldReturnToken() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO();
        LoginResponseDTO response = new LoginResponseDTO();
        response.setToken("mocked-token");

        Mockito.when(authService.login(any(LoginRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mocked-token"));
    }

    @Test
    @WithMockUser
    void register_shouldReturnUser() throws Exception {
        RegisterRequestDTO request = new RegisterRequestDTO();
        RegisterResponseDTO response = new RegisterResponseDTO();
        response.setId(1L);

        Mockito.when(authService.register(any(RegisterRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @WithMockUser(roles = {"CANDIDATE"})
    void getAccountInfo_shouldReturnUserDetails() throws Exception {
        UserDetailsDTO response = new UserDetailsDTO();
        response.setUsername("user");

        Mockito.when(authService.getAccountInfo(any())).thenReturn(response);

        mockMvc.perform(get("/auth")
                        .with(user("user").roles("CANDIDATE")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user"));
    }

    @Test
    @WithMockUser(roles = {"HR"})
    void updateAccountInfo_shouldReturnUpdatedUser() throws Exception {
        UpdateAccountDTO updateDto = new UpdateAccountDTO();
        UserDetailsDTO response = new UserDetailsDTO();
        response.setUsername("user");

        Mockito.when(authService.updateAccountInfo(any(), any(UpdateAccountDTO.class))).thenReturn(response);

        mockMvc.perform(put("/auth/user-details")
                        .with(csrf())
                        .with(user("user").roles("HR"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void changePassword_shouldReturnOk() throws Exception {
        ChangePasswordDTO dto = new ChangePasswordDTO();

        Mockito.doNothing().when(authService).changePassword(any(), any(ChangePasswordDTO.class));

        mockMvc.perform(post("/auth/change-password")
                        .with(csrf())
                        .with(user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }
} 
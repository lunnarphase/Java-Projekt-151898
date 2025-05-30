package org.example.javaprojekt151898.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.javaprojekt151898.interfaces.ApplicationRequestDTO;
import org.example.javaprojekt151898.interfaces.ApplicationResponseDTO;
import org.example.javaprojekt151898.service.ApplicationService;
import org.example.javaprojekt151898.security.JwtAuthFilter;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = ApplicationController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthFilter.class)
)
class ApplicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ApplicationService applicationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = {"CANDIDATE"})
    void getApplicationById_shouldReturnApplication() throws Exception {
        ApplicationResponseDTO responseDTO = new ApplicationResponseDTO();
        responseDTO.setId(1L);
        Mockito.when(applicationService.getApplicationById(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/applications/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @WithMockUser(roles = {"HR"})
    void getAllApplications_shouldReturnList() throws Exception {
        ApplicationResponseDTO responseDTO = new ApplicationResponseDTO();
        responseDTO.setId(1L);
        List<ApplicationResponseDTO> list = Collections.singletonList(responseDTO);
        Mockito.when(applicationService.getAllApplications()).thenReturn(list);

        mockMvc.perform(get("/api/applications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    @WithMockUser(roles = {"CANDIDATE"})
    void createApplication_shouldReturnCreatedApplication() throws Exception {
        ApplicationRequestDTO requestDTO = new ApplicationRequestDTO();
        ApplicationResponseDTO responseDTO = new ApplicationResponseDTO();
        responseDTO.setId(1L);

        Mockito.when(applicationService.createApplication(any(ApplicationRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/applications")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @WithMockUser(roles = {"CANDIDATE"})
    void updateApplication_shouldReturnUpdatedApplication() throws Exception {
        ApplicationRequestDTO requestDTO = new ApplicationRequestDTO();
        ApplicationResponseDTO responseDTO = new ApplicationResponseDTO();
        responseDTO.setId(1L);

        Mockito.when(applicationService.updateApplication(eq(1L), any(ApplicationRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/api/applications/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void deleteApplication_shouldReturnNoContent() throws Exception {
        Mockito.doNothing().when(applicationService).deleteApplication(1L);

        mockMvc.perform(delete("/api/applications/1")
                        .with(csrf()))
                .andExpect(status().isOk());
    }
}

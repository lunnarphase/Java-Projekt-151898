package org.example.javaprojekt151898.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.javaprojekt151898.interfaces.CandidateRequestDTO;
import org.example.javaprojekt151898.interfaces.CandidateResponseDTO;
import org.example.javaprojekt151898.service.CandidateService;
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

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = CandidateController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthFilter.class)
)
class CandidateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CandidateService candidateService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    @WithMockUser(roles = {"CANDIDATE"})
    void getCandidateById_shouldReturnCandidate() throws Exception {
        CandidateResponseDTO responseDTO = new CandidateResponseDTO();
        responseDTO.setId(1L);
        Mockito.when(candidateService.getCandidateById(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/candidates/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void getAllCandidates_shouldReturnList() throws Exception {
        CandidateResponseDTO responseDTO = new CandidateResponseDTO();
        responseDTO.setId(1L);
        List<CandidateResponseDTO> list = Collections.singletonList(responseDTO);
        Mockito.when(candidateService.getAllCandidates()).thenReturn(list);

        mockMvc.perform(get("/api/candidates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    @WithMockUser(roles = {"HR"})
    void getCandidatesByJobOfferId_shouldReturnList() throws Exception {
        CandidateResponseDTO responseDTO = new CandidateResponseDTO();
        responseDTO.setId(1L);
        List<CandidateResponseDTO> list = Collections.singletonList(responseDTO);
        Mockito.when(candidateService.getCandidatesByJobOfferId(1L)).thenReturn(list);

        mockMvc.perform(get("/api/candidates/by-job-offer/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    @WithMockUser(roles = {"CANDIDATE"})
    void createCandidate_shouldReturnCreatedCandidate() throws Exception {
        CandidateRequestDTO requestDTO = new CandidateRequestDTO();
        CandidateResponseDTO responseDTO = new CandidateResponseDTO();
        responseDTO.setId(1L);

        Mockito.when(candidateService.createCandidate(any(CandidateRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/candidates")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @WithMockUser(roles = {"HR"})
    void updateCandidate_shouldReturnUpdatedCandidate() throws Exception {
        CandidateRequestDTO requestDTO = new CandidateRequestDTO();
        CandidateResponseDTO responseDTO = new CandidateResponseDTO();
        responseDTO.setId(1L);

        Mockito.when(candidateService.updateCandidate(eq(1L), any(CandidateRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/api/candidates/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void deleteCandidate_shouldReturnNoContent() throws Exception {
        Mockito.doNothing().when(candidateService).deleteCandidate(1L);

        mockMvc.perform(delete("/api/candidates/1")
                        .with(csrf()))
                .andExpect(status().isOk());
    }
} 
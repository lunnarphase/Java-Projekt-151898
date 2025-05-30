package org.example.javaprojekt151898.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.javaprojekt151898.interfaces.JobOfferRequestDTO;
import org.example.javaprojekt151898.interfaces.JobOfferResponseDTO;
import org.example.javaprojekt151898.service.JobOfferService;
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
        controllers = JobOfferController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthFilter.class)
)
class JobOfferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JobOfferService jobOfferService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = {"CANDIDATE"})
    void getJobOfferById_shouldReturnJobOffer() throws Exception {
        JobOfferResponseDTO responseDTO = new JobOfferResponseDTO();
        responseDTO.setId(1L);
        Mockito.when(jobOfferService.getJobOfferById(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/job-offers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @WithMockUser(roles = {"CANDIDATE"})
    void getAllJobOffers_shouldReturnList() throws Exception {
        JobOfferResponseDTO responseDTO = new JobOfferResponseDTO();
        responseDTO.setId(1L);
        List<JobOfferResponseDTO> list = Collections.singletonList(responseDTO);
        Mockito.when(jobOfferService.getAllJobOffers()).thenReturn(list);

        mockMvc.perform(get("/api/job-offers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    @WithMockUser(roles = {"HR"})
    void createJobOffer_shouldReturnCreatedOffer() throws Exception {
        JobOfferRequestDTO requestDTO = new JobOfferRequestDTO();
        JobOfferResponseDTO responseDTO = new JobOfferResponseDTO();
        responseDTO.setId(1L);

        Mockito.when(jobOfferService.createJobOffer(any(JobOfferRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/job-offers")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @WithMockUser(roles = {"HR"})
    void updateJobOffer_shouldReturnUpdatedOffer() throws Exception {
        JobOfferRequestDTO requestDTO = new JobOfferRequestDTO();
        JobOfferResponseDTO responseDTO = new JobOfferResponseDTO();
        responseDTO.setId(1L);

        Mockito.when(jobOfferService.updateJobOffer(eq(1L), any(JobOfferRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/api/job-offers/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @WithMockUser(roles = {"HR"})
    void deleteJobOffer_shouldReturnNoContent() throws Exception {
        Mockito.doNothing().when(jobOfferService).deleteJobOffer(1L);

        mockMvc.perform(delete("/api/job-offers/1")
                        .with(csrf()))
                .andExpect(status().isOk());
    }
}
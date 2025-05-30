package org.example.javaprojekt151898.service;

import org.example.javaprojekt151898.entity.Application;
import org.example.javaprojekt151898.entity.ApplicationStatus;
import org.example.javaprojekt151898.interfaces.ApplicationRequestDTO;
import org.example.javaprojekt151898.interfaces.ApplicationResponseDTO;
import org.example.javaprojekt151898.repository.ApplicationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ApplicationServiceTest {
    @Mock
    private ApplicationRepository applicationRepository;

    @InjectMocks
    private ApplicationService applicationService;

    private Application application;
    private ApplicationRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        application = new Application();
        application.setId(1L);
        application.setStatus(ApplicationStatus.APPLIED);
        application.setNotes("Test notes");
        application.setAppliedAt(LocalDateTime.of(2023, 1, 1, 12, 0));

        requestDTO = new ApplicationRequestDTO();
        requestDTO.setStatus(ApplicationStatus.APPLIED);
        requestDTO.setNotes("Test notes");
        requestDTO.setCandidateId(2L);
        requestDTO.setJobOfferId(3L);
    }

    @Test
    void getApplicationById_shouldReturnResponseDTO_whenApplicationExists() {
        when(applicationRepository.findById(1L)).thenReturn(Optional.of(application));
        ApplicationResponseDTO dto = applicationService.getApplicationById(1L);
        assertNotNull(dto);
        assertEquals(application.getId(), dto.getId());
        assertEquals(application.getStatus(), dto.getStatus());
        assertEquals(application.getNotes(), dto.getNotes());
        assertEquals(application.getAppliedAt(), dto.getAppliedAt());
    }

    @Test
    void getApplicationById_shouldThrowException_whenNotFound() {
        when(applicationRepository.findById(2L)).thenReturn(Optional.empty());
        Exception exception = assertThrows(RuntimeException.class, () -> applicationService.getApplicationById(2L));
        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    void getAllApplications_shouldReturnListOfResponseDTOs() {
        when(applicationRepository.findAll()).thenReturn(Arrays.asList(application));
        List<ApplicationResponseDTO> dtos = applicationService.getAllApplications();
        assertEquals(1, dtos.size());
        assertEquals(application.getId(), dtos.get(0).getId());
    }

    @Test
    void createApplication_shouldSaveAndReturnResponseDTO() {
        when(applicationRepository.save(any(Application.class))).thenAnswer(i -> {
            Application app = i.getArgument(0);
            app.setId(1L);
            return app;
        });
        ApplicationResponseDTO dto = applicationService.createApplication(requestDTO);
        assertNotNull(dto);
        assertEquals(ApplicationStatus.APPLIED, dto.getStatus());
        assertEquals("Test notes", dto.getNotes());
        assertNotNull(dto.getAppliedAt());
    }

    @Test
    void updateApplication_shouldUpdateAndReturnResponseDTO_whenExists() {
        when(applicationRepository.findById(1L)).thenReturn(Optional.of(application));
        when(applicationRepository.save(any(Application.class))).thenAnswer(i -> i.getArgument(0));
        ApplicationRequestDTO updateDTO = new ApplicationRequestDTO();
        updateDTO.setStatus(ApplicationStatus.REJECTED);
        updateDTO.setNotes("Updated notes");
        ApplicationResponseDTO dto = applicationService.updateApplication(1L, updateDTO);
        assertEquals(ApplicationStatus.REJECTED, dto.getStatus());
        assertEquals("Updated notes", dto.getNotes());
    }

    @Test
    void updateApplication_shouldThrowException_whenNotFound() {
        when(applicationRepository.findById(2L)).thenReturn(Optional.empty());
        ApplicationRequestDTO updateDTO = new ApplicationRequestDTO();
        assertThrows(RuntimeException.class, () -> applicationService.updateApplication(2L, updateDTO));
    }

    @Test
    void deleteApplication_shouldDelete_whenExists() {
        when(applicationRepository.existsById(1L)).thenReturn(true);
        doNothing().when(applicationRepository).deleteById(1L);
        assertDoesNotThrow(() -> applicationService.deleteApplication(1L));
        verify(applicationRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteApplication_shouldThrowException_whenNotFound() {
        when(applicationRepository.existsById(2L)).thenReturn(false);
        Exception exception = assertThrows(RuntimeException.class, () -> applicationService.deleteApplication(2L));
        assertTrue(exception.getMessage().contains("does not exist"));
    }
} 
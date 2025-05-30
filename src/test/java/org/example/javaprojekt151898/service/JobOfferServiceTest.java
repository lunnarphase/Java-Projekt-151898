package org.example.javaprojekt151898.service;

import org.example.javaprojekt151898.entity.JobOffer;
import org.example.javaprojekt151898.entity.User;
import org.example.javaprojekt151898.interfaces.JobOfferRequestDTO;
import org.example.javaprojekt151898.interfaces.JobOfferResponseDTO;
import org.example.javaprojekt151898.repository.JobOfferRepository;
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

class JobOfferServiceTest {
    @Mock
    private JobOfferRepository jobOfferRepository;

    @InjectMocks
    private JobOfferService jobOfferService;

    private JobOffer jobOffer;
    private JobOfferRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jobOffer = new JobOffer();
        jobOffer.setId(1L);
        jobOffer.setTitle("Java Developer");
        jobOffer.setDescription("Opis oferty");
        jobOffer.setRequirements("Java, Spring");
        jobOffer.setLocation("Warszawa");
        jobOffer.setCompanyName("Firma XYZ");
        jobOffer.setPublishedAt(LocalDateTime.of(2023, 1, 1, 12, 0));
        jobOffer.setExpiresAt(LocalDateTime.of(2023, 12, 31, 23, 59));
        jobOffer.setActive(true);
        User user = new User();
        user.setId(2L);
        jobOffer.setCreatedBy(user);

        requestDTO = new JobOfferRequestDTO();
        requestDTO.setTitle("Java Developer");
        requestDTO.setDescription("Opis oferty");
        requestDTO.setRequirements("Java, Spring");
        requestDTO.setLocation("Warszawa");
        requestDTO.setCompanyName("Firma XYZ");
        requestDTO.setPublishedAt(LocalDateTime.of(2023, 1, 1, 12, 0));
        requestDTO.setExpiresAt(LocalDateTime.of(2023, 12, 31, 23, 59));
        requestDTO.setActive(true);
        requestDTO.setCreatedById(2L);
    }

    @Test
    void getJobOfferById_shouldReturnResponseDTO_whenJobOfferExists() {
        when(jobOfferRepository.findById(1L)).thenReturn(Optional.of(jobOffer));
        JobOfferResponseDTO dto = jobOfferService.getJobOfferById(1L);
        assertNotNull(dto);
        assertEquals(jobOffer.getId(), dto.getId());
        assertEquals(jobOffer.getTitle(), dto.getTitle());
        assertEquals(jobOffer.getDescription(), dto.getDescription());
        assertEquals(jobOffer.getRequirements(), dto.getRequirements());
        assertEquals(jobOffer.getLocation(), dto.getLocation());
        assertEquals(jobOffer.getCompanyName(), dto.getCompanyName());
        assertEquals(jobOffer.getPublishedAt(), dto.getPublishedAt());
        assertEquals(jobOffer.getExpiresAt(), dto.getExpiresAt());
        assertEquals(jobOffer.isActive(), dto.isActive());
        assertEquals(jobOffer.getCreatedBy().getId(), dto.getCreatedById());
    }

    @Test
    void getJobOfferById_shouldThrowException_whenNotFound() {
        when(jobOfferRepository.findById(2L)).thenReturn(Optional.empty());
        Exception exception = assertThrows(RuntimeException.class, () -> jobOfferService.getJobOfferById(2L));
        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    void getAllJobOffers_shouldReturnListOfResponseDTOs() {
        when(jobOfferRepository.findAll()).thenReturn(Arrays.asList(jobOffer));
        List<JobOfferResponseDTO> dtos = jobOfferService.getAllJobOffers();
        assertEquals(1, dtos.size());
        assertEquals(jobOffer.getId(), dtos.get(0).getId());
    }

    @Test
    void createJobOffer_shouldSaveAndReturnResponseDTO() {
        when(jobOfferRepository.save(any(JobOffer.class))).thenAnswer(i -> {
            JobOffer j = i.getArgument(0);
            j.setId(1L);
            return j;
        });
        JobOfferResponseDTO dto = jobOfferService.createJobOffer(requestDTO);
        assertNotNull(dto);
        assertEquals("Java Developer", dto.getTitle());
        assertEquals("Opis oferty", dto.getDescription());
        assertEquals("Java, Spring", dto.getRequirements());
        assertEquals("Warszawa", dto.getLocation());
        assertEquals("Firma XYZ", dto.getCompanyName());
        assertEquals(LocalDateTime.of(2023, 1, 1, 12, 0), dto.getPublishedAt());
        assertEquals(LocalDateTime.of(2023, 12, 31, 23, 59), dto.getExpiresAt());
        assertTrue(dto.isActive());
    }

    @Test
    void updateJobOffer_shouldUpdateAndReturnResponseDTO_whenExists() {
        when(jobOfferRepository.findById(1L)).thenReturn(Optional.of(jobOffer));
        when(jobOfferRepository.save(any(JobOffer.class))).thenAnswer(i -> i.getArgument(0));
        JobOfferRequestDTO updateDTO = new JobOfferRequestDTO();
        updateDTO.setTitle("Python Developer");
        updateDTO.setDescription("Nowy opis");
        updateDTO.setRequirements("Python, Django");
        updateDTO.setLocation("Kraków");
        updateDTO.setCompanyName("Firma ABC");
        updateDTO.setPublishedAt(LocalDateTime.of(2023, 2, 1, 10, 0));
        updateDTO.setExpiresAt(LocalDateTime.of(2023, 11, 30, 23, 59));
        updateDTO.setActive(false);
        updateDTO.setCreatedById(3L);
        JobOfferResponseDTO dto = jobOfferService.updateJobOffer(1L, updateDTO);
        assertEquals("Python Developer", dto.getTitle());
        assertEquals("Nowy opis", dto.getDescription());
        assertEquals("Python, Django", dto.getRequirements());
        assertEquals("Kraków", dto.getLocation());
        assertEquals("Firma ABC", dto.getCompanyName());
        assertEquals(LocalDateTime.of(2023, 2, 1, 10, 0), dto.getPublishedAt());
        assertEquals(LocalDateTime.of(2023, 11, 30, 23, 59), dto.getExpiresAt());
        assertFalse(dto.isActive());
    }

    @Test
    void updateJobOffer_shouldThrowException_whenNotFound() {
        when(jobOfferRepository.findById(2L)).thenReturn(Optional.empty());
        JobOfferRequestDTO updateDTO = new JobOfferRequestDTO();
        assertThrows(RuntimeException.class, () -> jobOfferService.updateJobOffer(2L, updateDTO));
    }

    @Test
    void deleteJobOffer_shouldDelete_whenExists() {
        when(jobOfferRepository.existsById(1L)).thenReturn(true);
        doNothing().when(jobOfferRepository).deleteById(1L);
        assertDoesNotThrow(() -> jobOfferService.deleteJobOffer(1L));
        verify(jobOfferRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteJobOffer_shouldThrowException_whenNotFound() {
        when(jobOfferRepository.existsById(2L)).thenReturn(false);
        Exception exception = assertThrows(RuntimeException.class, () -> jobOfferService.deleteJobOffer(2L));
        assertTrue(exception.getMessage().contains("does not exist"));
    }
} 
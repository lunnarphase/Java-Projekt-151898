package org.example.javaprojekt151898.service;

import org.example.javaprojekt151898.entity.Candidate;
import org.example.javaprojekt151898.entity.User;
import org.example.javaprojekt151898.interfaces.CandidateRequestDTO;
import org.example.javaprojekt151898.interfaces.CandidateResponseDTO;
import org.example.javaprojekt151898.repository.CandidateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CandidateServiceTest {
    @Mock
    private CandidateRepository candidateRepository;

    @InjectMocks
    private CandidateService candidateService;

    private Candidate candidate;
    private CandidateRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        candidate = new Candidate();
        candidate.setId(1L);
        candidate.setFirstName("Jan");
        candidate.setLastName("Kowalski");
        candidate.setContactEmail("jan.kowalski@example.com");
        candidate.setPhoneNumber("123456789");
        candidate.setCvPath("/cv/jan.pdf");
        candidate.setSkills("Java, Spring");
        User user = new User();
        user.setId(2L);
        candidate.setUser(user);

        requestDTO = new CandidateRequestDTO();
        requestDTO.setFirstName("Jan");
        requestDTO.setLastName("Kowalski");
        requestDTO.setContactEmail("jan.kowalski@example.com");
        requestDTO.setPhoneNumber("123456789");
        requestDTO.setCvPath("/cv/jan.pdf");
        requestDTO.setSkills("Java, Spring");
        requestDTO.setUserId(2L);
    }

    @Test
    void getCandidateById_shouldReturnResponseDTO_whenCandidateExists() {
        when(candidateRepository.findById(1L)).thenReturn(Optional.of(candidate));
        CandidateResponseDTO dto = candidateService.getCandidateById(1L);
        assertNotNull(dto);
        assertEquals(candidate.getId(), dto.getId());
        assertEquals(candidate.getFirstName(), dto.getFirstName());
        assertEquals(candidate.getLastName(), dto.getLastName());
        assertEquals(candidate.getContactEmail(), dto.getContactEmail());
        assertEquals(candidate.getPhoneNumber(), dto.getPhoneNumber());
        assertEquals(candidate.getCvPath(), dto.getCvPath());
        assertEquals(candidate.getSkills(), dto.getSkills());
        assertEquals(candidate.getUser().getId(), dto.getUserId());
    }

    @Test
    void getCandidateById_shouldThrowException_whenNotFound() {
        when(candidateRepository.findById(2L)).thenReturn(Optional.empty());
        Exception exception = assertThrows(RuntimeException.class, () -> candidateService.getCandidateById(2L));
        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    void getAllCandidates_shouldReturnListOfResponseDTOs() {
        when(candidateRepository.findAll()).thenReturn(Arrays.asList(candidate));
        List<CandidateResponseDTO> dtos = candidateService.getAllCandidates();
        assertEquals(1, dtos.size());
        assertEquals(candidate.getId(), dtos.get(0).getId());
    }

    @Test
    void getCandidatesByJobOfferId_shouldReturnListOfResponseDTOs() {
        when(candidateRepository.findByApplications_JobOffer_Id(5L)).thenReturn(Arrays.asList(candidate));
        List<CandidateResponseDTO> dtos = candidateService.getCandidatesByJobOfferId(5L);
        assertEquals(1, dtos.size());
        assertEquals(candidate.getId(), dtos.get(0).getId());
    }

    @Test
    void createCandidate_shouldSaveAndReturnResponseDTO() {
        when(candidateRepository.save(any(Candidate.class))).thenAnswer(i -> {
            Candidate c = i.getArgument(0);
            c.setId(1L);
            return c;
        });
        CandidateResponseDTO dto = candidateService.createCandidate(requestDTO);
        assertNotNull(dto);
        assertEquals("Jan", dto.getFirstName());
        assertEquals("Kowalski", dto.getLastName());
        assertEquals("jan.kowalski@example.com", dto.getContactEmail());
        assertEquals("123456789", dto.getPhoneNumber());
        assertEquals("/cv/jan.pdf", dto.getCvPath());
        assertEquals("Java, Spring", dto.getSkills());
    }

    @Test
    void updateCandidate_shouldUpdateAndReturnResponseDTO_whenExists() {
        when(candidateRepository.findById(1L)).thenReturn(Optional.of(candidate));
        when(candidateRepository.save(any(Candidate.class))).thenAnswer(i -> i.getArgument(0));
        CandidateRequestDTO updateDTO = new CandidateRequestDTO();
        updateDTO.setFirstName("Anna");
        updateDTO.setLastName("Nowak");
        updateDTO.setContactEmail("anna.nowak@example.com");
        updateDTO.setPhoneNumber("987654321");
        updateDTO.setCvPath("/cv/anna.pdf");
        updateDTO.setSkills("Python, SQL");
        CandidateResponseDTO dto = candidateService.updateCandidate(1L, updateDTO);
        assertEquals("Anna", dto.getFirstName());
        assertEquals("Nowak", dto.getLastName());
        assertEquals("anna.nowak@example.com", dto.getContactEmail());
        assertEquals("987654321", dto.getPhoneNumber());
        assertEquals("/cv/anna.pdf", dto.getCvPath());
        assertEquals("Python, SQL", dto.getSkills());
    }

    @Test
    void updateCandidate_shouldThrowException_whenNotFound() {
        when(candidateRepository.findById(2L)).thenReturn(Optional.empty());
        CandidateRequestDTO updateDTO = new CandidateRequestDTO();
        assertThrows(RuntimeException.class, () -> candidateService.updateCandidate(2L, updateDTO));
    }

    @Test
    void deleteCandidate_shouldDelete_whenExists() {
        when(candidateRepository.existsById(1L)).thenReturn(true);
        doNothing().when(candidateRepository).deleteById(1L);
        assertDoesNotThrow(() -> candidateService.deleteCandidate(1L));
        verify(candidateRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteCandidate_shouldThrowException_whenNotFound() {
        when(candidateRepository.existsById(2L)).thenReturn(false);
        Exception exception = assertThrows(RuntimeException.class, () -> candidateService.deleteCandidate(2L));
        assertTrue(exception.getMessage().contains("does not exist"));
    }
} 
package org.example.javaprojekt151898.service;

import org.example.javaprojekt151898.entity.User;
import org.example.javaprojekt151898.entity.UserRole;
import org.example.javaprojekt151898.interfaces.*;
import org.example.javaprojekt151898.repository.UserRepository;
import org.example.javaprojekt151898.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private AuthService authService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setLoginEmail("test@example.com");
        user.setPassword("encodedPassword");
        user.setRole(UserRole.CANDIDATE);
    }

    @Test
    void login_shouldReturnToken_whenCredentialsAreCorrect() {
        LoginRequestDTO loginRequest = new LoginRequestDTO();
        loginRequest.setLoginEmail("test@example.com");
        loginRequest.setPassword("password");
        when(userRepository.findByLoginEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);
        when(jwtUtil.generateToken("test@example.com", "CANDIDATE")).thenReturn("token123");

        LoginResponseDTO response = authService.login(loginRequest);
        assertNotNull(response);
        assertEquals("token123", response.getAccessToken());
    }

    @Test
    void login_shouldThrowException_whenUserNotFound() {
        LoginRequestDTO loginRequest = new LoginRequestDTO();
        loginRequest.setLoginEmail("notfound@example.com");
        loginRequest.setPassword("password");
        when(userRepository.findByLoginEmail("notfound@example.com")).thenReturn(Optional.empty());
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> authService.login(loginRequest));
        assertEquals(HttpStatus.UNAUTHORIZED, ex.getStatusCode());
    }

    @Test
    void login_shouldThrowException_whenPasswordIncorrect() {
        LoginRequestDTO loginRequest = new LoginRequestDTO();
        loginRequest.setLoginEmail("test@example.com");
        loginRequest.setPassword("wrong");
        when(userRepository.findByLoginEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "encodedPassword")).thenReturn(false);
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> authService.login(loginRequest));
        assertEquals(HttpStatus.UNAUTHORIZED, ex.getStatusCode());
    }

    @Test
    void register_shouldRegisterUser_whenEmailNotExists() {
        RegisterRequestDTO registerRequest = new RegisterRequestDTO();
        registerRequest.setUsername("testuser");
        registerRequest.setLoginEmail("test@example.com");
        registerRequest.setPassword("password");
        registerRequest.setRole(UserRole.CANDIDATE);
        when(userRepository.findByLoginEmail("test@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(i -> {
            User u = i.getArgument(0);
            u.setId(1L);
            return u;
        });
        RegisterResponseDTO response = authService.register(registerRequest);
        assertNotNull(response);
        assertEquals("User registered successfully.", response.getMessage());
        assertEquals(1L, response.getCreatedUserId());
    }

    @Test
    void register_shouldThrowException_whenEmailExists() {
        RegisterRequestDTO registerRequest = new RegisterRequestDTO();
        registerRequest.setLoginEmail("test@example.com");
        when(userRepository.findByLoginEmail("test@example.com")).thenReturn(Optional.of(user));
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> authService.register(registerRequest));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

    @Test
    void getAccountInfo_shouldReturnUserDetailsDTO_whenUserExists() {
        when(userDetails.getUsername()).thenReturn("test@example.com");
        when(userRepository.findByLoginEmail("test@example.com")).thenReturn(Optional.of(user));
        UserDetailsDTO dto = authService.getAccountInfo(userDetails);
        assertNotNull(dto);
        assertEquals(user.getId(), dto.getId());
        assertEquals(user.getUsername(), dto.getUsername());
        assertEquals(user.getLoginEmail(), dto.getLoginEmail());
        assertEquals(user.getRole(), dto.getRole());
    }

    @Test
    void getAccountInfo_shouldThrowException_whenUserNotFound() {
        when(userDetails.getUsername()).thenReturn("notfound@example.com");
        when(userRepository.findByLoginEmail("notfound@example.com")).thenReturn(Optional.empty());
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> authService.getAccountInfo(userDetails));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }

    @Test
    void updateAccountInfo_shouldUpdateAndReturnUserDetailsDTO_whenUserExists() {
        when(userDetails.getUsername()).thenReturn("test@example.com");
        when(userRepository.findByLoginEmail("test@example.com")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));
        UpdateAccountDTO updateDto = new UpdateAccountDTO();
        updateDto.setUsername("newuser");
        updateDto.setLoginEmail("new@example.com");
        UserDetailsDTO dto = authService.updateAccountInfo(userDetails, updateDto);
        assertEquals("newuser", dto.getUsername());
        assertEquals("new@example.com", dto.getLoginEmail());
    }

    @Test
    void updateAccountInfo_shouldThrowException_whenUserNotFound() {
        when(userDetails.getUsername()).thenReturn("notfound@example.com");
        when(userRepository.findByLoginEmail("notfound@example.com")).thenReturn(Optional.empty());
        UpdateAccountDTO updateDto = new UpdateAccountDTO();
        assertThrows(ResponseStatusException.class, () -> authService.updateAccountInfo(userDetails, updateDto));
    }

    @Test
    void changePassword_shouldChangePassword_whenCurrentPasswordCorrect() {
        when(userDetails.getUsername()).thenReturn("test@example.com");
        when(userRepository.findByLoginEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("oldpass", "encodedPassword")).thenReturn(true);
        when(passwordEncoder.encode("newpass")).thenReturn("encodedNew");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));
        ChangePasswordDTO dto = new ChangePasswordDTO();
        dto.setCurrentPassword("oldpass");
        dto.setNewPassword("newpass");
        assertDoesNotThrow(() -> authService.changePassword(userDetails, dto));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void changePassword_shouldThrowException_whenCurrentPasswordIncorrect() {
        when(userDetails.getUsername()).thenReturn("test@example.com");
        when(userRepository.findByLoginEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "encodedPassword")).thenReturn(false);
        ChangePasswordDTO dto = new ChangePasswordDTO();
        dto.setCurrentPassword("wrong");
        dto.setNewPassword("newpass");
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> authService.changePassword(userDetails, dto));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

    @Test
    void changePassword_shouldThrowException_whenUserNotFound() {
        when(userDetails.getUsername()).thenReturn("notfound@example.com");
        when(userRepository.findByLoginEmail("notfound@example.com")).thenReturn(Optional.empty());
        ChangePasswordDTO dto = new ChangePasswordDTO();
        assertThrows(ResponseStatusException.class, () -> authService.changePassword(userDetails, dto));
    }

    @Test
    void promoteToHR_shouldPromoteUser_whenUserExistsAndNotHR() {
        user.setRole(UserRole.CANDIDATE);
        when(userRepository.findByLoginEmail("test@example.com")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));
        User promoted = authService.promoteToHR("test@example.com");
        assertEquals(UserRole.HR, promoted.getRole());
    }

    @Test
    void promoteToHR_shouldThrowException_whenUserNotFound() {
        when(userRepository.findByLoginEmail("notfound@example.com")).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> authService.promoteToHR("notfound@example.com"));
    }

    @Test
    void promoteToHR_shouldThrowException_whenUserIsAlreadyHR() {
        user.setRole(UserRole.HR);
        when(userRepository.findByLoginEmail("test@example.com")).thenReturn(Optional.of(user));
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> authService.promoteToHR("test@example.com"));
        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }
} 
package org.example.javaprojekt151898.service;

import org.example.javaprojekt151898.entity.User;
import org.example.javaprojekt151898.interfaces.*;
import org.example.javaprojekt151898.repository.UserRepository;
import org.example.javaprojekt151898.security.JwtUtil; // Musisz utworzyć/posiadać klasę JwtUtil
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;  // Klasa do generowania/parsowania JWT

    public LoginResponseDTO login(LoginRequestDTO body) {
        String loginEmail = body.getLoginEmail();
        String password = body.getPassword();

        User user = userRepository
                .findByLoginEmail(loginEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
        // W roli (np. "CANDIDATE" lub "HR") musimy pamiętać, żeby Spring Security wymagał "ROLE_HR" / "ROLE_CANDIDATE" w configu
        String token = jwtUtil.generateToken(user.getLoginEmail(), user.getRole().name());
        LoginResponseDTO response = new LoginResponseDTO();
        response.setAccessToken(token);

        return response;
    }

    public RegisterResponseDTO register(RegisterRequestDTO body) {
        if (userRepository.findByLoginEmail(body.getLoginEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with this email already exists");
        }

        User newUser = new User();
        newUser.setUsername(body.getUsername());
        newUser.setLoginEmail(body.getLoginEmail());
        newUser.setPassword(passwordEncoder.encode(body.getPassword()));
        newUser.setRole(body.getRole()); // np. CANDIDATE lub HR

        User savedUser = userRepository.save(newUser);

        RegisterResponseDTO response = new RegisterResponseDTO();
        response.setMessage("User registered successfully.");
        response.setCreatedUserId(savedUser.getId());
        return response;
    }

    public UserDetailsDTO getAccountInfo(UserDetails userDetails) {
        User user = userRepository
                .findByLoginEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        UserDetailsDTO dto = new UserDetailsDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setLoginEmail(user.getLoginEmail());
        dto.setRole(user.getRole());
        return dto;
    }

    public UserDetailsDTO updateAccountInfo(UserDetails userDetails, UpdateAccountDTO updateDto) {
        User user = userRepository
                .findByLoginEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (updateDto.getUsername() != null && !updateDto.getUsername().trim().isEmpty()) {
            user.setUsername(updateDto.getUsername());
        }
        if (updateDto.getLoginEmail() != null && !updateDto.getLoginEmail().trim().isEmpty()) {
            user.setLoginEmail(updateDto.getLoginEmail());
        }

        User savedUser = userRepository.save(user);

        UserDetailsDTO dto = new UserDetailsDTO();
        dto.setId(savedUser.getId());
        dto.setUsername(savedUser.getUsername());
        dto.setLoginEmail(savedUser.getLoginEmail());
        dto.setRole(savedUser.getRole());
        return dto;
    }

    public void changePassword(UserDetails userDetails, ChangePasswordDTO dto) {
        User user = userRepository
                .findByLoginEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }

    public User promoteToHR(String loginEmail) {
        User user = userRepository
                .findByLoginEmail(loginEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (user.getRole() == null || user.getRole() == org.example.javaprojekt151898.entity.UserRole.HR) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is already HR or has no role set");
        }
        user.setRole(org.example.javaprojekt151898.entity.UserRole.HR);
        return userRepository.save(user);
    }
}
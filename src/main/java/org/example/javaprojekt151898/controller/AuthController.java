package org.example.javaprojekt151898.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.javaprojekt151898.interfaces.*;
import org.example.javaprojekt151898.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth API", description = "Authentication and user management")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Operation(summary = "ALL - Login", description = "Authenticate user and get JWT token")
    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO body) {
        return authService.login(body);
    }

    @Operation(summary = "ALL - Register", description = "Creates a new user account with provided details")
    @PostMapping("/register")
    public RegisterResponseDTO register(@RequestBody RegisterRequestDTO body) {
        return authService.register(body);
    }

    @Operation(summary = "ALL - Get user details", description = "Returns details of the currently authenticated user")
    @GetMapping
    @PreAuthorize("hasAnyRole('CANDIDATE', 'HR', 'ADMIN')")
    public UserDetailsDTO getAccountInfo(@AuthenticationPrincipal UserDetails userDetails) {
        return authService.getAccountInfo(userDetails);
    }

    @Operation(summary = "ALL - Update user details", description = "Updates basic information of the authenticated user")
    @PutMapping("/user-details")
    @PreAuthorize("hasAnyRole('CANDIDATE', 'HR', 'ADMIN')")
    public UserDetailsDTO updateAccountInfo(@AuthenticationPrincipal UserDetails userDetails,
                                            @RequestBody UpdateAccountDTO updateDto) {
        return authService.updateAccountInfo(userDetails, updateDto);
    }

    @Operation(summary = "ALL - Change password", description = "Changes password for the authenticated user after verifying current password")
    @PostMapping("/change-password")
    @PreAuthorize("hasAnyRole('CANDIDATE', 'HR', 'ADMIN')")
    public void changePassword(@AuthenticationPrincipal UserDetails userDetails,
                               @RequestBody ChangePasswordDTO dto) {
        authService.changePassword(userDetails, dto);
    }

    @Operation(summary = "ADMIN - Promote user to HR role", description = "Elevates user privileges to HR role (admin functionality)")
    @PostMapping("/promote-hr")
    @PreAuthorize("hasRole('ADMIN')")
    public UserDetailsDTO promoteUserToHR(@RequestParam String loginEmail) {
        var user = authService.promoteToHR(loginEmail);
        UserDetailsDTO dto = new UserDetailsDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setLoginEmail(user.getLoginEmail());
        dto.setRole(user.getRole());
        return dto;
    }

}
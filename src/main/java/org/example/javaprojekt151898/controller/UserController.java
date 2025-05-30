package org.example.javaprojekt151898.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.example.javaprojekt151898.entity.User;
import org.example.javaprojekt151898.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Operations related to users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "ALL - Get user by ID", description = "Retrieve a user by its ID")
    @PreAuthorize("hasAnyRole('CANDIDATE', 'HR', 'ADMIN')")
    public User getUserById(
            @Parameter(description = "ID of the user to be retrieved", required = true)
            @PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "ALL - Update existing user", description = "Updates an existing user by its ID")
    @PreAuthorize("hasAnyRole('CANDIDATE', 'HR', 'ADMIN')")
    public User updateUser(
            @Parameter(description = "ID of the user to be updated", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated user data", required = true)
            @RequestBody User updatedUser) {
        return userService.updateUser(id, updatedUser);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "ADMIN - Delete user by ID", description = "Deletes the user with the specified ID")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(
            @Parameter(description = "ID of the user to be deleted", required = true)
            @PathVariable Long id) {
        userService.deleteUser(id);
    }

    @PostMapping
    @Operation(summary = "ALL - Create a new user", description = "Creates a new user in the database")
    @PreAuthorize("hasAnyRole('CANDIDATE', 'HR', 'ADMIN')")
    public User createUser(
            @Parameter(description = "User to be created", required = true)
            @RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping
    @Operation(summary = "ADMIN - Get all users", description = "Retrieve a list of all users present in the database")
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
}
package org.example.javaprojekt151898.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.example.javaprojekt151898.interfaces.UserRequestDTO;
import org.example.javaprojekt151898.interfaces.UserResponseDTO;
import org.example.javaprojekt151898.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    @Operation(summary = "Get user by ID", description = "Retrieve a user by its ID")
    public UserResponseDTO getUserById(
            @Parameter(description = "ID of the user to be retrieved", required = true)
            @PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update existing user", description = "Updates an existing user by its ID")
    public UserResponseDTO updateUser(
            @Parameter(description = "ID of the user to be updated", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated user data", required = true)
            @RequestBody UserRequestDTO updatedUser) {
        return userService.updateUser(id, updatedUser);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user by ID", description = "Deletes the user with the specified ID")
    public void deleteUser(
            @Parameter(description = "ID of the user to be deleted", required = true)
            @PathVariable Long id) {
        userService.deleteUser(id);
    }

    @PostMapping
    @Operation(summary = "Create a new user", description = "Creates a new user in the database")
    public UserResponseDTO createUser(
            @Parameter(description = "User to be created", required = true)
            @RequestBody UserRequestDTO userRequestDTO) {
        return userService.createUser(userRequestDTO);
    }

    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieve a list of all users present in the database")
    public List<UserResponseDTO> getAllUsers() {
        return userService.getAllUsers();
    }
}

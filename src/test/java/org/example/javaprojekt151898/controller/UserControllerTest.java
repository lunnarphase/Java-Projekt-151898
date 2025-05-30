package org.example.javaprojekt151898.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.javaprojekt151898.entity.User;
import org.example.javaprojekt151898.entity.UserRole;
import org.example.javaprojekt151898.service.UserService;
import org.junit.jupiter.api.BeforeEach;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = UserController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = org.example.javaprojekt151898.security.JwtAuthFilter.class)
)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("password");
        user.setLoginEmail("test@example.com");
        user.setRole(UserRole.CANDIDATE);
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void getAllUsers_shouldReturnListOfUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of(user));
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].username").value("testuser"))
                .andExpect(jsonPath("$[0].loginEmail").value("test@example.com"))
                .andExpect(jsonPath("$[0].role").value("CANDIDATE"));
    }

    @Test
    @WithMockUser(roles = {"CANDIDATE"})
    void getUserById_shouldReturnUser_whenExists() throws Exception {
        when(userService.getUserById(1L)).thenReturn(user);
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.loginEmail").value("test@example.com"))
                .andExpect(jsonPath("$.role").value("CANDIDATE"));
    }

    @Test
    @WithMockUser(roles = {"CANDIDATE"})
    void createUser_shouldCreateAndReturnUser() throws Exception {
        when(userService.createUser(any(User.class))).thenReturn(user);
        mockMvc.perform(post("/api/users")
                        .with(csrf()) // <-- DODAJ TO!
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.loginEmail").value("test@example.com"))
                .andExpect(jsonPath("$.role").value("CANDIDATE"));
    }

    @Test
    @WithMockUser(roles = {"CANDIDATE"})
    void updateUser_shouldUpdateAndReturnUser() throws Exception {
        when(userService.updateUser(eq(1L), any(User.class))).thenReturn(user);
        mockMvc.perform(put("/api/users/1")
                        .with(csrf()) // <-- DODAJ TO!
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void deleteUser_shouldReturnOk() throws Exception {
        mockMvc.perform(delete("/api/users/1")
                        .with(csrf())) // <-- DODAJ TO!
                .andExpect(status().isOk());
        Mockito.verify(userService).deleteUser(1L);
    }
}
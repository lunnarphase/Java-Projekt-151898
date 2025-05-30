package org.example.javaprojekt151898.service;

import org.example.javaprojekt151898.entity.User;
import org.example.javaprojekt151898.repository.UserRepository;
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

class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("password");
        user.setLoginEmail("test@example.com");
    }

    @Test
    void getUserById_shouldReturnUser_whenUserExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        User found = userService.getUserById(1L);
        assertNotNull(found);
        assertEquals("testuser", found.getUsername());
    }

    @Test
    void getUserById_shouldThrowException_whenUserNotFound() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        Exception exception = assertThrows(RuntimeException.class, () -> userService.getUserById(2L));
        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    void updateUser_shouldUpdateAndReturnUser_whenUserExists() {
        User updated = new User();
        updated.setUsername("newuser");
        updated.setPassword("newpass");
        updated.setLoginEmail("new@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User result = userService.updateUser(1L, updated);
        assertEquals("newuser", result.getUsername());
        assertEquals("newpass", result.getPassword());
        assertEquals("new@example.com", result.getLoginEmail());
    }

    @Test
    void updateUser_shouldThrowException_whenUserNotFound() {
        User updated = new User();
        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> userService.updateUser(2L, updated));
    }

    @Test
    void deleteUser_shouldDelete_whenUserExists() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);
        assertDoesNotThrow(() -> userService.deleteUser(1L));
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteUser_shouldThrowException_whenUserNotFound() {
        when(userRepository.existsById(2L)).thenReturn(false);
        Exception exception = assertThrows(RuntimeException.class, () -> userService.deleteUser(2L));
        assertTrue(exception.getMessage().contains("does not exist"));
    }

    @Test
    void createUser_shouldSaveAndReturnUser() {
        when(userRepository.save(user)).thenReturn(user);
        User created = userService.createUser(user);
        assertNotNull(created);
        assertEquals("testuser", created.getUsername());
    }

    @Test
    void getAllUsers_shouldReturnListOfUsers() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(user));
        List<User> users = userService.getAllUsers();
        assertEquals(1, users.size());
        assertEquals("testuser", users.get(0).getUsername());
    }
}
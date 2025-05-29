package org.example.javaprojekt151898.service;

import org.example.javaprojekt151898.entity.User;
import org.example.javaprojekt151898.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) { this.userRepository = userRepository; } // Konstruktor

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User with ID " + id + " not found."));
    }

    public User updateUser(Long id, User updatedUserData) {
        User existingUser = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User with ID " + id + " not found."));

        existingUser.setUsername(updatedUserData.getUsername());
        existingUser.setUsername(updatedUserData.getUsername());
        existingUser.setPassword(updatedUserData.getPassword());
        existingUser.setLoginEmail(updatedUserData.getLoginEmail());

        return userRepository.save(existingUser);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User with ID " + id + " does not exist.");
        }
        userRepository.deleteById(id);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
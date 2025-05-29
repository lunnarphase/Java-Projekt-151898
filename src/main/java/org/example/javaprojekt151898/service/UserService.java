package org.example.javaprojekt151898.service;

import org.example.javaprojekt151898.entity.User;
import org.example.javaprojekt151898.entity.UserRole;
import org.example.javaprojekt151898.interfaces.UserRequestDTO;
import org.example.javaprojekt151898.interfaces.UserResponseDTO;
import org.example.javaprojekt151898.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User with ID " + id + " not found."));
        return convertToResponseDTO(user);
    }

    public UserResponseDTO updateUser(Long id, UserRequestDTO updatedUserData) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User with ID " + id + " not found."));

        existingUser.setUsername(updatedUserData.getUsername());
        existingUser.setPassword(passwordEncoder.encode(updatedUserData.getPassword()));
        existingUser.setLoginEmail(updatedUserData.getLoginEmail());
        existingUser.setRole(updatedUserData.getRole());

        User updatedUser = userRepository.save(existingUser);
        return convertToResponseDTO(updatedUser);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User with ID " + id + " does not exist.");
        }
        userRepository.deleteById(id);
    }

    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        User user = new User();
        user.setUsername(userRequestDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        user.setLoginEmail(userRequestDTO.getLoginEmail());
        user.setRole(userRequestDTO.getRole());

        User savedUser = userRepository.save(user);
        return convertToResponseDTO(savedUser);
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    private UserResponseDTO convertToResponseDTO(User user) {
        UserResponseDTO responseDTO = new UserResponseDTO();
        responseDTO.setId(user.getId());
        responseDTO.setUsername(user.getUsername());
        responseDTO.setLoginEmail(user.getLoginEmail());
        responseDTO.setRole(user.getRole());
        return responseDTO;
    }
}

package org.example.javaprojekt151898.repository;

import org.example.javaprojekt151898.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLoginEmail(String loginEmail);
}

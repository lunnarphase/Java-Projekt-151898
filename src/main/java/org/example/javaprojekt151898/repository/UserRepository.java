package org.example.javaprojekt151898.repository;

import org.example.javaprojekt151898.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}

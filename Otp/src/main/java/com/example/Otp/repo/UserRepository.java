package com.example.Otp.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Otp.Entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
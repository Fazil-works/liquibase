package com.example.Otp.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Otp.Entities.Otp;

public interface OtpRepository extends JpaRepository<Otp, Long> {
    Optional<Otp> findByEmail(String email);
    }
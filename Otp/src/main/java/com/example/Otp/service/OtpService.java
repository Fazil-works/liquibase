package com.example.Otp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.Otp.Entities.Otp;
import com.example.Otp.Entities.User;
import com.example.Otp.repo.OtpRepository;
import com.example.Otp.repo.UserRepository;
import jakarta.mail.MessagingException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class OtpService {

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    private String generateOtp() {
        int number = 100000 + new SecureRandom().nextInt(900000);
        return String.valueOf(number);
    }

    // ✅ Create user + OTP + send email
    public void createUserAndSendOtp(String email, String password) throws MessagingException {
        // 1️⃣ Save user with verified=false
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setVerified(false);
        userRepository.save(user);

        // 2️⃣ Generate OTP
        String otp = generateOtp();
        Otp otpEntity = new Otp();
        otpEntity.setEmail(email);
        otpEntity.setOtpCode(otp);
        otpEntity.setExpiryTime(LocalDateTime.now().plusMinutes(10));
        otpRepository.save(otpEntity);

        // 3️⃣ Send OTP email
        emailService.sendEmail(email, "Your OTP Code", "Your OTP is: " + otp + "\nValid for 10 minutes.");
    }

    // ✅ Verify OTP & update user + delete OTP
    public boolean verifyOtp(String email, String otp) {
        Optional<Otp> otpOpt = otpRepository.findByEmail(email);
        if (otpOpt.isPresent()) {
            Otp otpEntity = otpOpt.get();
            if (otpEntity.getOtpCode().equals(otp) && otpEntity.getExpiryTime().isAfter(LocalDateTime.now())) {

                // Update user verified = true
                userRepository.findByEmail(email).ifPresent(user -> {
                    user.setVerified(true);
                    userRepository.save(user);
                });

                // Delete OTP after successful verification
                otpRepository.delete(otpEntity);

                return true;
            }
        }
        return false;
    }
}

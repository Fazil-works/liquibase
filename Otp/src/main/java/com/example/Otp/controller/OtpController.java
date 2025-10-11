package com.example.Otp.controller;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.Otp.Dto.OtpVerifyDto;
import com.example.Otp.Dto.SignupDto;
import com.example.Otp.service.OtpService;

@RestController
@RequestMapping("/otp")
public class OtpController {

    @Autowired
    private OtpService otpService;

    // ✅ send OTP + create user
    @PostMapping("/send")
    public String sendOtp(@RequestBody SignupDto signupDto) throws MessagingException {
        otpService.createUserAndSendOtp(signupDto.getEmail(), signupDto.getPassword());
        return "OTP sent to " + signupDto.getEmail();
    }

    // ✅ verify OTP
    @PostMapping("/verify")
    public String verifyOtp(@RequestBody OtpVerifyDto otpDto) {
        boolean valid = otpService.verifyOtp(otpDto.getEmail(), otpDto.getOtp());
        if(valid) return "OTP verified successfully!";
        else return "Invalid or expired OTP!";
    }
    
}

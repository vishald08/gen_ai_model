package com.gam.serviceImpl;

import com.gam.entity.User;
import com.gam.repo.UserRepository;
import com.gam.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JavaMailSender mailSender;

    // Sender email property inject karo
    @Value("${spring.mail.username}")
    private String fromEmail;

    public UserServiceImpl(UserRepository userRepository, JavaMailSender mailSender) {
        this.userRepository = userRepository;
        this.mailSender = mailSender;
    }

    @Override
    public String generateAndSendOtp(String email) {
        try {
            if (email == null || email.trim().isEmpty()) {
                throw new IllegalArgumentException("Email address khali nahi ho sakta.");
            }

            if (userRepository.existsByEmail(email.trim().toLowerCase())) {
                throw new IllegalArgumentException("Ye Email pehle se registered hai. Kripya Sign In karein.");
            }

            // 4-digit OTP
            String otp = String.valueOf(1000 + new Random().nextInt(9000));

            // Setup Email Message with explicit FROM address
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail); // <--- YE LINE ERROR RESOLVE KAREGI
            message.setTo(email.trim());
            message.setSubject("Vishal World - Email Verification OTP");
            message.setText("Namaste,\n\nAapka verification OTP hai: " + otp + "\n\nYe code 5 minute ke liye valid hai.\n\nRegards,\nVishal World Team");

            mailSender.send(message);
            System.out.println(">>> Email OTP successfully sent to: " + email);

            return otp;

        } catch (IllegalArgumentException ex) {
            throw ex;
        } catch (Exception e) {
            throw new RuntimeException("Exception While sending Email (Check App Password/Network): " + e.getMessage());
        }
    }

    @Override
    public boolean verifyOtp(String enteredOtp, String actualOtp) {
        if (enteredOtp == null || actualOtp == null) return false;
        return actualOtp.trim().equals(enteredOtp.trim());
    }

    @Override
    public User registerUser(String name, String email, String mobileNumber, String password) {
        String cleanEmail = email.trim().toLowerCase();
        if (userRepository.existsByEmail(cleanEmail)) {
            throw new IllegalArgumentException("Ye email pehle se registered hai.");
        }

        User user = new User(
            name.trim(),
            cleanEmail,
            (mobileNumber != null && !mobileNumber.trim().isEmpty()) ? mobileNumber.trim() : null,
            password.trim()
        );

        return userRepository.save(user);
    }

    @Override
    public User authenticateUser(String email, String password) {
        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Email aur Password dono anivarya hain.");
        }

        Optional<User> userOptional = userRepository.findByEmail(email.trim().toLowerCase());

        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("Ye Email registered nahi hai. Pehle Sign Up karein.");
        }

        User user = userOptional.get();
        if (!user.getPassword().equals(password.trim())) {
            throw new IllegalArgumentException("Galat Password! Sahi password enter karein.");
        }

        return user;
    }
}
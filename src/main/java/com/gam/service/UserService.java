package com.gam.service;

import com.gam.entity.User;

public interface UserService {
    String generateAndSendOtp(String email);
    boolean verifyOtp(String enteredOtp, String actualOtp);
    User registerUser(String name, String email, String mobileNumber, String password);
    User authenticateUser(String email, String password);
}
package org.api.grocerystorebackend.service;

public interface IOtpService {
    void sendOtpEmail(String email);
    boolean verifyOtp(String inputOtp);
    void clearOtp(String otp);
}

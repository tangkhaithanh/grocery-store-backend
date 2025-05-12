package org.api.grocerystorebackend.service.impl;

import org.api.grocerystorebackend.model.OtpData;
import org.api.grocerystorebackend.service.IOtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpServiceImpl implements IOtpService {
    // Use hasmap to store OTP code
    private final Map<String, OtpData> otpStore = new ConcurrentHashMap<>();

    @Autowired
    private JavaMailSender mailSender;

    private static final long OTP_EXPIRATION_MINUTES = 1;
    @Override
    public void sendOtpEmail(String email) {
        String otp = String.valueOf(new Random().nextInt(900_000) + 100_000);
        LocalDateTime expireAt = LocalDateTime.now().plusMinutes(OTP_EXPIRATION_MINUTES);
        otpStore.put(otp, new OtpData(otp, expireAt));

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Mã OTP xác thực");
        message.setText("Mã OTP của bạn là: " + otp + "\nThời hạn: " + OTP_EXPIRATION_MINUTES + " phút.");
        mailSender.send(message);
    }

    @Override
    public boolean verifyOtp(String inputOtp) {
        OtpData data = otpStore.get(inputOtp);
        if (data == null) return false;

        boolean valid = LocalDateTime.now().isBefore(data.getExpireAt());
        if (valid) clearOtp(inputOtp);
        return valid;
    }

    @Override
    public void clearOtp(String otp) {
        otpStore.remove(otp);
    }
}

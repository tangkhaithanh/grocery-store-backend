package org.api.grocerystorebackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
public class OtpData {
    private final String otp;
    private final LocalDateTime expireAt;
}

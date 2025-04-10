package org.api.grocerystorebackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private String fullName;
    private String email;
    private String imageUrl;
}

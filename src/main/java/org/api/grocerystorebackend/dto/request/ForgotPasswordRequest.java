package org.api.grocerystorebackend.dto.request;

import lombok.Data;

@Data
public class ForgotPasswordRequest {
    private String email;
    private String newPassword;
}

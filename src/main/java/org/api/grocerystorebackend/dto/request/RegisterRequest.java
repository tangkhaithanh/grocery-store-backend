package org.api.grocerystorebackend.dto.request;

import lombok.Data;

@Data
public class RegisterRequest {
    private String fullName;
    private String phone;
    private String gender;
    private String email;
    private String password;
}

package org.api.grocerystorebackend.dto.request;

import lombok.Data;

@Data
public class OtpRequest {
    private String email;
    private boolean forRegistration;
}

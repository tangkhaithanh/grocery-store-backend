package org.api.grocerystorebackend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressRequest {
    private String city;
    private String district;
    private String streetAddress;
    private String userName;
    private String phoneNumber;
}

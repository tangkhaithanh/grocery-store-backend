package org.api.grocerystorebackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long    id;
    private String  fullName;
    private String  email;
    private String  phone;
    private String  gender;
    private String  imageUrl;
}

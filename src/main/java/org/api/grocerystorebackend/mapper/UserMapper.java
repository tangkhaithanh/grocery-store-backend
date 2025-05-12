package org.api.grocerystorebackend.mapper;

import org.api.grocerystorebackend.dto.response.UserDTO;
import org.api.grocerystorebackend.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }

        String email = (user.getAccount() != null) ? user.getAccount().getEmail() : "";

        return new UserDTO(
                user.getId(),
                user.getFullName(),
                email,
                user.getPhone(),
                user.getGender(),
                user.getImageUrl()
        );
    }
}

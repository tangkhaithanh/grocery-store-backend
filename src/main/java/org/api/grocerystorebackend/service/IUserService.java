package org.api.grocerystorebackend.service;

import org.api.grocerystorebackend.dto.response.UserDTO;
import org.api.grocerystorebackend.entity.User;

public interface IUserService {
    User updateUser(Long id, User updatedUser);
    void softDeleteUser(Long id);

    UserDTO getUserById(Long id);
}

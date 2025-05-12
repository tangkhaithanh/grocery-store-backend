package org.api.grocerystorebackend.service;

import org.api.grocerystorebackend.dto.request.UpdateUserRequest;
import org.api.grocerystorebackend.dto.response.UserDTO;
import org.api.grocerystorebackend.entity.User;

public interface IUserService {
    UserDTO updateUser(Long id, UpdateUserRequest request);
    void softDeleteUser(Long id);

    UserDTO getUserById(Long id);
}

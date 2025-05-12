package org.api.grocerystorebackend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.api.grocerystorebackend.dto.request.UpdateUserRequest;
import org.api.grocerystorebackend.dto.response.UserDTO;
import org.api.grocerystorebackend.entity.Account;
import org.api.grocerystorebackend.entity.User;
import org.api.grocerystorebackend.mapper.UserMapper;
import org.api.grocerystorebackend.repository.UserRepository;
import org.api.grocerystorebackend.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;


    @Override
    @Transactional
    public UserDTO updateUser(Long id, UpdateUserRequest request) {
        // Find user
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Không tìm thấy người dùng với ID = " + id));

        // Update fields
        user.setFullName(request.getFullName());
        user.setPhone(request.getPhone());
        user.setGender(request.getGender());
        user.setImageUrl(request.getImageUrl());
        user.setUpdatedAt(LocalDateTime.now());

        // Save changes to database
        User savedUser = userRepository.save(user);

        // Convert to DTO using mapper
        return userMapper.toDTO(savedUser);
    }

    @Transactional
    @Override
    public void softDeleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Không tìm thấy người dùng với ID = " + id));
        // Vô hiệu hóa tài khoảng của người dùng
        Account account = user.getAccount();
        if (account != null && Boolean.TRUE.equals(account.getIsActive())) {
            account.setIsActive(false);
            account.setUpdatedAt(LocalDateTime.now());
        }

        // Xóa cứng mọi quan hệ phụ thuộc trừ dữ kiện về đơn hàng:
        if (user.getAddresses()          != null) user.getAddresses().clear();
        if (user.getReviews()            != null) user.getReviews().clear();
        if (user.getFavouriteProducts()  != null) user.getFavouriteProducts().clear();
        if (user.getUserVouchers()       != null) user.getUserVouchers().clear();
        if (user.getUserScores()         != null) user.getUserScores().clear();
        user.setCart(null);
    }

    @Override
    public UserDTO getUserById(Long id) {
        User u =userRepository.findByIdAndAccount_IsActiveTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy user " + id));
        return new UserDTO(
                u.getId(),
                u.getFullName(),
                u.getAccount().getEmail(),
                u.getPhone(),
                u.getGender(),
                u.getImageUrl()
        );
    }
}

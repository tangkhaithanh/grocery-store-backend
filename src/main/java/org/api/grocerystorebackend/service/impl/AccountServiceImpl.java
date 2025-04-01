package org.api.grocerystorebackend.service.impl;

import org.api.grocerystorebackend.dto.request.ForgotPasswordRequest;
import org.api.grocerystorebackend.dto.request.RegisterRequest;
import org.api.grocerystorebackend.entity.Account;
import org.api.grocerystorebackend.entity.User;
import org.api.grocerystorebackend.repository.AccountRepository;
import org.api.grocerystorebackend.repository.UserRepository;
import org.api.grocerystorebackend.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AccountServiceImpl implements IAccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public boolean emailExists(String email) {
        return accountRepository.findByEmail(email).isPresent();
    }

    @Override
    public boolean registerUser(RegisterRequest request) {
        boolean checkEmail = emailExists(request.getEmail());
        if(checkEmail){
            return false;
        }
        // Tạo user
        User user = new User();
        user.setFullName(request.getFullName());
        user.setPhone(request.getPhone());
        user.setGender(request.getGender());
        user.setImageUrl("https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_960_720.png");
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        user = userRepository.save(user);
        // Tạo account
        Account account = new Account();
        account.setEmail(request.getEmail());
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        account.setIsActive(true);
        account.setUser(user);
        account.setCreatedAt(LocalDateTime.now());
        account.setUpdatedAt(LocalDateTime.now());

        accountRepository.save(account);

        return true;
    }

    @Override
    public boolean resetPassword(ForgotPasswordRequest request) {
        // Dùng lại hàm đã có để check email
        if (!emailExists(request.getEmail())) {
            return false;
        }

        // Lấy lại account để cập nhật (vì email tồn tại chắc chắn)
        Account account = accountRepository.findByEmail(request.getEmail()).get();

        account.setPassword(passwordEncoder.encode(request.getNewPassword()));
        account.setUpdatedAt(LocalDateTime.now());
        accountRepository.save(account);

        return true;
    }
}

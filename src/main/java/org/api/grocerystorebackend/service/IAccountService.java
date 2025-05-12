package org.api.grocerystorebackend.service;

import org.api.grocerystorebackend.dto.request.ForgotPasswordRequest;
import org.api.grocerystorebackend.dto.request.RegisterRequest;
import org.api.grocerystorebackend.entity.Account;

public interface IAccountService {
    boolean emailExists(String email);
    boolean registerUser(RegisterRequest request);
    boolean resetPassword(ForgotPasswordRequest request);
    Account getByEmail(String email);
}

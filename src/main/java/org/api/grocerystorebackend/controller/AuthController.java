package org.api.grocerystorebackend.controller;

import org.api.grocerystorebackend.dto.request.AuthRequest;
import org.api.grocerystorebackend.dto.request.ForgotPasswordRequest;
import org.api.grocerystorebackend.dto.request.RegisterRequest;
import org.api.grocerystorebackend.dto.response.ApiResponse;
import org.api.grocerystorebackend.dto.response.AuthResponse;
import org.api.grocerystorebackend.entity.Account;
import org.api.grocerystorebackend.entity.User;
import org.api.grocerystorebackend.repository.AccountRepository;
import org.api.grocerystorebackend.repository.UserRepository;
import org.api.grocerystorebackend.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    // API Login
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody AuthRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            // Generate new token for this user:
            String token = jwtUtil.generateToken(request.getEmail());
            return ResponseEntity.ok(ApiResponse.ok("Đăng nhập thành công", new AuthResponse(token)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.fail("Tài khoản hoặc mật khẩu không đúng"));
        }
    }

    // API Register
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> register(@RequestBody RegisterRequest request) {
        try {
            // Checked If email Exist
            if (accountRepository.findByEmail(request.getEmail()).isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.fail("Email đã được sử dụng"));
            }
            // Create User:
            User user = new User();
            user.setFullName(request.getFullName());
            user.setPhone(request.getPhone());
            user.setGender(request.getGender());
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());
            user.setImageUrl("https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_960_720.png");
            // Save user before to have an user_id
            user = userRepository.save(user);

            // Create account and attach user:
            Account account = new Account();
            account.setEmail(request.getEmail());
            account.setPassword(passwordEncoder.encode(request.getPassword()));
            account.setIsActive(true);
            account.setUser(user);
            account.setCreatedAt(LocalDateTime.now());
            account.setUpdatedAt(LocalDateTime.now());


            user.setAccount(account);

            // Lưu account
            accountRepository.save(account);

            return ResponseEntity.ok(ApiResponse.ok("Đăng ký thành công", null));
        } catch (Exception e) {
            e.printStackTrace(); // 👈 debug log nếu cần
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.fail("Đã xảy ra lỗi khi đăng ký"));
        }
    }

    // API Forgot Password:
    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<?>> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        String email = request.getEmail();
        String newPassword = request.getNewPassword();
        Account account = accountRepository.findByEmail(email).orElse(null);
        if (account == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.fail("Không tìm thấy tài khoản"));
        }

        account.setPassword(passwordEncoder.encode(newPassword));
        account.setUpdatedAt(LocalDateTime.now());
        accountRepository.save(account);

        return ResponseEntity.ok(ApiResponse.ok("Đặt lại mật khẩu thành công", null));
    }
}

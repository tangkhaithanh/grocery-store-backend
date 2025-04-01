package org.api.grocerystorebackend.controller;

import org.api.grocerystorebackend.dto.request.AuthRequest;
import org.api.grocerystorebackend.dto.request.ForgotPasswordRequest;
import org.api.grocerystorebackend.dto.request.RegisterRequest;
import org.api.grocerystorebackend.dto.response.ApiResponse;
import org.api.grocerystorebackend.dto.response.AuthResponse;
import org.api.grocerystorebackend.service.IAccountService;
import org.api.grocerystorebackend.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
    private IAccountService accountService;

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
            boolean success = accountService.registerUser(request);

            if (!success)
            {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.fail("Email đã được sử dụng"));
            }
            return ResponseEntity.ok(ApiResponse.ok("Đăng ký thành công", null));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.fail("Đã xảy ra lỗi khi đăng ký"));
        }
    }

    // API Forgot Password:
    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<?>> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        try {
            boolean result = accountService.resetPassword(request);
            if (!result)
            {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.fail("Không tìm thấy tài khoản"));
            }
            return ResponseEntity.ok(ApiResponse.ok("Đặt lại mật khẩu thành công", null));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.fail("Đã xảy ra lỗi khi đặt lại mật khẩu"));
        }
    }
}

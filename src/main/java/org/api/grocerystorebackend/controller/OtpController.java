package org.api.grocerystorebackend.controller;

import org.api.grocerystorebackend.dto.request.OtpRequest;
import org.api.grocerystorebackend.dto.request.OtpVerifyRequest;
import org.api.grocerystorebackend.dto.response.ApiResponse;
import org.api.grocerystorebackend.service.IAccountService;
import org.api.grocerystorebackend.service.IOtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/otp")
public class OtpController {
    @Autowired
    private IOtpService otpService;

    @Autowired
    private IAccountService accountService;

    // Send Otp
    @PostMapping("/send")
    public ResponseEntity<ApiResponse<?>> sendOtp(@RequestBody OtpRequest request)
    {
        try{
            String email= request.getEmail();
            boolean forRegistration = request.isForRegistration();
            // Check Email Exist
            boolean emailExists= accountService.emailExists(email);
            // Exception for Case Send otp to register
            if (forRegistration && emailExists) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.fail("Email đã tồn tại trong hệ thống"));
            }
            //Exception for Case send otp to Forgot Password
            if (!forRegistration && !emailExists) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.fail("Không tìm thấy tài khoản với email này"));
            }
            otpService.sendOtpEmail(email);
            return ResponseEntity.ok(ApiResponse.ok("OTP đã được gửi tới email", null));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.fail("Gửi OTP thất bại"));
        }
    }

    // Verify The OTP CODE:
    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<?>> verifyOtp(@RequestBody OtpVerifyRequest request)
    {
        try{
            String otp=request.getOtp();
            boolean isvalid= otpService.verifyOtp(otp);
            if (!isvalid) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.fail("OTP không đúng hoặc đã hết hạn"));
            }
            return ResponseEntity.ok(ApiResponse.ok("Xác thực OTP thành công",null));
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.fail("Lỗi hệ thống khi xác thực OTP"));
        }
    }
}

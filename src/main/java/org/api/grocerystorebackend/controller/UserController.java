package org.api.grocerystorebackend.controller;

import jakarta.persistence.EntityNotFoundException;
import org.api.grocerystorebackend.dto.request.UpdateUserRequest;
import org.api.grocerystorebackend.dto.response.ApiResponse;
import org.api.grocerystorebackend.dto.response.UserDTO;
import org.api.grocerystorebackend.entity.User;
import org.api.grocerystorebackend.repository.UserRepository;
import org.api.grocerystorebackend.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private IUserService userService;

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<?>> updateUser(
            @PathVariable Long id,
            @RequestBody User updatedUser) {
            @RequestBody UpdateUserRequest updatedUser) {

        try {
            User user = userService.updateUser(id, updatedUser);
            return ResponseEntity.ok(ApiResponse.ok("Cập nhật người dùng thành công", user));
            // Fix: Return DTO instead of Entity to avoid circular reference
            UserDTO userDTO = userService.updateUser(id, updatedUser);
            return ResponseEntity.ok(ApiResponse.ok("Cập nhật người dùng thành công", userDTO));

        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(ApiResponse.fail(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.fail("Lỗi cập nhật người dùng"));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<?>> deleteUser(@PathVariable Long id) {

        try {
            userService.softDeleteUser(id);
            return ResponseEntity.ok(ApiResponse.ok("Đã xoá (mềm) người dùng thành công",null));

        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(ApiResponse.fail(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.fail("Lỗi xoá người dùng"));
        }
    }

    // Lấy thông tin của user:
    @GetMapping("/{id}")                                   // GET /api/user/{id}
    public ResponseEntity<ApiResponse<?>> getUser(@PathVariable Long id) {
        try {
            UserDTO dto = userService.getUserById(id);   // gọi service
            return ResponseEntity.ok(
                    ApiResponse.ok("Lấy thông tin người dùng thành công", dto));

        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404)
                    .body(ApiResponse.fail(e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.fail("Lỗi khi lấy thông tin người dùng"));
        }
    }
}
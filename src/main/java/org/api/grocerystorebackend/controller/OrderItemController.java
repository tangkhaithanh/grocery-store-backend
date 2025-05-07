package org.api.grocerystorebackend.controller;

import jakarta.persistence.EntityNotFoundException;
import org.api.grocerystorebackend.dto.response.ApiResponse;
import org.api.grocerystorebackend.dto.response.OrderItemDTO;
import org.api.grocerystorebackend.service.IOrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orderItems")
public class OrderItemController {

    @Autowired
    IOrderItemService orderItemService;

    @GetMapping()
    public ResponseEntity<ApiResponse<?>> getOrderItem(@RequestParam("orderItemId") Long orderItemId) {
        try {
            OrderItemDTO orderItemDTO = orderItemService.getOrderItemById(orderItemId);
            return ResponseEntity.ok(ApiResponse.ok("Lấy đơn hàng sản phẩm của người dùng thành công", orderItemDTO));
        }
        catch (EntityNotFoundException entityNotFoundException) {
            return ResponseEntity.status(404).body(ApiResponse.fail("Không tìm thấy đơn hàng sản phẩm của người dùng"));
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.fail("Lỗi hệ thống khi thực hiện chức năng lấy đơn hàng sản phẩm của người dùng!!!!" + e.getMessage()));
        }

    }
}

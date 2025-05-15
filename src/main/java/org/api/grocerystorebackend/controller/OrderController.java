package org.api.grocerystorebackend.controller;

import jakarta.persistence.EntityNotFoundException;
import org.api.grocerystorebackend.dto.request.CancelOrderRequest;
import org.api.grocerystorebackend.dto.request.CreateOrderRequest;
import org.api.grocerystorebackend.dto.response.ApiResponse;
import org.api.grocerystorebackend.dto.response.CreateOrderResponse;
import org.api.grocerystorebackend.dto.response.DeliveredOrderDTO;
import org.api.grocerystorebackend.dto.response.OrderDTO;
import org.api.grocerystorebackend.entity.Order;
import org.api.grocerystorebackend.entity.User;
import org.api.grocerystorebackend.enums.StatusOrderType;
import org.api.grocerystorebackend.security.AccountDetails;
import org.api.grocerystorebackend.service.IOrderService;
import org.api.grocerystorebackend.service.IReviewService;
import org.api.grocerystorebackend.service.impl.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private IOrderService orderService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<?>> getOrders(@AuthenticationPrincipal AccountDetails accountDetails,
                                                    @RequestParam(name="typeStatusOrder",defaultValue = "ALL", required = false) StatusOrderType typeStatusOrder,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "20") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
            Long userID = accountDetails.getAccount().getId();
            Page<OrderDTO> orders = orderService.getAllOrdersByStatusAndIDUser(pageable, typeStatusOrder, userID);
            return ResponseEntity.ok(ApiResponse.ok("Lấy tất cả đơn hàng của người dùng thành công", orders));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.fail("Dữ liệu trạng thái không hợp lệ"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.fail("Lỗi hệ thống khi thực hiện chức năng lấy tất cả đơn hàng của người dùng!!!!"));
        }
    }

    @PostMapping("/cancel")
    public ResponseEntity<ApiResponse<?>> cancelOrder(@AuthenticationPrincipal AccountDetails accountDetails, @RequestBody Long orderId) {
        try {
            Long userID = accountDetails.getAccount().getId();
            Boolean result = orderService.cancelOrder(userID, orderId);
            if (result) {
                return ResponseEntity.ok(ApiResponse.ok("Hủy đơn hàng thành công", null));
            }
            return ResponseEntity.status(404).body(ApiResponse.fail("Hủy đơn hàng thất bại!!!!"));
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.fail("Lỗi hệ thống khi thực hiện chức năng hủy đơn hàng!!!"));
        }
    }
    @GetMapping("/order")
    public ResponseEntity<ApiResponse<?>> getOrderById(@RequestParam(name="orderId", required = true) Long orderId) {
        try {
            OrderDTO order = orderService.findById(orderId);
            return ResponseEntity.ok(ApiResponse.ok("Lấy đơn hàng của người dùng thành công", order));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.fail("Dữ liệu trạng thái không hợp lệ"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.fail("Lỗi hệ thống khi thực hiện chức năng lấy đơn hàng của người dùng!!!!"));
        }
    }

    // Thêm đơn hàng:
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<?>> createOrder(
            @AuthenticationPrincipal AccountDetails accountDetails,
            @RequestBody CreateOrderRequest request) {
        try {
            Long userId = accountDetails.getAccount().getUser().getId();
            CreateOrderResponse response = orderService.createOrder(userId, request);
            return ResponseEntity.ok(ApiResponse.ok("Đặt hàng thành công", response));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(ApiResponse.fail(e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.fail(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.fail("Lỗi hệ thống khi tạo đơn hàng"));
        }
    }
}

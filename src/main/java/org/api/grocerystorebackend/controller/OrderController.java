package org.api.grocerystorebackend.controller;

import org.api.grocerystorebackend.dto.request.CancelOrderRequest;
import org.api.grocerystorebackend.dto.response.ApiResponse;
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

    @Autowired
    private IReviewService reviewService;
    @GetMapping("")
    public ResponseEntity<ApiResponse<?>> getOrders(@RequestParam(name = "id") Long id,
                                                    @RequestParam(name="typeStatusOrder",defaultValue = "ALL", required = false) StatusOrderType typeStatusOrder,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "20") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
            Page<OrderDTO> orders = orderService.getAllOrdersByStatusAndIDUser(pageable, typeStatusOrder, id);
            return ResponseEntity.ok(ApiResponse.ok("Lấy tất cả đơn hàng của người dùng thành công", orders));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.fail("Dữ liệu trạng thái không hợp lệ"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.fail("Lỗi hệ thống khi thực hiện chức năng lấy tất cả đơn hàng của người dùng!!!!"));
        }
    }

    @PostMapping("/cancel")
    public ResponseEntity<ApiResponse<?>> cancelOrder(@RequestBody CancelOrderRequest request) {
        try {
            Boolean result = orderService.cancelOrder(request.getUserID(), request.getOrderID());
            if (result) {
                return ResponseEntity.ok(ApiResponse.ok("Hủy đơn hàng thành công", null));
            }
            return ResponseEntity.status(404).body(ApiResponse.fail("Hủy đơn hàng thất bại!!!!"));
        }
        catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.fail("Lỗi hệ thống khi thực hiện chức năng hủy đơn hàng!!!"));
        }
    }

    @GetMapping("/delivered-with-review-status")
    public ResponseEntity<ApiResponse<List<DeliveredOrderDTO>>> getDeliveredOrders(
            @AuthenticationPrincipal AccountDetails accountDetails) {
        try {
            User user = accountDetails.getAccount().getUser();
            List<DeliveredOrderDTO> result = reviewService.getDeliveredOrdersWithReviewStatus(user);
            return ResponseEntity.ok(ApiResponse.ok("Thành công", result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.fail("Lỗi khi lấy dữ liệu"));
        }
    }
}

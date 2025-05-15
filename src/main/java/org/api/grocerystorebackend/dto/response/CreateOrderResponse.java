package org.api.grocerystorebackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.api.grocerystorebackend.enums.StatusOrderType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderResponse {
    private Long orderId;
    private BigDecimal totalAmount;
    private StatusOrderType status;
    private String paymentMethod;
    private LocalDateTime createdAt;
    private String message;

    // Thêm thông tin địa chỉ giao hàng (snapshot)
    private String shippingUserName;
    private String shippingPhoneNumber;
    private String shippingCity;
    private String shippingDistrict;
    private String shippingStreetAddress;

    // Constructor không có địa chỉ (backward compatibility)
    public CreateOrderResponse(Long orderId, BigDecimal totalAmount, StatusOrderType status,
                               String paymentMethod, LocalDateTime createdAt, String message) {
        this.orderId = orderId;
        this.totalAmount = totalAmount;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.createdAt = createdAt;
        this.message = message;
    }
}

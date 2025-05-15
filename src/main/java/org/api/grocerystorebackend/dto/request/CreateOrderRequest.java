package org.api.grocerystorebackend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderRequest {
    private Long addressId;
    private Long voucherId;
    private String paymentMethod;
    private List<OrderItemRequest> orderItems;
}

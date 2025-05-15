package org.api.grocerystorebackend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.api.grocerystorebackend.dto.request.CreateOrderRequest;
import org.api.grocerystorebackend.dto.request.OrderItemRequest;
import org.api.grocerystorebackend.dto.response.*;
import org.api.grocerystorebackend.entity.*;
import org.api.grocerystorebackend.enums.StatusOrderType;
import org.api.grocerystorebackend.enums.VoucherType;
import org.api.grocerystorebackend.mapper.AddressMapper;
import org.api.grocerystorebackend.mapper.OrderMapper;
import org.api.grocerystorebackend.repository.*;
import org.api.grocerystorebackend.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements IOrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VoucherRepository voucherRepository;

    @Autowired
    private FlashSaleItemRepository flashSaleItemRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private AddressMapper addressMapper;

    @Override
    public Page<OrderDTO> getAllOrdersByStatusAndIDUser(Pageable pageable, StatusOrderType status, Long id) {
        Page<Order> listOrders;
        if (status == StatusOrderType.ALL) {
            listOrders = orderRepository.findAllByUserId(id, pageable);
            return listOrders.map(orderMapper::toDTO);
        }

        listOrders = orderRepository.findAllByStatusAndId(status, id, pageable);
        return listOrders.map(orderMapper::toDTO);
    }

    @Transactional
    @Override
    public Boolean cancelOrder(Long userID, long orderID) {
        //Lấy order theo mã Order và mã User
        Order order = orderRepository.findByUserIdAndId(userID, orderID);
        if (order == null || order.getOrderItems() == null) {
            return false;
        }

        // Lặp qua từng OrderItem để hoàn trả số lượng vào kho
        for (OrderItem item : order.getOrderItems()) {
            Product product = item.getProduct();
            int quantity = item.getQuantity();

            // Cập nhật lại số lượng trong kho
            product.setQuantity(product.getQuantity() + quantity);

            // Lưu thay đổi sản phẩm
            productRepository.save(product);
        }

        // Đặt trạng thái đơn hàng là "cancelled" hoặc tương đương
        order.setStatus(StatusOrderType.CANCELED);
        orderRepository.save(order);
        return true;
    }

    @Override
    public OrderDTO findById(Long id) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order != null) {
            return orderMapper.toDTO(order);
        }
        return null;
    }


    // Code tạo đơn hàng nằm đây:

    @Transactional
    @Override
    public CreateOrderResponse createOrder(Long userId, CreateOrderRequest request) {
        try {
            // 1. Validate User
            User user = validateUser(userId);

            // 2. Validate và lấy địa chỉ giao hàng
            AddressDTO address = validateAndGetAddress(request.getAddressId());

            // 3. Validate Voucher
            Voucher voucher = validateVoucher(request.getVoucherId());

            // 4. Tạo order entity
            Order order = createOrderEntity(user, voucher, address, request.getPaymentMethod());

            // 5. Lưu Order trước để lấy ID
            order = orderRepository.save(order);

            // 6. Tạo và validate order items
            OrderItemsResult orderItemsResult = validateAndCreateOrderItems(order, request.getOrderItems());
            order.setOrderItems(orderItemsResult.getOrderItems());

            // 7. Tính toán final total
            BigDecimal finalTotal = calculateFinalTotal(orderItemsResult.getCalculatedTotal(), voucher);

            // 8. Validate và set total amount (nếu cần thiết - có thể bỏ)
            //validateTotalAmount(finalTotal, request.getTotalAmount());
            order.setTotalAmount(finalTotal);
            orderRepository.save(order);

            // 9. Update stock và voucher quantity
            updateStockAndVoucher(request.getOrderItems(), voucher);

            // 10. Clear cart items
            clearCartAfterOrder(userId, request.getOrderItems());

            // 11. Build và return response
            return buildOrderResponse(order);

        } catch (EntityNotFoundException e) {
            throw e;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tạo đơn hàng: " + e.getMessage());
        }
    }

    // Method 1: Validate User
    private User validateUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    // Method 2: Validate và lấy địa chỉ
    private AddressDTO validateAndGetAddress(Long addressId) {
        if (addressId == null) {
            throw new RuntimeException("Address is required");
        }

        return addressRepository.findById(addressId)
                .map(addressMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Address not found"));
    }

    // Method 3: Validate voucher
    private Voucher validateVoucher(Long voucherId) {
        if (voucherId == null) {
            return null;
        }

        Voucher voucher = voucherRepository.findById(voucherId)
                .orElseThrow(() -> new EntityNotFoundException("Voucher not found"));

        // Validate voucher expiry
        if (voucher.getExpiryDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("Voucher đã hết hạn");
        }

        // Validate voucher quantity
        if (voucher.getQuantity() <= 0) {
            throw new RuntimeException("Voucher đã hết");
        }

        return voucher;
    }

    // Method 4: Tạo order entity
    private Order createOrderEntity(User user, Voucher voucher, AddressDTO address, String paymentMethod) {
        Order order = new Order();
        order.setUser(user);
        order.setVoucher(voucher);
        order.setStatus(StatusOrderType.PENDING);
        order.setPaymentMethod(paymentMethod);
        order.setCreatedAt(LocalDateTime.now());

        // Set shipping address snapshot
        setShippingAddress(order, address);

        return order;
    }

    // Method 5: Set shipping address
    private void setShippingAddress(Order order, AddressDTO address) {
        order.setShippingUserName(address.getUserName());
        order.setShippingPhoneNumber(address.getPhoneNumber());
        order.setShippingCity(address.getCity());
        order.setShippingDistrict(address.getDistrict());
        order.setShippingStreetAddress(address.getStreetAddress());
    }

    // Method 6: Validate và tạo order items
    private OrderItemsResult validateAndCreateOrderItems(Order order, List<OrderItemRequest> itemRequests) {
        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal calculatedTotal = BigDecimal.ZERO;

        for (OrderItemRequest itemRequest : itemRequests) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found"));

            OrderItem orderItem = createOrderItem(order, product, itemRequest);
            orderItems.add(orderItem);

            // Calculate total từ order items
            calculatedTotal = calculatedTotal.add(
                    itemRequest.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity()))
            );
        }

        return new OrderItemsResult(orderItems, calculatedTotal);
    }

    // Method 7: Tạo order item
    private OrderItem createOrderItem(Order order, Product product, OrderItemRequest itemRequest) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setQuantity(itemRequest.getQuantity());
        orderItem.setPrice(itemRequest.getPrice());

        // Xử lý Flash Sale hoặc Regular Product
        if (itemRequest.getFlashSaleItemId() != null) {
            validateAndSetFlashSaleItem(orderItem, itemRequest, product);
        } else {
            validateRegularProduct(product, itemRequest);
        }

        return orderItem;
    }

    // Method 8: Validate và set flash sale item
    private void validateAndSetFlashSaleItem(OrderItem orderItem, OrderItemRequest itemRequest, Product product) {
        FlashSaleItem flashSaleItem = flashSaleItemRepository
                .findById(itemRequest.getFlashSaleItemId())
                .orElseThrow(() -> new EntityNotFoundException("Flash sale item not found"));

        // Validate flash sale stock
        int remainingFlashSaleStock = flashSaleItem.getStockQuantity() - flashSaleItem.getSoldQuantity();
        if (remainingFlashSaleStock < itemRequest.getQuantity()) {
            throw new RuntimeException("Không đủ số lượng flash sale cho sản phẩm: " + product.getName());
        }

        // Validate max per customer
        if (flashSaleItem.getMaxPerCustomer() != null &&
                itemRequest.getQuantity() > flashSaleItem.getMaxPerCustomer()) {
            throw new RuntimeException("Vượt quá số lượng tối đa cho phép mua của sản phẩm flash sale: " + product.getName());
        }

        // Validate flash sale price
        if (itemRequest.getPrice().compareTo(flashSaleItem.getFlashSalePrice()) != 0) {
            throw new RuntimeException("Giá flash sale không khớp cho sản phẩm: " + product.getName());
        }

        orderItem.setFlashSaleItem(flashSaleItem);
    }

    // Method 9: Validate regular product
    private void validateRegularProduct(Product product, OrderItemRequest itemRequest) {
        // Validate regular product stock
        if (product.getQuantity() < itemRequest.getQuantity()) {
            throw new RuntimeException("Không đủ hàng trong kho cho sản phẩm: " + product.getName());
        }

        // Validate product price
        if (itemRequest.getPrice().compareTo(product.getPrice()) != 0) {
            throw new RuntimeException("Giá sản phẩm không khớp cho: " + product.getName());
        }
    }

    // Method 10: Tính toán final total
    private BigDecimal calculateFinalTotal(BigDecimal calculatedTotal, Voucher voucher) {
        BigDecimal finalTotal = calculatedTotal;
        BigDecimal shippingFee = BigDecimal.valueOf(30000); // 30,000 VND phí ship

        if (voucher != null) {
            VoucherType type = voucher.getType();

            // Áp dụng phí ship nếu không có FREESHIP
            if (type != VoucherType.FREESHIP && type != VoucherType.ALL) {
                finalTotal = finalTotal.add(shippingFee);
            }

            // Áp dụng giảm giá nếu có DISCOUNT
            if (type == VoucherType.DISCOUNT || type == VoucherType.ALL) {
                BigDecimal discount = voucher.getDiscount() != null ? voucher.getDiscount() : BigDecimal.ZERO;
                finalTotal = finalTotal.subtract(discount);
            }
        } else {
            // Không có voucher: tính phí ship bình thường
            finalTotal = finalTotal.add(shippingFee);
        }

        // Ensure total is not negative
        if (finalTotal.compareTo(BigDecimal.ZERO) < 0) {
            finalTotal = BigDecimal.ZERO;
        }

        return finalTotal;
    }

    // Method 11: Validate total amount (có thể bỏ nếu không cần)
    private void validateTotalAmount(BigDecimal finalTotal, BigDecimal requestTotal) {
        if (requestTotal != null && finalTotal.compareTo(requestTotal) != 0) {
            throw new RuntimeException("Tổng tiền không khớp. Tính toán: " + finalTotal + ", Gửi lên: " + requestTotal);
        }
    }

    // Method 12: Update stock và voucher
    private void updateStockAndVoucher(List<OrderItemRequest> orderItems, Voucher voucher) {
        // Update stock
        updateStock(orderItems);

        // Update voucher quantity
        if (voucher != null) {
            voucher.setQuantity(voucher.getQuantity() - 1);
            voucherRepository.save(voucher);
        }
    }

    // Method 13: Update stock
    private void updateStock(List<OrderItemRequest> orderItems) {
        for (OrderItemRequest itemRequest : orderItems) {
            Product product = productRepository.findById(itemRequest.getProductId()).get();

            if (itemRequest.getFlashSaleItemId() != null) {
                // Update flash sale item sold quantity
                FlashSaleItem flashSaleItem = flashSaleItemRepository
                        .findById(itemRequest.getFlashSaleItemId()).get();
                flashSaleItem.setSoldQuantity(flashSaleItem.getSoldQuantity() + itemRequest.getQuantity());
                flashSaleItemRepository.save(flashSaleItem);
            } else {
                // Update regular product quantity
                product.setQuantity(product.getQuantity() - itemRequest.getQuantity());
                productRepository.save(product);
            }
        }
    }

    // Method 14: Build order response
    private CreateOrderResponse buildOrderResponse(Order order) {
        CreateOrderResponse response = new CreateOrderResponse(
                order.getId(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getPaymentMethod(),
                order.getCreatedAt(),
                "Đặt hàng thành công"
        );

        // Thêm thông tin địa chỉ giao hàng vào response
        response.setShippingUserName(order.getShippingUserName());
        response.setShippingPhoneNumber(order.getShippingPhoneNumber());
        response.setShippingCity(order.getShippingCity());
        response.setShippingDistrict(order.getShippingDistrict());
        response.setShippingStreetAddress(order.getShippingStreetAddress());

        return response;
    }

    // Helper class để return multiple values
    private static class OrderItemsResult {
        private final List<OrderItem> orderItems;
        private final BigDecimal calculatedTotal;

        public OrderItemsResult(List<OrderItem> orderItems, BigDecimal calculatedTotal) {
            this.orderItems = orderItems;
            this.calculatedTotal = calculatedTotal;
        }

        public List<OrderItem> getOrderItems() {
            return orderItems;
        }

        public BigDecimal getCalculatedTotal() {
            return calculatedTotal;
        }
    }

    // Hàm để xóa các sản phẩm trong cart sau khi đặt hàng:
    private void clearCartAfterOrder(Long userId, List<OrderItemRequest> orderItems) {
        // Clear specific items that were ordered
        for (OrderItemRequest item : orderItems) {
            cartRepository.deleteByUserIdAndProductIdAndFlashSaleItemId(
                    userId,
                    item.getProductId(),
                    item.getFlashSaleItemId()
            );
        }
    }
}

package org.api.grocerystorebackend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.api.grocerystorebackend.enums.StatusOrderType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    @JsonBackReference
    private User user;

    @ManyToOne
    @JoinColumn(name="voucher_id", nullable = true)
    @JsonBackReference
    private Voucher voucher;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<OrderItem> orderItems;

    

    @Enumerated(EnumType.STRING) // Lưu enum dưới dạng String trong DB
    @Column(name = "status", columnDefinition = "varchar(50)")
    private StatusOrderType status;

    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "payment_method", columnDefinition = "varchar(50)")
    private String paymentMethod;
    // Thông tin địa chỉ giao hàng (snapshot)
    @Column(name = "shipping_user_name", columnDefinition = "varchar(255)")
    private String shippingUserName;

    @Column(name = "shipping_phone_number", columnDefinition = "varchar(20)")
    private String shippingPhoneNumber;

    @Column(name = "shipping_city", columnDefinition = "varchar(200)")
    private String shippingCity;

    @Column(name = "shipping_district", columnDefinition = "varchar(200)")
    private String shippingDistrict;

    @Column(name = "shipping_street_address", columnDefinition = "varchar(255)")
    private String shippingStreetAddress;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name="delivery_at")
    private LocalDateTime deliveryAt;
}

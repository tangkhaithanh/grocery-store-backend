package org.api.grocerystorebackend.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FlashSaleItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "flashSaleItem", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<OrderItem> orderItems;

    @ManyToOne
    @JoinColumn(name = "flash_sale_id", nullable = false)
    private FlashSale flashSale;

    @ManyToOne
    @JoinColumn(name="product_id", nullable = false)
    private Product product;

    @Column(name="flash_sale_price", nullable = false)
    private BigDecimal flashSalePrice;

    @Column(name="stock_quantity", nullable = false)
    private Integer stockQuantity;

    @Column(name="sold_quantity", nullable = false)
    private Integer soldQuantity=0;

    @Column(name = "max_per_customer")
    private Integer maxPerCustomer;
}

package org.api.grocerystorebackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.api.grocerystorebackend.enums.VoucherType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "code", nullable = false, columnDefinition = "varchar(50)")
    private String code;

    @Column(name = "discount", precision = 10, scale = 2)
    private BigDecimal discount;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Enumerated(EnumType.STRING) // Lưu enum dưới dạng String trong DB
    @Column(name = "type", columnDefinition = "varchar(50)")
    private VoucherType type;

    @OneToMany(mappedBy = "voucher", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<UserVoucher> userVouchers;

    @OneToMany(mappedBy = "voucher", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Order> orders;
}

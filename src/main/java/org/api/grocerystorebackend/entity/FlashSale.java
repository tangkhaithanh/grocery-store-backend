package org.api.grocerystorebackend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.api.grocerystorebackend.enums.FlashSaleStatus;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FlashSale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name="description")
    private String description;

    @Column(name="start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name="end_time", nullable = false)
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING) // Lưu enum dưới dạng String trong DB
    @Column(name = "status", columnDefinition = "varchar(50)")
    private FlashSaleStatus status;

    @OneToMany(mappedBy = "flashSale", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<FlashSaleItem> flashSaleItems;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

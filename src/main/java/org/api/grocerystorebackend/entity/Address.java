package org.api.grocerystorebackend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    @JsonBackReference
    private User user;

    @Column(name = "is_default")
    private boolean isDefault;

    @Column(name="city", columnDefinition ="varchar(200)")
    private String city;

    @Column(name="district", columnDefinition ="varchar(200)")
    private String district;

    @Column(name = "street_address", columnDefinition = "varchar(255)")
    private String streetAddress;

    @Column(name = "user_name", columnDefinition = "varchar(255)")
    private String userName;

    @Column(name = "phone_number", columnDefinition = "varchar(20)")
    private String phoneNumber;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

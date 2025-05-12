package org.api.grocerystorebackend.repository;

import org.api.grocerystorebackend.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}

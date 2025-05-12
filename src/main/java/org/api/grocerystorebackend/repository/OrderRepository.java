package org.api.grocerystorebackend.repository;

import org.api.grocerystorebackend.dto.response.OrderDTO;
import org.api.grocerystorebackend.entity.Order;
import org.api.grocerystorebackend.enums.StatusOrderType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findAllByUserId(Long id, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.status = :status AND o.user.id = :userID")
    Page<Order> findAllByStatusAndId(@Param("status") StatusOrderType status, @Param("userID") Long id, Pageable pageable);

    Order findByUserIdAndId(Long userID, Long orderID);

}

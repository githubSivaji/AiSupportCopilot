package com.sivaji.aisupportcopilot.repository;

import com.sivaji.aisupportcopilot.entity.Order;
import com.sivaji.aisupportcopilot.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findByUserId(UUID userId);

    List<Order> findByStatus(OrderStatus status);
}

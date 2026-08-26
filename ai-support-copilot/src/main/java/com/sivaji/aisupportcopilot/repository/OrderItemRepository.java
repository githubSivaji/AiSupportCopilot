package com.sivaji.aisupportcopilot.repository;

import com.sivaji.aisupportcopilot.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {
}

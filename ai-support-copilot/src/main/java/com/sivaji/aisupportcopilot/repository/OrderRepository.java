package com.sivaji.aisupportcopilot.repository;

import com.sivaji.aisupportcopilot.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


interface OrderRepository extends JpaRepository<Order, UUID> {
}

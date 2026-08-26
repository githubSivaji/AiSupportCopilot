package com.sivaji.aisupportcopilot.repository;

import com.sivaji.aisupportcopilot.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
}

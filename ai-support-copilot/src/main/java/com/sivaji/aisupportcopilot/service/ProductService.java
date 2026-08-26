package com.sivaji.aisupportcopilot.service;

import com.sivaji.aisupportcopilot.dto.ProductRequest;
import com.sivaji.aisupportcopilot.dto.ProductResponse;
import com.sivaji.aisupportcopilot.entity.Product;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    ProductResponse createProduct(ProductRequest request);

    ProductResponse getProduct(UUID id);

    List<ProductResponse> getAllProducts();

    ProductResponse updateProduct(
            UUID id,
            ProductRequest request
    );

    void deactivateProduct(UUID id);
}
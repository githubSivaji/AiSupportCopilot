package com.sivaji.aisupportcopilot.service;

import com.sivaji.aisupportcopilot.dto.ProductRequest;
import com.sivaji.aisupportcopilot.dto.ProductResponse;
import com.sivaji.aisupportcopilot.entity.Inventory;
import com.sivaji.aisupportcopilot.entity.Product;

import com.sivaji.aisupportcopilot.repository.InventoryRepository;
import com.sivaji.aisupportcopilot.repository.ProductRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {


    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    @Override
    public ProductResponse createProduct(ProductRequest request) {

        Product product = Product.builder()
                .name(request.name())
                .description(request.description())
                .price(request.price())
                .active(true)
                .build();

        Product savedProduct = productRepository.save(product);
        Inventory inventory = Inventory.builder()
                .productId(savedProduct.getId())
                .availableQuantity(0)
                .build();

        inventoryRepository.save(inventory);


        return ProductResponse.from(savedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProduct(UUID id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Product not found: " + id
                        )
                );

        return ProductResponse.from(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {

        return productRepository.findAll()
                .stream()
                .map(ProductResponse::from)
                .toList();
    }

    @Override
    public ProductResponse updateProduct(
            UUID id,
            ProductRequest request) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Product not found: " + id
                        )
                );

        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());

        return ProductResponse.from(product);
    }

    @Override
    public void deactivateProduct(UUID id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Product not found: " + id
                        )
                );

        product.setActive(false);
    }
}
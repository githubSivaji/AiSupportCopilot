package com.sivaji.aisupportcopilot.service;

import com.sivaji.aisupportcopilot.entity.Inventory;
import com.sivaji.aisupportcopilot.exception.InsufficientInventoryException;

import com.sivaji.aisupportcopilot.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    @Override
    public Inventory addStock(UUID productId, int quantity) {

        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be greater than zero"
            );
        }

        Inventory inventory = inventoryRepository
                .findByProductId(productId)
                .orElseGet(() ->
                        Inventory.builder()
                                .productId(productId)
                                .availableQuantity(0)
                                .build()
                );

        inventory.setAvailableQuantity(
                inventory.getAvailableQuantity() + quantity
        );

        return inventoryRepository.save(inventory);
    }

    @Override
    public Inventory reserveStock(
            UUID productId,
            int quantity) {

        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be greater than zero"
            );
        }

        Inventory inventory = inventoryRepository
                .findByProductId(productId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Inventory not found for product: "
                                        + productId
                        )
                );

        if (inventory.getAvailableQuantity() < quantity) {
            throw new InsufficientInventoryException(
                    "Insufficient inventory for product: "
                            + productId
            );
        }

        inventory.setAvailableQuantity(
                inventory.getAvailableQuantity() - quantity
        );

        return inventoryRepository.save(inventory);
    }

    @Override
    @Transactional(readOnly = true)
    public Inventory getInventory(UUID productId) {

        return inventoryRepository
                .findByProductId(productId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Inventory not found for product: "
                                        + productId
                        )
                );
    }
}
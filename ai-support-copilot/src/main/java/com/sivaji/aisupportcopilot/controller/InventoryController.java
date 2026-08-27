package com.sivaji.aisupportcopilot.controller;

import com.sivaji.aisupportcopilot.entity.Inventory;
import com.sivaji.aisupportcopilot.service.InventoryService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping("/{productId}/add")
    public ResponseEntity<Inventory> addStock(
            @PathVariable UUID productId,
            @RequestParam int quantity) {

        return ResponseEntity.ok(
                inventoryService.addStock(productId, quantity)
        );
    }

    @PostMapping("/{productId}/reserve")
    public ResponseEntity<Inventory> reserveStock(
            @PathVariable UUID productId,
            @RequestParam int quantity) {

        return ResponseEntity.ok(
                inventoryService.reserveStock(productId, quantity)
        );
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Inventory> getInventory(
            @PathVariable UUID productId) {

        return ResponseEntity.ok(
                inventoryService.getInventory(productId)
        );
    }
}
package com.sivaji.aisupportcopilot.service;

import com.sivaji.aisupportcopilot.entity.Inventory;

import java.util.UUID;

public interface InventoryService  {
    Inventory addStock(UUID productId, int quantity);


    Inventory reserveStock(UUID productId, int quantity);

    Inventory releaseStock(UUID productId, int quantity);
    Inventory getInventory(UUID productId);
}

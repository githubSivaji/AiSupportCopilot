package com.sivaji.aisupportcopilot.service;


import com.sivaji.aisupportcopilot.dto.OrderRequest;
import com.sivaji.aisupportcopilot.dto.OrderResponse;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    OrderResponse createOrder(
            UUID userId,
            OrderRequest request
    );

    OrderResponse getOrder(UUID userId, UUID orderId);

    List<OrderResponse> getUserOrders(UUID userId);

    void cancelOrder(
            UUID userId,
            UUID orderId
    );
}

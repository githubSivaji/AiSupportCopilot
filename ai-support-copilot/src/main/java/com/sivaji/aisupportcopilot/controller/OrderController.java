package com.sivaji.aisupportcopilot.controller;

import com.sivaji.aisupportcopilot.dto.OrderRequest;
import com.sivaji.aisupportcopilot.dto.OrderResponse;
import com.sivaji.aisupportcopilot.entity.User;
import com.sivaji.aisupportcopilot.service.OrderService;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;


    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody OrderRequest request) {

        OrderResponse response =
                orderService.createOrder(
                        user.getId(),
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(
            @AuthenticationPrincipal User user,
            @PathVariable UUID orderId) {

        return ResponseEntity.ok(
                orderService.getOrder(
                        user.getId(),
                        orderId
                )
        );
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getUserOrders(
            @AuthenticationPrincipal User user) {

        return ResponseEntity.ok(
                orderService.getUserOrders(user.getId())
        );
    }

    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<Void> cancelOrder(
            @AuthenticationPrincipal User user,
            @PathVariable UUID orderId) {

        orderService.cancelOrder(
                user.getId(),
                orderId
        );

        return ResponseEntity.noContent().build();
    }
}
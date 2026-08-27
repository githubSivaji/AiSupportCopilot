package com.sivaji.aisupportcopilot.service;
import com.sivaji.aisupportcopilot.dto.OrderItemRequest;
import com.sivaji.aisupportcopilot.dto.OrderItemResponse;
import com.sivaji.aisupportcopilot.dto.OrderRequest;
import com.sivaji.aisupportcopilot.dto.OrderResponse;
import com.sivaji.aisupportcopilot.entity.Order;
import com.sivaji.aisupportcopilot.entity.OrderItem;
import com.sivaji.aisupportcopilot.entity.Product;
import com.sivaji.aisupportcopilot.entity.User;
import com.sivaji.aisupportcopilot.enums.OrderStatus;
import com.sivaji.aisupportcopilot.repository.OrderRepository;
import com.sivaji.aisupportcopilot.repository.ProductRepository;
import com.sivaji.aisupportcopilot.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final InventoryService inventoryService;

    @Override
    public OrderResponse createOrder(
            UUID userId,
            OrderRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found: " + userId)
                );

        Order order = new Order();

        order.setUser(user);
        order.setStatus(OrderStatus.CREATED);
        order.setTotalAmount(BigDecimal.ZERO);

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (OrderItemRequest itemRequest : request.items()) {

            Product product = productRepository
                    .findById(itemRequest.productId())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Product not found: "
                                            + itemRequest.productId()
                            )
                    );

            if (!product.isActive()) {
                throw new RuntimeException(
                        "Product is not active: "
                                + product.getId()
                );
            }
            inventoryService.reserveStock(
                    product.getId(),
                    itemRequest.quantity()
            );

            BigDecimal unitPrice = product.getPrice();

            BigDecimal subtotal = unitPrice.multiply(
                    BigDecimal.valueOf(itemRequest.quantity())
            );

            OrderItem orderItem = new OrderItem();

            orderItem.setOrder(order);
            orderItem.setProductId(product.getId());
            orderItem.setQuantity(itemRequest.quantity());
            orderItem.setUnitPrice(unitPrice);

            order.getItems().add(orderItem);

            totalAmount = totalAmount.add(subtotal);
        }

        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);

        return toResponse(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrder(
            UUID userId,
            UUID orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Order not found: " + orderId
                        )
                );

        if (!order.getUser().getId().equals(userId)) {
            throw new RuntimeException(
                    "You cannot access this order"
            );
        }

        return toResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getUserOrders(UUID userId) {

        return orderRepository.findByUserId(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public void cancelOrder(
            UUID userId,
            UUID orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Order not found: " + orderId
                        )
                );

        if (!order.getUser().getId().equals(userId)) {
            throw new RuntimeException(
                    "You cannot cancel this order"
            );
        }

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new RuntimeException(
                    "Order is already cancelled"
            );
        }

        if (order.getStatus() == OrderStatus.DELIVERED) {
            throw new RuntimeException(
                    "Delivered order cannot be cancelled"
            );
        }

        for (OrderItem item : order.getItems()) {

            inventoryService.releaseStock(
                    item.getProductId(),
                    item.getQuantity()
            );
        }

        order.setStatus(OrderStatus.CANCELLED);
    }

    private OrderResponse toResponse(Order order) {

        List<OrderItemResponse> items =
                order.getItems()
                        .stream()
                        .map(item -> {

                            BigDecimal subtotal =
                                    item.getUnitPrice()
                                            .multiply(
                                                    BigDecimal.valueOf(
                                                            item.getQuantity()
                                                    )
                                            );

                            return new OrderItemResponse(
                                    item.getProductId(),
                                    item.getQuantity(),
                                    item.getUnitPrice(),
                                    subtotal
                            );
                        })
                        .toList();

        return new OrderResponse(
                order.getId(),
                order.getUser().getId(),
                order.getStatus(),
                order.getTotalAmount(),
                order.getCreatedAt(),
                items
        );
    }
}

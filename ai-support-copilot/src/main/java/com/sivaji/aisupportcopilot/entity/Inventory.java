package com.sivaji.aisupportcopilot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inventory {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private UUID productId;

    @Column(nullable = false)
    private Integer availableQuantity;

    @Version
    private Long version;
}
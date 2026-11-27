
package com.ecommerce.inventory.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "inventory_reservation")
@Getter
@Setter
@NoArgsConstructor
public class InventoryReservation {

    @Id
    @Column(name = "reservation_id", length = 20, nullable = false)
    private String reservationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id", nullable = false)
    private InventoryStock inventoryStock;

    @Column(name = "cart_id", length = 20)
    private String cartId;

    @Column(name = "cart_item_id", length = 20)
    private String cartItemId;
    
    @Column(
    	    name = "idempotency_key",
    	    length = 100,
    	    unique = true
    	)
    	private String idempotencyKey;

    @Column(name = "order_id", length = 20)
    private String orderId;

    @Column(name = "product_item_id", length = 7, nullable = false)
    private String productItemId;

    @Column(name = "variation_id", length = 7)
    private String variationId;

    @Column(name = "user_id", length = 15, nullable = false)
    private String userId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "reservation_status", length = 20, nullable = false)
    private String reservationStatus = "RESERVED";

    @CreationTimestamp
    @Column(name = "reserved_at", updatable = false)
    private LocalDateTime reservedAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    
}

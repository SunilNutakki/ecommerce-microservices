
package com.ecommerce.inventory.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "inventory_stock",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uq_inventory_product_item",
            columnNames = "product_item_id"
        )
    }
)
@Getter
@Setter
@NoArgsConstructor
public class InventoryStock {

    @Id
    @Column(name = "stock_id", length = 20, nullable = false)
    private String stockId;

    @Column(name = "product_item_id", length = 7, nullable = false)
    private String productItemId;

    @Column(name = "variation_id", length = 7)
    private String variationId;

    @Column(name = "total_quantity", nullable = false)
    private Integer totalQuantity = 0;

    @Column(name = "reserved_quantity", nullable = false)
    private Integer reservedQuantity = 0;

    @Column(name = "available_quantity", nullable = false)
    private Integer availableQuantity = 0;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}


package com.ecommerce.inventory.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

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

@Entity
@Table(name = "inventory_transaction")
@Getter
@Setter
@NoArgsConstructor
public class InventoryTransaction {

    @Id
    @Column(name = "transaction_id", length = 20, nullable = false)
    private String transactionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id")
    private InventoryStock inventoryStock;

    @Column(name = "transaction_type", length = 20, nullable = false)
    private String transactionType;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "reference_id", length = 20)
    private String referenceId;

    @Column(name = "reference_type", length = 20)
    private String referenceType;

    @Column(name = "remarks", length = 255)
    private String remarks;

    @Column(name = "created_by", length = 15)
    private String createdBy;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

  
}

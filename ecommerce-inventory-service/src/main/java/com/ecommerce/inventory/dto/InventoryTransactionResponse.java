package com.ecommerce.inventory.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class InventoryTransactionResponse {

    private String transactionId;
    private String stockId;
    private String transactionType;
    private Integer quantity;
    private String referenceId;
    private String referenceType;
    private String remarks;
    private String createdBy;
    private LocalDateTime createdAt;
}
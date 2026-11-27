
package com.ecommerce.inventory.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
public class InventoryStockResponse {

    private String stockId;
    private String productItemId;
    private String variationId;
    private Integer totalQuantity;
    private Integer reservedQuantity;
    private Integer availableQuantity;
    private LocalDateTime updatedAt;

   
}

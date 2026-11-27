
package com.ecommerce.inventory.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReserveStockRequest {

    private String productItemId;
    private String cartId;
    private String cartItemId;
    private String userId;
    private Integer quantity;

    private String idempotencyKey;
}
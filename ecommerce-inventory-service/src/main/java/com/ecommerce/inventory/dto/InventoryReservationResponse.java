
package com.ecommerce.inventory.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
public class InventoryReservationResponse {

    private String reservationId;
    private String stockId;
    private String productItemId;
    private String cartId;
    private String cartItemId;
    private String userId;
    private Integer quantity;
    private String reservationStatus;
    private LocalDateTime reservedAt;
    private LocalDateTime expiresAt;

    
}

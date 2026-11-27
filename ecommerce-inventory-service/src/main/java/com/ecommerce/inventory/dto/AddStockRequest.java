
package com.ecommerce.inventory.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddStockRequest {

    private String productItemId;
    private Integer quantity;
    private String createdBy;
    private String remarks;

   
}

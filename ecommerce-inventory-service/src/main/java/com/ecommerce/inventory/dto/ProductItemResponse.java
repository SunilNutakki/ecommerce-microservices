
package com.ecommerce.inventory.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class ProductItemResponse {

    private String productItemId;
    private String productId;
    private String productName;
    private BigDecimal originalPrice;
    private BigDecimal salePrice;
    private String productCode;
}

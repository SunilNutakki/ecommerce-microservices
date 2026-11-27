package com.ecommerce.cartservice.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductItemResponse {

    private String productItemId;
    private String productId;
    private String productName;
    private BigDecimal originalPrice;
    private BigDecimal salePrice;
    private String productCode;

}
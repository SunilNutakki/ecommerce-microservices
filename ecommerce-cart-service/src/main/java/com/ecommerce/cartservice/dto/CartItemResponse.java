
package com.ecommerce.cartservice.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItemResponse {

    private String cartItemId;

    private String cartId;

    private String productItemId;

    private String productId;

    private String productName;

    private Integer quantity;

    private BigDecimal unitPrice;

    private BigDecimal totalPrice;

    private String message;
}


package com.ecommerce.cartservice.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartDetailsResponse {

    private String cartId;

    private String userId;

    private String status;

    private List<CartItemResponse> items;

    private Integer totalItems;

    private BigDecimal grandTotal;
}

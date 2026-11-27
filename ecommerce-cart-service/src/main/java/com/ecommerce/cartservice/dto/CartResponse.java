
package com.ecommerce.cartservice.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartResponse {

    private String cartId;
    private String userId;
    private String status;
    private String message;

  
}


package com.ecommerce.cartservice.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class CreateCartRequest {

    @NotBlank(message = "User ID is required")
    private String userId;
    
    


}

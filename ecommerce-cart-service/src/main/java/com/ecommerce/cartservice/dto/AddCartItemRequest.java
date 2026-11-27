package com.ecommerce.cartservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddCartItemRequest {

    @NotBlank(message = "Product Item ID is required")
    private String productItemId;

    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

}
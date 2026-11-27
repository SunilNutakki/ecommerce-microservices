package com.nt.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductItemResponse {

    private String productItemId;

    private String productId;

    private String productName;

    private Integer originalPrice;

    private Integer salePrice;

    private String productCode;
}
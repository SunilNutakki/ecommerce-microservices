package com.nt.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.AllArgsConstructor;
//ProductDTO
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDTO {
    private String productId;  // null for new products
    private String productName;
    private String description;
    private Integer originalPrice;
    private Integer salePrice;
    private String categoryName;
    private String product_category_id;
    
    private List<ImageDTO> images; 
    
}

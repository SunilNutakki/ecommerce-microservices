package com.nt.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryWithProductsDTO {
    private String proCategoryId;
    private String cateoryName;
    private String cateoryImage;
    private String productGenderId;
    private String parent_category_id;
    
    //when we want to group json
    private List<ProductDTO> products;
    
    private List<CategoryWithProductsDTO> subCategories;


}

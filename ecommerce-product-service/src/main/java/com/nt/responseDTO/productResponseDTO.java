/**
 * 
 */
package com.nt.responseDTO;

import com.nt.dto.ProductDTO;

import lombok.Data;

/**
 * 
 */
@Data
public class productResponseDTO {
	
	private String productId;  // null for new products
	 private String productName;
	 private Integer originalPrice;
	 private Integer salePrice;
	 private String productDesc;

}

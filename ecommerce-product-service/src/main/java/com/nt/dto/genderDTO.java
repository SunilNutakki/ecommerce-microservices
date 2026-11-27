package com.nt.dto;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data	
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class genderDTO {
	private String productGenderId;
	private String genderName;
	private String productDesc;
	private int status;
	
	private List<CategoryWithProductsDTO> genderAgainstCatList;
	
}

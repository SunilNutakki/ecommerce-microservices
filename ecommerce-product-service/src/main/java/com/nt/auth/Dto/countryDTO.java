package com.nt.auth.Dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nt.dto.CategoryWithProductsDTO;
import com.nt.dto.genderDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data	
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)

public class countryDTO {
	private String countryCode;
	private String countryId;
	private String countryName;

}

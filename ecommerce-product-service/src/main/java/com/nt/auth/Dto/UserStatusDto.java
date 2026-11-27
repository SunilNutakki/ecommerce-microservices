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

public class UserStatusDto {
	private String id;
	private String statusName;
	private String activeStatus;
	private String createdBY;
	
	

}

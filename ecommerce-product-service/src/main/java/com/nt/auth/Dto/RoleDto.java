package com.nt.auth.Dto;

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
public class RoleDto {
	private String id;
	private String roleName;
	private String roleDesc;
	private String status;
	   private String createdBy;
	   private java.sql.Timestamp createdDate;

}

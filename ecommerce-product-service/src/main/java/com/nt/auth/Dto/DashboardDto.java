package com.nt.auth.Dto;

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
public class DashboardDto {
	private String dashBoardId;
	private String dashBoardName;
	private String urlPath;
	private String description;
	 private String isActive;

}

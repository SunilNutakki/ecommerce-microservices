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
public class UserDto {
	private String userId;
    private String userFullName;
    private String email;
    private String phoneNo;

    // Flattened references
    private String roleId;
    private String roleName;

    private String providerId;
    private String providerName;

    private String statusId;
    private String statusName;

    private String countryCodeId;

    private String createdAt;
    private String updatedAt;
    private String password;



}

package com.nt.auth.service;

import java.util.List;

import com.nt.auth.Dto.UserDto;
import com.nt.auth.model.User;
import com.nt.auth.projection.User_Dash_BeanView;


public interface ILoginService   {


	UserDto savesignApi(UserDto dto);

	UserDto signIn(UserDto dto);

	List<User_Dash_BeanView> getDashboardsByRole(String roleId);
	

}

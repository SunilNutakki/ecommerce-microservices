package com.nt.auth.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nt.auth.Dto.UserDto;
import com.nt.auth.model.User;
import com.nt.auth.projection.User_Dash_BeanView;
import com.nt.auth.repository.IContextRepository;
import com.nt.auth.repository.IUserRepository;

import lombok.RequiredArgsConstructor;
@Service
@Transactional
@RequiredArgsConstructor

public class LoginService implements ILoginService {
    
	   @Autowired
	   private IUserRepository userRepo;
	   @Autowired
	   private final IContextRepository contextRepo;
	
	   @Override
	   @Transactional
	   public UserDto savesignApi(UserDto dto) {

	       User user = new User();
	       user.setUserId(dto.getUserId());
	       user.setUserFullName(dto.getUserFullName());
	       user.setEmail(dto.getEmail());
	       user.setPhoneNo(dto.getPhoneNo());
	       user.setPassword(dto.getPassword());
	       // password not in DTO? then set default or handle separately
	       // user.setPassword(dto.getPassword());

	       User saved = userRepo.save(user);

	       // return same DTO or build new one
	       return UserDto.builder()
	               .userId(saved.getUserId())
	               .userFullName(saved.getUserFullName())
	               .email(saved.getEmail())
	               .phoneNo(saved.getPhoneNo())
	               .statusName("USER_SAVED_SUCCESSFULLY")
	               .build();
	   }



	   @Override
	   public UserDto signIn(UserDto dto) {

	       User user = userRepo.findByEmail(dto.getEmail())
	           .orElseThrow(() -> new RuntimeException("Invalid email"));

	       if (!user.getPassword().equals(dto.getPassword())) {
	           throw new RuntimeException("Invalid password");
	       }

	       return UserDto.builder()
	               .userId(user.getUserId())
	               .userFullName(user.getUserFullName())
	               .email(user.getEmail())
	               .statusName("LOGIN_SUCCESS")
	               .build();
	   }


	   @Override
	    public List<User_Dash_BeanView> getDashboardsByRole(String roleId) {

	        List<User_Dash_BeanView> dashboards =
	                contextRepo.findDashboardsByRole(roleId);

	        if (dashboards.isEmpty()) {
	            throw new RuntimeException("No dashboards found for role: " + roleId);
	        }

	        return dashboards;
	    }





	

}

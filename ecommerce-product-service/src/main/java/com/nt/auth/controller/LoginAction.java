package com.nt.auth.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nt.auth.Dto.UserDto;
import com.nt.auth.model.User;
import com.nt.auth.projection.User_Dash_BeanView;
import com.nt.auth.service.ILoginService;

import io.swagger.v3.oas.annotations.tags.Tag;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/auth-login")
@Tag(
	    name = "Login&DashBoard Management",
	    description = "APIs for creating, managing and checking out Auth"
	)

public class LoginAction {
	@Autowired
	  public ILoginService iloginServie;
	
	@PostMapping("/signup-api")
	public ResponseEntity<UserDto> saveLoginApi(
	        @RequestBody UserDto dto) {

	    System.out.println("LoginAction.saveLoginApi()");
	    UserDto response = iloginServie.savesignApi(dto);
	    return ResponseEntity.ok(response);
	}

@GetMapping("/test")
public String test() {
    System.out.println("TEST API HIT");
    return "OK";
}

@PostMapping("/sign-in")
public ResponseEntity<UserDto> signIn(@RequestBody UserDto dto) {
    return ResponseEntity.ok(iloginServie.signIn(dto));
}

@GetMapping("/{roleId}/dashboards")
public List<User_Dash_BeanView> getDashboardsByRole(
        @PathVariable String roleId) {

    return iloginServie.getDashboardsByRole(roleId);
}


	

}

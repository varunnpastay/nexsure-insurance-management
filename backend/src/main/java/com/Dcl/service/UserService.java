package com.Dcl.service;

import java.util.List;

import com.Dcl.dto.UserDto;
import com.Dcl.request.LoginRequest;
import com.Dcl.request.RegisterRequest;

public interface UserService {

	public UserDto register(RegisterRequest request);
	
	public UserDto login(LoginRequest request);
	
	public UserDto adminLogin(LoginRequest request);
	
	public List<UserDto> getAll();
	
	public void deleteUserById(Integer userId);
}

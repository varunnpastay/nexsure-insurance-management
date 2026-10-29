package com.Dcl.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Dcl.dto.UserDto;
import com.Dcl.request.LoginRequest;
import com.Dcl.request.RegisterRequest;
import com.Dcl.response.ApiResponse;
import com.Dcl.service.UserService;

@RestController  //@Controller + @ResponseBody
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService uservice;

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody RegisterRequest request){
		UserDto dto=uservice.register(request);
		return ResponseEntity.ok(new ApiResponse<>("Registration successful! Please login.",dto,HttpStatus.OK));
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest request){
		UserDto dto=uservice.login(request);
		return ResponseEntity.ok(new ApiResponse<>("Login successful!",dto,HttpStatus.OK));
	}
	
	@GetMapping("/get")
	public ResponseEntity<?> getAllUser(){
		List<UserDto> dtoList=uservice.getAll();
		return ResponseEntity.ok(new ApiResponse<>("Users Data!",dtoList,HttpStatus.OK));
	}
	
	@DeleteMapping("/delete/{userId}")
	public ResponseEntity<?> deleteById(@PathVariable Integer userId){
		uservice.deleteUserById(userId);
		return ResponseEntity.ok(new ApiResponse<>("User deleted successfully!",null,HttpStatus.OK));
	}
}

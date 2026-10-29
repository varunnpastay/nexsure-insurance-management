package com.Dcl.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Dcl.dto.UserDto;
import com.Dcl.request.LoginRequest;
import com.Dcl.response.ApiResponse;
import com.Dcl.service.DashboardService;
import com.Dcl.service.UserService;

//Admin portal APIs
@RestController
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private UserService uservice;
	
	@Autowired
	private DashboardService dservice;
	
	@PostMapping("/login")
	public ResponseEntity<?> adminLogin(@RequestBody LoginRequest request){
		UserDto dto=uservice.adminLogin(request);
		return ResponseEntity.ok(new ApiResponse<>("Welcome Admin!",dto,HttpStatus.OK));
	}
	
	@GetMapping("/stats")
	public ResponseEntity<?> getStats(){
		Map<String, Long> stats=dservice.getStats();
		return ResponseEntity.ok(new ApiResponse<>("Dashboard Stats",stats,HttpStatus.OK));
	}
}

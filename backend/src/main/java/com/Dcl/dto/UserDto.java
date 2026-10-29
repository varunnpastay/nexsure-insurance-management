package com.Dcl.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UserDto {
	
	private Integer userId;
	
	private String fullName;
	
	private String email;
	
	private String role;
	
	private LocalDateTime createdAt;
}

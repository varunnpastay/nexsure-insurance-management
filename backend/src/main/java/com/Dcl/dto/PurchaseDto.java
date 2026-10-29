package com.Dcl.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class PurchaseDto {

	private Integer purchaseId;
	
	private String policyNumber;
	
	private Double purchaseAmount;
	
	private LocalDate purchaseDate;
	
	private LocalDate expiryDate;
	
	private String paymentStatus;
	
	private String status;
	
	private UserDto user;
	
	private PolicyDto policy;
}

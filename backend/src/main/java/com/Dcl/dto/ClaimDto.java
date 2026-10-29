package com.Dcl.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ClaimDto {

	private Integer claimId;
	
	private Double claimAmount;
	
	private String claimReason;
	
	private LocalDate claimDate;
	
	private String status;
	
	private UserDto user;
	
	private PurchaseDto purchase;
}

package com.Dcl.dto;

import lombok.Data;

@Data
public class PolicyDto {

	private Integer policyId;
	
	private String policyName;
	
	private Double premiumAmount;
	
	private Double coverageAmount;
	
	private Integer durationMonths;
	
	private String description;
	
	private String status;
	
	private CategoryDto category;
}

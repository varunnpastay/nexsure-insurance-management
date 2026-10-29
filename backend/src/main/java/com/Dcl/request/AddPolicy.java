package com.Dcl.request;

import lombok.Data;

@Data
public class AddPolicy {

	private Integer categoryId;

	private String policyName;

	private Double premiumAmount;

	private Double coverageAmount;

	private Integer durationMonths;

	private String description;
}

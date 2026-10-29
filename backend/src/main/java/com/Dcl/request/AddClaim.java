package com.Dcl.request;

import lombok.Data;

@Data
public class AddClaim {

	private Integer userId;

	private Integer purchaseId;

	private Double claimAmount;

	private String claimReason;
}

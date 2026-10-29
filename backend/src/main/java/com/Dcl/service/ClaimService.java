package com.Dcl.service;

import java.util.List;

import com.Dcl.dto.ClaimDto;
import com.Dcl.request.AddClaim;

public interface ClaimService {

	public ClaimDto addClaim(AddClaim request);
	
	public List<ClaimDto> getAllClaims();
	
	public List<ClaimDto> getClaimsByUser(Integer userId);
	
	public ClaimDto updateStatus(Integer claimId, String status);
	
	public void deleteClaimById(Integer claimId);
}

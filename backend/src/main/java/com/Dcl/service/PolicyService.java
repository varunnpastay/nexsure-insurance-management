package com.Dcl.service;

import java.util.List;

import com.Dcl.dto.PolicyDto;
import com.Dcl.request.AddPolicy;
import com.Dcl.request.UpdatePolicy;

public interface PolicyService {

	public PolicyDto addPolicy(AddPolicy request);
	
	public PolicyDto getPolicyById(Integer policyId);
	
	public List<PolicyDto> getAllPolicies();
	
	public List<PolicyDto> getActivePolicies(Integer categoryId);
	
	public PolicyDto updatePolicy(Integer policyId, UpdatePolicy request);
	
	public void deletePolicyById(Integer policyId);
}

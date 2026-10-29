package com.Dcl.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Dcl.dto.PolicyDto;
import com.Dcl.request.AddPolicy;
import com.Dcl.request.UpdatePolicy;
import com.Dcl.response.ApiResponse;
import com.Dcl.service.PolicyService;

@RestController
@RequestMapping("/policy")
public class PolicyController {

	@Autowired
	private PolicyService pservice;
	
	@PostMapping("/add")
	public ResponseEntity<?> addPolicy(@RequestBody AddPolicy request){
		PolicyDto dto=pservice.addPolicy(request);
		return ResponseEntity.ok(new ApiResponse<>("Policy added successfully!",dto,HttpStatus.OK));
	}
	
	@GetMapping("/get/{policyId}")
	public ResponseEntity<?> getPolicyById(@PathVariable Integer policyId){
		PolicyDto dto=pservice.getPolicyById(policyId);
		return ResponseEntity.ok(new ApiResponse<>("Policy Info",dto,HttpStatus.OK));
	}
	
	//admin - all policies
	@GetMapping("/get")
	public ResponseEntity<?> getAllPolicies(){
		List<PolicyDto> dtoList=pservice.getAllPolicies();
		return ResponseEntity.ok(new ApiResponse<>("All Policies",dtoList,HttpStatus.OK));
	}
	
	//customer - /policy/active  or  /policy/active?categoryId=2
	@GetMapping("/active")
	public ResponseEntity<?> getActivePolicies(@RequestParam(required = false) Integer categoryId){
		List<PolicyDto> dtoList=pservice.getActivePolicies(categoryId);
		return ResponseEntity.ok(new ApiResponse<>("Active Policies",dtoList,HttpStatus.OK));
	}
	
	@PutMapping("/update/{policyId}")
	public ResponseEntity<?> updatePolicy(@PathVariable Integer policyId, 
										  @RequestBody UpdatePolicy request){
		PolicyDto dto=pservice.updatePolicy(policyId, request);
		return ResponseEntity.ok(new ApiResponse<>("Policy updated successfully!",dto,HttpStatus.OK));
	}
	
	@DeleteMapping("/delete/{policyId}")
	public ResponseEntity<?> deletePolicy(@PathVariable Integer policyId){
		pservice.deletePolicyById(policyId);
		return ResponseEntity.ok(new ApiResponse<>("Policy deleted successfully!",null,HttpStatus.OK));
	}
}

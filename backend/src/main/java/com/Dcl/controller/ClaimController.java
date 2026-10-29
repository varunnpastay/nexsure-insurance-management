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

import com.Dcl.dto.ClaimDto;
import com.Dcl.request.AddClaim;
import com.Dcl.response.ApiResponse;
import com.Dcl.service.ClaimService;

@RestController
@RequestMapping("/claim")
public class ClaimController {

	@Autowired
	private ClaimService claimService;
	
	@PostMapping("/add")
	public ResponseEntity<?> addClaim(@RequestBody AddClaim request){
		ClaimDto dto=claimService.addClaim(request);
		return ResponseEntity.ok(new ApiResponse<>("Claim submitted successfully!",dto,HttpStatus.OK));
	}
	
	//admin - all claims
	@GetMapping("/get")
	public ResponseEntity<?> getAllClaims(){
		List<ClaimDto> dtoList=claimService.getAllClaims();
		return ResponseEntity.ok(new ApiResponse<>("All Claims",dtoList,HttpStatus.OK));
	}
	
	//customer - my claims
	@GetMapping("/user/{userId}")
	public ResponseEntity<?> getClaimsByUser(@PathVariable Integer userId){
		List<ClaimDto> dtoList=claimService.getClaimsByUser(userId);
		return ResponseEntity.ok(new ApiResponse<>("My Claims",dtoList,HttpStatus.OK));
	}
	
	//admin - /claim/status/4?status=APPROVED
	@PutMapping("/status/{claimId}")
	public ResponseEntity<?> updateStatus(@PathVariable Integer claimId, @RequestParam String status){
		ClaimDto dto=claimService.updateStatus(claimId, status);
		return ResponseEntity.ok(new ApiResponse<>("Claim "+status.toLowerCase()+"!",dto,HttpStatus.OK));
	}
	
	@DeleteMapping("/delete/{claimId}")
	public ResponseEntity<?> deleteClaim(@PathVariable Integer claimId){
		claimService.deleteClaimById(claimId);
		return ResponseEntity.ok(new ApiResponse<>("Claim deleted successfully!",null,HttpStatus.OK));
	}
}

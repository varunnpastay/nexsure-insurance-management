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

import com.Dcl.dto.PurchaseDto;
import com.Dcl.request.BuyPolicy;
import com.Dcl.response.ApiResponse;
import com.Dcl.service.PurchaseService;

@RestController
@RequestMapping("/purchase")
public class PurchaseController {

	@Autowired
	private PurchaseService purchaseService;
	
	@PostMapping("/buy")
	public ResponseEntity<?> buyPolicy(@RequestBody BuyPolicy request){
		PurchaseDto dto=purchaseService.buyPolicy(request);
		return ResponseEntity.ok(new ApiResponse<>("Policy purchased successfully!",dto,HttpStatus.OK));
	}
	
	//admin - all purchases
	@GetMapping("/get")
	public ResponseEntity<?> getAllPurchases(){
		List<PurchaseDto> dtoList=purchaseService.getAllPurchases();
		return ResponseEntity.ok(new ApiResponse<>("All Purchases",dtoList,HttpStatus.OK));
	}
	
	//customer - my purchases
	@GetMapping("/user/{userId}")
	public ResponseEntity<?> getPurchasesByUser(@PathVariable Integer userId){
		List<PurchaseDto> dtoList=purchaseService.getPurchasesByUser(userId);
		return ResponseEntity.ok(new ApiResponse<>("My Purchases",dtoList,HttpStatus.OK));
	}
	
	//admin - /purchase/status/3?status=CANCELLED
	@PutMapping("/status/{purchaseId}")
	public ResponseEntity<?> updateStatus(@PathVariable Integer purchaseId, @RequestParam String status){
		PurchaseDto dto=purchaseService.updateStatus(purchaseId, status);
		return ResponseEntity.ok(new ApiResponse<>("Status updated successfully!",dto,HttpStatus.OK));
	}
	
	@DeleteMapping("/delete/{purchaseId}")
	public ResponseEntity<?> deletePurchase(@PathVariable Integer purchaseId){
		purchaseService.deletePurchaseById(purchaseId);
		return ResponseEntity.ok(new ApiResponse<>("Purchase deleted successfully!",null,HttpStatus.OK));
	}
}

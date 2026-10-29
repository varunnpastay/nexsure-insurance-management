package com.Dcl.service;

import java.util.List;

import com.Dcl.dto.PurchaseDto;
import com.Dcl.request.BuyPolicy;

public interface PurchaseService {

	public PurchaseDto buyPolicy(BuyPolicy request);
	
	public List<PurchaseDto> getAllPurchases();
	
	public List<PurchaseDto> getPurchasesByUser(Integer userId);
	
	public PurchaseDto updateStatus(Integer purchaseId, String status);
	
	public void deletePurchaseById(Integer purchaseId);
}

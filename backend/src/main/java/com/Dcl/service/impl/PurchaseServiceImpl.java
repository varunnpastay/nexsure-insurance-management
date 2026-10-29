package com.Dcl.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.Dcl.dto.PurchaseDto;
import com.Dcl.entity.Policy;
import com.Dcl.entity.Purchase;
import com.Dcl.entity.User;
import com.Dcl.exception.AppException;
import com.Dcl.repo.ClaimRepository;
import com.Dcl.repo.PolicyRepository;
import com.Dcl.repo.PurchaseRepository;
import com.Dcl.repo.UserRepository;
import com.Dcl.request.BuyPolicy;
import com.Dcl.service.PurchaseService;

@Service
public class PurchaseServiceImpl implements PurchaseService {

	@Autowired
	private PurchaseRepository purchaseRepo;
	
	@Autowired
	private UserRepository urepo;
	
	@Autowired
	private PolicyRepository prepo;
	
	@Autowired
	private ClaimRepository claimRepo;
	
	@Autowired
	private ModelMapper mapper;
	
	@Override
	public PurchaseDto buyPolicy(BuyPolicy request) {
		User user=urepo.findById(request.getUserId()).orElseThrow(()->new AppException("User Not Found!", HttpStatus.NOT_FOUND));
		Policy policy=prepo.findById(request.getPolicyId()).orElseThrow(()->new AppException("Policy Not Found!", HttpStatus.NOT_FOUND));
		
		if(!policy.getStatus().equals("ACTIVE")) {
			throw new AppException("Policy is not available!", HttpStatus.BAD_REQUEST);
		}
		
		Purchase purchase=new Purchase();
		purchase.setPolicyNumber("PN"+policy.getPolicyId()+user.getUserId()+System.currentTimeMillis());
		purchase.setUser(user);
		purchase.setPolicy(policy);
		purchase.setPurchaseAmount(policy.getPremiumAmount());
		purchase.setPurchaseDate(LocalDate.now());
		purchase.setExpiryDate(LocalDate.now().plusMonths(policy.getDurationMonths()));
		purchase.setPaymentStatus("PAID");
		purchase.setStatus("ACTIVE");
		
		purchase=purchaseRepo.save(purchase);
		return mapper.map(purchase, PurchaseDto.class);
	}

	@Override
	public List<PurchaseDto> getAllPurchases() {
		List<Purchase> purchaseList=purchaseRepo.findAll();
		return purchaseList.stream()
				.map(p->mapper.map(p, PurchaseDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public List<PurchaseDto> getPurchasesByUser(Integer userId) {
		List<Purchase> purchaseList=purchaseRepo.findByUser_UserId(userId);
		return purchaseList.stream()
				.map(p->mapper.map(p, PurchaseDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public PurchaseDto updateStatus(Integer purchaseId, String status) {
		Purchase purchase=purchaseRepo.findById(purchaseId).orElseThrow(()->new AppException("Purchase Not Found!", HttpStatus.NOT_FOUND));
		purchase.setStatus(status);
		purchase=purchaseRepo.save(purchase);
		return mapper.map(purchase, PurchaseDto.class);
	}

	@Override
	public void deletePurchaseById(Integer purchaseId) {
		purchaseRepo.findById(purchaseId).orElseThrow(()->new AppException("Purchase Not Found!", HttpStatus.NOT_FOUND));
		if(claimRepo.existsByPurchase_PurchaseId(purchaseId)) {
			throw new AppException("Purchase has claims, delete them first!", HttpStatus.CONFLICT);
		}
		purchaseRepo.deleteById(purchaseId);
	}

}

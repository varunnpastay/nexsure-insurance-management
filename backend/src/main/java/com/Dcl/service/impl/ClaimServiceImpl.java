package com.Dcl.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.Dcl.dto.ClaimDto;
import com.Dcl.entity.Claim;
import com.Dcl.entity.Purchase;
import com.Dcl.exception.AppException;
import com.Dcl.repo.ClaimRepository;
import com.Dcl.repo.PurchaseRepository;
import com.Dcl.request.AddClaim;
import com.Dcl.service.ClaimService;

@Service
public class ClaimServiceImpl implements ClaimService {

	@Autowired
	private ClaimRepository claimRepo;
	
	@Autowired
	private PurchaseRepository purchaseRepo;
	
	@Autowired
	private ModelMapper mapper;
	
	@Override
	public ClaimDto addClaim(AddClaim request) {
		Purchase purchase=purchaseRepo.findById(request.getPurchaseId()).orElseThrow(()->new AppException("Purchase Not Found!", HttpStatus.NOT_FOUND));
		
		//customer can claim only on his own ACTIVE policy
		if(!purchase.getUser().getUserId().equals(request.getUserId())) {
			throw new AppException("This policy does not belong to you!", HttpStatus.FORBIDDEN);
		}
		if(!purchase.getStatus().equals("ACTIVE")) {
			throw new AppException("Claim allowed only on ACTIVE policy!", HttpStatus.BAD_REQUEST);
		}
		if(request.getClaimAmount()>purchase.getPolicy().getCoverageAmount()) {
			throw new AppException("Claim amount is more than coverage amount!", HttpStatus.BAD_REQUEST);
		}
		
		Claim claim=new Claim();
		claim.setPurchase(purchase);
		claim.setUser(purchase.getUser());
		claim.setClaimAmount(request.getClaimAmount());
		claim.setClaimReason(request.getClaimReason());
		claim.setClaimDate(LocalDate.now());
		claim.setStatus("PENDING");
		
		claim=claimRepo.save(claim);
		return mapper.map(claim, ClaimDto.class);
	}

	@Override
	public List<ClaimDto> getAllClaims() {
		List<Claim> claimList=claimRepo.findAll();
		return claimList.stream()
				.map(c->mapper.map(c, ClaimDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public List<ClaimDto> getClaimsByUser(Integer userId) {
		List<Claim> claimList=claimRepo.findByUser_UserId(userId);
		return claimList.stream()
				.map(c->mapper.map(c, ClaimDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public ClaimDto updateStatus(Integer claimId, String status) {
		Claim claim=claimRepo.findById(claimId).orElseThrow(()->new AppException("Claim Not Found!", HttpStatus.NOT_FOUND));
		claim.setStatus(status);
		claim=claimRepo.save(claim);
		return mapper.map(claim, ClaimDto.class);
	}

	@Override
	public void deleteClaimById(Integer claimId) {
		claimRepo.findById(claimId).orElseThrow(()->new AppException("Claim Not Found!", HttpStatus.NOT_FOUND));
		claimRepo.deleteById(claimId);
	}

}

package com.Dcl.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final Logger logger=LoggerFactory.getLogger(ClaimServiceImpl.class);

	@Autowired
	private ClaimRepository claimRepo;

	@Autowired
	private PurchaseRepository purchaseRepo;

	@Autowired
	private ModelMapper mapper;

	@Override
	public ClaimDto addClaim(AddClaim request) {
		logger.trace("Entered addClaim()");
		logger.info("Claim request - user id: {}, purchase id: {}, amount: {}",
				request.getUserId(), request.getPurchaseId(), request.getClaimAmount());
		
		logger.trace("Step 1: finding purchase id {}", request.getPurchaseId());
		Purchase purchase=purchaseRepo.findById(request.getPurchaseId()).orElseThrow(()->{
			logger.warn("Claim failed - purchase not found: {}", request.getPurchaseId());
			return new AppException("Purchase Not Found!", HttpStatus.NOT_FOUND);
		});
		logger.trace("Step 1 done: purchase found, owner user id: {}, status: {}",
				purchase.getUser().getUserId(), purchase.getStatus());

		//customer can claim only on his own ACTIVE policy
		logger.trace("Step 2: checking ownership");
		if(!purchase.getUser().getUserId().equals(request.getUserId())) {
			logger.warn("Claim blocked - user {} tried to claim on purchase {} owned by user {}",
					request.getUserId(), purchase.getPurchaseId(), purchase.getUser().getUserId());
			throw new AppException("This policy does not belong to you!", HttpStatus.FORBIDDEN);
		}
		logger.trace("Step 2 done: ownership check passed");
		
		logger.trace("Step 3: checking purchase status");
		if(!purchase.getStatus().equals("ACTIVE")) {
			logger.warn("Claim failed - purchase {} is {}", purchase.getPurchaseId(), purchase.getStatus());
			throw new AppException("Claim allowed only on ACTIVE policy!", HttpStatus.BAD_REQUEST);
		}
		logger.trace("Step 3 done: status is ACTIVE");
		
		logger.trace("Step 4: checking amount {} against coverage {}",
				request.getClaimAmount(), purchase.getPolicy().getCoverageAmount());
		if(request.getClaimAmount()>purchase.getPolicy().getCoverageAmount()) {
			logger.warn("Claim failed - amount {} exceeds coverage {}",
					request.getClaimAmount(), purchase.getPolicy().getCoverageAmount());
			throw new AppException("Claim amount is more than coverage amount!", HttpStatus.BAD_REQUEST);
		}
		logger.trace("Step 4 done: amount is within coverage");

		logger.trace("Step 5: building claim object");
		Claim claim=new Claim();
		claim.setPurchase(purchase);
		claim.setUser(purchase.getUser());
		claim.setClaimAmount(request.getClaimAmount());
		claim.setClaimReason(request.getClaimReason());
		claim.setClaimDate(LocalDate.now());
		claim.setStatus("PENDING");

		logger.trace("Step 6: saving claim to database");
		claim=claimRepo.save(claim);
		logger.info("Claim submitted with id: {} (status: PENDING)", claim.getClaimId());
		logger.trace("Exiting addClaim()");
		return mapper.map(claim, ClaimDto.class);
	}

	@Override
	public List<ClaimDto> getAllClaims() {
		logger.trace("Entered getAllClaims()");
		List<Claim> claimList=claimRepo.findAll();
		logger.debug("Fetched {} claims", claimList.size());
		logger.trace("Converting {} claims to DTO", claimList.size());
		return claimList.stream()
				.map(c->mapper.map(c, ClaimDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public List<ClaimDto> getClaimsByUser(Integer userId) {
		logger.trace("Entered getClaimsByUser() with user id: {}", userId);
		List<Claim> claimList=claimRepo.findByUser_UserId(userId);
		logger.debug("Fetched {} claims for user id: {}", claimList.size(), userId);
		logger.trace("Converting {} claims to DTO", claimList.size());
		return claimList.stream()
				.map(c->mapper.map(c, ClaimDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public ClaimDto updateStatus(Integer claimId, String status) {
		logger.trace("Entered updateStatus() with claim id: {}, status: {}", claimId, status);
		logger.info("Status update for claim id: {} -> {}", claimId, status);
		Claim claim=claimRepo.findById(claimId).orElseThrow(()->{
			logger.warn("Status update failed - claim not found: {}", claimId);
			return new AppException("Claim Not Found!", HttpStatus.NOT_FOUND);
		});
		logger.trace("Claim found, current status: {}", claim.getStatus());
		String oldStatus=claim.getStatus();
		claim.setStatus(status);
		logger.trace("Saving claim with new status");
		claim=claimRepo.save(claim);
		logger.info("Claim {} status changed: {} -> {}", claimId, oldStatus, status);
		logger.trace("Exiting updateStatus()");
		return mapper.map(claim, ClaimDto.class);
	}

	@Override
	public void deleteClaimById(Integer claimId) {
		logger.trace("Entered deleteClaimById() with claim id: {}", claimId);
		logger.info("Delete request for claim id: {}", claimId);
		claimRepo.findById(claimId).orElseThrow(()->{
			logger.warn("Delete failed - claim not found: {}", claimId);
			return new AppException("Claim Not Found!", HttpStatus.NOT_FOUND);
		});
		logger.trace("Claim found, deleting");
		claimRepo.deleteById(claimId);
		logger.info("Claim deleted: {}", claimId);
		logger.trace("Exiting deleteClaimById()");
	}

}
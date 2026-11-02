package com.Dcl.service.impl;

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Dcl.repo.CategoryRepository;
import com.Dcl.repo.ClaimRepository;
import com.Dcl.repo.PolicyRepository;
import com.Dcl.repo.PurchaseRepository;
import com.Dcl.repo.UserRepository;
import com.Dcl.service.DashboardService;

@Service
public class DashboardServiceImpl implements DashboardService {

	private static final Logger logger=LoggerFactory.getLogger(DashboardServiceImpl.class);

	@Autowired
	private UserRepository urepo;

	@Autowired
	private CategoryRepository crepo;

	@Autowired
	private PolicyRepository prepo;

	@Autowired
	private PurchaseRepository purchaseRepo;

	@Autowired
	private ClaimRepository claimRepo;

	@Override
	public Map<String, Long> getStats() {
		Map<String, Long> stats=new LinkedHashMap<>();
		stats.put("users", urepo.count());
		stats.put("categories", crepo.count());
		stats.put("policies", prepo.count());
		stats.put("purchases", purchaseRepo.count());
		stats.put("claims", claimRepo.count());
		stats.put("pendingClaims", claimRepo.countByStatus("PENDING"));
		logger.debug("Dashboard stats: {}", stats);
		return stats;
	}

}

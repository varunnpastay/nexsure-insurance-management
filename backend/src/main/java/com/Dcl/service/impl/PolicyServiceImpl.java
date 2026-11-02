package com.Dcl.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.Dcl.dto.PolicyDto;
import com.Dcl.entity.Policy;
import com.Dcl.entity.PolicyCategory;
import com.Dcl.exception.AppException;
import com.Dcl.repo.CategoryRepository;
import com.Dcl.repo.PolicyRepository;
import com.Dcl.repo.PurchaseRepository;
import com.Dcl.request.AddPolicy;
import com.Dcl.request.UpdatePolicy;
import com.Dcl.service.PolicyService;

@Service
public class PolicyServiceImpl implements PolicyService {

	private static final Logger logger=LoggerFactory.getLogger(PolicyServiceImpl.class);

	@Autowired
	private PolicyRepository prepo;

	@Autowired
	private CategoryRepository crepo;

	@Autowired
	private PurchaseRepository purchaseRepo;

	@Autowired
	private ModelMapper mapper;

	@Override
	public PolicyDto addPolicy(AddPolicy request) {
		logger.info("Add policy request: {} (category id: {})", request.getPolicyName(), request.getCategoryId());
		PolicyCategory category=crepo.findById(request.getCategoryId())
				.orElseThrow(()->{
					logger.warn("Add policy failed - category not found: {}", request.getCategoryId());
					return new AppException("Category Not Found!", HttpStatus.NOT_FOUND);
				});

		Policy policy=mapper.map(request, Policy.class);
		policy.setCategory(category);
		policy.setStatus("ACTIVE");
		policy=prepo.save(policy);
		logger.info("Policy added with id: {}", policy.getPolicyId());
		return mapper.map(policy, PolicyDto.class);
	}

	@Override
	public PolicyDto getPolicyById(Integer policyId) {
		logger.debug("Fetching policy id: {}", policyId);
		Policy policy=prepo.findById(policyId).orElseThrow(()->{
			logger.warn("Policy not found: {}", policyId);
			return new AppException("Policy Not Found!", HttpStatus.NOT_FOUND);
		});
		return mapper.map(policy, PolicyDto.class);
	}

	@Override
	public List<PolicyDto> getAllPolicies() {
		List<Policy> policyList=prepo.findAll();
		logger.debug("Fetched {} policies", policyList.size());
		return policyList.stream()
				.map(p->mapper.map(p, PolicyDto.class))
				.collect(Collectors.toList());
	}

	//customer side - only ACTIVE policies, optionally filtered by category
	@Override
	public List<PolicyDto> getActivePolicies(Integer categoryId) {
		List<Policy> policyList;
		if(categoryId==null) {
			policyList=prepo.findByStatus("ACTIVE");
		}else {
			policyList=prepo.findByCategory_CategoryIdAndStatus(categoryId, "ACTIVE");
		}
		logger.debug("Fetched {} active policies (category filter: {})", policyList.size(), categoryId);
		return policyList.stream()
				.map(p->mapper.map(p, PolicyDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public PolicyDto updatePolicy(Integer policyId, UpdatePolicy request) {
		logger.info("Update request for policy id: {}", policyId);
		Policy existingPolicy=prepo.findById(policyId).orElseThrow(()->{
			logger.warn("Update failed - policy not found: {}", policyId);
			return new AppException("Policy Not Found!", HttpStatus.NOT_FOUND);
		});
		PolicyCategory category=crepo.findById(request.getCategoryId())
				.orElseThrow(()->{
					logger.warn("Update failed - category not found: {}", request.getCategoryId());
					return new AppException("Category Not Found!", HttpStatus.NOT_FOUND);
				});

		mapper.map(request, existingPolicy);
		existingPolicy.setCategory(category);
		Policy afterUpdate=prepo.save(existingPolicy);
		logger.info("Policy updated: {} (status: {})", policyId, afterUpdate.getStatus());
		return mapper.map(afterUpdate, PolicyDto.class);
	}

	@Override
	public void deletePolicyById(Integer policyId) {
		logger.info("Delete request for policy id: {}", policyId);
		prepo.findById(policyId).orElseThrow(()->{
			logger.warn("Delete failed - policy not found: {}", policyId);
			return new AppException("Policy Not Found!", HttpStatus.NOT_FOUND);
		});
		//a bought policy must stay, admin can make it INACTIVE instead
		if(purchaseRepo.existsByPolicy_PolicyId(policyId)) {
			logger.warn("Delete blocked - policy {} is already purchased", policyId);
			throw new AppException("Policy is already purchased, make it INACTIVE instead!", HttpStatus.CONFLICT);
		}
		prepo.deleteById(policyId);
		logger.info("Policy deleted: {}", policyId);
	}

}

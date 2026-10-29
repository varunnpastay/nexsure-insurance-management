package com.Dcl.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
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
		PolicyCategory category=crepo.findById(request.getCategoryId())
				.orElseThrow(()->new AppException("Category Not Found!", HttpStatus.NOT_FOUND));
		
		Policy policy=mapper.map(request, Policy.class);
		policy.setCategory(category);
		policy.setStatus("ACTIVE");
		policy=prepo.save(policy);
		return mapper.map(policy, PolicyDto.class);
	}

	@Override
	public PolicyDto getPolicyById(Integer policyId) {
		Policy policy=prepo.findById(policyId).orElseThrow(()->new AppException("Policy Not Found!", HttpStatus.NOT_FOUND));
		return mapper.map(policy, PolicyDto.class);
	}

	@Override
	public List<PolicyDto> getAllPolicies() {
		List<Policy> policyList=prepo.findAll();
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
		return policyList.stream()
				.map(p->mapper.map(p, PolicyDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public PolicyDto updatePolicy(Integer policyId, UpdatePolicy request) {
		Policy existingPolicy=prepo.findById(policyId).orElseThrow(()->new AppException("Policy Not Found!", HttpStatus.NOT_FOUND));
		PolicyCategory category=crepo.findById(request.getCategoryId())
				.orElseThrow(()->new AppException("Category Not Found!", HttpStatus.NOT_FOUND));
		
		mapper.map(request, existingPolicy);
		existingPolicy.setCategory(category);
		Policy afterUpdate=prepo.save(existingPolicy);
		return mapper.map(afterUpdate, PolicyDto.class);
	}

	@Override
	public void deletePolicyById(Integer policyId) {
		prepo.findById(policyId).orElseThrow(()->new AppException("Policy Not Found!", HttpStatus.NOT_FOUND));
		//a bought policy must stay, admin can make it INACTIVE instead
		if(purchaseRepo.existsByPolicy_PolicyId(policyId)) {
			throw new AppException("Policy is already purchased, make it INACTIVE instead!", HttpStatus.CONFLICT);
		}
		prepo.deleteById(policyId);
	}

}

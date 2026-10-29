package com.Dcl.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Dcl.entity.Claim;

@Repository
public interface ClaimRepository extends JpaRepository<Claim, Integer>{

	List<Claim> findByUser_UserId(Integer userId);
	
	boolean existsByPurchase_PurchaseId(Integer purchaseId);
	
	long countByStatus(String status);
}

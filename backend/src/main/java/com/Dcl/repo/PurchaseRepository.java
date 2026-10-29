package com.Dcl.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Dcl.entity.Purchase;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Integer>{

	//Purchase -> user -> userId
	List<Purchase> findByUser_UserId(Integer userId);
	
	boolean existsByUser_UserId(Integer userId);
	
	boolean existsByPolicy_PolicyId(Integer policyId);
}

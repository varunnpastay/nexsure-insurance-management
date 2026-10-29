package com.Dcl.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Dcl.entity.Policy;

@Repository
public interface PolicyRepository extends JpaRepository<Policy, Integer>{

	List<Policy> findByStatus(String status);
	
	//Policy -> category -> categoryId
	List<Policy> findByCategory_CategoryIdAndStatus(Integer categoryId, String status);
	
	boolean existsByCategory_CategoryId(Integer categoryId);
}

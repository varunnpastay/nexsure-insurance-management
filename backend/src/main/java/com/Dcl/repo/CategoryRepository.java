package com.Dcl.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Dcl.entity.PolicyCategory;

@Repository
public interface CategoryRepository extends JpaRepository<PolicyCategory, Integer>{

	Optional<PolicyCategory> findByCategoryName(String categoryName);
}

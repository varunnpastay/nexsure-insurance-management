package com.Dcl.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.Dcl.dto.CategoryDto;
import com.Dcl.entity.PolicyCategory;
import com.Dcl.exception.AppException;
import com.Dcl.repo.CategoryRepository;
import com.Dcl.repo.PolicyRepository;
import com.Dcl.request.AddCategory;
import com.Dcl.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepository crepo;
	
	@Autowired
	private PolicyRepository policyRepo;
	
	@Autowired
	private ModelMapper mapper;
	
	@Override
	public CategoryDto addCategory(AddCategory request) {
		PolicyCategory alreadyExists=crepo.findByCategoryName(request.getCategoryName()).orElse(null);
		if(alreadyExists!=null) {
			throw new AppException("Category already exists!", HttpStatus.CONFLICT);
		}
		PolicyCategory category=mapper.map(request, PolicyCategory.class);
		category=crepo.save(category);
		return mapper.map(category, CategoryDto.class);
	}

	@Override
	public List<CategoryDto> getAllCategories() {
		List<PolicyCategory> categoryList=crepo.findAll();
		return categoryList.stream()
				.map(c->mapper.map(c, CategoryDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public void deleteCategoryById(Integer categoryId) {
		crepo.findById(categoryId).orElseThrow(()->new AppException("Category Not Found!", HttpStatus.NOT_FOUND));
		if(policyRepo.existsByCategory_CategoryId(categoryId)) {
			throw new AppException("Category has policies, delete them first!", HttpStatus.CONFLICT);
		}
		crepo.deleteById(categoryId);
	}

}

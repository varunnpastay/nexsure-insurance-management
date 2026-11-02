package com.Dcl.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final Logger logger=LoggerFactory.getLogger(CategoryServiceImpl.class);

	@Autowired
	private CategoryRepository crepo;

	@Autowired
	private PolicyRepository policyRepo;

	@Autowired
	private ModelMapper mapper;

	@Override
	public CategoryDto addCategory(AddCategory request) {
		logger.info("Add category request: {}", request.getCategoryName());
		PolicyCategory alreadyExists=crepo.findByCategoryName(request.getCategoryName()).orElse(null);
		if(alreadyExists!=null) {
			logger.warn("Add category failed - already exists: {}", request.getCategoryName());
			throw new AppException("Category already exists!", HttpStatus.CONFLICT);
		}
		PolicyCategory category=mapper.map(request, PolicyCategory.class);
		category=crepo.save(category);
		logger.info("Category added with id: {}", category.getCategoryId());
		return mapper.map(category, CategoryDto.class);
	}

	@Override
	public List<CategoryDto> getAllCategories() {
		List<PolicyCategory> categoryList=crepo.findAll();
		logger.debug("Fetched {} categories", categoryList.size());
		return categoryList.stream()
				.map(c->mapper.map(c, CategoryDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public void deleteCategoryById(Integer categoryId) {
		logger.info("Delete request for category id: {}", categoryId);
		crepo.findById(categoryId).orElseThrow(()->{
			logger.warn("Delete failed - category not found: {}", categoryId);
			return new AppException("Category Not Found!", HttpStatus.NOT_FOUND);
		});
		if(policyRepo.existsByCategory_CategoryId(categoryId)) {
			logger.warn("Delete blocked - category {} has policies", categoryId);
			throw new AppException("Category has policies, delete them first!", HttpStatus.CONFLICT);
		}
		crepo.deleteById(categoryId);
		logger.info("Category deleted: {}", categoryId);
	}

}

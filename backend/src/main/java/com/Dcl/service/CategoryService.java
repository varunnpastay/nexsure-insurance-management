package com.Dcl.service;

import java.util.List;

import com.Dcl.dto.CategoryDto;
import com.Dcl.request.AddCategory;

public interface CategoryService {

	public CategoryDto addCategory(AddCategory request);
	
	public List<CategoryDto> getAllCategories();
	
	public void deleteCategoryById(Integer categoryId);
}

package com.Dcl.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Dcl.dto.CategoryDto;
import com.Dcl.request.AddCategory;
import com.Dcl.response.ApiResponse;
import com.Dcl.service.CategoryService;

@RestController
@RequestMapping("/category")
public class CategoryController {

	@Autowired
	private CategoryService cservice;
	
	@PostMapping("/add")
	public ResponseEntity<?> addCategory(@RequestBody AddCategory request){
		CategoryDto dto=cservice.addCategory(request);
		return ResponseEntity.ok(new ApiResponse<>("Category added successfully!",dto,HttpStatus.OK));
	}
	
	@GetMapping("/get")
	public ResponseEntity<?> getAllCategories(){
		List<CategoryDto> dtoList=cservice.getAllCategories();
		return ResponseEntity.ok(new ApiResponse<>("All Categories",dtoList,HttpStatus.OK));
	}
	
	@DeleteMapping("/delete/{categoryId}")
	public ResponseEntity<?> deleteCategory(@PathVariable Integer categoryId){
		cservice.deleteCategoryById(categoryId);
		return ResponseEntity.ok(new ApiResponse<>("Category deleted successfully!",null,HttpStatus.OK));
	}
}

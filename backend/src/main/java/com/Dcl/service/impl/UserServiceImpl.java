package com.Dcl.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.Dcl.dto.UserDto;
import com.Dcl.entity.User;
import com.Dcl.exception.AppException;
import com.Dcl.repo.PurchaseRepository;
import com.Dcl.repo.UserRepository;
import com.Dcl.request.LoginRequest;
import com.Dcl.request.RegisterRequest;
import com.Dcl.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository urepo;
	
	@Autowired
	private PurchaseRepository purchaseRepo;
	
	@Autowired
	private ModelMapper mapper;
	
	@Override
	public UserDto register(RegisterRequest request) {
		//validating whether the user is existing user or not!
		User alreadyExists=urepo.findByEmail(request.getEmail()).orElse(null);
		if(alreadyExists!=null) {
			throw new AppException("User already exists!", HttpStatus.CONFLICT);
		}
		
		//transfering the data from request to entity
		User user=mapper.map(request, User.class);
		user.setRole("CUSTOMER");
		user=urepo.save(user);
		
		//tranfering the data from entity to dto
		return mapper.map(user, UserDto.class);
	}

	@Override
	public UserDto login(LoginRequest request) {
		//Validating the user with email
		User user=urepo.findByEmail(request.getEmail()).orElseThrow(()->new AppException("User Not Found!", HttpStatus.NOT_FOUND));
		//Password validation
		if(!user.getPassword().equals(request.getPassword())) {
			throw new AppException("Incorrect Password!", HttpStatus.UNAUTHORIZED);
		}
		//customer portal - admin is not allowed here
		if(!user.getRole().equals("CUSTOMER")) {
			throw new AppException("Admin must login from the Admin Portal!", HttpStatus.FORBIDDEN);
		}
		return mapper.map(user, UserDto.class);
	}

	@Override
	public UserDto adminLogin(LoginRequest request) {
		User user=urepo.findByEmail(request.getEmail()).orElseThrow(()->new AppException("Admin Not Found!", HttpStatus.NOT_FOUND));
		if(!user.getPassword().equals(request.getPassword())) {
			throw new AppException("Incorrect Password!", HttpStatus.UNAUTHORIZED);
		}
		//admin portal - only ADMIN role allowed
		if(!user.getRole().equals("ADMIN")) {
			throw new AppException("Access denied! Not an admin account.", HttpStatus.FORBIDDEN);
		}
		return mapper.map(user, UserDto.class);
	}

	@Override
	public List<UserDto> getAll() {
		List<User> userList=urepo.findAll();
		return userList.stream()
		        .map(u->mapper.map(u, UserDto.class))
		        .collect(Collectors.toList());
	}

	@Override
	public void deleteUserById(Integer userId) {
		User user=urepo.findById(userId).orElseThrow(()->new AppException("User Not Found!", HttpStatus.NOT_FOUND));
		if(user.getRole().equals("ADMIN")) {
			throw new AppException("Admin can't be deleted!", HttpStatus.BAD_REQUEST);
		}
		if(purchaseRepo.existsByUser_UserId(userId)) {
			throw new AppException("User has purchased policies, can't be deleted!", HttpStatus.CONFLICT);
		}
		urepo.deleteById(userId);
	}

}
